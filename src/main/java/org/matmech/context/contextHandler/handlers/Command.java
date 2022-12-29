package org.matmech.context.contextHandler.handlers;

import org.matmech.userData.UserData;

import java.util.List;
import java.util.Map;

/**
 * Описывает структуру обработчиков основного функционала команд
 */
public interface Command {
    /**
     * Главный метод, который запускает обработку команды
     * @param context - информация о всех контекстов команд для всех пользователей
     * @param info - объект DataSaver с информацией о пользователе
     * @return - возвращает список сообщений для пользователя
     */
    List<String> handle(Context context, UserData info, Map<String, String> params);
}
