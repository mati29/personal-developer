package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.Components.ConverterComponent;
import com.mateuszjanwojtyna.personaldeveloper.Services.FilesService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service(value = "filesService")
public class FilesServiceImpl implements FilesService {

    @Autowired
    ConverterComponent converter;

    private static boolean filterPdf(File file) {
        return "pdf".equals(FilenameUtils.getExtension(file.getName()));
    }

    private static boolean addFiles(File file, List poemsTitle) {
        return poemsTitle.add(FilenameUtils.getBaseName(file.getName()));
    }

    @Override
    public List getFilesName() {
        List poemsTitle = new ArrayList<String>();
        File poemDirectory = new File("poems");
        Arrays
                .asList(poemDirectory.listFiles())
                .stream()
                .filter(FilesServiceImpl::filterPdf)
                .forEach(file -> poemsTitle.add(FilenameUtils.getBaseName(file.getName())));

        return poemsTitle;
    }

    @Override
    public byte[] getFileInBytes(String filename) throws IOException {
        File poemFile = new File("poems"+"\\"+filename+".pdf");
        RandomAccessFile fileAcess = new RandomAccessFile(poemFile, "rw");
        byte[] byteFile = new byte[(int)fileAcess.length()];
        fileAcess.readFully(byteFile);
        return byteFile;
    }

    @Override
    public HttpHeaders getFileHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String fullFilename = filename + ".pdf";
        headers.setContentDispositionFormData(fullFilename, fullFilename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

    private boolean isFileType(MultipartFile file) {// on java 9 isFile private method more polimophic or implements some utility interface to doc
        return !"doc".equals(FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    private File getFolder(String path) { // on java 9 private method more polimophic
        File directory = new File(path);
        if (!directory.exists()) {
            try{
                directory.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
        }
        return directory;
    }

    private StringBuilder getBaseFileNamePath(File directory, MultipartFile file) throws IOException{
        return new StringBuilder()
                .append(directory.getCanonicalPath())
                .append("\\")
                .append(FilenameUtils.getBaseName(file.getOriginalFilename()));
    }

    private String getDocFilePath(StringBuilder stringBuilder) {
        return stringBuilder.append(".doc").toString();
    }

    private String getPdfFilePath(StringBuilder stringBuilder) {
        return stringBuilder.append(".pdf").toString();
    }

    private File saveDocAtServer(File directory, MultipartFile file) throws IOException {
        File fileOnServer = new File(getDocFilePath(getBaseFileNamePath(directory,file)));
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, fileOnServer.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return fileOnServer;
    }

    private File createPdfAtServer(File directory, MultipartFile file) throws IOException {
        File filePDF = new File(getPdfFilePath(getBaseFileNamePath(directory,file)));
        Files.createFile(filePDF.toPath());
        return filePDF;
    }

    private String makeConversionFromDocToPdf(MultipartFile uploadedFile) throws IOException{
        File poemDirectory = getFolder("poems");
        File docAtServer = saveDocAtServer(poemDirectory, uploadedFile);
        File pdfAtServer= createPdfAtServer(poemDirectory, uploadedFile);
        converter.convertFromDocToPdf(docAtServer,pdfAtServer);
        return FilenameUtils.getBaseName(pdfAtServer.getName());
    }

    @Override
    public String uploadFile(MultipartFile uploadedFile) throws IOException{
        if(isFileType(uploadedFile))
            return null;

        return makeConversionFromDocToPdf(uploadedFile);
    }
}
