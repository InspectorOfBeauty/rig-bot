package com.village.bot.handlers.message;

import com.village.bot.components.RigComponent;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.RigData;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.Messages;
import com.village.bot.view.UserActions;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryParamsMessageHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final RigComponent rigComponent;
    private final UserSessionRepository userSessionRepository;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized()
                && userSession.getLastUserAction().equals(UserActions.PRESSED_HISTORY_BTN)
                && userRequest.getText().split(" ").length == 2) {
            String[] hoursAndMinutes = userRequest.getText().split(" ");
            String firstParam = hoursAndMinutes[0];
            String secondParam = hoursAndMinutes[1];

            final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            try {
                int historyDepthInHours = Integer.parseInt(firstParam);
                int intervalInMinutes = Integer.parseInt(secondParam);

                userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.ENTERED_HOURS_AND_INTERVAL);

                List<RigData> history = rigComponent.getRigDataHistoryFilteredByIntervalAndPeriod(historyDepthInHours, now);

                String stringOfHistory = view.formatHistory(history, historyDepthInHours, intervalInMinutes);
                int limit = 4000;
                StringBuilder result = new StringBuilder();

                if(stringOfHistory.endsWith(Messages.HISTORY_HEADER)) {
                    sender.sendMessage(userRequest.getChatId(),
                            Messages.NOT_FOUND,
                            view.getKeyboardForRole(userSession.getUser().getRoleName()));
                } else {
                    for(String line : stringOfHistory.split("\n")) {
                        if(result.length() + line.length() <= limit) {
                            result.append(line).append("\n");
                        } else {
                            sender.sendMessage(userRequest.getChatId(),
                                    result.toString(),
                                    view.getKeyboardForRole(userSession.getUser().getRoleName()));
                            result = new StringBuilder(line);
                        }
                    }

                    if(!result.isEmpty()) {
                        sender.sendMessage(userRequest.getChatId(),
                                result.toString(),
                                view.getKeyboardForRole(userSession.getUser().getRoleName()));
                    }
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(userRequest.getChatId(),
                        Messages.INVALID_HOURS_AND_MINUTES,
                        view.getKeyboardForRole(userSession.getUser().getRoleName()));
            }

            return true;
        } else {
            return false;
        }
    }

}