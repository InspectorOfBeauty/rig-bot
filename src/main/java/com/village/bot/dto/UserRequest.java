package com.village.bot.dto;

import lombok.Data;

@Data
public class UserRequest {
    private final boolean isMessage;
    private final long chatId;
    private final int messageId;
    private final String text;
}
