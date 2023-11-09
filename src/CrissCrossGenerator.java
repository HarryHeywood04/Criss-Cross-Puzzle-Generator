import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CrissCrossGenerator implements ICrissCrossGenerator {

    File wordsFile;
    String words[];
    char puzzle[][];

    public CrissCrossGenerator(){
        puzzle = new char[50][50];
    }

    @Override
    public void Generate() {
        Random random = new Random();
        String base = words[random.nextInt(words.length)];
        System.out.println(Place(base, new int[]{45, 32}, Direction.RIGHT));
        System.out.println(base);
    }

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
