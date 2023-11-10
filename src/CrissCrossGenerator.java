import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CrissCrossGenerator implements ICrissCrossGenerator {

    File wordsFile; // The file with the words
    String words[]; // An array of the words
    int currentWordIndex = 0;
    char puzzle[][]; // The puzzle grin
    Random random;
    ArrayList<String> usedWords; // A list of the used words
    String lastFilePath; // The last filepath used, allows for quick generation of multiple puzzles

    public CrissCrossGenerator(){
    }

    /**
     * Generates a puzzle with the width and height of the input size and attempts to reach the input word amount
     * @param size
     * @param wordAmount
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public char[][] Generate(int size, int wordAmount) throws FileNotFoundException {
        if (IsUsed(words))
            Load(lastFilePath);
        puzzle = new char[size][size];
        random = new Random();
        usedWords = new ArrayList<>();
        Begin();
        wordAmount--;
        while (wordAmount > 0){
            GenerateWord();
            wordAmount--;
        }
        return puzzle;
    }

    /**
     * Loads words from the input file path
     * @param filePath
     * @throws FileNotFoundException
     */
    @Override
    public void Load(String filePath) throws FileNotFoundException {
        lastFilePath = filePath;
        wordsFile = new File(filePath);
        ArrayList<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(wordsFile);
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            word.replace(' ', '\0'); // deletes any white space characters
            list.add(word);
        }
        words = list.toArray(new String[0]);
    }

    /**
     * Attempts to randomly place first word on the grid until it succeeds.
     */
    private void Begin(){
        boolean placed = false;
        while (!placed){
            currentWordIndex = random.nextInt(words.length);
            String base = words[currentWordIndex];
            int[] pos = new int[]{random.nextInt(5, puzzle.length-5), random.nextInt(5, puzzle[0].length-5)};
            Direction direction;
            if (random.nextBoolean())
                direction = Direction.RIGHT;
            else
                direction = Direction.DOWN;
            placed = Place(base, pos, direction);
            if (placed){
                usedWords.add(words[currentWordIndex]);
                words[currentWordIndex] = null;
            }
        }
    }

     /*
     1. Find a char on the puzzle Y
     2. Check that it is not blocked in this format where s is other chars and - is a blank space: Y
     -s- (p0-1,p1-1),(p0  ,p1-1),(p0+1,p1-1)
     -c- (p0-1,p1  ),(p0  ,p1  ),(p0+1,p1  )
     -s- (p0-1,p1+1),(p0  ,p1+1),(p0+1,p1+1)
     3. Find a word with that letter Y
     4. check it fits in the following format
     s---s--
     cwwwwww - yes
     s------

     s---sss
     cwwwwww - no
     s------

     s------
     cwwwwss - no
     s------

     5. add word to grid, list and remove from word list
      */

    /**
     * Checks that the word placement fits the rules
     * @param word
     * @param pos
     * @param dir
     * @return
     */
    private boolean CheckFits(String word, int[] pos, Direction dir){ //Needs some altering
        int x = pos[0];
        int y = pos[1];

        if ((dir == Direction.RIGHT && (x == 0 || puzzle[x-1][y] == '\0')) || (dir == Direction.DOWN && (y == 0 || puzzle[x][y-1] == '\0'))){ //if space before word is clear
            boolean marker = false;
            char[] processedWord = word.toCharArray();
            for (int i = 0; i < processedWord.length; i++){
                if (dir == Direction.RIGHT){
                    if (puzzle[x+i][y] != '\0' && puzzle[x+i][y] != processedWord[i]){
                        return false;
                    }
                    else if (y != 0 && puzzle[x+i][y+1] != '\0'){
                        if (marker)
                            return false;
                        else
                            marker = true;
                    }
                    else if (y != puzzle[0].length-1 && puzzle[x+i][y-1] != '\0'){
                        if (marker)
                            return false;
                        else
                            marker = true;
                    } else
                        marker = false;
                } else if (dir == Direction.DOWN){
                    if (!(puzzle[x][y+i] == '\0' || puzzle[x][y+i] == processedWord[i])){
                        return false;
                    }
                    else if (x != 0 && puzzle[x+1][y+i] != '\0'){
                        if (marker)
                            return false;
                        else
                            marker = true;
                    }
                    else if (x != puzzle.length-1 && puzzle[x-1][y+i] != '\0'){
                        if (marker)
                            return false;
                        else
                            marker = true;
                    } else
                        marker = false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a new word on the puzzle grid
     * @return
     */
    private boolean GenerateWord(){
        int MAX = 100000;
        Direction dir;
        for (int i = 0; i < MAX; i++){
            int[] point = new int[]{random.nextInt(10, puzzle.length-10), random.nextInt(1, puzzle[0].length-1)};
            if (puzzle[point[0]][point[1]] != '\0'){ // If char exists in this position
                dir = GetAvailableDirection(point); // get available direction
                if (dir != null){
                    String word = FindWord(puzzle[point[0]][point[1]]);
                    int wordIndex = 0;
                    char[] processedWord = word.toCharArray();
                    for (int j = 0; j < processedWord.length; j++){
                        if (processedWord[j] == puzzle[point[0]][point[1]])
                            wordIndex = j;
                    }
                    if (dir == Direction.RIGHT && CheckFits(word, new int[]{point[0] - wordIndex, point[1]}, dir)){
                        if (Place(word, new int[]{point[0] - wordIndex, point[1]}, dir)) {
                            usedWords.add(word);
                            words[currentWordIndex] = null;
                            break;
                        }
                    } else if (dir == Direction.DOWN && CheckFits(word, new int[]{point[0], point[1] - wordIndex}, dir)){
                        if(Place(word, new int[]{point[0], point[1] - wordIndex}, dir)) {
                            usedWords.add(word);
                            words[currentWordIndex] = null;
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds a word that contains the input char
     * @param c
     * @return
     */
    private String FindWord(char c){
        while (true){
            int t = random.nextInt(words.length);
            String word = words[t];
            if (word != null){
                char[] processedWord = word.toCharArray();
                for (int i = 0; i < processedWord.length; i++){
                    if (processedWord[i] == c){
                        currentWordIndex = t;
                        return word;
                    }
                }
            }
        }
    }

    /**
     * Takes in the word, position and rotation and attempts to place it on the grid.
     * @param word
     * @param position
     * @param direction
     * @return
     */
    private boolean Place(String word, int position[], Direction direction){
        int x = position[0];
        int y  = position[1];
        char processedWord[] = word.toCharArray();
        if (direction == Direction.RIGHT && position[0] + processedWord.length < puzzle.length){
            for (int i = 0; i < processedWord.length; i++){
                puzzle[x + i][y] = processedWord[i];
            }
            return true;
        }
        else if (direction == Direction.DOWN && position[1] + processedWord.length < puzzle[0].length){
            for (int i = 0; i < processedWord.length; i++){
                puzzle[x][y + i] = processedWord[i];
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the available direction from pos, returns null if none found.
     * @param pos
     * @return
     */
    private Direction GetAvailableDirection(int[] pos){
        int x = pos[0];
        int y = pos[1];
        if (puzzle[x-1][y-1] == '\0' && puzzle[x+1][y-1] == '\0' && puzzle[x-1][y+1] == '\0' && puzzle[x-1][y-1] == '\0'){
            if (puzzle[x-1][y] == '\0' && puzzle[x+1][y] == '\0'){
                return Direction.RIGHT;
            } else if (puzzle[x][y-1] == '\0' && puzzle[x][y+1] == '\0'){
                return Direction.DOWN;
            } else {
                return null;
            }
        } else{
            return null;
        }
    }

    /**
     * Checks if the passed in array has any null values (has been 'used')
     * @param words
     * @return
     */
    private boolean IsUsed(String[] words){
        boolean result = false;
        for (int i = 0; i < words.length; i++)
            if (words[i] == null)
                result = true;
        return result;
    }
}
