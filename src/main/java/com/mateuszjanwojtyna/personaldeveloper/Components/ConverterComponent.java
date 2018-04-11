package com.mateuszjanwojtyna.personaldeveloper.Components;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ConverterComponent {

    @Autowired
    IConverter converter;

    public void convertFromDocToPdf(File docFile, File pdfFile) {
        converter
                .convert(docFile).as(DocumentType.MS_WORD)
                .to(pdfFile).as(DocumentType.PDF)
                .prioritizeWith(1000)
                .execute();// change to future async
    }
}
