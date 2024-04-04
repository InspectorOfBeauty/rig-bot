package com.village.bot.telegram.impl;

import com.village.bot.telegram.TelegramController;
import com.village.bot.telegram.TelegramSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.function.Consumer;

@Component
@Slf4j
public class TelegramLongPollingBotImpl extends TelegramLongPollingBot implements TelegramSender, TelegramController {
    private final String botName;
    volatile private Consumer<Update> updateListener;

    public TelegramLongPollingBotImpl(@Value("${telegram.bot.token}") String botToken,
                                      @Value("${telegram.bot.name}") String botName) {
        super(botToken);
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (updateListener != null) {
            try {
                updateListener.accept(update);
            } catch (Exception e) {
                log.error("Unexpected exception", e);
            }
        }
    }

    @Override
    public void setUpdateListener(Consumer<Update> listener) {
        updateListener = listener;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sendMessage(String chatId, String text) {
        try {
            execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(long chatId, String text, ReplyKeyboard keyboardMarkup) {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
            sendMessage.setReplyMarkup(keyboardMarkup);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPhoto(String chatId, InputStream inputStream, String photoName, ReplyKeyboard keyboard) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(inputStream, "График"));
            sendPhoto.setReplyMarkup(keyboard);
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessage(long chatId, int messageId) {
        try {
            execute(new DeleteMessage(String.valueOf(chatId), messageId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReplyMarkup(long chatId, int messageId) {
        try {
            EditMessageReplyMarkup messageReplyMarkup = new EditMessageReplyMarkup();
            messageReplyMarkup.setChatId(chatId);
            messageReplyMarkup.setMessageId(messageId);
            execute(messageReplyMarkup);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
