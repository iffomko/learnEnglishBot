package org.matmech.params.paramsStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Контейнер для хранения параметров пользователя
 */
public class UserParameter {
    private long chatId;
    private String tag;
    private boolean setting;
    private Map<String, String> parameters;

    public UserParameter(long chatId, String tag) {
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
}
