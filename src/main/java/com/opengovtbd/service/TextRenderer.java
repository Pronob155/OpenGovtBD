package com.opengovtbd.service;

import com.opengovtbd.model.User;
import com.opengovtbd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextRenderer {

    private static final Pattern MENTION = Pattern.compile("@([a-zA-Z0-9._-]{2,40})");

    private final UserRepository userRepository;

    public TextRenderer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String render(String rawContent) {
        if (rawContent == null || rawContent.isBlank()) return "";
        String escaped = HtmlUtils.htmlEscape(rawContent).replace("\n", "<br/>");
        Matcher matcher = MENTION.matcher(escaped);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String handle = matcher.group(1);
            User user = userRepository.findByUsername(handle).orElse(null);
            String replacement;
            if (user != null) {
                replacement = "<a href=\"/users/" + user.getId() + "\" class=\"mention\" data-mention-preview=\"/users/" + user.getId() + "/preview\">@" + handle + "</a>";
            } else {
                replacement = matcher.group(0);
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
