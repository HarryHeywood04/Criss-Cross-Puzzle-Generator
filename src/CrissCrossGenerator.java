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
}
