package com.opengovtbd.controller;

import com.opengovtbd.model.Role;
import com.opengovtbd.service.NotificationService;
import com.opengovtbd.service.TextRenderer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAttributes {

    private final NotificationService notificationService;
    private final TextRenderer textRenderer;

    public GlobalModelAttributes(NotificationService notificationService, TextRenderer textRenderer) {
        this.notificationService = notificationService;
        this.textRenderer = textRenderer;
    }

    /** How many notifications the topbar dropdown previews before "View all". */
    private static final int TOPBAR_NOTIFICATION_PREVIEW = 4;

    @ModelAttribute
    public void addSharedAttributes(HttpServletRequest request, Model model) {
        model.addAttribute("textRenderer", textRenderer);
        HttpSession session = request.getSession(false);
        if (session == null) return;
        Object role = session.getAttribute("role");
        Object userId = session.getAttribute("userId");
        if (role instanceof Role r && userId instanceof Long id) {
            model.addAttribute("unreadCount", notificationService.unreadCount(id));
            // The topbar dropdown used to render three hard-coded sample
            // notifications for every user; it now previews the real feed.
            model.addAttribute("recentNotifications", notificationService.forUser(id).stream()
                    .limit(TOPBAR_NOTIFICATION_PREVIEW)
                    .toList());
            model.addAttribute("currentRole", r.name());
        }
    }
}
