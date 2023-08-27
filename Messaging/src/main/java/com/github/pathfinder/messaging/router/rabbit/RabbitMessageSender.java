package com.github.pathfinder.messaging.router.rabbit;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.router.IMessageSender;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMessageSender implements IMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    @Logged(logException = true, arguments = {"queue"})
    public <T> void send(String queue, T data) {
        log.info("sending message {} to queue {}", data, queue);
        rabbitTemplate.convertAndSend(queue, data);
    }

    @Override
    @Logged(logException = true, arguments = {"queue", "", "expectedClass"})
    public <R, T> Optional<T> sendAndReceive(String queue, R data, Class<T> expected) {
        return Optional.ofNullable(
                rabbitTemplate.convertSendAndReceiveAsType(queue, data,
                                                           new ParameterizedTypeReference<>() {
                                                           }));
    }
}
