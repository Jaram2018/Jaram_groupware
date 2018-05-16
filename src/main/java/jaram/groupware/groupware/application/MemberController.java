package jaram.groupware.groupware.application;

import jaram.groupware.groupware.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Controller
public class MemberController {
    @RequestMapping(name = "/", method = RequestMethod.GET)
    public String lookupMembers(Map<String, Object> model){

        List<Member> members = Member.getMembers();

        model.put("members", members);

        return "lookUPMembers";
    }
}
