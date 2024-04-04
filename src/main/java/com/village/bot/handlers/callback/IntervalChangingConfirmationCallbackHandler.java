package com.village.bot.handlers.callback;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.ApplicationDataRepository;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntervalChangingConfirmationCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final UserSessionRepository userSessionRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized()
                && userSession.getLastUserAction().equals(UserActions.PRESSED_CHANGE_REPEATED_NOTIFICATION_BTN)
                && userRequest.getText().equals(KeyboardButtonNames.YES)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.AGREED_CHANGE_REPEATED_NOTIFICATION_BTN);
            sender.sendMessage(String.valueOf(userRequest.getChatId()), Messages.INPUT_INTERVAL);
            return true;
        } else {
            return false;
        }
    }

}