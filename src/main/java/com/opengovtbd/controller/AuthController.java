package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Officer;
import com.opengovtbd.model.Admin;
import com.opengovtbd.model.Role;
import com.opengovtbd.service.AuthException;
import com.opengovtbd.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


    @PostMapping("/login/officer")
    public String loginOfficer(@RequestParam String officerId, @RequestParam String govEmail,
                                @RequestParam String password, @RequestParam(required = false) String next,
                                HttpSession session, Model model) {
        try {
            Officer officer = authService.loginOfficer(officerId, govEmail, password);
            putSession(session, officer.getId(), Role.OFFICER, officer.getFullName());
            return "redirect:" + (next != null && !next.isBlank() ? next : "/officer/dashboard");
        } catch (AuthException e) {
            model.addAttribute("tab", "officer");
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/login/admin")
    public String loginAdmin(@RequestParam String email, @RequestParam String password,
                              @RequestParam(required = false) String next,
                              HttpSession session, Model model) {
        try {
            Admin admin = authService.loginAdmin(email, password);
            putSession(session, admin.getId(), Role.ADMIN, admin.getFullName());
            return "redirect:" + (next != null && !next.isBlank() ? next : "/admin/dashboard");
        } catch (AuthException e) {
            model.addAttribute("tab", "admin");
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage() { return "auth/register"; }

    @PostMapping("/register")
    public String register(@RequestParam String fullName, @RequestParam String phone,
                            @RequestParam String nid, @RequestParam String dob,
                            @RequestParam String password, HttpSession session, Model model) {
        try {
            authService.beginCitizenRegistration(fullName, phone, nid, LocalDate.parse(dob), password);
            session.setAttribute("pendingPhone", phone);
            return "redirect:/otp";
        } catch (AuthException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/otp")
    public String otpPage(HttpSession session, Model model) {
        String phone = (String) session.getAttribute("pendingPhone");
        if (phone == null) return "redirect:/register";
        model.addAttribute("phone", phone);
        model.addAttribute("demoOtp", AuthService.DEMO_OTP);
        return "auth/otp";
    }

    @PostMapping("/otp/verify")
    public String verifyOtp(@RequestParam String otp, HttpSession session, Model model) {
        String phone = (String) session.getAttribute("pendingPhone");
        if (phone == null) return "redirect:/register";
        try {
            Citizen citizen = authService.confirmOtp(phone, otp);
            session.removeAttribute("pendingPhone");
            putSession(session, citizen.getId(), Role.CITIZEN, citizen.getFullName());
            return "redirect:/citizen/dashboard";
        } catch (AuthException e) {
            model.addAttribute("phone", phone);
            model.addAttribute("demoOtp", AuthService.DEMO_OTP);
            model.addAttribute("error", e.getMessage());
            return "auth/otp";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }

    private void putSession(HttpSession session, Long userId, Role role, String fullName) {
        session.setAttribute("userId", userId);
        session.setAttribute("role", role);
        session.setAttribute("fullName", fullName);
    }
}

   
