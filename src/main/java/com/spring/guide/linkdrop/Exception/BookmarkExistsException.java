// File: 'src/main/java/com/spring/guide/linkdrop/Exception/BookmarkExistsException.java'
package com.spring.guide.linkdrop.Exception;


public class BookmarkExistsException extends RuntimeException {
    public BookmarkExistsException(String message) {
        super(message);
    }
}