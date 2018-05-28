package jaram.groupware.groupware.repository;

import jaram.groupware.groupware.model.MemberModel;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Repository
public interface MemberRepository {
    public List<MemberModel> findAllMembers() throws IOException, GeneralSecurityException;

    public List<MemberModel> findMemberByCardinalNumber(int cardinalNumber) throws IOException, GeneralSecurityException;

    public List<MemberModel> findMemberByName(String name);

    public List<MemberModel> findMemberByPosition(String position);

    public List<MemberModel> findMemberByPhone(String phone);

    public List<MemberModel> findMemberByEmail(String email);

    public List<MemberModel> findMemberByAttendingState(String attendingState);

    public boolean writeMembers(List<MemberModel> memberModels) throws IOException, GeneralSecurityException;
}