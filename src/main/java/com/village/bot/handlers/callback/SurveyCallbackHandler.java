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
public class SurveyCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final RigComponent rigComponent;
    private final MarkupFactory markupFactory;
    private final UserSessionRepository userSessionRepository;
    private final ApplicationDataRepository applicationDataRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized() && userRequest.getText().equals(KeyboardButtonNames.STOP_START_SURVEY)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.PRESSED_STOP_START_SURVEY_BTN);

            if (applicationDataRepository.isSurveyEnabled()) {
                sender.sendMessage(userRequest.getChatId(), Messages.ACCEPT_STOP_SURVEY, markupFactory.getInlineYesNoButtons());
            } else {
                rigComponent.changeRigSurveySwitcher();
                sender.sendMessage(userRequest.getChatId(), Messages.START_SURVEY, markupFactory.getInlineAdministratorKeyboard());
            }

            return true;
        } else {
            return false;
        }
    }

}