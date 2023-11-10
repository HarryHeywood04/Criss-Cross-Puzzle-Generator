import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    static String wordsFile = "src/files/words_list.txt";

    public static void main(String[] args) throws FileNotFoundException {
        ICrissCrossGenerator ccg = new CrissCrossGenerator();
        ccg.Load(wordsFile);
        char[][] puzzle = ccg.Generate();
        CrissCrossPrinter.print(puzzle);
    }
}