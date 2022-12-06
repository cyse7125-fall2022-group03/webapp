package cyse7125.fall2022.group03.config;

import cyse7125.fall2022.group03.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${kafka.topic}")
    private String topicName;

    //private String topicString = "topic02-team3";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

   
    public void sendMessage(String task){
        LOGGER.info(String.format("Message sent %s -> %s", topicName, task));
        kafkaTemplate.send(topicName, task);
    }
    
    public void sendMessageAsJson(Task task){
        LOGGER.info(String.format("Message sent as json %s -> %s", topicName, task.toString()));
        kafkaTemplate.send(topicName, task);
    }
}