package in.net.sudhir.mailprocessor.mailprocessor.entity;

import in.net.sudhir.mailprocessor.mailprocessor.config.AppConstants;
import in.net.sudhir.mailprocessor.mailprocessor.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.processor
 User Name: SUDHIR
 Created Date: 19-08-2022 at 20:20
 Description:
 */
@Component
public class MailEntityBuilder {

    @Autowired
    private Environment environment;

    @Autowired
    EncryptionService encryptionService;

    public List<MailEntity> build(String mailProvider){
        List<MailEntity> entities = new ArrayList<>();
        String[] usernames = environment.getProperty(mailProvider + ".ids.list").split(";");
        for(String username : usernames){
            MailEntity entity = null;
            String password = encryptionService.decryptText(environment.getProperty(username + ".gmail.imap.password"));
            String host = environment.getProperty(mailProvider + ".imap.host");
            String port = environment.getProperty(mailProvider + ".imap.port");
            String protocol = environment.getProperty(mailProvider + ".imap.protocol");
            String sslenable = environment.getProperty(mailProvider + ".imap.ssl.enable");
            if(mailProvider.equals(AppConstants.MailProviders.GMAIL_PROVIDER)){
                entity = new MailEntity(username,password,host,port, protocol, sslenable);
                entities.add(entity);
            }
        }
        return entities;
    }

}
