package com.mateuszjanwojtyna.personaldeveloper.Controllers;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.FilenameUtils;
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

    @GetMapping("/poems")
    public List getPoemsName(){
        /*try (Stream<Path> paths = Files.walk(Paths.get("/home/you/Desktop"))) {
            paths
                    .filter(Files::isRegularFile) napisze tu wlasna funkcje w serwisie ktora doce rozpozna
                    .forEach(System.out::println);
        }*/ //in future and change from controller to service
        List poemsTitle = new ArrayList<String>();
        File poemDirectory = new File("poems");
        for (final File fileEntry : poemDirectory.listFiles()) {
            /*if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }*/
            String fileName = fileEntry.getName();
            if("pdf".equals(FilenameUtils.getExtension(fileName)))
                poemsTitle.add(FilenameUtils.getBaseName(fileName));
        }

        return poemsTitle;
    }

    @GetMapping("/poems/{name}")
    public ResponseEntity getPoem(@PathVariable("name") String name) throws IOException{
        File poemFile = new File("poems"+"\\"+name+".pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));//msword                                                                //na jego osobiste id
        String filename = name + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        RandomAccessFile fileAcess = new RandomAccessFile(poemFile, "rw");
        byte[] byteFile = new byte[(int)fileAcess.length()];
        fileAcess.readFully(byteFile);
        System.out.println(byteFile);
        return new ResponseEntity(byteFile, headers, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/poems/upload")
    public String uploadPoem(@RequestParam("poem") MultipartFile uploadedPoem) throws IOException{
        if(!"doc".equals(FilenameUtils.getExtension(uploadedPoem.getOriginalFilename())))
            return null;

        File poemDirectory = new File("poems");
        if (!poemDirectory.exists()) {
            try{
                poemDirectory.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
        }
        File fileOnServer = new File(poemDirectory.getCanonicalPath()+"\\"+FilenameUtils.getBaseName(uploadedPoem.getOriginalFilename())+".doc");
        try (InputStream input = uploadedPoem.getInputStream()) {
            Files.copy(input, fileOnServer.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        File filePDF = new File(poemDirectory.getCanonicalPath()+"\\"+FilenameUtils.getBaseName(uploadedPoem.getOriginalFilename())+".pdf");
        Files.createFile(filePDF.toPath());

        File tempDirectory = new File("temp");
        if (!tempDirectory.exists()) {
            try{
                tempDirectory.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
        }
        IConverter converter = LocalConverter.builder() // rozdzielić osobno umieścić ma być beanem
                .baseFolder(tempDirectory)
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();

        boolean conversion = converter
                .convert(fileOnServer).as(DocumentType.MS_WORD)
                .to(filePDF).as(DocumentType.PDF)
                .prioritizeWith(1000)
                .execute();

        return FilenameUtils.getBaseName(filePDF.getName());
    }


}
