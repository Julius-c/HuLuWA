import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件读写类
 * @author CC
 */
public class FileReplay {
    private Pane pane;

    FileReplay(Pane pane) { this.pane = pane; }

    public File loadFile() {
        FileChooser logChooser = new FileChooser();
        logChooser.setTitle("加载游戏存档");
        logChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("选择log文件","*.log");
        logChooser.getExtensionFilters().add(extensionFilter);
        Stage stage = (Stage)pane.getScene().getWindow();
        return logChooser.showOpenDialog(stage);
    }
    public ArrayList<String> parseFile(File file) {
        ArrayList<String> buffer = new ArrayList<>();
        if(file == null) return buffer;
        BufferedReader reader;
        try {
            //装饰器模式
            reader = new BufferedReader(new FileReader(file));
            String text;
            while((text = reader.readLine()) != null) {
                buffer.add(text);
            }
        } catch (IOException e) {
            throw new RuntimeException("加载存档错误");
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("关闭流失败");
        }
        return buffer;
    }
    public void writeFile(String filename, String Log) {
        try {
            //装饰器模式
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
            writer.write(Log);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("存档错误!");
        }
    }
}
