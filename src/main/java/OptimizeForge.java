import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class OptimizeForge {

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("优化Forge — 请选择Forge端");
        int r = chooser.showOpenDialog(null);
        if (r != APPROVE_OPTION) {
            return;
        }

        Path folder =chooser.getSelectedFile().toPath();

        Path libraries = folder.resolve("libraries");

        Path coremods = libraries.resolve("net/minecraftforge/coremods");
        for (Path cFolder : Files.newDirectoryStream(coremods)) {
            for (Path coreFile : Files.newDirectoryStream(cFolder)) {
                FileSystem fs = FileSystems.newFileSystem(coreFile);
                Path service = fs.getPath("META-INF/services/net.minecraftforge.forgespi.coremod.ICoreModProvider");
                if (Files.exists(service)) {
                    Files.delete(service);
                }
                fs.close();
            }
        }

        Path forge = libraries.resolve("net/minecraftforge/forge");
        for (Path fFolder : Files.newDirectoryStream(forge)) {
            for (Path forgeFile : Files.newDirectoryStream(fFolder)) {
                if(forgeFile.endsWith("server.jar")) {
                    FileSystem fs = FileSystems.newFileSystem(forgeFile);
                    Path coremodsData = fs.getPath("META-INF/coremods.json");
                    if (Files.exists(coremodsData)) {
                        Files.delete(coremodsData);
                    }
                    fs.close();
                }
            }
        }
    }
}
