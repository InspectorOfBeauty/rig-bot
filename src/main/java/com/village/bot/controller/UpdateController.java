package com.village.bot.controller;

import com.village.bot.dto.UserRequest;
import com.village.bot.services.RequestProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@RequiredArgsConstructor
public class UpdateController {
    private final RequestProcessingService requestProcessingService;

    public void onUpdate(Update update) {
        requestProcessingService.process(convertRequest(update));
    }

    private static UserRequest convertRequest(Update update) {
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        final long chatId;
        final int messageId;
        final String messageText;

        if (message != null) {
            chatId = message.getChatId();
            messageId = message.getMessageId();
            messageText = message.getText().trim();
        } else if (callbackQuery != null) {
            chatId = callbackQuery.getMessage().getChatId();
            messageId = callbackQuery.getMessage().getMessageId();
            messageText = callbackQuery.getData();
        } else {
            throw new RuntimeException("It failed to convert a request.!");
        }

        return new UserRequest(message != null, chatId, messageId, messageText);
    }
}
