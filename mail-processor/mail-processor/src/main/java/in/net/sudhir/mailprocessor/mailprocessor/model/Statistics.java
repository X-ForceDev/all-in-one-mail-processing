package in.net.sudhir.mailprocessor.mailprocessor.model;

import javax.persistence.*;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.model
 User Name: SUDHIR
 Created Date: 20-08-2022 at 10:30
 Description:
 */
@Entity
public class Statistics {

    public Statistics() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "count", nullable = false)
    private Long count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", count=" + count +
                '}';
    }
}
