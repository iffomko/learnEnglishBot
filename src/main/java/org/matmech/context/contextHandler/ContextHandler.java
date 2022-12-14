package org.matmech.context.contextHandler;

import org.matmech.context.Context;
import org.matmech.context.contextHandler.handlers.deleteWord.DeleteWord;
import org.matmech.context.contextHandler.handlers.edit.Edit;
import org.matmech.context.contextHandler.handlers.getGroup.GetGroup;
import org.matmech.context.contextHandler.handlers.startCommand.StartCommand;
import org.matmech.context.contextHandler.handlers.translateWord.TranslateWord;
import org.matmech.context.contextHandler.handlers.usuallyMessage.UsuallyMessage;
import org.matmech.context.contextHandler.handlers.wordAdd.WordAdd;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;
import org.matmech.context.contextHandler.handlers.testContext.TestCommand;
import java.util.List;

/**
 * В этом классе исполняется основной код контекстов после получения и валидации всех параметров
 */
public class ContextHandler {
    final private TestCommand TEST_CONTEXT;
    final private UsuallyMessage USUALLY_MESSAGE;
    final private StartCommand TO_START;
    final private TranslateWord TRANSLATE_WORD;
    final private GetGroup GET_GROUP;
    final private WordAdd WORD_ADD;
    final private Edit EDIT;
    final private DeleteWord DELETE_WORD;

    public ContextHandler(DBHandler db) {
        TEST_CONTEXT = new TestCommand(db);
        USUALLY_MESSAGE = new UsuallyMessage();
        TO_START = new StartCommand(db);
        TRANSLATE_WORD = new TranslateWord(db);
        GET_GROUP = new GetGroup(db);
        WORD_ADD = new WordAdd(db);
        EDIT = new Edit(db);
        DELETE_WORD = new DeleteWord(db);
    }

    /**
     * Вызывает нужную функцию, которая обрабатывает соответствующий контекст
     * @param context - информация о всех контекстов для всех пользователей
     * @param info - объект DataSaver с информацией о пользователе
     * @return - возвращает соответствующее сообщение для пользователя
     */
    public List<String> handle(Context context, UserData info, String message) {
        return switch (context.getParams(info.getChatId()).get("processName")) {
            case "testing" -> TEST_CONTEXT.handle(context, info);
            case "starting" -> TO_START.handle(context ,info);
            case "translating" -> TRANSLATE_WORD.handle(context,info);
            case "getGroup" -> GET_GROUP.handle(context,info);
            case "wordAdd" -> WORD_ADD.handle(context, info);
            case "edit" -> EDIT.handle(context, info);
            case "deleteWord" -> DELETE_WORD.handle(context, info);
            case null -> USUALLY_MESSAGE.handle(info, message);
            default -> throw new IllegalStateException("Unexpected value: " + context.getParams(info.getChatId()).get("processName"));
        };
    }
}
