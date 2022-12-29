package org.matmech.params;

import org.matmech.context.Context;
import org.matmech.db.DBHandler;
import org.matmech.params.paramsStorage.ParamsStorage;
import org.matmech.params.paramsStorage.ParamsStorageException;

import java.util.Map;

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
                    storage.setParam(chatId, "group", null);

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
                    storage.setParam(chatId, "group", null);

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
                storage.setParam(chatId, word, null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /get_group
     * @param context - контекст для всех пользователей
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
                storage.setParam(chatId, word, null);

                throw new ParamsException(ParamsException.NOT_EXIST_WORD);
            }
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Присваивает параметры для контекста команды /word_add
     * @param chatId - идентификатор чата с пользователем
     * @param message - сообщение, которое отправил пользователь
     */
    private void setWordAddParams(Context context, long chatId, String message) {
        Map<String, String> params = context.getParams(chatId);

        final String WORD = params.get("word");
        final String TRANSLATE = params.get("translate");
        final String GROUP = params.get("group");
        final String PROCESS_NAME = params.get("processName");

        if (WORD == null){
            context.addParam(chatId, PROCESS_NAME, "word", message);
            return;
        }

        if (GROUP == null){
            context.addParam(chatId, PROCESS_NAME, "group", message);
            return;
        }

        if (TRANSLATE == null)
            context.addParam(chatId, PROCESS_NAME, "translate", message);
    }

    /**
     * Обрабатывает полученные параметры для команды /word_add
     * @param context - контекст для всех пользователей
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String wordAddValidation(Context context, Long chatId){
        Map<String, String> params = context.getParams(chatId);

        final String PROCESS_NAME = params.get("processName");
        final String WORD = params.get("word");
        final String GROUP = params.get("group");
        final String TRANSLATE = params.get("translate");

        context.addParam(chatId, PROCESS_NAME, "settingParams", "true");

        if (WORD == null)
            return "Введи слово, которое хочешь добавить:";

        if (GROUP == null)
            return "Введи группу слова, которое хочешь добавить:";

        if (TRANSLATE == null)
            return "Введи перевод слова, которое хочешь добавить:";

        context.addParam(chatId, PROCESS_NAME, "settingParams", null);

        return null;
    }

    /**
     * Присваивает параметры для контекста команды /edit
     * @param chatId - идентификатор чата с пользователем
     * @param message - сообщение, которое отправил пользователь
     */
    private void setEditParams(Context context, long chatId, String message) {
        Map<String, String> params = context.getParams(chatId);

        final String WORD = params.get("word");
        final String WORD_PARAM = params.get("wordParam");
        final String PARAM_VALUE = params.get("paramValue");
        final String PROCESS_NAME = params.get("processName");

        if (WORD == null){
            context.addParam(chatId, PROCESS_NAME, "word", message);
            return;
        }

        if (WORD_PARAM == null){
            context.addParam(chatId, PROCESS_NAME, "wordParam", message);
            return;
        }

        if (PARAM_VALUE == null)
            context.addParam(chatId, PROCESS_NAME, "paramValue", message);
    }

    /**
     * Обрабатывает полученные параметры для команды /edit
     * @param context - контекст для всех пользователей
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String editValidation(Context context, long chatId) {
        Map<String, String> params = context.getParams(chatId);

        final String PROCESS_NAME = params.get("processName");
        final String WORD = params.get("word");
        final String WORD_PARAM = params.get("wordParam");
        final String PARAM_VALUE = params.get("paramValue");

        context.addParam(chatId, PROCESS_NAME, "settingParams", "true");

        if (WORD == null)
            return "Введи слово, которое хочешь изменить:";

        if (WORD_PARAM == null)
            return "Введи параметр слова, которое хочешь изменить:";

        switch (WORD_PARAM){
            case "group", "translation" -> {}
            default -> {
                params.remove("wordParam");
                params.put("wordParam", null);
                return "Ой, кажется ты ввёл параметр неправильно, либо этот параметр не подлежит изменению! Повтори ввод!";
            }
        }

        if (PARAM_VALUE == null)
            return "Введи значение данного параметра:";

        context.addParam(chatId, PROCESS_NAME, "settingParams", null);

        return null;
    }

    /**
     * Обрабатывает полученные параметры для команды /delete_word
     * @param context - контекст для всех пользователей
     * @param chatId - идентификатор чата с пользователем
     * @return - возвращает текст ошибки для пользователя или null при успехе
     */
    private String deleteWordValidation(Context context, long chatId) {
        Map<String, String> params = context.getParams(chatId);

        final String PROCESS_NAME = params.get("processName");
        final String WORD = params.get("word");

        context.addParam(chatId, PROCESS_NAME, "settingParams", "true");

        if (WORD == null)
            return "Введи слово, которое ты хочешь удалить:";

        if (!db.IsWordExist(WORD)) {
            params.remove("word");
            params.put("word", null);
            return "Ой, кажется ты ввёл слово неправильно! Повтори ввод!";
        }

        context.addParam(chatId, PROCESS_NAME, "settingParams", null);

        return null;
    }

    /**
     * Присваивает параметры для контекста команды /delete_word
     * @param chatId - идентификатор чата с пользователем
     * @param message - сообщение, которое отправил пользователь
     */
    private void setDeleteWordParams(Context context, long chatId, String message) {
        Map<String, String> params = context.getParams(chatId);

        final String WORD = params.get("word");
        final String PROCESS_NAME = params.get("processName");

        if (WORD == null)
            context.addParam(chatId, PROCESS_NAME, "word", message);
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
     * @param message - сообщение, которое отправил пользователь
     * @return - возвращает сообщение-валидации или null, если заполнение параметров прошло успешно
     */
    public String handler(long chatId, String tag, String message) throws ParamsException {
        try {
            if (!storage.isExist(chatId)) {
                storage.createParams(chatId, tag);
            }

            final String processName = storage.getParam(chatId, "processName");

            storage.setParam(chatId, "message", message);

            return switch (processName) {
                case "testing" -> testParams(chatId);
                case "translating" -> translateParams(chatId);
                case "getGroup" -> getGroupParams(chatId);
                case "wordAdd" -> {
                    if (context.getParams(chatId).get("settingParams") != null)
                        setWordAddParams(context, chatId, message);

                    yield wordAddValidation(context, chatId);
                }
                case "edit" -> {
                    if (context.getParams(chatId).get("settingParams") != null)
                        setEditParams(context, chatId, message);

                    yield editValidation(context, chatId);
                }
                case "deleteWord" -> {
                    if (context.getParams(chatId).get("settingParams") != null)
                        setDeleteWordParams(context, chatId, message);

                    yield deleteWordValidation(context, chatId);
                }
                default -> null;
            };
        } catch (ParamsStorageException e) {
            System.out.println(e.getMessage());
        }
    }
}
