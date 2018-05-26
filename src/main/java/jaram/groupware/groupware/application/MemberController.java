package jaram.groupware.groupware.application;

import jaram.groupware.groupware.model.Member;
import jaram.groupware.groupware.model.value.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Controller
public class MemberController {
    @Autowired
    Member member;

    
    @RequestMapping(path = "/member", method = RequestMethod.POST)
    public String addMember(Map<String, Object> model, HttpServletRequest request){

        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        if (cardinalNumber == null || name == null || phone == null || email == null ||
                !member.checkIntegrity(new Email(email))){
            model.put("isError", true);

            return "addMember";
        }

        Member newMember = new Member(Integer.parseInt(cardinalNumber), name, phone, email);
        member.addMember(newMember);
        List<Member> members = member.getMembers();

        model.put("members", members);

        return "lookUPMembers";
    }
}
