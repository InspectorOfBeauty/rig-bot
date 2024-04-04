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
public class CoefficientCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final MarkupFactory markupFactory;
    private final UserSessionRepository userSessionRepository;
    private final ApplicationDataRepository applicationDataRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized() && userRequest.getText().equals(KeyboardButtonNames.CHANGE_COEFFICIENT)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.PRESSED_CHANGE_COEFFICIENT_BTN);
            sender.sendMessage(userRequest.getChatId(),
                    view.formatCurrentCoefficient(applicationDataRepository.getRigPowerDropCoefficient()),
                    markupFactory.getInlineYesNoButtons());
            return true;
        } else {
            return false;
        }
    }

}