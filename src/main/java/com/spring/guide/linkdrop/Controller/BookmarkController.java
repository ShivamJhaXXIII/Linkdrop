package com.spring.guide.linkdrop.Controller;

import com.spring.guide.linkdrop.Model.Bookmark;
import com.spring.guide.linkdrop.service.BookmarkService;
import com.spring.guide.linkdrop.DTO.addBookmarkReq;
import com.spring.guide.linkdrop.DTO.EditBookmarkReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    public String allBookmarks(Model model, Pageable pageable) {
        Page<Bookmark> bookmarks = bookmarkService.getBookmarksForCurrentUser(pageable);
        model.addAttribute("bookmarks", bookmarks);
        return "bookmarks/list";
    }

    @GetMapping("/list")
    public String allBookmarksList(Model model) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksForCurrentUser();
        model.addAttribute("bookmarks", bookmarks);
        return "bookmarks/list";
    }

    @GetMapping("/{id}")
    public String viewBookmark(@PathVariable Long id, Model model) {
        try {
            Bookmark bookmark = bookmarkService.getBookmarkById(id);
            model.addAttribute("bookmark", bookmark);
            return "bookmarks/view";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/add")
    public String showAddBookmarkForm(Model model) {
        model.addAttribute("addBookmarkReq", new addBookmarkReq());
        return "bookmarks/add";
    }

    @PostMapping("/add")
    public String addBookmark(@Valid @ModelAttribute addBookmarkReq addBookmarkReq,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "bookmarks/add";
        }

        try {
            bookmarkService.addBookmark(
                addBookmarkReq.getUrl(),
                addBookmarkReq.getTitle(),
                addBookmarkReq.getDescription()
            );
            redirectAttributes.addFlashAttribute("success", "Bookmark added successfully!");
            return "redirect:/bookmarks";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add bookmark: " + e.getMessage());
            return "bookmarks/add";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditBookmarkForm(@PathVariable Long id, Model model) {
        try {
            Bookmark bookmark = bookmarkService.getBookmarkById(id);

            EditBookmarkReq editReq = new EditBookmarkReq();
            editReq.setUrl(bookmark.getUrl());
            editReq.setTitle(bookmark.getTitle());
            editReq.setDescription(bookmark.getDescription());

            model.addAttribute("bookmark", bookmark);
            model.addAttribute("editBookmarkReq", editReq);
            return "bookmarks/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/edit")
    public String editBookmark(@PathVariable Long id,
                              @Valid @ModelAttribute EditBookmarkReq editBookmarkReq,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            try {
                Bookmark bookmark = bookmarkService.getBookmarkById(id);
                model.addAttribute("bookmark", bookmark);
                return "bookmarks/edit";
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
                return "error";
            }
        }

        try {
            bookmarkService.updateBookmark(id,
                editBookmarkReq.getUrl(),
                editBookmarkReq.getTitle(),
                editBookmarkReq.getDescription());
            redirectAttributes.addFlashAttribute("success", "Bookmark updated successfully!");
            return "redirect:/bookmarks/" + id;
        } catch (Exception e) {
            try {
                Bookmark bookmark = bookmarkService.getBookmarkById(id);
                model.addAttribute("bookmark", bookmark);
                model.addAttribute("error", "Failed to update bookmark: " + e.getMessage());
                return "bookmarks/edit";
            } catch (RuntimeException re) {
                model.addAttribute("error", re.getMessage());
                return "error";
            }
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteBookmark(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookmarkService.deleteBookmarkForCurrentUser(id);
            redirectAttributes.addFlashAttribute("success", "Bookmark deleted successfully!");
            return "redirect:/bookmarks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete bookmark: " + e.getMessage());
            return "redirect:/bookmarks";
        }
    }
}
