package jaram.groupware.groupware.persistence.event;

import jaram.groupware.groupware.model.value.Amount;
import jaram.groupware.groupware.persistence.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

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
    private String title;

    @Column
    @NotNull
    private Date startDate;

    @Column
    @NotNull
    private Date endDate;

    @Column
    private long estimatedAmount;

    @Column
    @NotNull
    private long typedTotalBudget;

    @JoinColumn
    @OneToMany(cascade = CascadeType.ALL)
    private List<AccountEventMember> accountEventMember;

    public AccountEvent() {

    }

    public AccountEvent(String title, Amount amount, long typedTotalBudget, Date startDate, Date endDate, List<Member> members) {
        this.title = title;
        this.typedTotalBudget = typedTotalBudget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountEventMember = new LinkedList<>();
        for (int i = 0; i < members.size(); i++) {
//            accountEventMember.add(new AccountEventMember(members, amount));
        }
    }

    //immutable
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getEstimatedAmount() {
        return estimatedAmount;
    }

    public long getTypedTotalBudget() {
        return typedTotalBudget;
    }

    public List<AccountEventMember> getUnpaidMember() {
        return null;
    }

    public double calculateRate(){
        return 0;
    }

    public long calculateCurrentAmount(){
        return 0;
    }

    public boolean sendEmailToUnpaidMember(){
        return true;
    }

}
