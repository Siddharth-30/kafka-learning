package kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for(int i = 0 ; i<10 ; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<>("second_topic", "Hello World! "+Integer.toString(i));

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("Topic is : " + recordMetadata.topic() +"\n"+
                            "Partition is : " + recordMetadata.partition() +"\n"+
                            "Offset is : " + recordMetadata.offset() +"\n"+
                            "Timestamp is : " + recordMetadata.timestamp() +"\n");
                    } else {
                        logger.error("Error occurred : " + e);
                    }
                }
            });
        }
        producer.flush();

        producer.close();

    }
}
