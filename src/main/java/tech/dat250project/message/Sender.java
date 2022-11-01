package tech.dat250project.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import tech.dat250project.model.Poll;
import tech.dat250project.model.Vote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Sender {
    private static final String EXCHANGE_NAME = "answer";
    private static final String EXCHANGE_TYPE = "fanout";

    public static void send(Poll poll) {
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

            // COUNT NUMBER OF YES
            Integer yes = poll.getVotes().stream()
                    .map(Vote::getAnswer)
                    .filter(answer -> answer==true)
                    .collect(Collectors.toList()).size();

            // COUNT NUMBER OF NO
            Integer no = poll.getVotes().stream()
                    .map(Vote::getAnswer)
                    .filter(answer -> answer==false)
                    .collect(Collectors.toList()).size();
            stb.append("YES: " + yes + "\nNO: " + no);

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
