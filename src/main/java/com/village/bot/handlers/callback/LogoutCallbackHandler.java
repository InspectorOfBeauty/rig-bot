package com.village.bot.handlers.callback;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.KeyboardButtonNames;
import com.village.bot.view.MarkupFactory;
import com.village.bot.view.Messages;
import com.village.bot.view.UserActions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final MarkupFactory markupFactory;
    private final UserSessionRepository userSessionRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized() && userRequest.getText().equals(KeyboardButtonNames.LOGOUT)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.PRESSED_LOGOUT_BTN);
            sender.sendMessage(userRequest.getChatId(), Messages.ACCEPT_LOG_OUT, markupFactory.getInlineYesNoButtons());
            return true;
        } else {
            return false;
        }
    }

}