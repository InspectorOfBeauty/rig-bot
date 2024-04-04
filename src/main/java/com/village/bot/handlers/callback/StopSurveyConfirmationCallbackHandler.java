package com.village.bot.handlers.callback;

import com.village.bot.components.RigComponent;
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
public class StopSurveyConfirmationCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final RigComponent rigComponent;
    private final MarkupFactory markupFactory;
    private final UserSessionRepository userSessionRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized()
                && userSession.getLastUserAction().equals(UserActions.PRESSED_STOP_START_SURVEY_BTN)
                && userRequest.getText().equals(KeyboardButtonNames.YES)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.AGREED_STOP_START_SURVEY_BTN);
            rigComponent.changeRigSurveySwitcher();
            sender.sendMessage(userRequest.getChatId(), Messages.STOP_SURVEY, markupFactory.getInlineAdministratorKeyboard());
            return true;
        } else {
            return false;
        }
    }

}