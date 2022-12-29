package org.matmech.context.contextManager;

import org.matmech.context.contextHandler.ContextHandler;
import org.matmech.paramsCollector.ParamsCollector;
import org.matmech.paramsCollector.paramsStorage.ParamsStorage;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;

import java.util.List;

/**
 * Класс, который обрабатывает контекст команды
 */
public class ContextManager {
    final private ParamsCollector paramsCollector;

    final private ContextHandler contextHandler;
    private DBHandler db;

    /**
     * Метод проверяет, является ли сообщение командой
     * @param message - сообщение пользователя
     * @return - возвращает true, если является командной, и false, если не является
     */
    private boolean isCmd(String message) {
        return message.charAt(0) == '/';
    }

    /**
     * Возвращает стандартный ответ на не определенную команду
     * @return - возвращает строку, содержащую стандартный ответ
     */
    private String defaultAnswer() {
        return "Простите, я не знаю такой команды\n" +
                "Если хотите узнать полных список команд, то напишите /help";
    }

    /**
     * Возвращает название контекста
     * @param message - сообщение, которое отправил
     * @return - возвращает контекст
     */
    private String getContext(String message) {
        return switch (message) {
            case "/test" -> "testing";
            case "/translate" -> "translating";
            case "/start" -> "starting";
            case "/get_group" -> "getGroup";
            case "/word_add" -> "wordAdd";
            case "/edit" -> "edit";
            case "/delete_word" -> "deleteWord";
            case "/help" -> "helping";
            default -> null;
        };
    }

    public ContextManager(DBHandler dbHandler) {
        db = dbHandler;
        paramsCollector = new ParamsCollector(dbHandler);
        contextHandler = new ContextHandler(dbHandler);
    }

    /**
     * Определяет контекст общения с пользователем. Назначаем новый контекст
     * @param message - сообщение пользователя
     * @param info - информация о пользователе
     * @return возвращает список сообщений для пользователя
     */
    public List<String> execute(String message, UserData info) {
        if (!db.userIsExist(info.getTag()))
            return List.of("Вы не зарегистрированы в системе! Чтобы зарегистрироваться в системе напишите /start");

        final long CHAT_ID = info.getChatId();
        final String CONTEXT = getContext(message);

        if (context.getParams(CHAT_ID) == null)
            context.setProcessName(CHAT_ID, null);

        if (CONTEXT == null && context.getParams(CHAT_ID).get("processName") == null) {
            if (!isCmd(message))
                return contextHandler.handle(context, info, message);

            return List.of(defaultAnswer());
        }

        if (!context.isBusy(CHAT_ID))
            context.setProcessName(CHAT_ID, CONTEXT);

        String paramsAnswer = paramsCollector.handler(context, CHAT_ID, message);

        if (paramsAnswer == null)
            return contextHandler.handle(context, info, message);

        return List.of(paramsAnswer);
    }
}
