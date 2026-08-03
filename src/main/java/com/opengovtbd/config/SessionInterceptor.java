package com.opengovtbd.config;

import com.opengovtbd.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Simple, dependency-light Role-Based Access Control (RBAC) guard.
 * Protects /citizen/**, /officer/** and /admin/** based on the role
 * stored in the HTTP session at login time.
 */
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);
        Role role = session == null ? null : (Role) session.getAttribute("role");

        Role required = null;
        if (path.startsWith("/citizen/")) required = Role.CITIZEN;
        else if (path.startsWith("/officer/")) required = Role.OFFICER;
        else if (path.startsWith("/admin/")) required = Role.ADMIN;

        if (required == null) return true;

        if (role != required) {
            response.sendRedirect("/login?next=" + path);
            return false;
        }
        return true;
    }
}
