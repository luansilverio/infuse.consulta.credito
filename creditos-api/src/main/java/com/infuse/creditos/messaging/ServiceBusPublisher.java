package com.infuse.creditos.messaging;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceBusPublisher implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(ServiceBusPublisher.class);
    private final boolean enabled;
    private final ServiceBusSenderClient sender;
    private final ObjectMapper mapper = new ObjectMapper();

    public ServiceBusPublisher(
            @Value("${azure.servicebus.connection-string:}") String connectionString,
            @Value("${azure.servicebus.topic-name:}") String topicName,
            @Value("${azure.servicebus.enabled:true}") boolean enabledFlag) {

        boolean hasConfig = connectionString != null && !connectionString.isEmpty()
                && topicName != null && !topicName.isEmpty();
        this.enabled = enabledFlag && hasConfig;

        if (this.enabled) {
            this.sender = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .topicName(topicName)
                    .buildClient();
            log.info("Azure Service Bus publisher enabled for topic '{}'", topicName);
        } else {
            this.sender = null;
            log.warn("Azure Service Bus publisher DISABLED (missing connection string/topic or feature disabled).");
        }
    }

    public void sendConsultaEvento(String tipo, String parametro, boolean sucesso, int totalResultados, String numeroCredito) {
        if (!enabled) return;
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("tipo", tipo);
            payload.put("parametro", parametro);
            payload.put("sucesso", sucesso);
            payload.put("totalResultados", totalResultados);
            payload.put("numeroCredito", numeroCredito);
            payload.put("timestamp", Instant.now().toString());

            byte[] body = mapper.writeValueAsBytes(payload);
            ServiceBusMessage message = new ServiceBusMessage(body);
            message.getApplicationProperties().put("eventType", "consulta.credito");
            message.getApplicationProperties().put("tipo", tipo);
            sender.sendMessage(message);
        } catch (Exception e) {
            log.error("Falha ao enviar mensagem para Azure Service Bus", e);
        }
    }

    @Override
    public void destroy() {
        if (sender != null) sender.close();
    }
}