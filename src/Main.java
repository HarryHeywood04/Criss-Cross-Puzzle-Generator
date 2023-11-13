import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    static String filePath = "src/files/";
    static String wordsFile;

    public static void main(String[] args) throws FileNotFoundException {
        wordsFile = filePath + args[0];
        ICrissCrossGenerator ccg = new CrissCrossGenerator();
        ccg.Load(wordsFile);
        CrissCrossPuzzle puzzle = ccg.Generate(100, 1000);
        CrissCrossPrinter.print(puzzle.getGrid());
    }
}