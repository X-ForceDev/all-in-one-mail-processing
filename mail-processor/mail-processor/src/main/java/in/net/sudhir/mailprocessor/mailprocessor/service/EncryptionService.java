package in.net.sudhir.mailprocessor.mailprocessor.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.service
 User Name: SUDHIR
 Created Date: 26-08-2022 at 17:36
 Description:
 */
@Service
public class EncryptionService {

    public static final String ENCRYPTION_CODE = System.getenv("ENCRYPTION_CODE");

    public String encryptText(String inputText){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ENCRYPTION_CODE);
        return textEncryptor.encrypt(inputText);
    }

    public String decryptText(String inputText){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ENCRYPTION_CODE);
        return textEncryptor.decrypt(inputText);
    }

}
