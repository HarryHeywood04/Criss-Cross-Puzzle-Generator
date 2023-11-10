public class WordObject implements IWordObject {
    String word;
    int[] pos;
    Direction rot;

    public WordObject(String wordIn, int[] position, Direction rotation){
        word = wordIn;
        pos = position;
        rot = rotation;
    }

    @Override
    public int[] GetPos() {
        return pos;
    }

    @Override
    public Direction GetRot() {
        return rot;
    }

    @Override
    public String GetWord() {
        return word;
    }

    @Override
    public int GetLength() {
        return word.length();
    }
}
