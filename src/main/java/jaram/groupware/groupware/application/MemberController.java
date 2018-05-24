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

    @RequestMapping(path = "/members", method = RequestMethod.POST)
    public String searchMembers(Map<String, Object> model, HttpServletRequest request){
        model.put("isError", false);

        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        List<Member> members;

        if (attendingState != null){
            members = member.searchMembers(new AttendingState(attendingState));
        } else if (cardinalNumber != null){
            members = member.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)));
        } else if (email != null){
            members = member.searchMembers(new Email(email));
        } else if (name != null){
            members = member.searchMembers(new Name(name));
        } else if (phone != null){
            members = member.searchMembers(new Phone(phone));
        } else if (position != null){
            members = member.searchMembers(new Position(position));
        } else {
            members = new LinkedList<>();
        }

        model.put("members", members);

        return "searchMembers";
    }
}
