package jaram.groupware.groupware.application;

import jaram.groupware.groupware.model.value.*;
import jaram.groupware.groupware.persistent.Member;
import jaram.groupware.groupware.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
    private MemberRepository memberRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String lookupMembers(Map<String, Object> model) throws IOException, GeneralSecurityException {

        List<MemberRepository> members = memberRepository.getMembers();

        model.put("members", members);

        return "member/list";
    }

    @RequestMapping(path = "/members", method = RequestMethod.POST)
    public String searchMembers(Map<String, Object> model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        Map<String, String[]> filter = request.getParameterMap();

        List<Member> members = null;
        if (filter.get("filter")[0].equals("cardinalNumber")) {
            members = memberRepository.findMemberByCardinalNumber(new CardinalNumber(Integer.parseInt(filter.get("q")[0])));
        } else if (filter.get("filter")[0].equals("name")) {
            members = memberRepository.findMemberByName(new Name(filter.get("q")[0]));
        } else if (filter.get("filter")[0].equals("position")) {
            members = memberRepository.findMemberByPosition(Position.valueOf(filter.get("q")[0]));
        } else if (filter.get("filter")[0].equals("phone")) {
            members = memberRepository.findMemberByPhone(new Phone(filter.get("q")[0]));
        } else if (filter.get("filter")[0].equals("email")) {
            members = memberRepository.findMemberByEmail(new Email(filter.get("q")[0]));
        } else if (filter.get("filter")[0].equals("attendingState")) {
            members = memberRepository.findMemberByAttendingState(AttendingState.valueOf(filter.get("q")[0]));
        }

        model.put("members", members);

        return "member/list";
    }

    @RequestMapping(path = "/member", method = RequestMethod.POST)
    public String addMember(Map<String, Object> model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        Member member = new Member(new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(name), new Phone(phone), new Email(email));

        List<Member> members = memberRepository.addMember(member);
        model.put("members", members);

        return "member/list";
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

        List<MemberRepository> memberRepository;
        if (searchEmail != null) {
            memberRepository = this.memberRepository.searchMembers(new Email(searchEmail));
        } else if (searchCardinalNumber != null && searchName != null) {
            memberRepository = this.memberRepository.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(searchName));
        } else {
            List<MemberRepository> memberRepositorys = this.memberRepository.getMembers();
            model.put("members", memberRepositorys);

            return "lookupMembers";
        }

        if (cardinalNumber.equals("") || name.equals("") || position.equals("") || phone.equals("")
                || email.equals("") || attendingState.equals("") || memberRepository.size() > 1) {
            model.put("isError", true);

            return "updateMember";
        }

        memberRepository.updateMember(Integer.parseInt(cardinalNumber), name, position, phone, email, attendingState);

        List<MemberRepository> memberRepositorys = this.getMembers();
        model.put("members", memberRepositorys);

        return "lookupMembers";
    }
}
