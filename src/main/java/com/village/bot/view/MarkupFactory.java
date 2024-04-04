package com.village.bot.view;

import com.village.bot.view.KeyboardButtonNames;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkupFactory {
    public InlineKeyboardMarkup getInlineAuthButton(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton authorizationBtn = new InlineKeyboardButton();

        authorizationBtn.setText(KeyboardButtonNames.AUTHORIZATION);
        authorizationBtn.setCallbackData(KeyboardButtonNames.AUTHORIZATION);

        keyboardRow.add(authorizationBtn);
        keyboard.add(keyboardRow);
        markup.setKeyboard(keyboard);

        return markup;
    }

    public InlineKeyboardMarkup getInlineYesNoButtons(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton yes = new InlineKeyboardButton();
        InlineKeyboardButton no = new InlineKeyboardButton();

        yes.setText(KeyboardButtonNames.YES);
        yes.setCallbackData(KeyboardButtonNames.YES);

        no.setText(KeyboardButtonNames.NO);
        no.setCallbackData(KeyboardButtonNames.NO);

        keyboardRow.add(yes);
        keyboardRow.add(no);
        keyboard.add(keyboardRow);
        markup.setKeyboard(keyboard);

        return markup;
    }

    public InlineKeyboardMarkup getInlineAdministratorKeyboard(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow4 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow5 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow6 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow7 = new ArrayList<>();

        InlineKeyboardButton graphic = new InlineKeyboardButton();
        graphic.setText(KeyboardButtonNames.GRAPHIC);
        graphic.setCallbackData(KeyboardButtonNames.GRAPHIC);

        InlineKeyboardButton currentStateOfTheFarm = new InlineKeyboardButton();
        currentStateOfTheFarm.setText(KeyboardButtonNames.CURRENT_STATE_OF_THE_FARM);
        currentStateOfTheFarm.setCallbackData(KeyboardButtonNames.CURRENT_STATE_OF_THE_FARM);

        InlineKeyboardButton history = new InlineKeyboardButton();
        history.setText(KeyboardButtonNames.HISTORY);
        history.setCallbackData(KeyboardButtonNames.HISTORY);

        InlineKeyboardButton stopOrStopSurvey = new InlineKeyboardButton();
        stopOrStopSurvey.setText(KeyboardButtonNames.STOP_START_SURVEY);
        stopOrStopSurvey.setCallbackData(KeyboardButtonNames.STOP_START_SURVEY);

        InlineKeyboardButton changeCoefficient = new InlineKeyboardButton();
        changeCoefficient.setText(KeyboardButtonNames.CHANGE_COEFFICIENT);
        changeCoefficient.setCallbackData(KeyboardButtonNames.CHANGE_COEFFICIENT);

        InlineKeyboardButton changeIntervalBetweenRepeatedNotification = new InlineKeyboardButton();
        changeIntervalBetweenRepeatedNotification.setText(KeyboardButtonNames.CHANGE_INTERVAL_BETWEEN_REPEATED_NOTIFICATIONS);
        changeIntervalBetweenRepeatedNotification.setCallbackData(KeyboardButtonNames.CHANGE_INTERVAL_BETWEEN_REPEATED_NOTIFICATIONS);

        InlineKeyboardButton logout = new InlineKeyboardButton();
        logout.setText(KeyboardButtonNames.LOGOUT);
        logout.setCallbackData(KeyboardButtonNames.LOGOUT);

        keyboardRow1.add(graphic);
        keyboardRow2.add(currentStateOfTheFarm);
        keyboardRow3.add(history);
        keyboardRow4.add(stopOrStopSurvey);
        keyboardRow5.add(changeCoefficient);
        keyboardRow6.add(changeIntervalBetweenRepeatedNotification);
        keyboardRow7.add(logout);

        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);
        keyboard.add(keyboardRow4);
        keyboard.add(keyboardRow5);
        keyboard.add(keyboardRow6);
        keyboard.add(keyboardRow7);

        markup.setKeyboard(keyboard);

        return markup;
    }

    public InlineKeyboardMarkup getInlineUserKeyboard(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow4 = new ArrayList<>();

        InlineKeyboardButton graphic = new InlineKeyboardButton();
        graphic.setText(KeyboardButtonNames.GRAPHIC);
        graphic.setCallbackData(KeyboardButtonNames.GRAPHIC);

        InlineKeyboardButton currentStateOfTheFarm = new InlineKeyboardButton();
        currentStateOfTheFarm.setText(KeyboardButtonNames.CURRENT_STATE_OF_THE_FARM);
        currentStateOfTheFarm.setCallbackData(KeyboardButtonNames.CURRENT_STATE_OF_THE_FARM);

        InlineKeyboardButton history = new InlineKeyboardButton();
        history.setText(KeyboardButtonNames.HISTORY);
        history.setCallbackData(KeyboardButtonNames.HISTORY);

        InlineKeyboardButton logout = new InlineKeyboardButton();
        logout.setText(KeyboardButtonNames.LOGOUT);
        logout.setCallbackData(KeyboardButtonNames.LOGOUT);

        keyboardRow1.add(graphic);
        keyboardRow2.add(currentStateOfTheFarm);
        keyboardRow3.add(history);
        keyboardRow4.add(logout);


        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);
        keyboard.add(keyboardRow4);

        markup.setKeyboard(keyboard);

        return markup;
    }
}
