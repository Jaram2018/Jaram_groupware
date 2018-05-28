package jaram.groupware.groupware.model;

import jaram.groupware.groupware.persistent.AccountEvent;
import jaram.groupware.groupware.repository.AccountEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by NamHyunsil on 2018. 5. 28..
 */
@Component
public class AccountEventModel {
    public AccountEventModel(){

    }

    @Autowired
    AccountEventRepository accountEventRepository;

    public AccountEvent createAccountEvent(String name, int amountPerMan, int totalAmount, Date startDate, Date endDate, String[] members){
        AccountEvent newAccountEvent = new AccountEvent(name, amountPerMan, totalAmount, startDate, endDate, members);
        saveNewAccountEvent(newAccountEvent);
        return newAccountEvent;
    }

    @Transactional
    public void saveNewAccountEvent(AccountEvent newAccountEvent){
        accountEventRepository.save(newAccountEvent);
    }

    public AccountEvent getAccountEventById(Long id){
        return accountEventRepository.findAccountEventsById(id);
    }
}
