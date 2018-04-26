package com.mateuszjanwojtyna.personaldeveloper.Utility;

import java.io.File;
import java.util.Optional;

public class FilesHelper {

    public static File getFolder(String path) {
        Optional<File> optionalDirectory = Optional.of(new File(path));
        return optionalDirectory
                .filter(File::exists)
                .orElseGet(()-> {
                    optionalDirectory
                            .map(File::mkdir);
                    return optionalDirectory.get();
                });
    }

}
