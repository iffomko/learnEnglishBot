package org.matmech.paramsCollector;

import org.matmech.db.DBHandler;
import org.matmech.paramsCollector.paramsStorage.ParamsStorage;
import org.matmech.paramsCollector.paramsStorage.ParamsStorageException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Класс, который отвечает за получение и обработку входящих параметров
 */
public class ParamsCollector {
    private final DBHandler db;
    private final ParamsStorage storage;
    private final ArrayList<ParamsState> state;
    private final String STANDARD_COUNT_WORDS;
    private final String STANDARD_MODE;

    private ParamsState getParamsState(long chatID) {
        for (ParamsState item : state)
            if (item.getChatID() == chatID) {
                return item;
            }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /test
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String testParams(long chatId) throws ParamsCollectorException {
        try {
            String group = storage.getParam(chatId, "group");
            String countWords = storage.getParam(chatId, "countWords");
            String mode = storage.getParam(chatId, "mode");

            String message = storage.getParam(chatId, "message");

            ParamsState paramsState = getParamsState(chatId);

            if (paramsState == null) {
                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_STATE);
            }

            if (group == null && paramsState.getSetting()) {
                storage.setParam(chatId, "group", message.toLowerCase());
                paramsState.toggleSetting();
            }

            if (countWords == null && paramsState.getSetting()) {
                storage.setParam(chatId, "countWords", message.toLowerCase());
                paramsState.toggleSetting();
            }

            if (mode == null  && paramsState.getSetting()) {
                storage.setParam(chatId, "mode", message.toLowerCase());
                paramsState.toggleSetting();
            }

            if (group == null) {
                paramsState.toggleSetting();
                return "Пожалуйста, введите группу слов, по которым вы хотите произвести тестирование\n" +
                        "Если хотите провести тестирование по всем группу, то напишите `Все`";
            }

            if (!db.groupIsExist(group) && !group.equals("все")) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "group", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_GROUP);
            }

            if (countWords == null) {
                paramsState.toggleSetting();
                return "Пожалуйста, введите количество слов в тесте\n" +
                        "Если хотите провести тестирование по всем группу, то напишите `По всем`\n" +
                        "Если хотите стандартное количество слов (10), то напишите `Стандартное`";
            }

            if (!countWords.equals("по всем") && !countWords.equals("стандартное")) {
                try {
                    Integer testForCorrectNumber = Integer.valueOf(countWords);
                } catch (NumberFormatException e) {
                    paramsState.toggleSetting();
                    storage.setParam(chatId, "countWords", null);

                    throw new ParamsCollectorException(ParamsCollectorException.INVALID_COUNT_WORDS);
                }
            }

            if (mode == null) {
                paramsState.toggleSetting();
                return "Введите режим тестирование: `Easy` - легкий, `Difficult` - сложный\n" +
                        "Если хотите стандартный режим (Easy), то введите `Стандартный`";
            }

            switch (mode) {
                case "easy":
                case "difficult":
                case "стандартный":
                    break;
                default:
                    paramsState.toggleSetting();
                    storage.setParam(chatId, "mode", null);

                    throw new ParamsCollectorException(ParamsCollectorException.INVALID_TEST_MODE);
            }

            if (countWords.equals("стандартное")) {
                storage.setParam(chatId, "countWords", STANDARD_COUNT_WORDS);
            }

