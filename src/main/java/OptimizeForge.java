import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

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

        Path forge = libraries.resolve("net/minecraftforge/forge");
        for (Path fFolder : Files.newDirectoryStream(forge)) {
            for (Path forgeFile : Files.newDirectoryStream(fFolder)) {
                String fileName = forgeFile.getFileName().toString();

                // 移除 forge 的 coremods.json
                if(fileName.endsWith("universal.jar")) {
                    FileSystem fs = FileSystems.newFileSystem(forgeFile);
                    Path coremodsData = fs.getPath("META-INF/coremods.json");
                    if (Files.exists(coremodsData)) {
                        Files.delete(coremodsData);
                    }
                    fs.close();
                // 移除没用的依赖
                } else if(fileName.endsWith(".txt")) {
                    List<String> forgeArgs = Files.readAllLines(forgeFile);

                    String CP_KEY = "-DlegacyClassPath";

                    int cpLine = 0;
                    Set<String> cp = new LinkedHashSet<>();

                    for (int i = 0; i < forgeArgs.size(); i++) {
                        String line = forgeArgs.get(i);
                        if(line.startsWith(CP_KEY)) {
                            cpLine = i;
                            String[] cpArray = line.split("=")[1].split(";");
                            cp.addAll(List.of(cpArray));
                        }
                    }

                    removeCP(cp, "coremods");
                    removeCP(cp, "nashorn-core");

                    cp.add("libraries/dummy.jar");

                    forgeArgs.set(cpLine, CP_KEY + "=" + String.join(";", cp));

                    Files.writeString(forgeFile, String.join("\n", forgeArgs));
                }
            }
        }
    }

    static void removeCP(Collection<String> list, String target) {
        list.removeIf(cp -> {
            String[] split = cp.split("/");
            // fix libraries/dummy.jar
            if(split.length < 3) {
                return false;
            }
            // -1 file |  -2 version | -3 name
            return  split[split.length - 3].equals(target);
        });
    }
}
