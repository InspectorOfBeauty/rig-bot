package com.village.bot.view;

public class Messages {
    public static final String WELCOME = "Здравствуйте. Вас приветствует \"Деревня-бот\". \n" +
            "Для дальнейшей работы c ботом необходимо авторизоваться.";
    public static final String INTRO_FOR_USER = "Поздравляем. Вы вошли в систему, и теперь вам доступны следующий возможности:";
    public static final String INTRO_FOR_ADMINISTRATOR = "Поздравляем. Вы вошли в систему как администратор, и теперь вам доступны следующий возможности:";

    public static final String UNAUTHORIZED = "Вы не авторизованы. Для работы с ботом необходимо авторизоваться.";

    public static final String INVALID_LOGIN_OR_PASSWORD = "Неверное имя пользователя или пароль.";
    public static final String INVALID_HOURS_AND_MINUTES = "Некорректно введины часы или минуты.";
    public static final String INVALID_INTERVAL = "Некорректный интервал.";
    public static final String INVALID_COEFFICIENT = "Некорректный коэффициент.";

    public static final String ACCEPT_STOP_SURVEY = "Вы точно хотите остановить опрос данных о ферме?";
    public static final String ACCEPT_CHANGES = "Вы точно хотите изменить его?";
    public static final String ACCEPT_LOG_OUT = "Вы уверены, что хотите выйти из системы?";

    public static final String REFUSAL_OF_ACTION_FOR_SETTINGS = "Вот и славно.";
    public static final String LOGGED_OUT = "Вы вышли из системы. Авторизуйтесь, если хотите продолжить работу с ботом.";
    public static final String STOP_SURVEY = "Сбор данных о ферме приостановлен. ";
    public static final String START_SURVEY = "Начат сбор данных о ферме.";

    public static final String INPUT_USERDATA = "Пожалуйста введите логин и пароль через ПРОБЕЛ в формате: \n \"логин пароль\"";
    public static final String INPUT_HOURS_AND_MINUTES = "Введите через ПРОБЕЛ количество целых часов, " +
            "за которые нужно вывести историю, и интервал в минутах в формате: \n \"часы минуты\"";
    public static final String INPUT_COEFFICIENT = "Введите дробное число в формате \"0.00\" не больше 1";
    public static final String INPUT_INTERVAL = "Введите целое число.";

    public static final String CHANGED_COEFFICIENT = "Коэффициент изменен: ";
    public static final String CHANGED_INTERVAL = "Интервал между повторными оповещениями изменен: ";

    public static final String UNKNOWN_COMMAND = "Простите, я вас не понимаю.";

    public static final String HISTORY_HEADER = "Текущий MH/s; За 24 часа MH/s; время\n";

    public static final String NOT_FOUND = "Записей не найдено!";
}
