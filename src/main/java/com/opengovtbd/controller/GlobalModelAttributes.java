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

    @ModelAttribute
    public void addSharedAttributes(HttpServletRequest request, Model model) {
        model.addAttribute("textRenderer", textRenderer);
        HttpSession session = request.getSession(false);
        if (session == null) return;
        Object role = session.getAttribute("role");
        Object userId = session.getAttribute("userId");
        if (role instanceof Role && userId instanceof Long id) {
            model.addAttribute("unreadCount", notificationService.unreadCount(id));
        }
    }
}
