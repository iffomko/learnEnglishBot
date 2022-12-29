package org.matmech.params.paramsStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Контейнер для хранения параметров пользователя
 */
public class UserParam {
    private long chatId;
    private String tag;
    private boolean setting;
    private Map<String, String> parameters;

    public UserParam(long chatId, String tag) {
        this.chatId = chatId;
        this.tag = tag;
        this.setting = false;
        parameters = new HashMap<String, String>();
    }

    public long getChatID() {
        return chatId;
    }

    public String getTag() {
        return tag;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public boolean getSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserParam)) {
            throw new IllegalArgumentException("Неправильный аргумент для сравнения");
        }

        UserParam other = (UserParam) obj;

        return other.chatId == chatId;
    }

    @Override
    public int hashCode() {
        return (int)chatId;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("ChatID: ");
        result.append(String.valueOf(chatId));
        result.append("\n");
        result.append("Tag: ");
        result.append(tag);
        result.append("\n");
        result.append("Setting: ");
        result.append(setting);
        result.append("\n");
        result.append(parameters.toString());

        return result.toString();
    }
}
