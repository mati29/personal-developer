package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.mateuszjanwojtyna.personaldeveloper.Services.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import java.util.List;

@RestController
@RequestMapping({"/files"})
public class FileController {

    private FilesService filesService;

    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

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
