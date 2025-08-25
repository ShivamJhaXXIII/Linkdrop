package com.spring.guide.linkdrop.Controller;

import com.spring.guide.linkdrop.DTO.CreateUserRequest;
import com.spring.guide.linkdrop.service.UserService;
import com.spring.guide.linkdrop.Exception.UserAlreadyExistsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "signUp";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute CreateUserRequest createUserRequest,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "signUp";
        }

        try {
            userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword());
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please log in.");
            return "redirect:/login";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            return "signUp";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating your account. Please try again.");
            return "signUp";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
