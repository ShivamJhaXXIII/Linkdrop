package com.spring.guide.linkdrop.Repository;

import com.spring.guide.linkdrop.Model.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserIdAndUrl(Long userId, String url);

    List<Bookmark> findByUserId(Long userId);

    Page<Bookmark> findByUserId(Long userId, Pageable pageable);
}
