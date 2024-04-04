package com.village.bot.telegram;

import com.village.bot.controller.UpdateController;
import com.village.bot.telegram.impl.TelegramLongPollingBotImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.Producer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramInitializer implements CommandLineRunner {
    private final TelegramLongPollingBotImpl bot;
    private final UpdateController controller;

    @Override
    public void run(String... args) throws Exception {
        bot.setUpdateListener(controller::onUpdate);

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("On connecting to server", e);
            throw e;
        }
    }
}
