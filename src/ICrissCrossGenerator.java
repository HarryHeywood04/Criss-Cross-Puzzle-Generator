import java.io.FileNotFoundException;

public interface ICrissCrossGenerator {
    public void Generate();

    public void Load(String filePath) throws FileNotFoundException;
}
