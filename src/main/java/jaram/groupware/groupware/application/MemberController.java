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
    private List<Member> members = member.getMembers();


    @RequestMapping(path = "/member", method = RequestMethod.PUT)
    public String updateMember(Map<String, Object> model, HttpServletRequest request){
        model.put("isError", false);

        String searchEmail = request.getParameter("searchEmail");
        String searchCardinalNumber = request.getParameter("searchCardinalNumber");
        String searchName = request.getParameter("searchName");

        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        Member member;
        if (searchEmail != null){
            member = this.member.searchMembers(new Email(searchEmail));
        } else if (searchCardinalNumber != null && searchName != null){
            member = this.member.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(searchName));
        } else {
            model.put("isError", true);
            model.put("members", members);

            return "lookUPMembers";
        }

        member.updateMember(Integer.parseInt(cardinalNumber), name, position, phone, email, attendingState);

        List<Member> members = this.getMembers();
        model.put("members", members);

        return "lookUPMembers";
    }
}
