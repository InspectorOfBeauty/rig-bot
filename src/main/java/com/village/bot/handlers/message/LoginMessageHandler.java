package com.village.bot.handlers.message;

import com.village.bot.handlers.RequestHandler;
import com.village.bot.components.AuthorizationComponent;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.Roles;
import com.village.bot.entities.UserSession;
import com.village.bot.view.MarkupFactory;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.Messages;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginMessageHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final MarkupFactory markupFactory;
    private final AuthorizationComponent authorizationComponent;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if ((userSession == null || !userSession.isAuthorized()) && userRequest.getText().split(" ").length == 2) {
            sender.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());

            String[] userData = userRequest.getText().split(" ");
            String login = userData[0].toLowerCase();
            String password = userData[1];

            Roles userRole = authorizationComponent.authorizeUser(userRequest.getChatId(), login, password);

            if (userRole != null) {
                sender.sendMessage(userRequest.getChatId(), view.getGreetingForRole(userRole), view.getKeyboardForRole(userRole));
            } else {
                sender.sendMessage(userRequest.getChatId(), Messages.INVALID_LOGIN_OR_PASSWORD, markupFactory.getInlineAuthButton());
            }

            return true;
        } else {
            return false;
        }
    }

}