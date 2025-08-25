package com.spring.guide.linkdrop.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "bookmarks") //The @ToString(exclude = "bookmarks") annotation from Lombok generates a toString() method for the User class, but excludes the bookmarks field from the output. This helps prevent potential issues like infinite recursion or large/unreadable string representations when the bookmarks list is included
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;


    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookmark> bookmarks;


//    public void addBookmark(Bookmark bookmark) {
//        bookmarks.add(bookmark);
//        bookmark.setUser(this);
//    }
//
//    public void removeBookmark(Bookmark bookmark) {
//        bookmarks.remove(bookmark);
//        bookmark.setUser(null);
//    }
}
