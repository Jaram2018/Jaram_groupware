package jaram.groupware.groupware.application;

import jaram.groupware.groupware.model.MemberModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Controller
public class MemberController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String lookupMembers(Map<String, Object> model){

        List<MemberModel> members = MemberModel.getMembers();

        model.put("members", members);

        return "member/list";
    }
}
