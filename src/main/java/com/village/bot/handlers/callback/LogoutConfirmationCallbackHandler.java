package com.village.bot.handlers.callback;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutConfirmationCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final UserSessionRepository userSessionRepository;
    private final MarkupFactory markupFactory;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized()
                && userSession.getLastUserAction().equals(UserActions.PRESSED_LOGOUT_BTN)
                && userRequest.getText().equals(KeyboardButtonNames.YES)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.makeUserSessionUnauthorizedByChatId(userRequest.getChatId());
            sender.sendMessage(userRequest.getChatId(), Messages.LOGGED_OUT, markupFactory.getInlineAuthButton());
            return true;
        } else {
            return false;
        }
    }

}