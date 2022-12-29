package TestCommands;

import org.junit.Assert;
import org.junit.Test;
import org.matmech.context.contextManager.ContextManager;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TestRequestHandler {
    private final UserData user = new UserData("User", "Unknown", "Unknown", 12243);
    private final DBHandler db = Mockito.mock(DBHandler.class);
    private final Context context = new Context();
    private ContextManager contextManager = new ContextManager(context, db);

    /**
     * Unit-тест метода execute: проверяет запуск команды /test и то, что программа запросит группу для тестирования
     */
    @Test
    public void testGetGroupForTesting() {
        when(db.userIsExist(any())).thenReturn(true);

        List<String> result = contextManager.execute("/test", user);

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Пожалуйста, введите группу слов, по которым вы хотите произвести тестирование\n" +
                "Если хотите провести тестирование по всем группу, то напишите `Все`"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /test и то, что программа скажет,
     * чтобы повторили попытку ввода
     */
    @Test
    public void testNotExistGroupForTesting() {
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/test", user);

        when(db.groupIsExist(any(String.class))).thenReturn(false);

        List<String> result = contextManager.execute("глаголы", user);

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add("Такой группы не существует. Пожалуйста, введите существующую группу!");

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /test и то,
     * что программа запросит количество слов
     */
    @Test
    public void testExecuteForTestingGetCountWord() {
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/test", user);
        when(db.groupIsExist(any(String.class))).thenReturn(true);
        List<String> result = contextManager.execute("глаголы", user);

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Пожалуйста, введите количество слов в тесте\n" +
                "Если хотите провести тестирование по всем группу, то напишите `По всем`\n" +
                "Если хотите стандартное количество слов (10), то напишите `Стандартное`"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /test и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForTestingValidateCountWord() {
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/test", user);
        when(db.groupIsExist(any(String.class))).thenReturn(true);
        contextManager.execute("глаголы", user);
        List<String> result = contextManager.execute("10лол", user);

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Вы ввели недопустимое значение! Повторите ввод:"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /test и то,
     * что программа запросит режим тестирования
     */
    @Test
    public void testExecuteForTestingGetMode() {
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/test", user);
        when(db.groupIsExist(any(String.class))).thenReturn(true);
        contextManager.execute("глаголы", user);
        List<String> result = contextManager.execute("10", user);

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Введите режим тестирование: `Easy` - легкий, `Difficult` - сложный\n" +
                "Если хотите стандартный режим (Easy), то введите `Стандартный`"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /test и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForTestingValidateMode() {
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/test", user);
        when(db.groupIsExist(any(String.class))).thenReturn(true);
        contextManager.execute("глаголы", user);
        contextManager.execute("10", user);
        List<String> result = contextManager.execute("10", user);

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Вы ввели не существующий режим!\n"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }
    /**
     * Unit-тест метода execute: проверяет запуск команды /translate и то,
     * что будет если всё введено верно
     */
    @Test
    public void testExecuteForTranslateValidationAllRight(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());


        contextManager.execute("/translate", user);
        when(db.IsWordExist(any(String.class))).thenReturn(true);
        when(db.translateWord(any())).thenReturn("Перевод слова тест_слово: тест_слово");

        List<String> result = contextManager.execute("тест_слово", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Перевод слова тест_слово: тест_слово"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /translate и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForTranslateValidation(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());


        contextManager.execute("/translate", user);
        when(db.IsWordExist(any(String.class))).thenReturn(false);

        List<String> result = contextManager.execute("тест_слово", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Ой, кажется ты ввёл слово неправильно! Повтори ввод!"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /get_group и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForGetGroupValidation(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());


        contextManager.execute("/get_group", user);
        when(db.IsWordExist(any(String.class))).thenReturn(false);

        List<String> result = contextManager.execute("тест_слово", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Ой, кажется ты ввёл слово неправильно! Повтори ввод!"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /get_group и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForGetGroupValidationAllRight(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());


        contextManager.execute("/get_group", user);
        when(db.IsWordExist(any(String.class))).thenReturn(true);
        when(db.getGroup(any(String.class))).thenReturn("Группа у слова тест_слово: тест_группа");

        List<String> result = contextManager.execute("тест_слово", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Группа у слова тест_слово: тест_группа"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /edit и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForEditValidation(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/edit", user);
        when(db.IsWordExist(any(String.class))).thenReturn(true);

        contextManager.execute("тест_слово", user);

        List<String> result = contextManager.execute("неправильный_метод", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Ой, кажется ты ввёл параметр неправильно, либо этот параметр не подлежит изменению! Повтори ввод!"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /edit и то,
     * что программа скажет, при правильном выполнение
     */
    @Test
    public void rightTestExecuteForEditValidation(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());

        contextManager.execute("/edit", user);
        when(db.IsWordExist(any(String.class))).thenReturn(true);

        contextManager.execute("тест_слово", user);
        contextManager.execute("translation", user);

        when(db.edit(any(),any(),any())).thenReturn("Вы успешно изменили translation");
        List<String> result = contextManager.execute("test", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Вы успешно изменили translation"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }

    /**
     * Unit-тест метода execute: проверяет запуск команды /delete_word и то,
     * что программа скажет, чтобы повторили попытку ввода
     */
    @Test
    public void testExecuteForDeleteWordValidation(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());


        contextManager.execute("/delete_word", user);
        when(db.IsWordExist(any(String.class))).thenReturn(false);

        List<String> result = contextManager.execute("тест_слово", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Ой, кажется ты ввёл слово неправильно! Повтори ввод!"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }
    /**
     * Unit-тест метода execute: проверяет запуск команды /delete_word и то,
     * что программа скажет, при правильном выполнении
     */
    @Test
    public void rightTestExecuteForDeleteWordValidation(){
        when(db.userIsExist(any())).thenReturn(true);

        context.clear(user.getChatId());


        contextManager.execute("/delete_word", user);
        when(db.IsWordExist(any(String.class))).thenReturn(true);
        when(db.deleteWord(any())).thenReturn("Слово было удалено из базы данных!");

        List<String> result = contextManager.execute("тест_слово", user);
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add(
                "Слово было удалено из базы данных!"
        );

        for (int i = 0; i < result.size(); i++)
            Assert.assertEquals(expectedResult.get(0), result.get(0));
    }
}