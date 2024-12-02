package task4;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task4.exceptions.AccountIsLockedException;
import ru.sberSchool.tasks.task4.exceptions.InsufficientFundsException;
import ru.sberSchool.tasks.task4.exceptions.InvalidAmountException;
import ru.sberSchool.tasks.task4.exceptions.InvalidPinException;
import ru.sberSchool.tasks.task4.service.PinValidator;
import ru.sberSchool.tasks.task4.service.TerminalServer;
import ru.sberSchool.tasks.task4.service.impl.TerminalImpl;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TerminalImplTest {

    private TerminalImpl terminal;
    private TerminalServer terminalServer;
    private PinValidator pinValidator;

    @BeforeEach
    public void setUp() {
        log.debug("setUp[0]: Инициализация компонентов");
        terminalServer = new TerminalServer();
        pinValidator = new PinValidator("0000");
        terminal = new TerminalImpl(terminalServer, pinValidator);
    }

    @Test
    public void testEnterPin_Success() {
        log.debug("testEnterPin_Success[0]: Начало теста");
        char[] pin = {'1', '2', '3', '4'};
        for (char digit : pin) {
            log.debug("testEnterPin_Success[1]: Ввод цифры пин-кода: {}", digit);
            assertDoesNotThrow(() -> terminal.enterPin(digit));
        }
        log.debug("testEnterPin_Success[2]: Пин-код успешно принят");
    }

    @Test
    public void testEnterPin_InvalidDigit() {
        log.debug("testEnterPin_InvalidDigit[0]: Начало теста");
        char invalidDigit = 'a';
        log.debug("testEnterPin_InvalidDigit[1]: Ввод неверного символа: {}", invalidDigit);
        Exception exception = assertThrows(InvalidPinException.class, () -> terminal.enterPin(invalidDigit));
        log.debug("testEnterPin_InvalidDigit[2]: Получено исключение: {}", exception.getMessage());
    }

    @Test
    public void testEnterPin_LockAfterThreeFailures() {
        log.debug("testEnterPin_LockAfterThreeFailures[0]: Начало теста");

        // Массив неверных пин-кодов
        String[] wrongPins = {"1234", "5678", "9101"};

        // Проверяем первую и вторую попытки (должно выбрасывать InvalidPinException только после ввода последнего символа)
        for (int attempt = 0; attempt < 2; attempt++) {
            String pin = wrongPins[attempt];
            log.debug("testEnterPin_LockAfterThreeFailures[1]: Ввод неверного пин-кода (попытка {}): {}", attempt + 1, pin);
            for (int i = 0; i < pin.length(); i++) {
                char digit = pin.charAt(i);
                if (i < pin.length() - 1) {
                    // Для первых символов PIN-кода исключений не будет
                    assertDoesNotThrow(() -> terminal.enterPin(digit));
                } else {
                    // На последнем символе выбрасывается InvalidPinException
                    Exception exception = assertThrows(InvalidPinException.class, () -> terminal.enterPin(digit));
                    log.debug("testEnterPin_LockAfterThreeFailures[2]: Исключение на попытке {} после ввода {}: {}", attempt + 1, digit, exception.getMessage());
                }
            }
        }

        // Проверяем третью попытку, которая должна привести к блокировке
        String thirdWrongPin = wrongPins[2];
        log.debug("testEnterPin_LockAfterThreeFailures[3]: Ввод неверного пин-кода (попытка 3): {}", thirdWrongPin);
        for (int i = 0; i < thirdWrongPin.length(); i++) {
            char digit = thirdWrongPin.charAt(i);
            if (i < thirdWrongPin.length() - 1) {
                // Для первых символов исключений не будет
                assertDoesNotThrow(() -> terminal.enterPin(digit));
            } else {
                // На последнем символе выбрасывается AccountIsLockedException
                Exception exception = assertThrows(AccountIsLockedException.class, () -> terminal.enterPin(digit));
                log.debug("testEnterPin_LockAfterThreeFailures[4]: Аккаунт заблокирован после ввода {}: {}", digit, exception.getMessage());
            }
        }

        // Убеждаемся, что после блокировки любые попытки выбрасывают AccountIsLockedException
        log.debug("testEnterPin_LockAfterThreeFailures[5]: Проверка блокировки аккаунта");
        Exception lockException = assertThrows(AccountIsLockedException.class, () -> terminal.enterPin('1'));
        log.debug("testEnterPin_LockAfterThreeFailures[6]: Исключение при заблокированном аккаунте: {}", lockException.getMessage());
    }


    @Test
    public void testCheckBalance_Success() throws AccountIsLockedException, InvalidAmountException {
        log.debug("testCheckBalance_Success[0]: Начало теста");
        terminal.depositMoney(1000);
        double balance = terminal.checkBalance();
        log.debug("testCheckBalance_Success[1]: Проверка баланса. Баланс: {}", balance);
        assertEquals(1000.0, balance, "Баланс должен быть равен 1000 при инициализации");
    }

    @Test
    public void testDepositMoney_Success() throws InvalidAmountException, AccountIsLockedException {
        log.debug("testDepositMoney_Success[0]: Начало теста");
        int depositAmount = 500;
        log.debug("testDepositMoney_Success[1]: Внесение на счет суммы: {}", depositAmount);
        int deposited = terminal.depositMoney(depositAmount);
        log.debug("testDepositMoney_Success[2]: Сумма успешно внесена. Итог: {}", deposited);
        assertEquals(depositAmount, deposited, "Внесенная сумма должна совпадать");
    }

    @Test
    public void testDepositMoney_InvalidAmount() {
        log.debug("testDepositMoney_InvalidAmount[0]: Начало теста");
        int invalidAmount = 55;
        log.debug("testDepositMoney_InvalidAmount[1]: Внесение неверной суммы: {}", invalidAmount);
        Exception exception = assertThrows(InvalidAmountException.class, () -> terminal.depositMoney(invalidAmount));
        log.debug("testDepositMoney_InvalidAmount[2]: Получено исключение: {}", exception.getMessage());
    }

    @Test
    public void testWithdrawMoney_Success() throws InvalidAmountException, InsufficientFundsException, AccountIsLockedException {
        log.debug("testWithdrawMoney_Success[0]: Начало теста");
        terminal.depositMoney(1000);
        int withdrawAmount = 500;
        log.debug("testWithdrawMoney_Success[1]: Снятие суммы: {}", withdrawAmount);
        int withdrawn = terminal.withdrawMoney(withdrawAmount);
        log.debug("testWithdrawMoney_Success[2]: Сумма успешно снята. Итог: {}", withdrawn);
        assertEquals(withdrawAmount, withdrawn, "Снятая сумма должна совпадать");
    }

    @Test
    public void testWithdrawMoney_InsufficientFunds() {
        log.debug("testWithdrawMoney_InsufficientFunds[0]: Начало теста");
        int withdrawAmount = 2000;
        log.debug("testWithdrawMoney_InsufficientFunds[1]: Попытка снять сумму: {}", withdrawAmount);
        Exception exception = assertThrows(InsufficientFundsException.class, () -> terminal.withdrawMoney(withdrawAmount));
        log.debug("testWithdrawMoney_InsufficientFunds[2]: Получено исключение: {}", exception.getMessage());
    }

    @Test
    public void testWithdrawMoney_InvalidAmount() {
        log.debug("testWithdrawMoney_InvalidAmount[0]: Начало теста");
        int invalidAmount = 55;
        log.debug("testWithdrawMoney_InvalidAmount[1]: Попытка снять неверную сумму: {}", invalidAmount);
        Exception exception = assertThrows(InvalidAmountException.class, () -> terminal.withdrawMoney(invalidAmount));
        log.debug("testWithdrawMoney_InvalidAmount[2]: Получено исключение: {}", exception.getMessage());
    }
}
