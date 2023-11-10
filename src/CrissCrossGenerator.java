import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CrissCrossGenerator implements ICrissCrossGenerator {

    File wordsFile;
    String words[];
    char puzzle[][];
    Random random;
    ArrayList<String> usedWords;
    String lastFilePath;

    public CrissCrossGenerator(){
    }

    @Override
    public char[][] Generate(int size) throws FileNotFoundException {
        if (IsUsed(words))
            Load(lastFilePath);
        puzzle = new char[size][size];
        random = new Random();
        usedWords = new ArrayList<>();
        Begin();
        GenerateWord();
        return puzzle;
    }

    /*
    Loads the words list from the input file
     */
    @Override
    public void Load(String filePath) throws FileNotFoundException {
        lastFilePath = filePath;
        wordsFile = new File(filePath);
        ArrayList<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(wordsFile);
        while (scanner.hasNextLine()){
            list.add(scanner.nextLine());
        }
        words = list.toArray(new String[0]);
    }

    /*
    Attempts to randomly place first word on the grid until it succeeds.
     */
    private void Begin(){
        boolean placed = false;
        while (!placed){
            int wordIndex = random.nextInt(words.length);
            String base = words[wordIndex];
            int[] pos = new int[]{random.nextInt((int) (puzzle.length * 0.8f)), random.nextInt((int) (puzzle[0].length * 0.8f))};
            Direction direction;
            if (random.nextBoolean())
                direction = Direction.RIGHT;
            else
                direction = Direction.DOWN;
            placed = Place(base, pos, direction);
            if (placed){
                usedWords.add(words[wordIndex]);
                words[wordIndex] = null;
            }
        }
    }

    private boolean GenerateWord(){
        int MAX = 100000;
        boolean down = false, right = false;
        Direction dir;
        for (int i = 0; i < MAX; i++){
            int[] point = new int[]{random.nextInt(puzzle.length), random.nextInt(puzzle[0].length)};
            if (puzzle[point[0]][point[1]] != '\0'){
                //find free direction
                if (point[1] - 1 >= 0 && point[1] + 1 < puzzle[0].length){ // if down is in range
                    if (puzzle[point[0]][point[1] - 1] == '\0' && puzzle[point[0]][point[1] + 1] == '\0'){ // if space is free
                        down = true;
                    }
                }
                if (point[0] - 1 >= 0 && point[0] + 1 < puzzle.length){ // if across is in range
                    if (puzzle[point[0] - 1][point[1]] == '\0' && puzzle[point[0] - 1][point[1]] == '\0'){ // if space is free
                        right = true;
                    }
                }
                //find word that fits
                char c = puzzle[point[0]][point[1]];
                boolean wordFound = false;
                int letterIndex = 0;
                int t = 0;
                String word = "";
                while (!wordFound){
                    word = words[random.nextInt(words.length)];
                    if (word != null){
                        char[] processedWord = word.toCharArray();
                        for (int j = 0; j < processedWord.length; j++) {
                            if (c == processedWord[j]){
                                wordFound = true;
                                letterIndex = j;
                            }
                        }
                    }
                    t++;
                    if (t > 100)
                        break;
                }
                if (wordFound){
                    //direction decision
                    dir = null;
                    if (down && right) {
                        if (random.nextBoolean())
                            dir = Direction.DOWN;
                        else
                            dir = Direction.RIGHT;
                    } else if (down){
                        dir = Direction.DOWN;
                    } else if (right){
                        dir = Direction.RIGHT;
                    }
                    if (dir == Direction.RIGHT){
                        Place(word, new int[]{point[0] - letterIndex, point[1]}, Direction.RIGHT);
                        //final checks
                        //place word
                        //remove word from list
                    } else if (dir == Direction.DOWN){
                        Place(word, new int[]{point[0], point[1] - letterIndex}, Direction.DOWN);
                        //final checks
                        //place word
                        //remove word from list
                    }
                }
            }
        }
        return false;
    }

    /*
    Takes in the word, position and rotation and attempts to place it on the grid.
     */
    private boolean Place(String word, int position[], Direction direction){
        char processedWord[] = word.toCharArray();
        if (direction == Direction.RIGHT && position[0] + processedWord.length < puzzle.length){
            for (int i = 0; i < processedWord.length; i++){
                puzzle[position[0] + i][position[1]] = processedWord[i];
            }
            return true;
        }
        else if (direction == Direction.DOWN && position[1] + processedWord.length < puzzle[0].length){
            for (int i = 0; i < processedWord.length; i++){
                puzzle[position[0]][position[1] + i] = processedWord[i];
            }
            return true;
        }
        return false;
    }

    private boolean IsUsed(String[] words){
        boolean result = false;
        for (int i = 0; i < words.length; i++)
            if (words[i] == null)
                result = true;
        return result;
    }
}
