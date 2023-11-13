import java.util.ArrayList;

public class CrissCrossPuzzle {
    char[][] grid;
    IWordObject[] words;

    public CrissCrossPuzzle(char[][] puzzleIn, ArrayList<IWordObject> wordsIn) {
        grid = puzzleIn;
        words = wordsIn.toArray(new IWordObject[0]);
    }

    public char[][] getGrid() {
        return grid;
    }

    public IWordObject[] getWords() {
        return words;
    }
}
