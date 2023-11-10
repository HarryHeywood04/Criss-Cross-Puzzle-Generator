import java.io.FileNotFoundException;

public interface ICrissCrossGenerator {
    public char[][] Generate(int size, int wordAmount) throws FileNotFoundException;

    public void Load(String filePath) throws FileNotFoundException;
}
