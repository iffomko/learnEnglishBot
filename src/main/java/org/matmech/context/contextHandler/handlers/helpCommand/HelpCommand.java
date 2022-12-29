package org.matmech.context.contextHandler.handlers.helpCommand;

import org.matmech.context.contextHandler.handlers.Command;
import org.matmech.userData.UserData;

import java.util.List;
import java.util.Map;

/**
 * Команда /help
 */
public class HelpCommand implements Command {
    final private String helpText =
            "Список команд:\n" +
                    "/word_add - добавление слова в твой словарь\n" +
                    "/edit - изменение параметров слова\n" +
                    "/delete_word - удаление слова из твоего словаря\n" +
                    "/get_group - получение группы слова\n" +
                    "/translate - получение перевода слова\n" +
                    "/test - начать тестирование по словам\n";

    /**
     * Главный метод, который запускает обработку команды
     *
     * @param context - информация о всех контекстов команд для всех пользователей
     * @param info    - объект DataSaver с информацией о пользователе
     * @return - возвращает список сообщений для пользователя
     */
    @Override
    public List<String> handle(Context context, UserData info, Map<String, String> params) {
        try {
            return List.of(helpText);
        } finally {
            context.clear(info.getChatId());
        }
    }
}
