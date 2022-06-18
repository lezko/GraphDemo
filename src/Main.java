import config.Config;
import gui.FrameMain;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        Config.currentDir =
                Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        Config.currentDir = ".";
        FrameMain frameMain = new FrameMain(1000, 600);
    }
}
