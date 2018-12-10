import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Main {
    public static void main(String[] args) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection("test");
        clip.setContents(ss, ss);
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }
    }
}
