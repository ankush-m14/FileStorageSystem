package com.filestorage.FileStorageSystem.services;

import com.filestorage.FileStorageSystem.exceptions.UserNotFoundException;
import com.filestorage.FileStorageSystem.model.User;
import com.filestorage.FileStorageSystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //save a new user
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //Retrieve a user by username
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: "+ username));
    }

    // Retrieve a user by ID
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // Retrieve all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }



    // Delete a user by ID
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Update user details
    public User updateUser(Long id, User updatedUser) {
        User user = findUserById(id);
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());

        return userRepository.save(user);
    }
}
