package com.village.bot.repositories;

import com.village.bot.view.KeyboardButtonNames;
import com.village.bot.entities.User;
import com.village.bot.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    UserSession findByChatIdAndUser(long chatId, User user);

    @Query("SELECT us FROM UserSession us WHERE us.chatId = :chatId AND us.isAuthorized = true")
    UserSession getAuthorizedUserSessionByChatId(@Param("chatId") long chatId);

    @Query("SELECT us FROM UserSession us WHERE us.isAuthorized = true")
    List<UserSession> getAllAuthorizedUserSession();

    @Modifying
    @Query("UPDATE UserSession us SET us.isAuthorized = true  WHERE us.chatId = :chatId AND us.user = :user")
    void makeUserSessionAuthorizedByChatIdAndUser(@Param("chatId") long chatId, @Param("user") User user);

    @Modifying
    @Query("UPDATE UserSession us SET " +
            "us.lastUserAction = 'Нажал на кнопку" + KeyboardButtonNames.YES + "при выходе из системы', " +
            "us.isAuthorized = false " +
            "WHERE us.chatId = :chatId")
    void makeUserSessionUnauthorizedByChatId(@Param("chatId") long chatId);

    @Modifying
    @Query("UPDATE UserSession us SET " +
            "us.lastUserAction = :lastUserAction " +
            "WHERE us.chatId = :chatId")
    void updateLastUserActionByChatId(@Param("chatId") long chatId, @Param("lastUserAction") String lastUserAction);

    @Modifying
    @Query(value = "INSERT INTO user_sessions (chat_id, user_id, last_user_action, user_auth_status) " +
            "VALUES(:chatId, :userId, 'Авторизовался', true)", nativeQuery = true)
    void createNewUserSession(@Param("chatId") long chatId, @Param("userId") int userId);
}

