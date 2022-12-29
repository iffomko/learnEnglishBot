package org.matmech.context.contextHandler.handlers.wordAddCommand;

import org.matmech.context.contextHandler.handlers.Command;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;

import java.util.List;
import java.util.Map;

/**
 * Команда /word_add
 */
public class WordAddCommand implements Command {
    private DBHandler db;

    public WordAddCommand(DBHandler db) {
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
            final String TAG = info.getTag();
            return List.of(db.wordAdd(params.get("word"), params.get("translate"), params.get("group"), TAG));
        } finally {
            context.clear(info.getChatId());
        }
    }
}
