package org.matmech.bot.cmd.cmdBot;

import org.matmech.bot.Bot;
import org.matmech.bot.cmd.cmdLogic.CmdLogic;
import org.matmech.context.contextManager.ContextManager;

import java.util.Scanner;

/**
 * Класс-интерфейс, который реализует консольную обертку над ботом
 */
public class CmdBot implements Bot {
    private String firstName;
    private String lastName;
    private String tag;
    private int chatId;
    private final Scanner input;
    private final ContextManager contextManager;

    private CmdLogic bot;

    /**
     * Инициализирует пользователя, который пользуется сейчас консольным ботом
     */
    private void initUser() {
        System.out.println("Добро пожаловать! Авторизуйте свой компьютер!");
        System.out.print("Введите свое имя:");
        this.firstName = input.nextLine();
        System.out.print("Введите свою фамилию:");
        this.lastName = input.nextLine();
        System.out.print("Введите/придумайте себе тег:");
        this.tag = input.nextLine();
        System.out.print("Введите/придумайте себе id чата:");
        this.chatId = input.nextInt();

        System.out.println("Вы были успешно авторизированы! Для того, чтобы начать работать с ботом, напишите /start");
    }

    public CmdBot(ContextManager contextManager) {
        this.contextManager = contextManager;
        this.input = new Scanner(System.in);

        initUser();

        bot = new CmdLogic(firstName, lastName, tag, chatId, contextManager);
    }

    /**
     * Запускает бота
     */
    public void start() {
        bot.handle();
    }
}
