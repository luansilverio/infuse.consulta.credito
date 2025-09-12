package com.infuse.creditos.messaging;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ProcessErrorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class ServiceBusSubscriber implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(ServiceBusSubscriber.class);
    private final boolean enabled;
    private final String connectionString;
    private final String topicName;
    private final String subscriptionName;
    private ServiceBusProcessorClient processor;

    public ServiceBusSubscriber(
            @Value("${azure.servicebus.processor.enabled:false}") boolean enabledFlag,
            @Value("${azure.servicebus.connection-string:}") String connectionString,
            @Value("${azure.servicebus.topic-name:}") String topicName,
            @Value("${azure.servicebus.subscription-name:}") String subscriptionName) {
        this.enabled = enabledFlag && !connectionString.isEmpty() && !topicName.isEmpty() && !subscriptionName.isEmpty();
        this.connectionString = connectionString;
        this.topicName = topicName;
        this.subscriptionName = subscriptionName;
    }

    @PostConstruct
    public void start() {
        if (!enabled) {
            log.info("ServiceBusSubscriber disabled (set azure.servicebus.processor.enabled=true to enable).");
            return;
        }
        this.processor = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .topicName(topicName)
                .subscriptionName(subscriptionName)
                .processMessage(ctx -> {
                    try {
                        String payload = new String(ctx.getMessage().getBody().toBytes(), StandardCharsets.UTF_8);
                        log.info("ASB message received on {}/{}: {}", topicName, subscriptionName, payload);
                        ctx.complete();
                    } catch (Exception e) {
                        log.error("Error handling ASB message", e);
                        ctx.abandon();
                    }
                })
                .processError(this::onError)
                .buildProcessorClient();
        this.processor.start();
        log.info("ServiceBusSubscriber started for {}/{}", topicName, subscriptionName);
    }

    private void onError(ProcessErrorContext ctx) {
        log.error("ASB processor error. Source={}, Namespace={}, Entity={}", ctx.getErrorSource(), ctx.getFullyQualifiedNamespace(), ctx.getEntityPath(), ctx.getException());
    }

    @Override
    public void destroy() {
        if (processor != null) try { processor.close(); } catch (Exception ignored) {}
    }
}