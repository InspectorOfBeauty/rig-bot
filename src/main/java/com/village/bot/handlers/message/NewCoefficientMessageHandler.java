package com.village.bot.handlers.message;

import com.village.bot.components.RigComponent;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.ApplicationDataRepository;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.MarkupFactory;
import com.village.bot.view.Messages;
import com.village.bot.view.UserActions;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewCoefficientMessageHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final MarkupFactory markupFactory;
    private final UserSessionRepository userSessionRepository;
    private final ApplicationDataRepository applicationDataRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized()
                && userSession.getLastUserAction().equals(UserActions.AGREED_CHANGE_COEFFICIENT_BTN)) {
            try {
                double value = Double.parseDouble(userRequest.getText().replace(',', '.'));
                if (value > 1) {
                    // bad practice :(
                    throw new NumberFormatException();
                }
                sender.sendMessage(userRequest.getChatId(),
                        view.formatChangedCoefficient(applicationDataRepository.getRigPowerDropCoefficient(), value),
                        markupFactory.getInlineAdministratorKeyboard());
                applicationDataRepository.updateRigPowerDropCoefficient(value);
                userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.CHANGED_COEFFICIENT);
            } catch (NumberFormatException e) {
                sender.sendMessage(userRequest.getChatId(), Messages.INVALID_COEFFICIENT, markupFactory.getInlineAdministratorKeyboard());
            }

            return true;
        } else {
            return false;
        }
    }

}