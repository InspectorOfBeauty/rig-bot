package com.village.bot.handlers.callback;

import com.village.bot.components.RigComponent;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.KeyboardButtonNames;
import com.village.bot.view.UserActions;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentStateCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final RigComponent rigComponent;
    private final UserSessionRepository userSessionRepository;


    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized() && userRequest.getText().equals(KeyboardButtonNames.CURRENT_STATE_OF_THE_FARM)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.PRESSED_CURRENT_STATE_OF_THE_FARM_BTN);
            sender.sendMessage(userRequest.getChatId(),
                    view.formatCurrentHashRates(rigComponent.loadRigData()),
                    view.getKeyboardForRole(userSession.getUser().getRoleName()));
            return true;
        } else {
            return false;
        }
    }

}