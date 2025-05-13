package org.example.backend.core.event.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.core.event.DomainEvent;
import org.example.backend.core.event.EventHandler;
import org.example.backend.core.event.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Basic implementation of the EventPublisher interface
 * using Spring's ApplicationEventPublisher
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SimpleEventPublisher implements EventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing event: {}", event.getEventType());
        applicationEventPublisher.publishEvent(event);
    }
}
