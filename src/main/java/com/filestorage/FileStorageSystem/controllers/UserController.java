package com.filestorage.FileStorageSystem.controllers;

import com.filestorage.FileStorageSystem.model.User;
import com.filestorage.FileStorageSystem.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    //Endpoint to create a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userServices.saveUser(user);
    }

    //Endpoint to get user by username
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username){
        return userServices.findUserByUsername(username);
    }

    //Endpoint to get a user By Id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userServices.findUserById(id);
    }

    // Endpoint to get all users
    @GetMapping("/all")
    public List<User> getAllUser(){
        return userServices.findAllUsers();
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable Long id) {
        userServices.deleteUserById(id);
    }

    // Endpoint to update a user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userServices.updateUser(id, updatedUser);
    }
}
