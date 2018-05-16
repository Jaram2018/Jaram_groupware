package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.persistent.AccountEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */
public interface AccountEventRepository extends JpaRepository<AccountEvent,Long> {
    List<AccountEvent> findAccountEventsByAmountPerMan(int amountPerMan);
}
