package com.jobportal.controller;

import com.jobportal.model.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("user", userDetails.getUser());
        return "profile";
    }

    @PostMapping("/upload-resume")
    public String uploadResume(@RequestParam("resume") MultipartFile file,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/profile";
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            userService.saveUserResume(userDetails.getUser().getId(), newFileName);
            // Update the user details object in the current session so UI reflects changes immediately
            userDetails.getUser().setResumeFilePath(newFileName);

            redirectAttributes.addFlashAttribute("successMessage", "Resume uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload resume.");
        }

        return "redirect:/profile";
    }
}
