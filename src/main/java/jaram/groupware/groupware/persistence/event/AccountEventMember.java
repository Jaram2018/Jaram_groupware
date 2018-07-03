package jaram.groupware.groupware.persistence.event;

import jaram.groupware.groupware.model.value.Amount;
import jaram.groupware.groupware.model.value.log.AccountEventMemberLog;
import jaram.groupware.groupware.persistence.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Entity
@Table(name = "account_event_member")
public class AccountEventMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL)
    private Member member;

    @Column
    private boolean isPaid;

    @Column
    protected Date paidDate;

    @Embedded
    @NotNull
    protected Amount amount;

    @Transient
    private AccountEventMemberLog log;

    protected AccountEventMember() {

    }

    protected AccountEventMember(Member member, Amount amount) {
        this.member = member;
        this.isPaid = false;
        this.amount = amount;
    }

    protected long getId() {
        return id;
    }

    protected boolean getIsPaid() {
        return isPaid;
    }

    protected void setIsPaid(boolean isPaid){
        this.isPaid = isPaid;
    }

    protected Date getPaidDate() {
        return paidDate;
    }

    protected void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    protected int getCardinalNumber(){
        return 0;
    }

    protected String getName(){
        return null;
    }

    protected String getPhone(){
        return null;
    }

    protected String getEmail(){
        return null;
    }

    protected AccountEventMemberLog getLog() {
        return log;
    }

    protected void setLog(AccountEventMemberLog log) {
        this.log = log;
    }

}