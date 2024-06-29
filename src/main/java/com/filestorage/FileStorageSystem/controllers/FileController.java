package com.filestorage.FileStorageSystem.controllers;

import com.filestorage.FileStorageSystem.model.File;
import com.filestorage.FileStorageSystem.model.User;
import com.filestorage.FileStorageSystem.services.FileServices;
import com.filestorage.FileStorageSystem.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileServices fileServices;

    @Autowired
    private UserServices userServices;

    // Endpoint to upload a new file
    @PostMapping("/upload")
    public File uploadFile(@RequestParam String fileName,
                           @RequestParam int version,
                           @RequestParam String username,
                           @RequestParam List<String> sharedWith) {
        User user = userServices.findUserByUsername(username);
        return fileServices.saveFile(fileName, version, user, sharedWith);
    }

    // Endpoint to get files by username
    @GetMapping("/user/{username}")
    public List<File> getFilesByUsername(@PathVariable String username) {
        return fileServices.getFilesByUsername(username);
    }

    // Endpoint to get files shared with a specific user
    @GetMapping("/shared/{username}")
    public List<File> getFilesSharedWithUser(@PathVariable String username) {
        return fileServices.getFilesSharedWithUser(username);
    }

    // Endpoint to get a file by its ID
    @GetMapping("/{id}")
    public File getFileById(@PathVariable Long id) {
        return fileServices.getFileById(id);
    }

    // Endpoint to delete a file by its ID
    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileServices.deleteFile(id);
    }

    // Endpoint to update a file's sharedWith list
    @PutMapping("/updateSharedWith/{id}")
    public File updateFileSharedWith(@PathVariable Long id, @RequestBody List<String> sharedWith) {
        return fileServices.updateFileSharedWith(id, sharedWith);
    }
}
