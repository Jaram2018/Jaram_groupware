package jaram.groupware.groupware.model;

import jaram.groupware.groupware.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Component
public class MemberModel {
    public int cardinalNumber;
    public String name;
    public String position;
    public String phone;
    public String email;
    public String attendingState;

    @Autowired
    private MemberRepository memberRepository;

    public MemberModel() {

    }

    public MemberModel(int cardinalNumber, String name, String position, String phone, String email, String attendingState){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.attendingState = attendingState;
    }

    public MemberModel(int cardinalNumber, String name, String phone, String email){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = "수습";
        this.attendingState = "재학";
    }

    public List<MemberModel> getMembers() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        return this.memberRepository.findAllMembers();
    }
}
