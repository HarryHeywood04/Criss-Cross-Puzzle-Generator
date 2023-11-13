import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class CrissCrossPrinter {
    static int px = 35;
    static int curLength = 0;
    static IWordObject[] words;

    public static void print(CrissCrossPuzzle puzzle, String fileName) throws IOException {
        ConsolePrint(puzzle.getGrid());

        FileWriter file = new FileWriter("src/output/" + fileName);
        file.write("<svg height=\"" + ((px * puzzle.getGrid().length) + 500) + "\" width=\"" + ((px * puzzle.getGrid().length) + "\" xmlns=\"http://www.w3.org/2000/svg\">\n"));

        //Renders the boxes on the image
        for (int i = 0; i < puzzle.getGrid().length; i++){
            for (int j = 0; j < puzzle.getGrid()[0].length; j++){
                if (puzzle.getGrid()[j][i] != '\0'){
                    file.write(GenerateBox(j, i));
                }
            }
        }

        //Renders the length numbers on the grid
        for (int i = 0; i < puzzle.getWords().length; i++){
            IWordObject word = puzzle.getWords()[i];
            file.write(GenerateCountText(word.GetLength(), word.GetPos()[0], word.GetPos()[1]));
        }

        //Renders the words list at the bottom
        words = puzzle.getWords();
        for (int i = 1; i < words.length; i++) {
            int j = i;
            while (j > 0 && words[j-1].GetLength() > words[j].GetLength()) {
                IWordObject temp = words[j - 1];
                words[j - 1] = words[j];
                words[j] = temp;
                j -= 1;
            }
        }

        int h = 30;
        int y = h*px;
        int x = 20;
        for (int i = 0; i < words.length; i++){
            if (words[i].GetLength() != curLength){
                if (y > (h*px) + 220){
                    y = h*px;
                    x += 250;
                }
                curLength = words[i].GetLength();
                y += (px / 1.2);
                file.write ("<text x=\"" + x + "\" y=\"" + y + "\" font-size=\"2em\" fill=\"black\">" + curLength + " letter words:</text>");
                y += (px / 1.2);
            }
            file.write ("<text x=\"" + x + "\" y=\"" + y + "\" font-size=\"1.8em\" fill=\"black\">" + words[i].GetWord() + "</text>");
            y += (px / 1.2);
        }

        file.write("</svg>\n");
        file.close();
    }

    private static String GenerateBox(int x, int y){
        x*=px;
        y*=px;
        String line = "<polyline points=\"" + x + ".00," + y + ".00 " + (x+px) + ".00," + y + ".00 " + (x+px) + ".00," + (y+px) + ".00 " + x + ".00," + (y+px) + ".00 " + x + ".00," + y + ".00 " + "\" style=\"fill:white;stroke:black;stroke-width:1.0\"/>\n";
        return line;
    }

    private static String GenerateCountText(int count, int x, int y){
        x*=px;
        y*=px;
        String line = "<text x=\"" + (x+3) + "\" y=\"" + (y+13) + "\" fill=\"black\">" + count + "</text>";
        return line;
    }

    private static void ConsolePrint(char[][] puzzle){
        char c;
        for (int i = 0; i < puzzle.length; i++){
            for (int j = 0; j < puzzle[i].length; j++){
                if (puzzle[j][i] != '\0')
                    System.out.print("|" + puzzle[j][i]);
                else
                    System.out.print("|_");
            }
            System.out.print("\n");
        }
    }
}
