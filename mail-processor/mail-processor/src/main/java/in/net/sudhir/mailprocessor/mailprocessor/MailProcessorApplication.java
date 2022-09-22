package in.net.sudhir.mailprocessor.mailprocessor;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import in.net.sudhir.mailprocessor.mailprocessor.processor.MailProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@RefreshScope
@EnableEncryptableProperties
@EnableJpaRepositories(basePackages = {"in.net.sudhir.mailprocessor.mailprocessor"})
public class MailProcessorApplication {

    Logger logger = LoggerFactory.getLogger(MailProcessorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MailProcessorApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            MailProcessor mailProcessor = (MailProcessor) ctx.getBean("mailProcessor");
            logger.info("Starting Operation: " + args[0]);

            if(args[0].equals("COLLECT_STATS")){
                mailProcessor.collectStats();
            } else if (args[0].equals("DELETE_BLOCKED_EMAILS")) {
                mailProcessor.deleteEmailsFromBlockedList();
            } else if (args[0].equals("DELETE_FROM_BLOCKED_SENDERS")) {
                mailProcessor.deleteFromBlockedSenders();
            } else if (args[0].equals("CUSTOM_DELETE_FROM_BLOCKED_SENDERS")) {
                mailProcessor.customDeleteFromBlockedSenders(args[1], args[2]);
            }

        };
    }

}
