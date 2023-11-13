import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static String filePath = "src/files/";
    static String wordsFile;

    public static void main(String[] args) throws IOException {
        wordsFile = filePath + args[0];
        ICrissCrossGenerator ccg = new CrissCrossGenerator();
        ccg.Load(wordsFile);
        CrissCrossPuzzle puzzle = ccg.Generate(30, 12);
        CrissCrossPrinter.print(puzzle, "test.svg");
    }
}