package com.mateuszjanwojtyna.personaldeveloper.UtilityTest;

import com.mateuszjanwojtyna.personaldeveloper.Utility.FilesHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FilesUtilityTest {


    @Test
    public void testFilesHelperWhenFolderExistThenReturnFolder() {
        String path = "temp";
        assertNotNull(FilesHelper.getFolder(path));
    }

    @Test
    public void testFilesHelperWhenFolderNotExistThenReturnFolder() {
        String uuid = UUID.randomUUID().toString();
        assertNotNull(FilesHelper.getFolder(uuid));
    }
}
