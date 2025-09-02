package com.spring.guide.linkdrop.service;

import com.spring.guide.linkdrop.Exception.UserNotFoundException;
import com.spring.guide.linkdrop.Model.Bookmark;
import com.spring.guide.linkdrop.Model.User;
import com.spring.guide.linkdrop.Repository.BookmarkRepository;
import com.spring.guide.linkdrop.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Bookmark addBookmark(String url, String title, String description, Long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        Bookmark bookmark = Bookmark.builder()
                .url(url)
                .user(user)
                .dateTime(LocalDateTime.now())
                .title(title != null ? title : url)
                .description(description != null ? description : "No description")
                .build();

        return bookmarkRepository.save(bookmark);
    }

    /**
     * Add a bookmark for the currently authenticated user
     * @param url The URL to bookmark
     * @param title The title of the bookmark
     * @param description The description of the bookmark
     * @return The created bookmark
     */
    @Transactional
    public Bookmark addBookmark(String url, String title, String description) {
        User currentUser = getCurrentUser();

        Bookmark bookmark = Bookmark.builder()
                .url(url)
                .user(currentUser)
                .dateTime(LocalDateTime.now())
                .title(title != null && !title.trim().isEmpty() ? title : url)
                .description(description != null && !description.trim().isEmpty() ? description : "No description")
                .build();

        return bookmarkRepository.save(bookmark);
    }

    public List<Bookmark> allBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    /**
     * Get all bookmarks for the currently authenticated user
     * @return Page of bookmarks for the current user
     */
    public Page<Bookmark> getBookmarksForCurrentUser(Pageable pageable) {
        User currentUser = getCurrentUser();
        return bookmarkRepository.findByUserId(currentUser.getId(), pageable);
    }

    /**
     * Get all bookmarks for the currently authenticated user (without pagination)
     * @return List of bookmarks for the current user
     */
    public List<Bookmark> getBookmarksForCurrentUser() {
        User currentUser = getCurrentUser();
        return bookmarkRepository.findByUserId(currentUser.getId());
    }

    /**
     * Get a specific bookmark by ID for the current user
     * @param bookmarkId The ID of the bookmark to retrieve
     * @return The bookmark if found and belongs to current user
     * @throws RuntimeException if bookmark not found or doesn't belong to current user
     */
    public Bookmark getBookmarkById(Long bookmarkId) {
        User currentUser = getCurrentUser();
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        // Ensure the bookmark belongs to the current user
        if (!bookmark.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("User not authorized to access this bookmark");
        }

        return bookmark;
    }

    /**
     * Get the currently authenticated user
     * @return The current user
     * @throws RuntimeException if user not found or not authenticated
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId, Long userId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        if (!bookmark.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this bookmark");
        }

        bookmarkRepository.delete(bookmark);
    }

    /**
     * Delete a bookmark for the current user
     * @param bookmarkId The ID of the bookmark to delete
     */
    @Transactional
    public void deleteBookmarkForCurrentUser(Long bookmarkId) {
        User currentUser = getCurrentUser();
        deleteBookmark(bookmarkId, currentUser.getId());
    }

    /**
     * Update a bookmark for the current user
     * @param bookmarkId The ID of the bookmark to update
     * @param url The new URL
     * @param title The new title
     * @param description The new description
     * @return The updated bookmark
     */
    @Transactional
    public Bookmark updateBookmark(Long bookmarkId, String url, String title, String description) {
        User currentUser = getCurrentUser();

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        // Ensure the bookmark belongs to the current user
        if (!bookmark.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("User not authorized to update this bookmark");
        }

        // Update bookmark fields
        bookmark.setUrl(url);
        bookmark.setTitle(title != null && !title.trim().isEmpty() ? title : url);
        bookmark.setDescription(description != null && !description.trim().isEmpty() ? description : "No description");

        return bookmarkRepository.save(bookmark);
    }
}
