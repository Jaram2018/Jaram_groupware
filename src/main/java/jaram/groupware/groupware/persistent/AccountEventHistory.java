package jaram.groupware.groupware.persistent;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Entity
@Table(name = "account_event_history")
public class AccountEventHistory {

    @Id
    @GeneratedValue
    private long id;

    @JoinColumn
    @ManyToOne
    private AccountEvent accountEvent;

    @Column
    private boolean isCollect;

    @Column
    private Date collectDate;
}
