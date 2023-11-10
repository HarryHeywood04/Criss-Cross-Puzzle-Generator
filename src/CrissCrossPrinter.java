public class CrissCrossPrinter {
    public static void print(char[][] puzzle){
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
