package com.ecommerce.productservice.modules.cart.event;

import com.ecommerce.productservice.events.OrderCreatedEvent;
import com.ecommerce.productservice.modules.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventConsumer {

    private final CartService cartService;

    @KafkaListener(
            topics = "order.created",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: eventId={}, orderId={}, userId={}",
                event.getEventId(), event.getOrderId(), event.getUserId());

        cartService.clearCartByUserId(event.getUserId());
        log.info("Cleared cart for userId={}", event.getUserId());
    }
}
