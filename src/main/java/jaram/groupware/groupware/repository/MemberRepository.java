package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.model.MemberModel;
import jaram.groupware.groupware.model.value.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Member;
import java.security.GeneralSecurityException;
import java.util.List;

@Repository
public interface MemberRepository {
    List<MemberModel> findAllMembers() throws IOException, GeneralSecurityException;

    List<MemberModel> findMemberByCardinalNumber(CardinalNumber cardinalNumber) throws IOException, GeneralSecurityException;

    List<MemberModel> findMemberByName(Name name);

    List<MemberModel> findMemberByPosition(Position position);

    List<MemberModel> findMemberByPhone(Phone phone);

    List<MemberModel> findMemberByEmail(Email email);

    List<MemberModel> findMemberByAttendingState(AttendingState attendingState);

    boolean writeMembers(List<MemberModel> memberModels) throws IOException, GeneralSecurityException;

    boolean checkIntegrity(Email email);

    List<MemberModel> addMember(MemberModel newMemberModel);

    List<MemberModel> findMemberByCardinalNumberAndName(CardinalNumber cardinalNumber, Name name);

    List<MemberModel> updateMember(CardinalNumber cardinalNumber, Name name , Position position, Phone phone, Email email, AttendingState attendingState);
}