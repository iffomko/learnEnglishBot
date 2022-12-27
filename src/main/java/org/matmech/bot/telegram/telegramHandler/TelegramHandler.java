package org.matmech.bot.telegram.telegramHandler;

import org.matmech.context.contextManager.ContextManager;
import org.matmech.userData.UserData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Логика класса-интерфейса телеграмм бота
 */
public class TelegramHandler extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final ContextManager contextManager;

    public TelegramHandler(String botUsername, String botToken, ContextManager contextManager) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.contextManager = contextManager;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Метод, который реагирует на новые обновления в чате с ботом
     * @param update - объект, который хранит в себе информацию о новом обновлении в чате с ботом
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            final Message LAST_MESSAGE = update.getMessage();

            if (update.hasMessage() && LAST_MESSAGE.hasText()) {
                SendMessage message = new SendMessage();
                message.setChatId(LAST_MESSAGE.getChatId());

                UserData data = new UserData(
                        LAST_MESSAGE.getFrom().getFirstName(),
                        LAST_MESSAGE.getFrom().getLastName(),
                        LAST_MESSAGE.getFrom().getUserName(),
                        LAST_MESSAGE.getChatId()
                );

                String messageString = LAST_MESSAGE.getText();

                List<String> answers = contextManager.execute(messageString, data);

                for (String answer : answers) {
                    message.setText(answer);
                    execute(message);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
