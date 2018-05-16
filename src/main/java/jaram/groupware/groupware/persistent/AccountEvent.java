package jaram.groupware.groupware.persistent;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */
@Entity
@Table(name = "account_event")
public class AccountEvent {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private int amountPerMan;

    @Column
    private int currentAmount;

    @Column
    private int totalAmount;
}
