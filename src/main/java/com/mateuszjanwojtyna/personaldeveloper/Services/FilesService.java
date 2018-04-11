package com.mateuszjanwojtyna.personaldeveloper.Services;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FilesService {

    List getFilesName();
    byte[] getFileInBytes(String filename) throws IOException;
    String uploadFile(MultipartFile uploadedFile) throws IOException;
    HttpHeaders getFileHeaders(String filename);

}
