package in.net.sudhir.mailprocessor.mailprocessor.service;

import in.net.sudhir.mailprocessor.mailprocessor.model.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.service
 User Name: SUDHIR
 Created Date: 22-08-2022 at 07:11
 Description:
 */
//@Service
public class KafkaService {

//    @Autowired
    KafkaTemplate kafkaTemplate;

    public KafkaService() {

    }

    public void sendMailsStatsInfo(List<Statistics> statistics){
        for(Statistics statisticsItem : statistics){
            kafkaTemplate.send("mail-store-topic", statisticsItem.toString());
        }
    }

    public void sendDeletedMailInformation(String deletedMailInfo){
        kafkaTemplate.send("mail-store-topic", deletedMailInfo);
    }
}
