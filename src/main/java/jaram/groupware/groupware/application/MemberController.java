package jaram.groupware.groupware.application;

import jaram.groupware.groupware.model.MemberModel;
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
    MemberModel memberModel;

    @RequestMapping(path = "/members", method = RequestMethod.POST)
    public String searchMembers(Map<String, Object> model, HttpServletRequest request) {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String attendingState = request.getParameter("attendingState");

        List<MemberModel> memberModels;
        if (attendingState != null) {
            memberModels = memberModel.searchMembers(new AttendingState(attendingState));
        } else if (cardinalNumber != null) {
            memberModels = memberModel.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)));
        } else if (email != null) {
            memberModels = memberModel.searchMembers(new Email(email));
        } else if (name != null) {
            memberModels = memberModel.searchMembers(new Name(name));
        } else if (phone != null) {
            memberModels = memberModel.searchMembers(new Phone(phone));
        } else if (position != null) {
            memberModels = memberModel.searchMembers(new Position(position));
        } else {
            memberModels = new LinkedList<>();
        }

        model.put("members", memberModels);

        return "searchMembers";
    }

    @RequestMapping(path = "/member", method = RequestMethod.POST)
    public String addMember(Map<String, Object> model, HttpServletRequest request) {
        String cardinalNumber = request.getParameter("cardinalNumber");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        if (cardinalNumber == null || name == null || phone == null || email == null ||
                !memberModel.checkIntegrity(email)) {
            model.put("isError", true);

            return "addMember";
        }

        MemberModel newMemberModel = new MemberModel(Integer.parseInt(cardinalNumber), name, phone, email);
        memberModel.addMember(newMemberModel);
        List<MemberModel> memberModels = memberModel.getMembers();

        model.put("members", memberModels);

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

        List<MemberModel> memberModel;
        if (searchEmail != null) {
            memberModel = this.memberModel.searchMembers(new Email(searchEmail));
        } else if (searchCardinalNumber != null && searchName != null) {
            memberModel = this.memberModel.searchMembers(new CardinalNumber(Integer.parseInt(cardinalNumber)), new Name(searchName));
        } else {
            List<MemberModel> memberModels = this.memberModel.getMembers();
            model.put("members", memberModels);

            return "lookUPMembers";
        }

        if (cardinalNumber.equals("") || name.equals("") || position.equals("") || phone.equals("")
                || email.equals("") || attendingState.equals("") || memberModel.size() > 1) {
            model.put("isError", true);

            return "updateMember";
        }

        memberModel.updateMember(Integer.parseInt(cardinalNumber), name, position, phone, email, attendingState);

        List<MemberModel> memberModels = this.getMembers();
        model.put("members", memberModels);

        return "lookUPMembers";
    }
}
