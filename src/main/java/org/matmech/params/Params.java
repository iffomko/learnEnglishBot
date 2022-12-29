package org.matmech.params;

import org.matmech.db.DBHandler;
import org.matmech.params.paramsStorage.ParamsStorage;
import org.matmech.params.paramsStorage.ParamsStorageException;

/**
 * Класс, который отвечает за получение и обработку входящих параметров
 */
public class Params {
    // toDo: заменить флажки "testing" на контейнер с этими флажками и chatID. Можно целых класс написать для этого.

    private final DBHandler db;
    private final ParamsStorage storage;
    private final String STANDARD_COUNT_WORDS;
    private final String STANDARD_MODE;

    /**
     * Обрабатывает полученные параметры для команды /test
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String testParams(long chatId) throws ParamsException {
        try {
            String group = storage.getParam(chatId, "group");
            String countWords = storage.getParam(chatId, "countWords");
            String mode = storage.getParam(chatId, "mode");

            String message = storage.getParam(chatId, "message");

            if (group == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "group", message.toLowerCase());
                storage.setParam(chatId, "setting", "false");
            }

            if (countWords == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "countWords", message.toLowerCase());
                storage.setParam(chatId, "setting", "false");
            }

            if (mode == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "mode", message.toLowerCase());
                storage.setParam(chatId, "setting", "false");
            }

            if (group == null) {
                storage.setParam(chatId, "setting", "true");
                return "Пожалуйста, введите группу слов, по которым вы хотите произвести тестирование\n" +
                        "Если хотите провести тестирование по всем группу, то напишите `Все`";
            }

            if (!db.groupIsExist(group) && !group.equals("все")) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "group", null);

                throw new ParamsException(ParamsException.NOT_EXIST_GROUP);
            }

            if (countWords == null) {
                storage.setParam(chatId, "setting", "true");
                return "Пожалуйста, введите количество слов в тесте\n" +
                        "Если хотите провести тестирование по всем группу, то напишите `По всем`\n" +
                        "Если хотите стандартное количество слов (10), то напишите `Стандартное`";
            }

            if (!countWords.equals("по всем") && !countWords.equals("стандартное")) {
                try {
                    Integer testForCorrectNumber = Integer.valueOf(countWords);
                } catch (NumberFormatException e) {
                    storage.setParam(chatId, "setting", "true");
                    storage.setParam(chatId, "countWords", null);

                    throw new ParamsException(ParamsException.INVALID_COUNT_WORDS);
                }
            }

            if (mode == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введите режим тестирование: `Easy` - легкий, `Difficult` - сложный\n" +
                        "Если хотите стандартный режим (Easy), то введите `Стандартный`";
            }

            switch (mode) {
                case "easy":
                case "difficult":
                case "стандартный":
                    break;
                default:
                    storage.setParam(chatId, "setting", "true");
                    storage.setParam(chatId, "mode", null);

                    throw new ParamsException(ParamsException.INVALID_TEST_MODE);
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
    private String translateParams(long chatId) throws ParamsException {
        try {
            String word = storage.getParam(chatId, "word");

            String message = storage.getParam(chatId, "storage");

            if (word == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "word", message.toLowerCase());
                storage.setParam(chatId, "setting", "false");
            }

            if (word == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи слово, которое хочешь перевести:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "word", null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
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
    private String getGroupParams(long chatId) throws ParamsException {
        try {
            String word = storage.getParam(chatId, "word");

            String message = storage.getParam(chatId, "storage");

            if (word == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "word", message.toLowerCase());
                storage.setParam(chatId, "setting", "false");
            }

            if (word == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи слово, которое хочешь перевести:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "word", null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
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
    private String wordAddParams(long chatId) throws ParamsException {
        try {
            String word = storage.getParam(chatId, "word");
            String group = storage.getParam(chatId, "group");
            String translate = storage.getParam(chatId, "translate");

            String message = storage.getParam(chatId, "message");

            if (word == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "word", message);
                storage.setParam(chatId, "setting", "false");
            }

            if (group == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "group", message);
                storage.setParam(chatId, "setting", "false");
            }

            if (translate == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "translate", message);
                storage.setParam(chatId, "setting", "false");
            }

            if (word == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи слово, которое хочешь добавить:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "word", null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
            }

            if (group == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи группу слова, которое хочешь добавить:";
            }

            if (!db.groupIsExist(group)) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "group", null);

                throw new ParamsException(ParamsException.NOT_EXIST_GROUP);
            }

            if (translate == null) {
                storage.setParam(chatId, "setting", "true");
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
    private String editParams(long chatId) throws ParamsException {
        try {
            String word = storage.getParam(chatId, "word");
            String param = storage.getParam(chatId, "wordParam");
            String paramValue = storage.getParam(chatId, "paramValue");

            String message = storage.getParam(chatId, "message");

            if (word == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "word", message);
                storage.setParam(chatId, "setting", "false");
            }

            if (param == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "param", message);
                storage.setParam(chatId, "setting", "false");
            }

            if (paramValue == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "paramValue", message);
                storage.setParam(chatId, "setting", "false");
            }

            if (word == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи слово, которое хочешь добавить:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "word", null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
            }

            if (param == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи слово, которое хочешь изменить:";
            }

            switch (param) {
                case "group", "translation": break;
                default: {
                    storage.setParam(chatId, "setting", "true");
                    storage.setParam(chatId, "wordParam", null);

                    throw new ParamsException(ParamsException.INVALID_PARAMETER);
                }
            }

            if (paramValue == null) {
                storage.setParam(chatId, "setting", "true");
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
    private String deleteWordParams(long chatId) throws ParamsException {
        try {
            String word = storage.getParam(chatId, "word");

            String message = storage.getParam(chatId, "storage");

            if (word == null && storage.getParam(chatId, "setting").equals("true")) {
                storage.setParam(chatId, "word", message.toLowerCase());
                storage.setParam(chatId, "setting", "false");
            }

            if (word == null) {
                storage.setParam(chatId, "setting", "true");
                return "Введи слово, которое ты хочешь удалить:";
            }

            if (!db.IsWordExist(word.toLowerCase())) {
                storage.setParam(chatId, "setting", "true");
                storage.setParam(chatId, "word", null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Params(DBHandler db) {
        this.db = db;
        this.storage = new ParamsStorage();
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
    public String handler(long chatId, String tag, String contextName, String message) throws ParamsException {
        try {
            if (!storage.isExist(chatId)) {
                storage.createParams(chatId, tag);
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
}
