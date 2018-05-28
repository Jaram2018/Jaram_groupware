package jaram.groupware.groupware.model;

import jaram.groupware.groupware.persistent.AccountEventHistory;
import jaram.groupware.groupware.repository.AccountEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Component
public class MemberModel {
    // FIXME position, attendingState  enum으로 변경
    public int cardinalNumber;
    public String name;
    public String position;
    public String phone;
    public String email;
    public String attendingState;

    MemberModel(){}

    public MemberModel(int cardinalNumber, String name, String phone, String email){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.position = "준회원";
        this.attendingState = "재학";
    }

    public MemberModel updateMember
            (int cardinalNumber, String name, String position, String phone, String email, String attendingState){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.attendingState = attendingState;

        return this;
    }
}
