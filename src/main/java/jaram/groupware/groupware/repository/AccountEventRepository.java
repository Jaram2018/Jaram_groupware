package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.persistence.event.AccountEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEvent,Long> {
    AccountEvent findAccountEventsById(Long id);
}