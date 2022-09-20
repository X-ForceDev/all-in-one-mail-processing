package in.net.sudhir.mailprocessor.mailprocessor.processor;

import in.net.sudhir.mailprocessor.mailprocessor.entity.MailEntity;
import in.net.sudhir.mailprocessor.mailprocessor.entity.MailEntityBuilder;
import in.net.sudhir.mailprocessor.mailprocessor.model.Statistics;
import in.net.sudhir.mailprocessor.mailprocessor.service.DataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.search.FromStringTerm;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.processor
 User Name: SUDHIR
 Created Date: 19-08-2022 at 21:03
 Description:
 */
@Component
public class MailProcessor {

    Logger logger = LoggerFactory.getLogger(MailProcessor.class);

    @Autowired
    Environment environment;
    @Autowired
    MailEntityBuilder mailEntityBuilder;

    @Autowired
    DataService dataService;

//    @Autowired
//    KafkaService kafkaService;

    public void deleteEmailsFromBlockedList(){
        String[] providers = environment.getProperty("all.providers").split(";");
        List<String> blockedEmailIDs = dataService.getBlockedEmailIds();
        Calendar newDate = Calendar.getInstance();
        newDate.add(Calendar.DATE, -30);
        AtomicInteger deletedMailCount = new AtomicInteger(0);
        for(String provider : providers){
            System.out.println("Processing Provider: " + provider);
            List<MailEntity> entities = mailEntityBuilder.build(provider);
            for(MailEntity entity : entities){
                System.out.println("Processing UserName: " + entity.getUserName());
                Properties props = new Properties();
                props.setProperty("mail.store.protocol",entity.getProtocol());
//                props.setProperty("mail.debug", "true");
                props.setProperty("mail.imap.host",entity.getHostname());
                props.setProperty("mail.imap.port", entity.getPort());
                props.setProperty("mail.imap.ssl.enable",entity.getSslenable());
                Session session = Session.getDefaultInstance(props, null);
                try{
                    Store store = session.getStore(entity.getProtocol());
                    store.connect(entity.getHostname(), Integer.parseInt(entity.getPort()), entity.getUserName(), entity.getPassword());
                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_WRITE);
                    Message[] messageobjs = inbox.getMessages();
                    logger.info("Messages Size: " + messageobjs.length);
                    AtomicInteger loopcount = new AtomicInteger();
                    Arrays.asList(messageobjs).stream().forEach(message -> {
                        try {
                            if(message.getSentDate() != null){
                                if(message.getSentDate().getTime() < newDate.getTimeInMillis()){
                                    String s = null;
                                    s = message.getFrom()[0].toString();
                                    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(s);
                                    while(m.find()){
                                        String email = m.group();
                                        if(blockedEmailIDs.contains(email)){
                                            message.setFlag(Flags.Flag.DELETED, true);
                                            deletedMailCount.getAndIncrement();
                                            writeIntoFile(email + " | " + message.getSubject() + "|" + message.getContent().toString(), provider, entity.userName);
                                            logger.info("Deleted Message Count -  " + deletedMailCount.get());
//                                        kafkaService.sendDeletedMailInformation("Message Sender: " + message.getFrom()[0].toString() + ", Subject: " + message.getSubject());
                                        }
                                    }
                                }
                            }else{
                                message.setFlag(Flags.Flag.DELETED, true);
                                deletedMailCount.getAndIncrement();
                                writeIntoFile(message.getFrom()[0].toString() + " | " + message.getSubject() + "|" + message.getContent().toString(), provider, entity.userName);
                            }
                        } catch (MessagingException e) {
                            logger.error("Error Occurred: " + e.getMessage());
                        } catch (IOException e) {
                            logger.error("Error Occurred: " + e.getMessage());
                        } catch (NullPointerException e) {
                            logger.error("Error Occurred: " + e.getMessage());
                        } catch (Exception e) {
                            logger.error("Error Occurred: " + e.getMessage());
                        }
                        if((loopcount.get() % 3000) == 0){
                            try {
                                inbox.expunge();
                                inbox.close();
                                inbox.open(Folder.READ_WRITE);
                            } catch (MessagingException e) {
                                logger.error("Error Occurred: " + e.getMessage());
                            }
                        }
                        loopcount.getAndIncrement();
                    });
                    inbox.expunge();
                    inbox.close();
                    store.close();
                    logger.info("Deleted Message Count -  " + deletedMailCount.get());
                    logger.info("Messages Verified: " + loopcount);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void collectStats(){
        String[] providers = environment.getProperty("all.providers").split(";");
        Map<String, Long> stats = new HashMap<>();
        for(String provider : providers){
            logger.info("Processing Provider: " + provider);
            List<MailEntity> entities = mailEntityBuilder.build(provider);
            for(MailEntity entity : entities){
                logger.info("Processing UserName: " + entity.getUserName());
                Properties props = new Properties();
                props.setProperty("mail.store.protocol",entity.getProtocol());
//                props.setProperty("mail.debug", "true");
                props.setProperty("mail.imap.host",entity.getHostname());
                props.setProperty("mail.imap.port", entity.getPort());
                props.setProperty("mail.imap.ssl.enable",entity.getSslenable());
                Session session = Session.getDefaultInstance(props, null);
                try{
                    Store store = session.getStore(entity.getProtocol());
                    store.connect(entity.getHostname(), Integer.parseInt(entity.getPort()), entity.getUserName(), entity.getPassword());
                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    Message[] messageobjs = inbox.getMessages();
                    AtomicInteger loopcount = new AtomicInteger();
                    List<Statistics> statsList = new ArrayList<>();
                    logger.info("Number of Messages: " + messageobjs.length);
                    Arrays.asList(messageobjs).stream().forEach(message -> {
                        logger.info("Messages Processed: " + loopcount);
                        String s = null;
                        try {
                            s = message.getFrom()[0].toString();
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(s);
                        while(m.find()){
                            if(stats.containsKey(m.group())){
                                stats.put(m.group(), stats.get(m.group()) + 1);
                            }else{
                                stats.put(m.group(), 1L);
                            }
                        }
                        loopcount.getAndIncrement();
                    });
                    inbox.close();
                    store.close();
                    for(String emailId : stats.keySet()){
                        Statistics statistics = new Statistics();
                        statistics.setCount(stats.get(emailId));
                        statistics.setEmailAddress(emailId);
                        statsList.add(statistics);
                    }
                    dataService.deleteAllInBatch();
                    dataService.populateStatistics(statsList);
//                    kafkaService.sendMailsStatsInfo(statsList);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteFromBlockedSenders(){
        String[] providers = environment.getProperty("all.providers").split(";");
        Calendar newDate = Calendar.getInstance();
        newDate.add(Calendar.DATE, -30);
        Map<String, Long> stats = new HashMap<>();
        AtomicInteger deletedMailCount = new AtomicInteger(0);
        for(String provider : providers){
            logger.info("Processing Provider: " + provider);
            List<MailEntity> entities = mailEntityBuilder.build(provider);
            for(MailEntity entity : entities){
                logger.info("Processing UserName: " + entity.getUserName());
                Properties props = new Properties();
                props.setProperty("mail.store.protocol",entity.getProtocol());
//                props.setProperty("mail.debug", "true");
                props.setProperty("mail.imap.host",entity.getHostname());
                props.setProperty("mail.imap.port", entity.getPort());
                props.setProperty("mail.imap.ssl.enable",entity.getSslenable());
                Session session = Session.getDefaultInstance(props, null);
                try{
                    Store store = session.getStore(entity.getProtocol());
                    store.connect(entity.getHostname(), Integer.parseInt(entity.getPort()), entity.getUserName(), entity.getPassword());
                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_WRITE);
                    List<String> blockedSenders = dataService.getBlockedEmailIds();
                    AtomicInteger loopcount = new AtomicInteger();
                    for(String blockedSender : blockedSenders){
                        logger.info("Deleting message from sender: " + blockedSender);
                        Message[] messagesFromBlockedSenders = inbox.search(new FromStringTerm(blockedSender));
                        for(Message message : messagesFromBlockedSenders){
                            if(message.getSentDate().getTime() < newDate.getTimeInMillis()){
                                message.setFlag(Flags.Flag.DELETED, true);
                                deletedMailCount.getAndIncrement();
                                writeIntoFile( blockedSender + " | " + message.getSubject() + "|" + message.getContent().toString(), provider, entity.userName);
                                logger.info("Deleted Message Count -  " + deletedMailCount.get());
                            }
                        }
                        loopcount.getAndIncrement();
                    }
                    inbox.expunge();
                    inbox.close();
                    store.close();
//                    kafkaService.sendMailsStatsInfo(statsList);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeIntoFile(String text, String provider, String username){
        try{
            String fileName = environment.getProperty("deleted.email.info.file.location") + provider + "-" + username + "-" + environment.getProperty("deleted.email.info.file.nodeValue");
            Files.writeString(
                    Path.of(fileName),
                    text + System.lineSeparator(),
                    CREATE, APPEND
            );
        }catch(Exception e){System.out.println(e);}
    }
}
