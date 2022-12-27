package org.matmech.bot.cmd.cmdLogic;

import org.matmech.context.contextManager.ContextManager;
import org.matmech.userData.UserData;
import java.util.Scanner;

/**
 * Логика класса-интерфейса, который реализует консольную обертку над ботом
 */
public class CmdLogic {
    private final Scanner input;
    private final ContextManager contextManager;
    private final String firstName;
    private final String lastName;
    private final String tag;
    private final int chatId;

    public CmdLogic(String firstName, String lastName, String tag, int chatId, ContextManager contextManager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tag = tag;
        this.chatId = chatId;
        this.contextManager = contextManager;
        this.input = new Scanner(System.in);
    }

    /**
     * Метод, получает сообщения с консоли и обрабатывает их
     */
    public void handle() {
        while(true) {
            String messageFromCMD = input.nextLine();
            StringBuilder answer = new StringBuilder("");
            UserData data = new UserData(firstName, lastName, tag, chatId);

            answer.append(contextManager.execute(messageFromCMD, data));

            System.out.println(answer);
        }
    }
}
