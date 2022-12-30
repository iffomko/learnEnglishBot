package org.matmech.paramsCollector;

/**
 * Состояние обработки параметров
 */
public class ParamsState {
    private final long chatID;
    private boolean setting;

    /**
     * Конструктор класса ParamsState
     * @param chatID - уникальный идентификатор чата с пользователем
     */
    public ParamsState(long chatID) {
        this.chatID = chatID;
        this.setting = false;
    }

    /**
     * Возвращает уникальный идентификатор чата с пользователем
     * @return - возвращает поле chatID
     */
    public long getChatID() {
        return chatID;
    }

    /**
     * Возвращает значение поля setting, которое отвечает за присваивание параметров
     * @return - значение поля setting
     */
    public boolean getSetting() {
        return setting;
    }

    /**
     * Устанавливает значение полю setting, которое отвечает за присваивание параметров
     * @param setting - желаемое значение поля setting
     */
    public void setSetting(boolean setting) {
        this.setting = setting;
    }

    /**
     * Меняет значение поля setting на противоположное
     */
    public void toggleSetting() {
        setting = !setting;
    }

    @Override
    public int hashCode() {
        return (int)chatID;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParamsState other)) {
            throw new  IllegalArgumentException("Неправильный аргумент в equals");
        }

        return (other.chatID == this.chatID);
    }
}
