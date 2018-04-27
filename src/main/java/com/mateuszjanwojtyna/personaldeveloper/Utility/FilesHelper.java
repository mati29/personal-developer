package com.mateuszjanwojtyna.personaldeveloper.Utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

    public static File createFileOnPath(final File file) {
        try {
            Files.createFile(file.toPath());
            return file;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static RandomAccessFile getAccessFile(final File file) {
        try {
            return new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] getByteFile(final RandomAccessFile openFile) {
        try {
            byte[] byteFile = new byte[(int)openFile.length()];
            openFile.readFully(byteFile);
            return byteFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String getCanonicalPath(final File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static File saveStreamInDoc(final MultipartFile file, final File docFile) {
        try(InputStream input = file.getInputStream()) {
            Files.copy(input, docFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return docFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
