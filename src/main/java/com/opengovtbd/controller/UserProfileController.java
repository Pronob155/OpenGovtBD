package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.User;
import com.opengovtbd.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserProfileController {

    private final UserRepository userRepository;

    public UserProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{id}")
    public String profile(@PathVariable Long id, HttpSession session, Model model) {
        if (session == null || session.getAttribute("userId") == null)
            return "redirect:/login";
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return "error/404";
        model.addAttribute("profileUser", user);
        model.addAttribute("viewerRole", session.getAttribute("role"));
        return "public/user-profile";
    }

    @GetMapping("/users/{id}/preview")
    public String preview(@PathVariable Long id, HttpSession session, Model model) {
        if (session == null || session.getAttribute("userId") == null)
            return "public/profile-preview :: notfound";
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return "public/profile-preview :: notfound";
        model.addAttribute("profileUser", user);
        return "public/profile-preview :: card";
    }

    @GetMapping("/api/mentions/search")
    @ResponseBody
    public List<Map<String, Object>> searchMentions(@RequestParam(required = false) String q) {
        return userRepository.searchByNameOrUsername(q, 8).stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getId());
            m.put("username", u.getUsername());
            m.put("fullName", u.getFullName());
            m.put("role", u.getDisplayRole());
            m.put("verified", u instanceof Citizen c && c.isVerified());
            return m;
        }).collect(Collectors.toList());
    }
}
