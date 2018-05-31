package jaram.groupware.groupware.application;

import jaram.groupware.groupware.persistent.AccountEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by NamHyunsil on 2018. 5. 17..
 */

@Controller
public class AccountEventController {
    public String[] members = null;

    @Autowired
    AccountEventModel accountEventModel;

    @RequestMapping(value = "/account-event/{id}", method = RequestMethod.GET)
    public String lookupAccountEvent(@PathVariable String id, Map<String, Object> model){
        //error page logic
        if(id == null || isStringLong(id) == false){
            return "account_event/errorPage";
        }

        long idL = Long.parseLong(id);
        AccountEvent accountEventWithId = accountEventModel.getAccountEventById(idL);
        if(accountEventWithId == null){
            return  "errorPage";
        }
        model.put("accountEvent",accountEventWithId);

        return "account_event/lookup";
    }

    @RequestMapping(value = "/account-event", method = RequestMethod.GET)
    public String getAddAccountEvent(Map<String, Object> model){
        model.put("members", MemberModel.getMembers());
        return "account_event/create";
    }


    @RequestMapping(value = "/account-event", method = RequestMethod.POST)
    public String createAccountEvent(HttpServletRequest request, RedirectAttributes redirectAttributes, Map<String, Object> model){
        String name = request.getParameter("name");
        String amountPerMan = request.getParameter("amountPerMan");
        String totalAmount = request.getParameter("totalAmount");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        this.members = request.getParameterValues("collectedMember");

        redirectAttributes.addFlashAttribute("name",name);
        redirectAttributes.addFlashAttribute("amountPerMan",amountPerMan);
        redirectAttributes.addFlashAttribute("totalAmount",totalAmount);
        redirectAttributes.addFlashAttribute("startDate",startDate);
        redirectAttributes.addFlashAttribute("endDate",endDate);

        int amountPerManInt = 0;
        int totalAmountInt = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateDate = null;
        Date endDateDate = null;

        //handle null error
        if(name.equals("")|| amountPerMan.equals("") || totalAmount.equals("") || startDate.equals("") || endDate.equals("") || members == null){
            redirectAttributes.addFlashAttribute("atLeastOneNullError", "You must write everything");

            handleStringTypeNullError(name,"nameNullError","Wrint your Name", redirectAttributes);
            handleStringTypeNullError(amountPerMan, "amountPerManNullError", "Wrint your amountPerMan", redirectAttributes);
            handleStringTypeNullError(totalAmount,"totalAmountNullError", "Wrint your totalAmount", redirectAttributes);
            handleStringTypeNullError(startDate,"startDateNullError", "Select your startDate", redirectAttributes);
            handleStringTypeNullError(endDate,"endDateNullError", "Select your endDate", redirectAttributes);
            if(this.members == null){
                redirectAttributes.addFlashAttribute("memberNullError","Check your members");
            }
            else;
            return "redirect:/account-event";
        }
        //handle type error
        if (isStringInt(amountPerMan) == false || isStringInt(totalAmount) == false || isStringDate(startDate, dateFormat) == false
                || isStringDate(endDate, dateFormat) == false) {
            if (isStringInt(amountPerMan) == false) {
                redirectAttributes.addFlashAttribute("amountPerManTypeError", "It have to be int type");
            }
            else if (isStringInt(totalAmount) == false) {
                redirectAttributes.addFlashAttribute("totalAmountTypeError", "It have to be int type");
            }
            else if (isStringDate(startDate, dateFormat) == false) {
                redirectAttributes.addFlashAttribute("startDateTypeError", "It have to be date type");
            }
            else if (isStringDate(endDate, dateFormat) == false) {
                redirectAttributes.addFlashAttribute("startDateTypeError", "It have to be date type");
            }else ;
            return "redirect:/account-event";
        }
        //change type
        amountPerManInt = Integer.parseInt(amountPerMan);
        totalAmountInt = Integer.parseInt(totalAmount);
        startDateDate = makeStringToDate(startDate, dateFormat);
        endDateDate = makeStringToDate(endDate, dateFormat);
        //handle startDate, endDate error
        if (isEndDateAfterStartDate(startDateDate, endDateDate) == false) {
            redirectAttributes.addFlashAttribute("endDateIsBeforeStartDateError", "end date have to be after start date");
            return "redirect:/account-event";
        }
        //createAccountEvent
        AccountEvent newAccountEvent = accountEventModel.createAccountEvent(name, amountPerManInt, totalAmountInt, startDateDate, endDateDate, members);
        model.put("newAccountEvent", newAccountEvent.getName());

        return "redirect:/account-event";
    }


    //only checkinput is String type
    public void handleStringTypeNullError(String checkinput, String nullErrorMessage, String message, RedirectAttributes redirectAttributes){
        if(checkinput.equals("")){
            redirectAttributes.addFlashAttribute(nullErrorMessage,message);
        }
        else;
    }


    public boolean isStringLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean isStringInt(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean isStringDate(String s, DateFormat dateFormat) {
        try {
            dateFormat.parse(s);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Date makeStringToDate(String dateString, DateFormat dateFormat) {
        Date dateDate = null;
        try {
            dateDate = dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateDate;
    }

    public boolean isEndDateAfterStartDate(Date startDateDate, Date endDateDate) {
        if (startDateDate.compareTo(endDateDate)>0){
            return false;
        }
        else {
            return true;
        }
    }

    //TODO:코드리뷰 필요 : 더 나은 방법이 있는가? checkbox의 value에는 오직 string만 들어갈 수 있는가?
    @ModelAttribute("memberList")
    public List<String> getMemberList(){
        if(this.members != null){
            return  Arrays.asList(this.members);
        }
        return new ArrayList<>();

    }
}

