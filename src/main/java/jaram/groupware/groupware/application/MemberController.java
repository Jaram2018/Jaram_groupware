package jaram.groupware.groupware.application;

import jaram.groupware.groupware.model.Member;
import jaram.groupware.groupware.model.value.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Controller
public class MemberController {
    @Autowired
    private Member member;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String lookupMembers(Map<String, Object> model) throws IOException, GeneralSecurityException{

        List<Member> members = member.getMembers();

        model.put("members", members);

        return "lookUPMembers";
    }
}
