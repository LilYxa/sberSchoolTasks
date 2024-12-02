package ru.sberSchool.tasks;

public class Constants {

    public static final String MILLISECOND = " мс";

    public static final int LOCK_TIME_SECONDS = 10;
    public static final int PIN_LENGTH = 4;
    public static final int AMOUNT_MULTIPLICITY = 100;

    public static final String ACCOUNT_LOCKED_MESSAGE = "Ваш аккаунт заблокирован. Осталось ждать %d секунд";
    public static final String INVALID_AMOUNT_MESSAGE = "Сумма должна быть кратна 100.";
    public static final String THREE_ATTEMPTS_MESSAGE = "Три неверных попытки. Аккаунт заблокирован.";
    public static final String ONLY_DIGIT_MESSAGE = "Ожидается ввод только цифр.";
    public static final String INVALID_PIN_MESSAGE = "Введен некорректный пин-код!";
    public static final String SERVER_CODE_RESPONSE_MESSAGE = "Сервер вернул код ответа: %s";

    public static final String NON_NEGATIVE_NUMBER_MESSAGE = "Number must be non-negative.";
    public static final String FAILED_TO_ACCESS_FIELD = "Failed to access field: %s";
    public static final String ASSIGN_EXCEPTION_MESSAGE = "Error during assigning!";

    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";

}
