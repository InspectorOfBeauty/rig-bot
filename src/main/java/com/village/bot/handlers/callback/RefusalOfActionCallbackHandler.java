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
public class RefusalOfActionCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final UserSessionRepository userSessionRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized()
                && userRequest.getText().equals(KeyboardButtonNames.NO)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.REFUSED_ACTION);
            sender.sendMessage(userRequest.getChatId(), Messages.REFUSAL_OF_ACTION_FOR_SETTINGS, view.getKeyboardForRole(userSession.getUser().getRoleName()));
            return true;
        } else {
            return false;
        }
    }

}