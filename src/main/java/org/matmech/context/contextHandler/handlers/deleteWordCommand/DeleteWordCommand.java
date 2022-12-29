package org.matmech.context.contextHandler.handlers.deleteWordCommand;

import org.matmech.context.contextHandler.handlers.Command;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;

import java.util.List;
import java.util.Map;

/**
 * Команда /delete_word
 */
public class DeleteWordCommand implements Command {
    private DBHandler db;

    public DeleteWordCommand(DBHandler db) {
        this.db = db;
    }

    /**
     * Главный метод, который запускает обработку команда
     *
     * @param context - информация о всех контекстов для всех пользователей
     * @param info    - объект DataSaver с информацией о пользователе
     * @return - возвращает сообщение для пользователя соответствующее
     */
    @Override
    public List<String> handle(Context context, UserData info, Map<String, String> params) {
        try {
            return List.of(db.deleteWord(params.get("word")));
        } finally {
            context.clear(info.getChatId());
        }
    }
}
