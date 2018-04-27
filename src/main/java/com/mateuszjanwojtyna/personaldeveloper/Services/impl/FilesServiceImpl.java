package com.mateuszjanwojtyna.personaldeveloper.Services.impl;

import com.mateuszjanwojtyna.personaldeveloper.Components.ConverterComponent;
import com.mateuszjanwojtyna.personaldeveloper.Services.FilesService;
import com.mateuszjanwojtyna.personaldeveloper.Utility.FilesHelper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "filesService")
public class FilesServiceImpl implements FilesService {

    private ConverterComponent converter;

    public FilesServiceImpl(ConverterComponent converter) {
        this.converter = converter;
    }

    @Override
    public List getFilesName() {
        return Optional.of(new File("poems"))
                .map(File::listFiles)
                .map(Arrays::stream)
                .map(files-> files.filter(FilesServiceImpl::filterPdf))
                .map(file -> file.collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public byte[] getFileInBytes(String filename){
        return Optional.of(new File("poems"+"\\"+filename+".pdf"))
                .map(FilesHelper::getAccessFile)
                .map(FilesHelper::getByteFile)
                .orElse(null);
    }

    @Override
    public HttpHeaders getFileHeaders(String filename) {
        String fullFilename = filename + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData(fullFilename, fullFilename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

    public static boolean filterPdf(File file) {
        return Optional.of(file)
                .map(File::getName)
                .map(FilenameUtils::getExtension)
                .filter(ext -> ext.equals("pdf"))
                .isPresent();
    }

    public boolean isFileType(MultipartFile file) {
        return Optional.of(file)
                .map(MultipartFile::getOriginalFilename)
                .map(FilenameUtils::getExtension)
                .filter(ext -> ext.equals("doc"))
                .isPresent();
    }

    public StringBuilder getBaseFileNamePath(File directory, MultipartFile file) {
        return Optional.of(createBaseFileNamePath(directory,file))
                .filter(path -> path.toString().matches("^.+[\\\\]{2}.+$"))
                .orElse(null);
    }

    public StringBuilder createBaseFileNamePath(File directory, MultipartFile file) {
        StringBuilder baseFileNamePath = new StringBuilder();
        appendDirectoryPath(directory, baseFileNamePath);
        appendFolderSlash(baseFileNamePath);
        appendFileName(file, baseFileNamePath);
        return  baseFileNamePath;
    }

    public void appendFolderSlash(StringBuilder path) {
        path.append("//");
    }

    public void appendFileName(MultipartFile file, StringBuilder path) {
        Optional.of(file)
                .map(MultipartFile::getOriginalFilename)
                .map(FilenameUtils::getBaseName)
                .ifPresent(path::append);
    }

    public void appendDirectoryPath(File directory, StringBuilder path) {
        Optional.of(directory)
                .map(FilesHelper::getCanonicalPath)
                .ifPresent(path::append);
    }

    private String getDocFilePath(StringBuilder stringBuilder) {
        return stringBuilder.append(".doc").toString();
    }

    private String getPdfFilePath(StringBuilder stringBuilder) {
        return stringBuilder.append(".pdf").toString();
    }

    private File saveDocAtServer(File directory, MultipartFile file){
        return Optional.of(getBaseFileNamePath(directory,file))
                .map(this::getDocFilePath)
                .map(File::new)
                .map(docFile -> FilesHelper.saveStreamInDoc(file, docFile))
                .orElse(null);
    }

    private File createPdfAtServer(File directory, MultipartFile file){
        return Optional.of(getBaseFileNamePath(directory,file))
                .map(this::getPdfFilePath)
                .map(File::new)
                .map(FilesHelper::createFileOnPath)
                .orElse(null);
    }

    private String makeConversionFromDocToPdf(MultipartFile uploadedFile){
        File poemDirectory = FilesHelper.getFolder("poems");
        File docAtServer = saveDocAtServer(poemDirectory, uploadedFile);
        File pdfAtServer= createPdfAtServer(poemDirectory, uploadedFile);
        converter.convertFromDocToPdf(docAtServer,pdfAtServer);
        return FilenameUtils.getBaseName(pdfAtServer.getName());
    }

    @Override
    public String uploadFile(MultipartFile uploadedFile){
        return Optional.of(uploadedFile)
                .filter(this::isFileType)
                .map(this::makeConversionFromDocToPdf)
                .orElse(null);
    }

}
