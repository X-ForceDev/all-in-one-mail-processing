package in.net.sudhir.mailprocessor.mailprocessor.entity;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.entity
 User Name: SUDHIR
 Created Date: 02-09-2022 at 11:15
 Description:
 */
public class OutlookMailEntity extends MailEntity{

    public OutlookMailEntity(String userName, String password, String hostname, String port, String sslenable, String protocol) {
        this.userName = userName;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.protocol = protocol;
        this.sslenable = sslenable;
    }

}
