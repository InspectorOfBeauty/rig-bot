package com.village.bot.view;

import com.village.bot.entities.RigData;
import com.village.bot.entities.Roles;
import com.village.bot.entities.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.village.bot.util.HashUtil.hashToMegaHash;

@Component
@RequiredArgsConstructor
public class View {
    private final DateTimeFormatter dataTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //Telegram-Bot isn't allowed to send message which is longer than 4092 symbols.
    @Value("${telegram.message.size.max}")
    private final int limitOnCharactersInMessage;

    private final MarkupFactory markupFactory;

    public String formatCurrentHashRates(RigData rigData) {
        return "Текущее состояние фермы:\n" + formatHashRates(rigData);
    }

    public String formatHistory(List<RigData> rigDataList, int historyDepthInHours, int intervalInMinutes) {
        StringBuilder result = new StringBuilder(String.format("История за %s ч, интервал %s мин:\nТекущий MH/s; За 24 часа MH/s; время\n", historyDepthInHours, intervalInMinutes));

        if (!rigDataList.isEmpty()) {
            LocalDateTime counter = null;
            for (RigData rigData : rigDataList) {
                if (result.length() <= limitOnCharactersInMessage) {
                    if ((counter == null) || (ChronoUnit.MINUTES.between(rigData.getDataTime(), counter) >= intervalInMinutes)) {
                        result.append(formatHistoryLine(rigData));
                        counter = rigData.getDataTime();
                    }
                } else {
                    break;
                }
            }
        }

        return result.toString();
    }

    private String formatHistoryLine(RigData rigData) {
        if (rigData.getStatus() == RequestStatus.RIG_ONLINE) {
            return String.format("%.2f; %.2f; %s\n",
                    hashToMegaHash(rigData.getHashRate()),
                    hashToMegaHash(rigData.getDayHashRate()),
                    rigData.getDataTime().format(dataTimeFormat)
            );
        } else {
            return String.format("%s; %s\n",
                    rigData.getStatus().toString(),
                    rigData.getDataTime().format(dataTimeFormat)
            );
        }

    }

    public String formatCurrentCoefficient(double currentValue) {
        return String.format("На данный момент коэффициент равен: %.2f. %s",
                currentValue,
                Messages.ACCEPT_CHANGES);
    }

    public String formatChangedCoefficient(double oldValue, double newValue) {
        return String.format("%s %.2f --> %.2f", Messages.CHANGED_COEFFICIENT, oldValue, newValue);
    }

    public String formatCurrentNotificationInterval(int currentValue) {
        return String.format("На данный момент интервал между оповещениями равен: %d минут. %s",
                currentValue,
                Messages.ACCEPT_CHANGES);
    }

    public String formatChangedNotificationInterval(int oldValue, int newValue) {
        return String.format("%s %d --> %d минут", Messages.CHANGED_INTERVAL, oldValue, newValue);
    }

    public String formatOffline(LocalDateTime fromTime) {
        return String.format("WARNING: Ферма недоступна с %s", fromTime.format(dataTimeFormat));
    }

    public String formatOfflineTime(LocalDateTime fromTime, int minutes) {
        int hours = minutes / 60;
        int minutesOfHour = minutes % 60;

        return String.format("WARNING: Ферма недоступна с %s в течение %o часов %o минут",
                fromTime.format(dataTimeFormat), hours, minutesOfHour);
    }

    public String formatDropTime(LocalDateTime fromTime, int minutes) {
        int hours = minutes / 60;
        int minutesOfHour = minutes % 60;

        return String.format("WARNING: Падение мощности с %s в течение %o часов %o минут",
                fromTime.format(dataTimeFormat), hours, minutesOfHour);
    }

    public String formatDrop(LocalDateTime fromTime) {
        return String.format("WARNING: Падение мощности с %s", fromTime.format(dataTimeFormat));
    }

    public InlineKeyboardMarkup getKeyboardForRole(Roles role) {
        if (role == Roles.USER) {
            return markupFactory.getInlineUserKeyboard();
        } else {
            return markupFactory.getInlineAdministratorKeyboard();
        }
    }

    public String getGreetingForRole(Roles role) {
        if (role == Roles.USER) {
            return Messages.INTRO_FOR_USER;
        } else {
            return Messages.INTRO_FOR_ADMINISTRATOR;
        }
    }

    private String formatHashRates(RigData rigData) {
        if (rigData.getStatus() == RequestStatus.RIG_ONLINE) {
            return String.format("%.2f MH/s; 24 часа: %.2f MH/s",
                    hashToMegaHash(rigData.getHashRate()),
                    hashToMegaHash(rigData.getDayHashRate())
            );
        } else {
            return rigData.getStatus().toString();
        }
    }
}
