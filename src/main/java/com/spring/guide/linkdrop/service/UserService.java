package com.spring.guide.linkdrop.service;

import com.spring.guide.linkdrop.Model.User;
import com.spring.guide.linkdrop.Repository.UserRepository;
import com.spring.guide.linkdrop.Exception.UserAlreadyExistsException;
import com.spring.guide.linkdrop.Exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user with username and password
     * @param username the username for the new user
     * @param password the raw password (will be encoded)
     * @return the created User entity
     * @throws UserAlreadyExistsException if username already exists
     */
    public User createUser(String username, String password) {
        // Check if user already exists
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User with username '" + username + "' already exists");
        }

        // Create new user with encoded password
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(user);
    }

    /**
     * Deletes a user by username
     * @param username the username of the user to delete
     * @throws UserNotFoundException if user doesn't exist
     */
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        userRepository.delete(user);
    }

    /**
     * Deletes a user by ID
     * @param userId the ID of the user to delete
     * @throws UserNotFoundException if user doesn't exist
     */
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID '" + userId + "' not found");
        }

        userRepository.deleteById(userId);
    }

    /**
     * Finds a user by username
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    /**
     * Checks if a user exists by username
     * @param username the username to check
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    /**
     * Updates user password
     * @param username the username of the user
     * @param newPassword the new raw password (will be encoded)
     * @throws UserNotFoundException if user doesn't exist
     */
    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
