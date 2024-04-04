package com.village.bot.handlers.message;

import com.village.bot.components.AuthorizationComponent;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.Roles;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.MarkupFactory;
import com.village.bot.view.Messages;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnauthorizedUserMessageHandler implements RequestHandler {
    private final TelegramSender sender;
    private final MarkupFactory markupFactory;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession == null || !userSession.isAuthorized()) {
            sender.sendMessage(userRequest.getChatId(), Messages.UNAUTHORIZED, markupFactory.getInlineAuthButton());
            return true;
        } else {
            return false;
        }
    }

}