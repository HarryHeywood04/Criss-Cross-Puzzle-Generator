import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CrissCrossGenerator implements ICrissCrossGenerator {

    File wordsFile; // The file with the words
    String[] words; // An array of the words
    int currentWordIndex = 0;
    char[][] puzzle; // The puzzle grin
    Random random;
    ArrayList<IWordObject> usedWords; // A list of the used words
    String lastFilePath; // The last filepath used, allows for quick generation of multiple puzzles
    int MAX = 100000;//The max loops allowed

    public CrissCrossGenerator(){
    }

    /**
     * Generates a puzzle with the width and height of the input size and attempts to reach the input word amount
     * @param size Width and height of the puzzle
     * @param wordAmount Amount of words to put in puzzle
     * @return Returns the puzzle
     * @throws FileNotFoundException Can throw an error if the no file has been loaded
     */
    @Override
    public CrissCrossPuzzle Generate(int size, int wordAmount) throws FileNotFoundException {
        if (IsUsed(words))
            Load(lastFilePath);
        puzzle = new char[size][size];
        random = new Random();
        usedWords = new ArrayList<>();
        Begin();
        wordAmount--;
        int i = 0;
        while (wordAmount > 0){
            if (GenerateWord())
                wordAmount--;
            if (i>= MAX)
                break;
            i++;
        }
        CrissCrossPuzzle crissCrossPuzzle = new CrissCrossPuzzle(puzzle, usedWords);
        return crissCrossPuzzle;
    }

    /**
     * Loads words from the input file path
     * @param filePath Path of the file
     * @throws FileNotFoundException Throws error if input file is not found
     */
    @Override
    public void Load(String filePath) throws FileNotFoundException {
        lastFilePath = filePath;
        wordsFile = new File(filePath);
        ArrayList<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(wordsFile);
        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            word = word.replace(' ', '\0'); // deletes any white space characters
            list.add(word);
        }
        words = list.toArray(new String[0]);
    }

    /**
     * Attempts to randomly place first word on the grid until it succeeds.
     */
    private void Begin(){
        boolean placed = false;
        int i = 0;
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
                usedWords.add(new WordObject(words[currentWordIndex], pos, direction));
                words[currentWordIndex] = null;
            }
            if (i > MAX)
                break;
            i++;
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
     * @param word The word to be placed
     * @param pos The position to place it
     * @param dir The rotation of the word
     * @return Returns if it fits or not
     */
    private boolean CheckFits(String word, int[] pos, Direction dir){ //Needs some altering
        int x = pos[0];
        int y = pos[1];
        if ((dir == Direction.RIGHT && x + word.length() - 1 >= puzzle.length) || (dir == Direction.DOWN && y + word.length() - 1 >= puzzle[0].length))
            return false;
        if (x < 1 || y < 1)
            return false;

        if ((dir == Direction.RIGHT && (x == 0 || puzzle[x-1][y] == '\0')) || (dir == Direction.DOWN && (y == 0 || puzzle[x][y-1] == '\0'))){ //if space before word is clear
            boolean marker = false;
            char[] processedWord = word.toCharArray();
            for (int i = 0; i < processedWord.length; i++){
                if (dir == Direction.RIGHT){
                    if ((puzzle[x+i][y] != '\0' && puzzle[x+i][y] != processedWord[i]) || (x != 0 && puzzle[x-1][y] != '\0')){ // If needed slot is used and doesn't match or space before word is used
                        return false;
                    }
                    else if (y != 0 && puzzle[x+i][y+1] != '\0' && puzzle[x+i][y] != processedWord[i]){ // If space below is used
                        if (marker || i == processedWord.length-1 || puzzle[x+i][y] != processedWord[i])
                            return false;
                        else
                            marker = true;
                    }
                    else if (y != puzzle[0].length-1 && puzzle[x+i][y-1] != '\0' && puzzle[x+i][y] != processedWord[i]){ // If space above is used
                        if (marker || i == processedWord.length-1 || puzzle[x+i][y] != processedWord[i])
                            return false;
                        else
                            marker = true;
                    } else
                        marker = false;
                    if (x+i+1 < puzzle.length && i == processedWord.length-1 && puzzle[x+i+1][y] != '\0')
                        return false;
                } else {
                    if ((puzzle[x][y+i] != '\0' && puzzle[x][y+i] != processedWord[i]) || (y != 0 && puzzle[x][y-1] != '\0')){
                        return false;
                    }
                    else if (x != 0 && puzzle[x+1][y+i] != '\0'){
                        if ((marker || i == processedWord.length-1 || puzzle[x][y+i] != processedWord[i]))
                            return false;
                        else
                            marker = true;
                    }
                    else if (x != puzzle.length-1 && puzzle[x-1][y+i] != '\0'){
                        if ((marker || i == processedWord.length-1 || puzzle[x][y+i] != processedWord[i]))
                            return false;
                        else
                            marker = true;
                    } else
                        marker = false;
                    if (y+i+1 < puzzle[0].length && i == processedWord.length-1 && puzzle[x][y+i+1] != '\0')
                        return false;
                }
            }
            return !marker;
        } else {
            return false;
        }
    }

    /**
     * Generates a new word on the puzzle grid
     * @return Returns whether it succeeded
     */
    private boolean GenerateWord(){
        Direction dir;
        for (int i = 0; i < MAX; i++){
            int[] point = new int[]{random.nextInt(10, puzzle.length-10), random.nextInt(1, puzzle[0].length-1)};
            if (puzzle[point[0]][point[1]] != '\0'){ // If char exists in this position
                dir = GetAvailableDirection(point); // get available direction
                if (dir != null){
                    String word = FindWord(puzzle[point[0]][point[1]]);
                    if (word == null)
                        return false;
                    int wordIndex = 0;
                    char[] processedWord = word.toCharArray();
                    for (int j = 0; j < processedWord.length; j++){
                        if (processedWord[j] == puzzle[point[0]][point[1]])
                            wordIndex = j;
                    }
                    if (dir == Direction.RIGHT && CheckFits(word, new int[]{point[0] - wordIndex, point[1]}, dir)){
                        if (Place(word, new int[]{point[0] - wordIndex, point[1]}, dir)) {
                            usedWords.add(new WordObject(word, point, dir));
                            words[currentWordIndex] = null;
                            return true;
                        }
                    } else if (dir == Direction.DOWN && CheckFits(word, new int[]{point[0], point[1] - wordIndex}, dir)){
                        if(Place(word, new int[]{point[0], point[1] - wordIndex}, dir)) {
                            usedWords.add(new WordObject(word, point, dir));
                            words[currentWordIndex] = null;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds a word that contains the input char
     * @param c The char to find a word match for
     * @return A string containing the word match
     */
    private String FindWord(char c){
        for (int i = 0; i < MAX; i++){
            int t = random.nextInt(words.length);
            String word = words[t];
            if (word != null){
                char[] processedWord = word.toCharArray();
                for (char value : processedWord) {
                    if (value == c) {
                        currentWordIndex = t;
                        return word;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Takes in the word, position and rotation and attempts to place it on the grid.
     * @param word The word to place
     * @param position The position to place the word
     * @param direction The rotation of the word
     * @return Whether it was successful or not
     */
    private boolean Place(String word, int[] position, Direction direction){
        int x = position[0];
        int y  = position[1];
        char[] processedWord = word.toCharArray();
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
     * @param pos The position to get the free direction from
     * @return The available direction
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
     * @param words The words array to check
     * @return Whether it has been used or not
     */
    private boolean IsUsed(String[] words){
        boolean result = false;
        for (String word : words)
            if (word == null) {
                result = true;
                break;
            }
        return result;
    }
}
