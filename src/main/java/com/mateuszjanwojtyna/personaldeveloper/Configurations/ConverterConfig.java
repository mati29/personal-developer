package com.mateuszjanwojtyna.personaldeveloper.Configurations;

import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class ConverterConfig {

    private File getFolder(String path) { // zrobić klase utility
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
    @Bean
    public IConverter converter(){
        File tempDirectory = getFolder("temp");
        return LocalConverter.builder()
                .baseFolder(tempDirectory)
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();
    }
}
