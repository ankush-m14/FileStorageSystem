package com.filestorage.FileStorageSystem.servicrTest;

import com.filestorage.FileStorageSystem.exceptions.FileStorageException;
import com.filestorage.FileStorageSystem.model.File;
import com.filestorage.FileStorageSystem.model.User;
import com.filestorage.FileStorageSystem.repositories.FileRepository;
import com.filestorage.FileStorageSystem.services.FileServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServicesTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServices fileServices;

    private User user;
    private File file;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        file = new File();
        file.setId(1L);
        file.setFileName("testfile.txt");
        file.setUploadedAt(LocalDateTime.now());
        file.setVersion(1);
        file.setUser(user);
        file.setSharedWith(new ArrayList<>());
    }

    @Test
    public void testSaveFile() {
        when(fileRepository.save(any(File.class))).thenReturn(file);

        File savedFile = fileServices.saveFile("testfile.txt", 1, user, new ArrayList<>());

        assertNotNull(savedFile);
        assertEquals("testfile.txt", savedFile.getFileName());
        assertEquals(1, savedFile.getVersion());
        assertEquals(user, savedFile.getUser());
        verify(fileRepository, times(1)).save(any(File.class));
    }


    @Test
    public void testSaveFile_Exception() {
        when(fileRepository.save(any(File.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(FileStorageException.class, () -> fileServices.saveFile("testfile.txt", 1, user, new ArrayList<>()));
    }

    @Test
    public void testGetFilesByUsername() {
        List<File> files = new ArrayList<>();
        files.add(file);

        when(fileRepository.findByUserUsername(anyString())).thenReturn(files);

        List<File> retrievedFiles = fileServices.getFilesByUsername("testuser");

        assertNotNull(retrievedFiles);
        assertFalse(retrievedFiles.isEmpty());
        assertEquals(1, retrievedFiles.size());
        assertEquals("testfile.txt", retrievedFiles.get(0).getFileName());
        verify(fileRepository, times(1)).findByUserUsername(anyString());
    }

    @Test
    public void testGetFilesSharedWithUser() {
        List<File> files = new ArrayList<>();
        files.add(file);

        when(fileRepository.findBySharedWithContaining(anyString())).thenReturn(files);

        List<File> retrievedFiles = fileServices.getFilesSharedWithUser("testuser");

        assertNotNull(retrievedFiles);
        assertFalse(retrievedFiles.isEmpty());
        assertEquals(1, retrievedFiles.size());
        assertEquals("testfile.txt", retrievedFiles.get(0).getFileName());
        verify(fileRepository, times(1)).findBySharedWithContaining(anyString());
    }

    @Test
    public void testGetFileById() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));

        File retrievedFile = fileServices.getFileById(1L);

        assertNotNull(retrievedFile);
        assertEquals("testfile.txt", retrievedFile.getFileName());
        verify(fileRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetFileById_NotFound() {
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FileStorageException.class, () -> fileServices.getFileById(1L));
    }

    @Test
    public void testDeleteFile() {
        doNothing().when(fileRepository).deleteById(anyLong());

        fileServices.deleteFile(1L);

        verify(fileRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteFile_Exception() {
        doThrow(new RuntimeException("Database error")).when(fileRepository).deleteById(anyLong());

        assertThrows(FileStorageException.class, () -> fileServices.deleteFile(1L));
    }

    @Test
    public void testUpdateFileSharedWith() {
        List<String> sharedWith = new ArrayList<>();
        sharedWith.add("anotherUser");

        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));
        when(fileRepository.save(any(File.class))).thenReturn(file);

        File updatedFile = fileServices.updateFileSharedWith(1L, sharedWith);

        assertNotNull(updatedFile);
        assertEquals(1, updatedFile.getId());
        assertEquals(sharedWith, updatedFile.getSharedWith());
        verify(fileRepository, times(1)).findById(anyLong());
        verify(fileRepository, times(1)).save(any(File.class));
    }
}
