package in.net.sudhir.mailprocessor.mailprocessor.entity;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.processor
 User Name: SUDHIR
 Created Date: 19-08-2022 at 20:22
 Description:
 */
public class GMailEntity extends MailEntity {

    public GMailEntity(String userName, String password, String hostname, String port, String protocol, String sslenable) {
        this.userName = userName;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.protocol = protocol;
        this.sslenable = sslenable;
    }

}
