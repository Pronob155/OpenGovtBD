package com.opengovtbd.controller;

import com.opengovtbd.model.Admin;
import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Officer;
import com.opengovtbd.model.User;
import com.opengovtbd.service.AuthService;
import jakarta.servlet.http.HttpSession;

/** Small helper to resolve the logged-in domain object from the HTTP session. */
final class SessionUser {

    private SessionUser() {}

    static User require(HttpSession session, AuthService authService) {
        Long id = (Long) session.getAttribute("userId");
        if (id == null) throw new IllegalStateException("Not authenticated");
        return authService.findById(id).orElseThrow(() -> new IllegalStateException("Session user missing"));
    }

    static Citizen requireCitizen(HttpSession session, AuthService authService) {
        return (Citizen) require(session, authService);
    }

    static Officer requireOfficer(HttpSession session, AuthService authService) {
        return (Officer) require(session, authService);
    }

    static Admin requireAdmin(HttpSession session, AuthService authService) {
        return (Admin) require(session, authService);
    }
}
