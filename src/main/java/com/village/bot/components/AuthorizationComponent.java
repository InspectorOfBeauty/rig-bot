package com.village.bot.components;

import com.village.bot.entities.User;
import com.village.bot.entities.UserSession;
import com.village.bot.entities.Roles;
import com.village.bot.repositories.UserRepository;
import com.village.bot.repositories.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationComponent {
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    public Roles authorizeUser(long chatId, String login, String password) {
        User user = userRepository.findByLoginAndPassword(login, password);

        if (user != null) {
            UserSession userSession = userSessionRepository.findByChatIdAndUser(chatId, user);

            if (userSession != null) {
                userSessionRepository.makeUserSessionAuthorizedByChatIdAndUser(chatId, user);
            } else {
                userSessionRepository.createNewUserSession(chatId, user.getId());
            }

            return user.getRoleName();
        } else {
            return null;
        }
    }
}
