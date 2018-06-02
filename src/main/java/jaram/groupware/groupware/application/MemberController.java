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
        Map<String, String[]> filter = request.getParameterMap();

        List<Member> members = null;
        switch (filter.get("filter")[0]) {
            case "cardinalNumber":
                try {
                    members = memberRepository.findMemberByCardinalNumber(new CardinalNumber(Integer.parseInt(filter.get("q")[0])));
                } catch (NumberFormatException ignored) {
                }
                break;
            case "name":
                members = memberRepository.findMemberByName(new Name(filter.get("q")[0]));
                break;
            case "position":
                try {
                    members = memberRepository.findMemberByPosition(Position.valueOf(filter.get("q")[0]));
                } catch (IllegalArgumentException ignored) {
                }
                break;
            case "phone":
                members = memberRepository.findMemberByPhone(new Phone(filter.get("q")[0]));
                break;
            case "email":
                members = memberRepository.findMemberByEmail(new Email(filter.get("q")[0]));
                break;
            case "attendingState":
                try {
                    members = memberRepository.findMemberByAttendingState(AttendingState.valueOf(filter.get("q")[0]));
                } catch (IllegalArgumentException ignored) {
                }
                break;
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
        if (!memberRepository.addMember(member)){
            model.put("errorMsg", "추가를 실패하였습니다.");
            return "error";
        }

        List<Member> members = memberRepository.findAllMembers();
        model.put("members", members);

        return "member/list";
    }

    @RequestMapping(path = "/member/update", method = RequestMethod.POST)
    public String updateMember(Map<String, Object> model, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String searchEmail = request.getParameter("searchEmail");
        String searchPhone = request.getParameter("searchPhone");
        String searchCardinalNumber = request.getParameter("searchCardinalNumber");
        String searchName = request.getParameter("searchName");

        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        Member targetMember;
        if (searchEmail != null) {
            targetMember = memberRepository.findOneMemberByEmail(new Email(searchEmail));
        } else if (searchPhone != null) {
            targetMember = memberRepository.findOneMemberByPhone(new Phone(searchPhone));
        } else if (searchCardinalNumber != null && searchName != null) {
            try {
                targetMember = memberRepository.findOneMemberByCardinalNumberAndName(new CardinalNumber(Integer.parseInt(searchCardinalNumber)), new Name(searchName));
            } catch (NumberFormatException e){
                model.put("errorMsg", "잘못된 기수입니다.");
                return "error";
            }
        } else {
            model.put("errorMsg", "해당 유저가 없습니다");
            return "error";
        }

        if (cardinalNumber.equals("") || name.equals("") || position.equals("") || phone.equals("")
                || email.equals("") || attendingState.equals("")) {
            model.put("errorMsg", "입력되지 않은 값이 있습니다.");

            return "updateMember";
        }
        try {
            if (!memberRepository.updateMember(targetMember, new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(name),
                    Position.valueOf(position), new Phone(phone), new Email(email), AttendingState.valueOf(attendingState))) {
                model.put("errorMsg", "수정을 실패하였습니다.");
                return "error";
            }
        } catch (NumberFormatException e) {
            model.put("errorMsg", "잘못된 기수입니다.");
            return "error";
        } catch (IllegalArgumentException e) {
            model.put("errorMsg", "잘못된 값을 입력하셨습니다.");
            return "error";
        }

        List<Member> members = memberRepository.findAllMembers();
        model.put("members", members);

        return "member/list";
    }

    @RequestMapping(path = "/members/delete", method = RequestMethod.POST)
    public String deleteMember(Map<String, Object> model, HttpServletRequest request) {
        String memberString = request.getParameter("members");

        if (memberString == null){
            model.put("error","삭제할 멤버를 선택해주세요");
            return "error";
        }

        Gson gson = new Gson();

        JsonArray members = gson.fromJson(memberString, JsonArray.class);

        Iterator<JsonElement> iterator = members.iterator();


        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            JsonObject member = element.getAsJsonObject();

            if (member.get("email") != null) {
                String email = member.get("email").getAsString();
                try {
                    Member m = memberRepository.findOneMemberByEmail(new Email(email));
                    if (!memberRepository.deleteMember(m)){
                        model.put("errorMsg", "삭제 실패");
                        return "error";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (member.get("phone") != null) {
                String phone = member.get("phone").getAsString();
                try {
                    Member m = memberRepository.findOneMemberByPhone(new Phone(phone));
                    if (!memberRepository.deleteMember(m)){
                        model.put("errorMsg", "삭제 실패");
                        return "error";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    String name = member.get("name").getAsString();
                    int cardinalNumber = member.get("cardinalNumber").getAsInt();
                    Member m = memberRepository.findOneMemberByCardinalNumberAndName(new CardinalNumber(cardinalNumber), new Name(name));
                    if (!memberRepository.deleteMember(m)){
                        model.put("errorMsg", "삭제 실패");
                        return "error";
                    }
                } catch (NumberFormatException e){
                    model.put("errorMsg", "잘못된 기수입니다.");
                    return "error";
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return "lookupMembers";
    }


}