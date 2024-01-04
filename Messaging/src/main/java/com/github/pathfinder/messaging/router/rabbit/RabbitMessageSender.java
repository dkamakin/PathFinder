package com.github.pathfinder.messaging.router.rabbit;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.message.IMessage;
import com.github.pathfinder.messaging.router.IMessageSender;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMessageSender implements IMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    @Logged(logException = true, value = {"message", "expected"})
    public <R, T> Optional<T> sendAndReceive(IMessage<R> message, Class<T> expected) {
        return handleReceived(rabbitTemplate.convertSendAndReceive(
                                      message.exchangeName(),
                                      message.routingKey(),
                                      message.data()
                              ),
                              expected
        );
    }

    @Override
    @Logged(logException = true, value = "message")
    public <T> void send(IMessage<T> message) {
        rabbitTemplate.convertAndSend(message.exchangeName(), message.routingKey(), message.data());
    }

    private <T> Optional<T> handleReceived(Object received, Class<T> expected) {
        return Optional.ofNullable(received).map(expected::cast);
    }

}
