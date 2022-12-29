package org.matmech;

import org.matmech.bot.vk.VKBot;
import org.matmech.bot.cmd.cmdBot.CmdBot;
import org.matmech.bot.Bot;
import org.matmech.bot.telegram.telegramBot.TelegramBot;
import org.matmech.context.contextManager.ContextManager;
import org.matmech.db.DBHandler;

public class Main {
    final static String TELEGRAM_BOT_USERNAME = System.getenv("TELEGRAM_BOT_USERNAME");
    final static String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    final static int VK_BOT_GROUP_ID = Integer.parseInt(System.getenv("VK_BOT_GROUP_ID"));
    final static String VK_BOT_ACCESS_TOKEN = System.getenv("VK_BOT_ACCESS_TOKEN");
    final static String DB_URL = System.getenv("DB_URL");
    final static String DB_USERNAME = System.getenv("DB_USERNAME");
    final static String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        // database

        DBHandler db = new DBHandler(DB_URL, DB_USERNAME, DB_PASSWORD);

        // cache

        Context context = new Context();

        // request handler

        ContextManager contextManager = new ContextManager(context, db);

        // bots

        Bot bot = new TelegramBot(TELEGRAM_BOT_USERNAME, TELEGRAM_BOT_TOKEN, contextManager);
        bot.start();

        Bot vkBot = new VKBot(VK_BOT_GROUP_ID, VK_BOT_ACCESS_TOKEN, contextManager);
        vkBot.start();

        Bot cmdBot = new CmdBot(contextManager);
        cmdBot.start();
    }
}