package com.village.bot.handlers.callback;

import com.village.bot.components.GraphComponent;
import com.village.bot.components.RigComponent;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.RigData;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.GraphText;
import com.village.bot.view.KeyboardButtonNames;
import com.village.bot.view.UserActions;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class GraphicCallbackHandler implements RequestHandler {
    private final TelegramSender sender;
    private final View view;
    private final RigComponent rigComponent;
    private final UserSessionRepository userSessionRepository;
    private final GraphComponent graphComponent;

    @Override
    public boolean handle(UserRequest userRequest, UserSession userSession) {
        if (userSession != null && userSession.isAuthorized() && userRequest.getText().equals(KeyboardButtonNames.GRAPHIC)) {
            sender.deleteReplyMarkup(userRequest.getChatId(), userRequest.getMessageId());
            userSessionRepository.updateLastUserActionByChatId(userRequest.getChatId(), UserActions.PRESSED_GRAPHIC_BTN);

            int historyDepthInHours = 24;

            final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            List<RigData> history = rigComponent.getRigDataHistoryFilteredByIntervalAndPeriod(historyDepthInHours, now);

            Consumer<InputStream> imageSender = s -> {
                sender.sendPhoto(String.valueOf(userRequest.getChatId()),
                        s,
                        GraphText.GRAPH_FILE_NAME,
                        view.getKeyboardForRole(userSession.getUser().getRoleName()));
            };

            try {
                graphComponent.loadGraph(history, now, imageSender);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;
        } else {
            return false;
        }
    }
}
