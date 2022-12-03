package cyse7125.fall2022.group03.config;

import cyse7125.fall2022.group03.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private String topicString = "topic02-team3";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

   
    public void sendMessage(String task){
        LOGGER.info(String.format("Message sent %s -> %s", topicString, task));
        kafkaTemplate.send(topicString, task);
    }
    
    public void sendMessageAsJson(Task task){
        LOGGER.info(String.format("Message sent as json %s -> %s", topicString, task.toString()));
        kafkaTemplate.send(topicString, task);
    }
}