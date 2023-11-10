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

    public CrissCrossGenerator(){
        puzzle = new char[50][50];
        random = new Random();
    }

    @Override
    public char[][] Generate() {
        Begin();
        return puzzle;
    }

    /*
    Loads the words list from the input file
     */
    @Override
    public void Load(String filePath) throws FileNotFoundException {
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
            int[] pos = new int[]{random.nextInt(40), random.nextInt(40)};
            Direction direction;
            if (random.nextBoolean())
                direction = Direction.RIGHT;
            else
                direction = Direction.DOWN;
            placed = Place(base, pos, direction);
            if (placed)
                words[wordIndex] = null;
        }
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
}
