package com.village.bot.telegram;


import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

public interface TelegramController {
    void setUpdateListener(Consumer<Update> listener);
}
