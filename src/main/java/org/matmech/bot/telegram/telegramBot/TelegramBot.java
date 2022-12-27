package org.matmech.bot.telegram.telegramBot;

import org.matmech.bot.Bot;
import org.matmech.bot.telegram.telegramHandler.TelegramHandler;
import org.matmech.context.contextManager.ContextManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Этот класс-интерфейс, который реализует обертку над логикой телеграмм-бота
 */
public class TelegramBot implements Bot {
    private final TelegramHandler bot;

    public TelegramBot(String botUsername, String botToken, ContextManager contextManager) {
        bot = new TelegramHandler(botUsername, botToken, contextManager);
    }

    /**
     * Запускает бота
     */
    public void start() {
        {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(bot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
