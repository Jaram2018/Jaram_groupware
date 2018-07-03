package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.model.value.*;
import jaram.groupware.groupware.persistence.Member;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Repository
public interface MemberRepository {
    List<Member> findAllMembers() throws IOException, GeneralSecurityException;

    List<Member> findMemberByCardinalNumber(CardinalNumber cardinalNumber) throws IOException, GeneralSecurityException;

    List<Member> findMemberByName(Name name) throws IOException, GeneralSecurityException;

    List<Member> findMemberByPosition(Position position) throws IOException, GeneralSecurityException;

    List<Member> findMemberByPhone(Phone phone) throws IOException, GeneralSecurityException;

    List<Member> findMemberByEmail(Email email) throws IOException, GeneralSecurityException;

    List<Member> findMemberByAttendingState(AttendingState attendingState) throws IOException, GeneralSecurityException;

    boolean addMember(Member newMember) throws IOException, GeneralSecurityException;

    Member findOneMemberByEmail(Email email) throws IOException, GeneralSecurityException;

    Member findOneMemberByPhone(Phone phone) throws IOException, GeneralSecurityException;

    Member findOneMemberByCardinalNumberAndName(CardinalNumber cardinalNumber, Name name) throws IOException, GeneralSecurityException;

    boolean updateMember(Member targetMember, CardinalNumber cardinalNumber, Name name, Position position, Phone phone, Email email, AttendingState attendingState) throws IOException, GeneralSecurityException;

    boolean deleteMember(Member targetMember) throws IOException, GeneralSecurityException;
}