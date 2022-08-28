package in.net.sudhir.mailprocessor.mailprocessor.entity;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.processor
 User Name: SUDHIR
 Created Date: 19-08-2022 at 20:22
 Description:
 */
public class MailEntity {
    public String userName;
    public String password;
    public String hostname;
    public String port;
    public String protocol;
    public String sslenable;

    public MailEntity(String userName, String password, String hostname, String port, String protocol, String sslenable) {
        this.userName = userName;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.protocol = protocol;
        this.sslenable = sslenable;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSslenable() {
        return sslenable;
    }

    public void setSslenable(String sslenable) {
        this.sslenable = sslenable;
    }
}
