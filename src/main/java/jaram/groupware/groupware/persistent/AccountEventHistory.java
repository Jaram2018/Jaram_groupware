package jaram.groupware.groupware.persistent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Entity
@Table(name = "account_event_history")
public class AccountEventHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private boolean isCollect;

    @Column
    private Date collectDate;

    @Column
    @NotNull
    private String email;

    protected AccountEventHistory(){

    }
    protected AccountEventHistory(String email){
        this.isCollect = false;
        this.email = email;
    }

    protected long getId() {
        return id;
    }

    protected Date getCollectDate() {
        return collectDate;
    }

    protected void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }

    protected String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected boolean isCollect() {
        return isCollect;
    }

    protected void setCollect(boolean collect) {
        isCollect = collect;
    }
}
