package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.mateuszjanwojtyna.personaldeveloper.Services.FilesService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@RestController
@RequestMapping({"/files"})
public class FileController {

    @Autowired
    private FilesService filesService;

    @GetMapping("/poems")
    public List getPoemsName(){
        return filesService.getFilesName();
    }

    @GetMapping("/poems/{name}")
    public ResponseEntity getPoem(@PathVariable("name") String name) throws IOException{
        return new ResponseEntity(
                filesService.getFileInBytes(name),
                filesService.getFileHeaders(name),
                HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/poems/upload")
    public String uploadPoem(@RequestParam("poem") MultipartFile uploadedPoem) throws IOException{
        return filesService.uploadFile(uploadedPoem);
    }


}
