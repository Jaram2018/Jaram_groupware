package jaram.groupware.groupware.persistent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */
@Entity
@Table(name = "account_event")
public class AccountEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private Date startDate;

    @Column
    @NotNull
    private Date endDate;

    @Column
    @NotNull
    private int amountPerMan;

    @Column
    private int currentAmount;

    @Column
    @NotNull
    private int totalAmount;

    @JoinColumn
    @OneToMany(cascade = CascadeType.ALL)
    private Set<AccountEventHistory> accountEventHistory;

    public AccountEvent() {

    }

    //TODO:이렇게 하는 것이 옳은 것인가
    public AccountEvent(String name, int amountPerMan, int totalAmount, Date startDate, Date endDate, String[] members) {
        this.name = name;
        this.amountPerMan = amountPerMan;
        this.totalAmount = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountEventHistory = new HashSet<AccountEventHistory>();
        for (int i = 0; i < members.length; i++) {
            accountEventHistory.add(new AccountEventHistory(members[i]));
        }
    }

    //immutable
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getAmountPerMan() {
        return amountPerMan;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