            if (mode.equals("стандартный")) {
                storage.setParam(chatId, "mode", STANDARD_COUNT_WORDS);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /translate
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String translateParams(long chatId) throws ParamsCollectorException {
        try {
            String word = storage.getParam(chatId, "word");

            String message = storage.getParam(chatId, "storage");

            ParamsState paramsState = getParamsState(chatId);

            if (paramsState == null) {
                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_STATE);
            }

            if (word == null && paramsState.getSetting()) {
                storage.setParam(chatId, "word", message.toLowerCase());
                paramsState.toggleSetting();
            }

            if (word == null) {
                paramsState.toggleSetting();
                return "Введи слово, которое хочешь перевести:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "word", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_WORD);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /get_group
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String getGroupParams(long chatId) throws ParamsCollectorException {
        try {
            String word = storage.getParam(chatId, "word");

            String message = storage.getParam(chatId, "storage");

            ParamsState paramsState = getParamsState(chatId);

            if (paramsState == null) {
                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_STATE);
            }

            if (word == null && paramsState.getSetting()) {
                storage.setParam(chatId, "word", message.toLowerCase());
                paramsState.toggleSetting();
            }

            if (word == null) {
                paramsState.toggleSetting();
                return "Введи слово, которое хочешь перевести:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "word", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_WORD);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /word_add
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String wordAddParams(long chatId) throws ParamsCollectorException {
        try {
            String word = storage.getParam(chatId, "word");
            String group = storage.getParam(chatId, "group");
            String translate = storage.getParam(chatId, "translate");

            String message = storage.getParam(chatId, "message");

            ParamsState paramsState = getParamsState(chatId);

            if (paramsState == null) {
                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_STATE);
            }

            if (word == null && paramsState.getSetting()) {
                storage.setParam(chatId, "word", message);
                paramsState.toggleSetting();
            }

            if (group == null && paramsState.getSetting()) {
                storage.setParam(chatId, "group", message);
                paramsState.toggleSetting();
            }

            if (translate == null && paramsState.getSetting()) {
                storage.setParam(chatId, "translate", message);
                paramsState.toggleSetting();
            }

            if (word == null) {
                paramsState.toggleSetting();
                return "Введи слово, которое хочешь добавить:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "word", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_WORD);
            }

            if (group == null) {
                paramsState.toggleSetting();
                return "Введи группу слова, которое хочешь добавить:";
            }

            if (!db.groupIsExist(group)) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "group", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_GROUP);
            }

            if (translate == null) {
                paramsState.toggleSetting();
                return "Введи перевод слова, которое хочешь добавить:";
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /edit
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String editParams(long chatId) throws ParamsCollectorException {
        try {
            String word = storage.getParam(chatId, "word");
            String param = storage.getParam(chatId, "wordParam");
            String paramValue = storage.getParam(chatId, "paramValue");

            String message = storage.getParam(chatId, "message");

            ParamsState paramsState = getParamsState(chatId);

            if (paramsState == null) {
                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_STATE);
            }

            if (word == null && paramsState.getSetting()) {
                storage.setParam(chatId, "word", message);
                paramsState.toggleSetting();
            }

            if (param == null && paramsState.getSetting()) {
                storage.setParam(chatId, "param", message);
                paramsState.toggleSetting();
            }

            if (paramValue == null && paramsState.getSetting()) {
                storage.setParam(chatId, "paramValue", message);
                paramsState.toggleSetting();
            }

            if (word == null) {
                paramsState.toggleSetting();
                return "Введи слово, которое хочешь добавить:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "word", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_WORD);
            }

            if (param == null) {
                paramsState.toggleSetting();
                return "Введи слово, которое хочешь изменить:";
            }

            switch (param) {
                case "group", "translation": break;
                default: {
                    paramsState.toggleSetting();
                    storage.setParam(chatId, "wordParam", null);

                    throw new ParamsCollectorException(ParamsCollectorException.INVALID_PARAMETER);
                }
            }

            if (paramValue == null) {
                paramsState.toggleSetting();
                return "Введи значение данного параметра:";
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /delete_word
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String deleteWordParams(long chatId) throws ParamsCollectorException {
        try {
            String word = storage.getParam(chatId, "word");

            String message = storage.getParam(chatId, "storage");

            ParamsState paramsState = getParamsState(chatId);

            if (paramsState == null) {
                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_STATE);
            }

            if (word == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "word", message.toLowerCase());
                paramsState.toggleSetting();
            }

            if (word == null) {
                paramsState.toggleSetting();
                return "Введи слово, которое ты хочешь удалить:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                paramsState.toggleSetting();
                storage.setParam(chatId, "word", null);

                throw new ParamsCollectorException(ParamsCollectorException.NOT_EXIST_WORD);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ParamsCollector(DBHandler db) {
        this.db = db;
        this.storage = new ParamsStorage();
        this.state = new ArrayList<ParamsState>();
        STANDARD_COUNT_WORDS = "10";
        STANDARD_MODE = "easy";
    }

    /**
     * Обработчик параметров
     * @param chatId - идентификатор чата с пользователем
     * @param contextName - имя контекста, для которого мы собираем параметры
     * @param message - сообщение, которое отправил пользователь
     * @return - возвращает сообщение-валидации или null, если заполнение параметров прошло успешно
     */
    public String handler(long chatId, String tag, String contextName, String message) throws ParamsCollectorException {
        try {
            if (!storage.isExist(chatId)) {
                storage.createParams(chatId, tag);
            }

            ParamsState paramsState = new ParamsState(chatId);

            if (!state.contains(paramsState)) {
                state.add(paramsState);
            }

            storage.setParam(chatId, "message", message);

            return switch (contextName) {
                case "testing" -> testParams(chatId);
                case "translating" -> translateParams(chatId);
                case "getGroup" -> getGroupParams(chatId);
                case "wordAdd" -> wordAddParams(chatId);
                case "edit" -> editParams(chatId);
                case "deleteWord" -> deleteWordParams(chatId);
                default -> null;
            };
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Возвращает параметры пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     * @return - Map<String, String> с параметрами
     */
    public Map<String, String> getParams(long chatID) {
        try {
            return storage.getAllParams(chatID);
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Очищает параметры для пользователя
     * @param chatID - уникальный идентификатор чата с пользователем
     */
    public void clearParams(long chatID) {
        try {
            storage.clearParams(chatID);
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }
    }
}
