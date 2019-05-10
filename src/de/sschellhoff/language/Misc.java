package de.sschellhoff.language;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Misc {
    @SafeVarargs
    public static <E> Object[] unpack(E... objects) {
        List<Object> list = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof Object[]) {
                list.addAll(Arrays.asList((Object[]) object));
            }
            else{
                list.add(object);
            }
        }

        return list.toArray(new Object[list.size()]);
    }

    public static String getWorkingDirectory() {
        return FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
    }

    public static Path toAbsolutePath(String path, String basePath) {
        if(path.startsWith("~")) {
             return Paths.get(System.getProperty("user.home") + path.substring(1));
        } else {
            File file = new File(path);
            if (!file.isAbsolute()) {
                return Paths.get(basePath, path);
            }
        }
        return Paths.get(path);
    }

    public static Path getDirectory(Path path) {
        return path.getParent().toAbsolutePath();
    }

    public static String getDirectory(String path) {
        return getDirectory(Paths.get(path)).toString();
    }
}
