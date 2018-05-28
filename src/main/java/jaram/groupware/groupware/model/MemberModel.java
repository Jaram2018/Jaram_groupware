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

//@Component
public class MemberModel {
    public int cardinalNumber;
    public String name;
    public String position;
    public String phone;
    public String email;
    public Boolean isAttending;

    public MemberModel(int cardinalNumber, String name, String email){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
    }


    public static List<MemberModel> getMembers(){
        List<MemberModel> members = new LinkedList<>();
        members.add(new MemberModel(31,"남현실","hsilnam3"));
        members.add(new MemberModel(32,"남현실","hsilnam2"));
        members.add(new MemberModel(33,"남현실","hsilnam1"));

        return members;
    }
}
