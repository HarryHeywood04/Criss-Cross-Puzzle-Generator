import java.io.FileNotFoundException;

public interface ICrissCrossGenerator {
    public char[][] Generate();

    public void Load(String filePath) throws FileNotFoundException;
}
