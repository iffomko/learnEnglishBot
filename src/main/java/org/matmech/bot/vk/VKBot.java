package org.matmech.bot.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.User;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.matmech.bot.Bot;
import org.matmech.context.contextManager.ContextManager;
import org.matmech.userData.UserData;
import java.util.List;
import java.util.Random;

/**
 * Бот для платформы VK
 */
public class VKBot implements Bot {
    private final int groupId;
    private final String accessToken;
    private ContextManager contextManager;

    public VKBot(int groupId, String accessToken, ContextManager contextManager) {
        this.groupId = groupId;
        this.accessToken = accessToken;
        this.contextManager = contextManager;
    }

    /**
     * Запускает бота
     */
    @Override
    public void start() {
        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);
            Random random = new Random();
            GroupActor actor = new GroupActor(groupId, accessToken);
            Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();

            while (true) {
                MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
                List<Message> messages = historyQuery.execute().getMessages().getItems();

                if (!messages.isEmpty()) {
                    messages.forEach(message -> {
                        System.out.println(message.toString());

                        try {
                            User user = vk.users().get(actor).userIds(
                                    String.valueOf(message.getFromId())
                            ).execute().get(0);

                            UserData data = new UserData(
                                    user.getFirstName(),
                                    user.getLastName(),
                                    user.getFirstName() + user.getLastName() + user.getId(),
                                    message.getPeerId()
                            );

                            List<String> messagesTexts = contextManager.execute(message.getText(), data);

                            for (String messageTxt : messagesTexts) {
                                vk.messages().send(actor).message(messageTxt)
                                        .userId(message.getFromId())
                                        .randomId(random.nextInt(10000))
                                        .execute();
                            }
                        } catch (ClientException | ApiException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                }

                ts = vk.messages().getLongPollServer(actor).execute().getTs();
                Thread.sleep(500);
            }
        } catch (ClientException | ApiException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
