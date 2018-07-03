package jaram.groupware.groupware.model.value.log;

import jaram.groupware.groupware.persistence.Member;

import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by NamHyunsil on 2018. 7. 3..
 */
public class AccountEventMemberLog implements Log{
    @Transient
    public Member member;

    public Date paidDate;

    public AccountEventMemberLog(){}

    public AccountEventMemberLog(Member member, Date paidDate){
        this.member = member;
        this.paidDate = paidDate;
    }

    @Override
    public String getMessage() {
        String message = "<AccountEventMemberLog>\n"
                + "member: " + member.getCardinalNumber() + " " + member.getName() + " " + member.getEmail() + " " + member.getPhone() + "\n"
                + "paidDate: " + paidDate;
        return message;
    }
}
