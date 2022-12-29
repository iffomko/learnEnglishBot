package org.matmech.context.contextHandler.handlers.translateWordCommand;

import org.matmech.context.contextHandler.handlers.Command;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;

import java.util.List;
import java.util.Map;

/**
 * Команда /translate
 */
public class TranslateWordCommand implements Command {
    private DBHandler db;

    public TranslateWordCommand(DBHandler db) {
        this.db = db;

    }

    /**
     * Главный метод, который запускает обработку команды
     *
     * @param context - информация о всех контекстов для всех пользователей
     * @param info    - объект DataSaver с информацией о пользователе
     * @return - возвращает сообщение для пользователя соответствующее
     */
    @Override
    public List<String> handle(Context context, UserData info, Map<String, String> params) {
        try {
            return List.of(db.translateWord(params.get("word")));
        } finally {
            context.clear(info.getChatId());
        }
    }
}
