package com.village.bot.services;

import com.village.bot.handlers.RequestHandler;
import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.repositories.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestProcessingService {
    private final List<RequestHandler> handlers;
    private final UserSessionRepository userSessionRepository;

    public void process(UserRequest request) {
        UserSession userSession = userSessionRepository.getAuthorizedUserSessionByChatId(request.getChatId());

        for (RequestHandler handler : handlers) {
            if (handler.handle(request, userSession)) {
                return;
            }
        }
    }
}
