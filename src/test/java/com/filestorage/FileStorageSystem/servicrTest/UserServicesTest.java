package com.filestorage.FileStorageSystem.servicrTest;

import com.filestorage.FileStorageSystem.exceptions.UserNotFoundException;
import com.filestorage.FileStorageSystem.model.User;
import com.filestorage.FileStorageSystem.repositories.UserRepository;
import com.filestorage.FileStorageSystem.services.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServices userServices;

    private User user;
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testSaveUser(){
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userServices.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }
    @Test
    public void testFindUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User foundUser = userServices.findUserByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testFindUserByUsername_NotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServices.findUserByUsername("testuser"));
    }

    @Test
    public void testFindUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userServices.findUserById(1L);

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testFindUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServices.findUserById(1L));
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userServices.findAllUsers();

        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
        assertEquals(1, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUserById() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userServices.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteUserById_NotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userServices.deleteUserById(1L));
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setPassword("updatedPassword");

        User result = userServices.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updatedUsername", result.getUsername());
        assertEquals("updatedPassword", result.getPassword());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
