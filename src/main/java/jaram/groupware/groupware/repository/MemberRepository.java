package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.model.Member;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface MemberRepository {
    public List<Member> findAllMembers() throws IOException, GeneralSecurityException;
    public List<Member> findMemberByCardinalNumber(int cardinalNumber);
    public List<Member> findMemberByName(String name);
    public List<Member> findMemberByPosition(String position);
    public List<Member> findMemberByPhone(String phone);
    public List<Member> findMemberByEmail(String email);
    public List<Member> findMemberByAttendingState(String attendingState);
    public boolean writeMembers(List<Member> members) throws IOException, GeneralSecurityException;
}
