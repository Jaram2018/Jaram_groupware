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
    public String isAttending;

    public MemberModel(int cardinalNumber, String name, String position, String phone, String email, String isAttending){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.isAttending = isAttending;
    }


    public static List<MemberModel> getMembers(){
        List<MemberModel> members = new LinkedList<>();
        members.add(new MemberModel(30,"오용택", "OB", "010-1234-5678","ka123ak@gamil.com","졸업"));
        members.add(new MemberModel(31,"남현실","정회원","010-9012-3456", "hsilnam3@gmail.com","휴학생"));
        members.add(new MemberModel(31,"박진오","준OB", "010-7890-1234", "pjessesso@gmail.com", "재학"));

        return members;
    }
}
