package com.filestorage.FileStorageSystem.repositories;

import com.filestorage.FileStorageSystem.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
   List<File> findByUserUsername(String username);
   List<File> findBySharedWithContaining(String username);
}
