import Classes.CrissCrossGenerator;
import Classes.CrissCrossPrinter;
import Classes.CrissCrossPuzzle;
import Interfaces.ICrissCrossGenerator;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static String filePath = "src/files/";
    static String wordsFile;

    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the file-name: ");
        wordsFile = filePath + s.nextLine();
        System.out.println("Enter the desired grid size: ");
        int size = s.nextInt();
        System.out.println("Enter desired word count: ");
        int count = s.nextInt();
        ICrissCrossGenerator ccg = new CrissCrossGenerator();
        ccg.Load(wordsFile);
        CrissCrossPuzzle puzzle = ccg.Generate(size, count);
        CrissCrossPrinter.print(puzzle, "output.svg");
        System.out.println("Generated to output.svg");
    }
}