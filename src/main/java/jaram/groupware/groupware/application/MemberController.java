package jaram.groupware.groupware.application;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jaram.groupware.groupware.model.value.*;
import jaram.groupware.groupware.persistent.Member;
import jaram.groupware.groupware.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
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

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String lookupMembers(Map<String, Object> model) throws IOException, GeneralSecurityException {

        List<Member> members = memberRepository.findAllMembers();

        model.put("members", members);

        return "member/list";
    }

    @RequestMapping(path = "/members", method = RequestMethod.POST)
    public String searchMembers(Map<String, Object> model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        List<Member> memberRepositorys;
        if (attendingState != null) {
            memberRepositorys = memberRepository.findMemberByAttendingState(new AttendingState(attendingState));
        } else if (cardinalNumber != null) {
            memberRepositorys = memberRepository.findMemberByCardinalNumber(new CardinalNumber(Integer.parseInt(cardinalNumber)));
        } else if (email != null) {
            memberRepositorys = memberRepository.findMemberByEmail(new Email(email));
        } else if (name != null) {
            memberRepositorys = memberRepository.findMemberByName(new Name(name));
        } else if (phone != null) {
            memberRepositorys = memberRepository.findMemberByPhone(new Phone(phone));
        } else if (position != null) {
            memberRepositorys = memberRepository.findMemberByPosition(new Position(position));
        } else {
            memberRepositorys = new LinkedList<>();
        }

        model.put("members", memberRepositorys);

        return "searchMembers";
    }

    @RequestMapping(path = "/member", method = RequestMethod.POST)
    public String addMember(Map<String, Object> model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        if (cardinalNumber == null || name == null || phone == null || email == null ||
                !memberRepository.checkIntegrity(email)) {
            model.put("isError", true);

            return "addMember";
        }

        MemberRepository newMemberRepository = new MemberRepository(Integer.parseInt(cardinalNumber), name, phone, email);
        memberRepository.addMember(newMemberRepository);
        List<MemberRepository> memberRepositorys = memberRepository.getMembers();

        model.put("members", memberRepositorys);

        return "lookupMembers";
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
            memberRepository = this.memberRepository.findMemberByEmail(new Email(searchEmail));
        } else if (searchCardinalNumber != null && searchName != null) {
            memberRepository = this.memberRepository.findMemberByCardinalNumber(new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(searchName));
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

    @RequestMapping(path = "/member", method = RequestMethod.DELETE)
    public boolean deleteMember(Map<String, Object> model, HttpServletRequest request) {
        String memberString = request.getParameter("members");

        Gson gson = new Gson();

        JsonArray members = gson.fromJson(memberString, JsonArray.class);

        Iterator<JsonElement> iterator = members.iterator();

        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            JsonObject member = element.getAsJsonObject();

            if (member.get("email") != null) {
                String email = member.get("email").getAsString();
                try {
                    Member m = findMember(new Email(email));
                    if (!deleteMember(m)) return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (member.get("phone") != null) {
                String phone = member.get("phone").getAsString();
                try {
                    Member m = findMember(new Phone(phone));
                    if (!deleteMember(m)) return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String name = member.get("name").getAsString();
                int cardinalNumber = member.get("cardinalNumber").getAsInt();
                try {
                    Member m = findMember(new Name(name), new CardinalNumber(cardinalNumber));
                    if (deleteMember(m)) return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private void checkMemberSize(List<Member> m) throws Exception {
        if (m.size() == 0 || m.size() >= 2) throw new Exception();
    }

    private Member findMember(Phone phone) throws Exception {
        List<Member> m = memberRepository.findMemberByPhone(phone);
        checkMemberSize(m);
        return m.get(0);
    }

    private Member findMember(Email email) throws Exception {
        List<Member> m = memberRepository.findMemberByEmail(email);
        checkMemberSize(m);
        return m.get(0);
    }

    private Member findMember(Name name, CardinalNumber cardinalNumber) throws Exception {
        List<Member> m = memberRepository.findMemberByCardinalNumberAndName(cardinalNumber, name);
        checkMemberSize(m);
        return m.get(0);
    }

    private boolean deleteMember(Member m) throws IOException, GeneralSecurityException {
        memberRepository.deleteMemeber(m);
        return true;
    }

}