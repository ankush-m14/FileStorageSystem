package com.filestorage.FileStorageSystem.services;

import com.filestorage.FileStorageSystem.exceptions.FileStorageException;
import com.filestorage.FileStorageSystem.model.File;
import com.filestorage.FileStorageSystem.model.User;
import com.filestorage.FileStorageSystem.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileServices {

    @Autowired
    FileRepository fileRepository;

    //save new file
    public File saveFile(String fileName, int version, User user, List<String> sharedWith) {

        try {
            File file = new File();
            file.setFileName(fileName);
            file.setUploadedAt(LocalDateTime.now());
            file.setVersion(version);
            file.setUser(user);
            file.setSharedWith(sharedWith);

            return fileRepository.save(file);
        }
        catch (Exception ex){
            throw new FileStorageException("Failed to save file: " + fileName, ex);
        }
    }

    // Retrieve files by username
    public List<File> getFilesByUsername(String username) {
        return fileRepository.findByUserUsername(username);
    }

    // Retrieve files shared with a specific user
    public List<File> getFilesSharedWithUser(String username) {
        return fileRepository.findBySharedWithContaining(username);
    }

    // Retrieve a file by its ID
    public File getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new FileStorageException("File not found with id: " + id));
    }

    // Delete a file by its ID
    public void deleteFile(Long id) {
        try {
            fileRepository.deleteById(id);
        } catch (Exception ex) {
            throw new FileStorageException("Failed to delete file with id: " + id, ex);
        }
    }


    // Update a file's sharedWith list
    public File updateFileSharedWith(Long id, List<String> sharedWith) {
        File file = getFileById(id);
        file.setSharedWith(sharedWith);
        return fileRepository.save(file);
    }
}
