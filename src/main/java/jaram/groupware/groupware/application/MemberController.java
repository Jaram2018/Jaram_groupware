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

    @RequestMapping(path = "/members", method = RequestMethod.POST)
    public String searchMembers(Map<String, Object> model, HttpServletRequest request) {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        List<Member> members;
        if (attendingState != null) {
            members = member.searchMembers(new AttendingState(attendingState));
        } else if (cardinalNumber != null) {
            members = member.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)));
        } else if (email != null) {
            members = member.searchMembers(new Email(email));
        } else if (name != null) {
            members = member.searchMembers(new Name(name));
        } else if (phone != null) {
            members = member.searchMembers(new Phone(phone));
        } else if (position != null) {
            members = member.searchMembers(new Position(position));
        } else {
            members = new LinkedList<>();
        }

        model.put("members", members);

        return "searchMembers";
    }

    @RequestMapping(path = "/member", method = RequestMethod.POST)
    public String addMember(Map<String, Object> model, HttpServletRequest request) {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        if (cardinalNumber == null || name == null || phone == null || email == null ||
                !member.checkIntegrity(email)) {
            model.put("isError", true);

            return "addMember";
        }

        Member newMember = new Member(Integer.parseInt(cardinalNumber), name, phone, email);
        member.addMember(newMember);
        List<Member> members = member.getMembers();

        model.put("members", members);

        return "lookUPMembers";
    }

    @RequestMapping(path = "/member", method = RequestMethod.PUT)
    public String updateMember(Map<String, Object> model, HttpServletRequest request) {
        String searchEmail = request.getParameter("searchEmail");
        String searchCardinalNumber = request.getParameter("searchCardinalNumber");
        String searchName = request.getParameter("searchName");

        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        List<Member> member;
        if (searchEmail != null) {
            member = this.member.searchMembers(new Email(searchEmail));
        } else if (searchCardinalNumber != null && searchName != null) {
            member = this.member.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(searchName));
        } else {
            List<Member> members = this.member.getMembers();
            model.put("members", members);

            return "lookUPMembers";
        }

        if (cardinalNumber.equals("") || name.equals("") || position.equals("") || phone.equals("")
                || email.equals("") || attendingState.equals("") || member.size() > 1) {
            model.put("isError", true);

            return "updateMember";
        }

        member.updateMember(Integer.parseInt(cardinalNumber), name, position, phone, email, attendingState);

        List<Member> members = this.getMembers();
        model.put("members", members);

        return "lookUPMembers";
    }
}
