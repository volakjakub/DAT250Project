package tech.dat250project.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;
import tech.dat250project.model.Poll;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

@Service
public class Sender {
    private static final String EXCHANGE_NAME = "pollResults";
    private static final String EXCHANGE_TYPE = "fanout";

    public void send(Poll poll) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            // show id and question
            StringBuilder stb = new StringBuilder();
            stb.append("Id: " + poll.getId());
            stb.append('\n');
            stb.append("Question: " + poll.getQuestion());
            stb.append('\n');

            
            stb.append("YES: " + poll.countYes() + "\nNO: " + poll.countNo());

            String results = stb.toString();

            channel.basicPublish(EXCHANGE_NAME, "", null, results.getBytes("UTF-8"));
            System.out.println(" [x] Sent\n" + results);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
