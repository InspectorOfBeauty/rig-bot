package com.village.bot.handlers.callback;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.KeyboardButtonNames;
import com.village.bot.view.Messages;
import com.village.bot.view.UserActions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final UserSessionRepository userSessionRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized() && userRequest.getText().equals(KeyboardButtonNames.HISTORY)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.PRESSED_HISTORY_BTN);
            sender.sendMessage(String.valueOf(userRequest.getChatId()), Messages.INPUT_HOURS_AND_MINUTES);
            return true;
        } else {
            return false;
        }
    }

}