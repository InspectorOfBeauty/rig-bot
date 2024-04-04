package com.village.bot.components;

import com.village.bot.entities.ApplicationData;
import com.village.bot.entities.UserSession;
import com.village.bot.repositories.ApplicationDataRepository;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.telegram.TelegramSender;
import com.village.bot.view.View;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
@RequiredArgsConstructor
public class Notificator {
    private final TelegramSender sender;
    private final RigComponent rigComponent;
    private final View view;
    private final UserSessionRepository userSessionRepository;
    private final ApplicationDataRepository applicationDataRepository;

    public void notifyIfNeeded() {
        ApplicationData applicationData = applicationDataRepository.getData();

        int notificationInterval = applicationData.getNotificationInterval();
        double rigPowerDropCoefficient = applicationData.getRigPowerDropCoefficient();

        boolean isRigOffline = rigComponent.isRigOffline();
        LocalDateTime sinceRigOffline = applicationData.getSinceRigOffline();
        LocalDateTime prevRigOfflineNotification = applicationData.getPrevRigOfflineNotification();

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);


        boolean isRigPowerDropped = rigComponent.isRigPowerDropped(rigPowerDropCoefficient);
        LocalDateTime sinceRigDrop = applicationData.getSinceRigDrop();
        LocalDateTime prevRigDropNotification = applicationData.getPrevRigDropNotification();

        boolean isTimeToNotifyAboutRigOffline = (prevRigOfflineNotification != null)
                && ChronoUnit.MINUTES.between(prevRigOfflineNotification, now) >= notificationInterval;

        boolean isTimeToNotifyAboutRigDrop = (prevRigDropNotification != null)
                && ChronoUnit.MINUTES.between(prevRigDropNotification, now) >= notificationInterval;

        if (isRigOffline) {
            if (sinceRigOffline == null) {
                LocalDateTime newSinceOffline = now.minusMinutes(3);

                applicationData.setSinceRigOffline(newSinceOffline);
                applicationData.setPrevRigOfflineNotification(now);

                List<UserSession> allUserSessions = userSessionRepository.getAllAuthorizedUserSession();

                for (UserSession userSession : allUserSessions) {
                    sender.sendMessage(String.valueOf(userSession.getChatId()), view.formatOffline(newSinceOffline));
                }
            } else {
                if (isTimeToNotifyAboutRigOffline) {
                    applicationData.setPrevRigOfflineNotification(now);

                    List<UserSession> allUserSessions = userSessionRepository.getAllAuthorizedUserSession();

                    int offlineMinutes = (int) ChronoUnit.MINUTES.between(sinceRigOffline, now);
                    for (UserSession userSession : allUserSessions) {
                        sender.sendMessage(String.valueOf(userSession.getChatId()), view.formatOfflineTime(sinceRigOffline, offlineMinutes));
                    }
                }
            }
        } else {
            if (sinceRigOffline != null) {
                applicationData.setSinceRigOffline(null);
                applicationData.setPrevRigOfflineNotification(null);
            }

            if (isRigPowerDropped) {
                if (sinceRigDrop == null) {
                    LocalDateTime newSinceDrop = now.minusMinutes(3);
                    applicationData.setSinceRigDrop(newSinceDrop);
                    applicationData.setPrevRigDropNotification(now);

                    for (UserSession userSession : userSessionRepository.getAllAuthorizedUserSession()) {
                        sender.sendMessage(String.valueOf(userSession.getChatId()), view.formatDrop(newSinceDrop));
                    }
                } else {
                    if (isTimeToNotifyAboutRigDrop) {
                        applicationData.setPrevRigDropNotification(now);

                        int dropMinutes = (int) ChronoUnit.MINUTES.between(sinceRigDrop, now);
                        for (UserSession userSession : userSessionRepository.getAllAuthorizedUserSession()) {
                            sender.sendMessage(String.valueOf(userSession.getChatId()), view.formatDropTime(sinceRigDrop, dropMinutes));
                        }
                    }
                }
            } else {
                if (sinceRigDrop != null) {
                    applicationData.setSinceRigDrop(null);
                    applicationData.setPrevRigDropNotification(null);
                }
            }
        }

        applicationDataRepository.save(applicationData);
    }
}
