package in.net.sudhir.mailprocessor.mailprocessor.model;

import javax.persistence.*;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.model
 User Name: SUDHIR
 Created Date: 20-08-2022 at 06:30
 Description:
 */
@Entity
public class BlockedEmailIds {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "BLOCKED_EMAIL_ID", nullable = false)
    private String blockedEmailId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlockedEmailId() {
        return blockedEmailId;
    }

    public void setBlockedEmailId(String blockedEmailId) {
        this.blockedEmailId = blockedEmailId;
    }
}
