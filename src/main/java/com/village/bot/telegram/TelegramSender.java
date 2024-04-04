package com.village.bot.telegram;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.InputStream;

public interface TelegramSender {
    void sendMessage(String chatId, String text);
    void sendMessage(long chatId, String text, ReplyKeyboard keyboard);
    void sendPhoto(String chatId, InputStream inputStream, String photoName, ReplyKeyboard keyboard);
    void deleteMessage(long chatId, int messageId);
    void deleteReplyMarkup(long chatId, int messageId);
}
