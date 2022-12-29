package org.matmech.context.contextHandler.handlers.testCommand;

import org.matmech.context.contextHandler.handlers.Command;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;

import java.util.*;

/**
 * Команда /test
 */
public class TestCommand implements Command {
    private HashMap<String, ArrayList<String>> cachedWords;
    private DBHandler db;

    /**
     * Формирует готовый вопрос для теста
     *
     * @param tag   - тег пользователя
     * @param group - группа, по которому производится тестирование
     * @return - возвращает Map с готовым вопросом и словом, которое загадали в вопросе
     */
    private Map<String, String> getQuestion(String tag, String group) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("word", db.getRandomWord(tag, group));

        cachedWords.computeIfAbsent(tag, k -> new ArrayList<String>());

        ArrayList<String> userCachedWords = cachedWords.get(tag);

        if (db.getCountsWordsOfUser(tag) == userCachedWords.size())
            userCachedWords.clear();

        while (userCachedWords.contains(result.get("word")))
            result.put("word", db.getRandomWord(tag, group));

        userCachedWords.add(result.get("word"));

        result.put("question", "Укажите перевод слова " + result.get("word") + ": ");

        return result;
    }

    /**
     * Формулирует ответы на вопрос
     *
     * @param tag   - тег пользователя
     * @param group - группа слов
     * @param mode  - режим тестирования
     * @return - готовый текст, содержащий ответы на вопрос
     */
    private String getAnswers(String tag, String group, String mode, String trueWord) {
        final int COUNT_ANSWERS = 4;

        if (mode.equals("difficult"))
            return "";

        List<String> words = new ArrayList<String>();
        String word = db.getRandomWord(tag, group);
        words.add(word);

        for (int i = 0; i < 2; i++) {
            while (words.contains(word) || word.equals(trueWord))
                word = db.getRandomWord(tag, group);

            words.add(word);
        }

        StringBuilder answers = new StringBuilder();
        int trueAnswerIndex = (int) (Math.random() * COUNT_ANSWERS);
        int wordIndex = 0;

        for (int i = 0; i < COUNT_ANSWERS; i++) {
            answers.append("-- ");

            if (i == trueAnswerIndex) {
                answers.append(db.translateWordWithoutMessage(trueWord));
                answers.append("\n");
                continue;
            }

            answers.append(db.translateWordWithoutMessage(words.get(wordIndex++)));
            answers.append("\n");
        }

        return answers.toString();
    }

    /**
     * Очищает кеш-вопросов
     *
     * @param tag - тег пользователя
     */
    private void clearTestQuestions(String tag) {
        cachedWords.get(tag).clear();
    }

    public TestCommand(DBHandler db) {
        this.db = db;
        cachedWords = new HashMap<String, ArrayList<String>>();
    }

    /**
     * Запускает тест
     *
     * @param context - информация о всех контекстов для всех пользователей
     * @param info    - объект DataSaver с информацией о пользователе
     * @return - возвращает список сообщений пользователю
     */
    public List<String> handle(Context context, UserData info, Map<String, String> params) {
        final long CHAT_ID = info.getChatId();
        final String TAG = info.getTag();

        final String GROUP = params.get("group").toLowerCase();
        final String COUNT_WORDS = params.get("countWords").toLowerCase();
        final String MODE = params.get("mode").toLowerCase();

        params.putIfAbsent("countTrueAnswers", "0");

        if (
                (
                        !COUNT_WORDS.equals("по всем") &&
                                Integer.parseInt(params.get("currentQuestion")) > Integer.parseInt(COUNT_WORDS)
                ) ||
                        params.get("message").equals("/stop")) {
            final int countTrueAnswers = Integer.parseInt(params.get("countTrueAnswers"));

            StringBuilder textOfEndTest = new StringBuilder();
            textOfEndTest.append("Тест завершен!\n");
            textOfEndTest.append("Ты ответили правильно на ");
            textOfEndTest.append(countTrueAnswers);

            if (
                    countTrueAnswers % 10 >= 1 &&
                            countTrueAnswers % 10 <= 4
            )
                textOfEndTest.append(" вопроса!");
            else
                textOfEndTest.append(" вопросов!");

            textOfEndTest.append(" Молодец!");

            context.clear(CHAT_ID);
            clearTestQuestions(TAG);

            return List.of(textOfEndTest.toString());
        }

        final Map<String, String> questionData = getQuestion(TAG, GROUP);
        final String TRUE_WORD = questionData.get("word");

        StringBuilder resultOfAnswer = new StringBuilder();

        final String lastTrueAnswer = params.get("lastTrueAnswer");

        if (lastTrueAnswer != null && lastTrueAnswer.equals(params.get("message"))) {
            params.replace("countTrueAnswers", String.valueOf(Integer.parseInt(params.get("countTrueAnswers")) + 1));
            resultOfAnswer.append("Правильный ответ!");
        } else if (lastTrueAnswer != null) {
            resultOfAnswer.append("Неправильный ответ!\n");
            resultOfAnswer.append("Правильный ответ: ");
            resultOfAnswer.append(lastTrueAnswer);
        }

        params.remove("lastTrueAnswer");
        params.put("lastTrueAnswer", db.translateWordWithoutMessage(TRUE_WORD));

        String currentQuestion = String.valueOf(Integer.parseInt(params.get("currentQuestion")) + 1);
        params.replace("currentQuestion", currentQuestion);

        StringBuilder result = new StringBuilder(questionData.get("question"));
        result.append("\n");
        result.append(getAnswers(TAG, GROUP, MODE, TRUE_WORD));

        if (currentQuestion.equals("1"))
            return List.of(result.toString());

        return List.of(
                resultOfAnswer.toString(),
                result.toString()
        );
    }
}
