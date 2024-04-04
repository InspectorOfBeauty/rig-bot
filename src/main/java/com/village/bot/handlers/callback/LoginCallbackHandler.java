package com.village.bot.handlers.callback;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.KeyboardButtonNames;
import com.village.bot.view.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginCallbackHandler implements RequestHandler {
    private final TelegramSender sender;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if ((userSession == null || !userSession.isAuthorized()) && userRequest.getText().equals(KeyboardButtonNames.AUTHORIZATION)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            sender.sendMessage(String.valueOf(userRequest.getChatId()), Messages.INPUT_USERDATA);
            return true;
        } else {
            return false;
        }
    }

}