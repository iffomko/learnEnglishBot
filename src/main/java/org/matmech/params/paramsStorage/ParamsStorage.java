package org.matmech.params.paramsStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, который хранит в себе список параметров для всех пользователей
 */
public class ParamsStorage {
    private List<UserParam> users;

    /**
     * Ищет объект с параметрами для конкретного пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     * @return - объект с параметрами
     */
    private UserParam searchParam(long chatID) {
        for (UserParam param : users) {
            if (param.getChatID() == chatID) {
                return param;
            }
        }

        return null;
    }

    public ParamsStorage() {
        users = new ArrayList<UserParam>();
    }

    /**
     * Проверяет существование объекта с параметрами для конкретного пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     * @return - возвращает true, если объект существует, или false, если не существует
     */
    public boolean isExist(long chatID) {
        for (UserParam param : users) {
            if (param.getChatID() == chatID) {
                return true;
            }
        }

        return false;
    }

    /**
     * Создает параметры для пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     * @param tag - тег пользователя
     * @throws ParamsStorageException - может возвращать ошибку, если объект с параметрами у пользователя уже существуют
     */
    public void createParams(long chatID, String tag) throws ParamsStorageException {
        UserParam param = new UserParam(chatID, tag);

        if (users.contains(param)) {
            throw new ParamsStorageException(ParamsStorageException.EXIST_PARAM);
        }

        users.add(param);
    }

    /**
     * Устанавливает параметр пользователю
     * @param chatID - уникальный идентификатор чата с пользователем
     * @param key - название параметра
     * @param value - значение параметра
     * @throws ParamsStorageException - может возвращать ошибку, если объект с параметрами у пользователя не существует
     */
    public void setParam(long chatID, String key, String value) throws ParamsStorageException {
        UserParam param = searchParam(chatID);

        if (param == null) {
            throw new ParamsStorageException(ParamsStorageException.NOT_EXIST_PARAM);
        }

        switch (key) {
            case "setting" -> param.setSetting(Boolean.parseBoolean(value));
            default -> {
                if (param.getParameters().get(key) == null) {
                    param.getParameters().put(key, value);
                    return;
                }

                param.getParameters().replace(key, value);
            }
        }
    }
    /**
     * Удаляет параметр у пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     * @param key - название параметра
     * @param value - значение параметра
     * @throws ParamsStorageException - может возвращать ошибку, если объект с параметрами у пользователя не существует
     */
    public void removeParam(long chatID, String key, String value) throws ParamsStorageException {
        UserParam param = searchParam(chatID);

        if (param == null) {
            throw new ParamsStorageException(ParamsStorageException.NOT_EXIST_PARAM);
        }

        param.getParameters().remove(key, value);
    }

    /**
     * Получает параметр у пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     * @param key - параметр
     * @return - значение параметра
     * @throws ParamsStorageException - может возвращать ошибку, если объект с параметрами у пользователя не существует
     */
    public String getParam(long chatID, String key) throws ParamsStorageException {
        UserParam result = searchParam(chatID);

        if (result == null) {
            throw new ParamsStorageException(ParamsStorageException.NOT_EXIST_PARAM);
        }

        return switch (key) {
            case "setting" -> String.valueOf(result.getSetting());
            case "tag" -> result.getTag();
            default -> result.getParameters().get(key);
        };
    }

    /**
     * Получает строковое представление параметров для пользователей.
     * Следует использовать исключительно для дебага.
     * @param chatID - уникальный идентификатор чата с пользователем
     * @return - строковое представление для параметров пользователя
     * @throws ParamsStorageException - может возвращать ошибку, если объект с параметрами у пользователя не существует
     */
    public String getStringView(int chatID) throws ParamsStorageException {
        UserParam param = searchParam(chatID);

        if (param == null) {
            throw new ParamsStorageException(ParamsStorageException.NOT_EXIST_PARAM);
        }

        return param.toString();
    }
}
