package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.model.value.*;
import jaram.groupware.groupware.persistent.Member;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Repository
public interface MemberRepository {
    List<Member> findAllMembers() throws IOException, GeneralSecurityException;
    List<Member> findMemberByEmail(Email email) throws IOException, GeneralSecurityException;

}