package org.matmech.context.contextHandler;

import org.matmech.context.contextHandler.handlers.deleteWordCommand.DeleteWordCommand;
import org.matmech.context.contextHandler.handlers.editCommand.EditCommand;
import org.matmech.context.contextHandler.handlers.getGroupCommand.GetGroupCommand;
import org.matmech.context.contextHandler.handlers.helpCommand.HelpCommand;
import org.matmech.context.contextHandler.handlers.startCommand.StartCommand;
import org.matmech.context.contextHandler.handlers.translateWordCommand.TranslateWordCommand;
import org.matmech.context.contextHandler.handlers.usuallyMessage.UsuallyMessage;
import org.matmech.context.contextHandler.handlers.wordAddCommand.WordAddCommand;
import org.matmech.userData.UserData;
import org.matmech.db.DBHandler;
import org.matmech.context.contextHandler.handlers.testCommand.TestCommand;
import java.util.List;
import java.util.Map;

/**
 * В этом классе исполняется команды после получения и валидации всех параметров
 */
public class ContextHandler {
    final private TestCommand TEST_COMMAND;
    final private UsuallyMessage USUALLY_MESSAGE;
    final private StartCommand TO_START_COMMAND;
    final private TranslateWordCommand TRANSLATE_WORD_COMMAND;
    final private GetGroupCommand GET_GROUP_COMMAND;
    final private WordAddCommand WORD_ADD_COMMAND;
    final private EditCommand EDIT_COMMAND;
    final private DeleteWordCommand DELETE_WORD_COMMAND;
    final private HelpCommand HELP_COMMAND;

    public ContextHandler(DBHandler db) {
        TEST_COMMAND = new TestCommand(db);
        USUALLY_MESSAGE = new UsuallyMessage();
        TO_START_COMMAND = new StartCommand(db);
        TRANSLATE_WORD_COMMAND = new TranslateWordCommand(db);
        GET_GROUP_COMMAND = new GetGroupCommand(db);
        WORD_ADD_COMMAND = new WordAddCommand(db);
        EDIT_COMMAND = new EditCommand(db);
        DELETE_WORD_COMMAND = new DeleteWordCommand(db);
        HELP_COMMAND = new HelpCommand();
    }

    /**
     * Вызывает нужную функцию, которая обрабатывает соответствующий контекст/команду
     * @param context - информация о всех контекстов для всех пользователей
     * @param info - объект DataSaver с информацией о пользователе
     * @return - возвращает соответствующее сообщение для пользователя
     */
    public List<String> handle(Context context, UserData info, String message) {
        Map<String,String> params = context.getParams(info.getChatId());
        return switch (context.getParams(info.getChatId()).get("processName")) {
            case "testing" -> TEST_COMMAND.handle(context, info, params);
            case "starting" -> TO_START_COMMAND.handle(context, info, params);
            case "translating" -> TRANSLATE_WORD_COMMAND.handle(context, info, params);
            case "getGroup" -> GET_GROUP_COMMAND.handle(context, info, params);
            case "wordAdd" -> WORD_ADD_COMMAND.handle(context, info, params);
            case "edit" -> EDIT_COMMAND.handle(context, info, params);
            case "deleteWord" -> DELETE_WORD_COMMAND.handle(context, info, params);
            case "helping" -> HELP_COMMAND.handle(context, info, params);
            case null -> USUALLY_MESSAGE.handle(info, message);
            default -> throw new IllegalStateException("Unexpected value: " + context.getParams(info.getChatId()).get("processName"));
        };
    }
}
