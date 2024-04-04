package com.village.bot.handlers;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;

public interface RequestHandler {
    boolean handle(UserRequest userRequest, UserSession userSession);
}
