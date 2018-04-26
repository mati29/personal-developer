package com.mateuszjanwojtyna.personaldeveloper.Configurations;

import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.mateuszjanwojtyna.personaldeveloper.Utility.FilesHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class ConverterConfig {

    @Bean
    public IConverter converter(){
        File tempDirectory = FilesHelper.getFolder("temp");
        return LocalConverter.builder()
                .baseFolder(tempDirectory)
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();
    }
}
