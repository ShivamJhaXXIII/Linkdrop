package com.spring.guide.linkdrop.service;

import com.spring.guide.linkdrop.Exception.UserNotFoundException;
import com.spring.guide.linkdrop.Model.Bookmark;
import com.spring.guide.linkdrop.Model.User;
import com.spring.guide.linkdrop.Repository.BookmarkRepository;
import com.spring.guide.linkdrop.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public List<Bookmark> allBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
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
}
