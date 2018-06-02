package jaram.groupware.groupware.module;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jaram.groupware.groupware.model.value.*;
import jaram.groupware.groupware.persistent.Member;
import jaram.groupware.groupware.repository.MemberRepository;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class Spreadsheets implements MemberRepository {
    public Spreadsheets() {
    }

    private static final String APPLICATION_NAME = "jaram_groupware";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CLIENT_SECRET_DIR = "../../../../client_secret.json";
    private static final String spreadsheetId = "1jbZu0nQ4ru-ypfXaz4gY6_XI9JV7gVnHq_3T7i9K-1c";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Member.class.getResourceAsStream(CLIENT_SECRET_DIR);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("hyu.cse.jaram@gmail.com");
    }

    @Override
    public List<Member> findAllMembers() throws IOException, GeneralSecurityException {
        Jedis jedis = new Jedis("localhost", 6379);
        Gson gson = new Gson();

        if (jedis.get("members") == null) {
            this.getMembers();
        }

        return gson.fromJson(jedis.get("members"), new TypeToken<List<Member>>() {}.getType());
    }

    private boolean getMembers() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = "A2:F";

        Sheets service = getSheets(HTTP_TRANSPORT);
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        List<Member> members = new LinkedList<>();

        if (values == null || values.isEmpty()) {
            return false;
        } else {
            for (List row : values) {
                CardinalNumber cardinalNumber = new CardinalNumber(Integer.parseInt((String)row.get(0)));
                Name name = new Name((String)row.get(1));
                Position position = Position.valueOf((String)row.get(2));
                Phone phone = new Phone((String)row.get(3));
                Email email = new Email((String)row.get(4));
                AttendingState attendingState = AttendingState.valueOf((String)row.get(5));
                members.add(new Member(cardinalNumber, name, position, phone, email, attendingState));
            }
        }

        writeJsonToRedis(members);

        return true;
    }

    @Override
    public List<Member> findMemberByCardinalNumber(CardinalNumber cardinalNumber) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        List<Member> result = new LinkedList<>();

        for (Member member : members) {
            if (member.getCardinalNumber() == cardinalNumber.getCardinalNumber()) {
                result.add(member);
            }
        }

        return result;
    }

    @Override
    public List<Member> findMemberByName(Name name) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        List<Member> result = new LinkedList<>();

        for (Member member : members) {
            if (member.getName().contains(name.getName())) {
                result.add(member);
            }
        }

        return result;
    }

    @Override
    public List<Member> findMemberByPosition(Position position) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        List<Member> result = new LinkedList<>();

        for (Member member : members) {
            if (member.getPosition().equals(position.getPosition())) {
                result.add(member);
            }
        }

        return result;
    }

    @Override
    public List<Member> findMemberByPhone(Phone phone) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        List<Member> result = new LinkedList<>();

        for (Member member : members) {
            if (member.getPhone().contains(phone.getPhone())) {
                result.add(member);
            }
        }

        return result;
    }

    @Override
    public List<Member> findMemberByEmail(Email email) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        List<Member> result = new LinkedList<>();

        for (Member member : members) {
            if (member.getEmail().contains(email.getEmail())) {
                result.add(member);
            }
        }

        return result;
    }

    @Override
    public List<Member> findMemberByAttendingState(AttendingState attendingState) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        List<Member> result = new LinkedList<>();

        for (Member member : members) {
            if (member.getAttendingState().equals(attendingState.getAttendingState())) {
                result.add(member);
            }
        }

        return result;
    }
    
    private boolean writeMembers() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = "A2:F";

        List<List<Object>> value = new LinkedList<>();
        List<Member> members = findAllMembers();

        createValue(value, members);

        Sheets service = getSheets(HTTP_TRANSPORT);

        ValueRange body = new ValueRange()
                .setValues(value);
        UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();

        return true;
    }

    private Sheets getSheets(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
    }

    private void createValue(List<List<Object>> value, List<Member> members) {
        for (Member member : members) {
            value.add(Arrays.asList(
                    member.getCardinalNumber(), member.getName(), member.getPosition(), member.getPhone(), member.getEmail(), member.getAttendingState()
            ));
        }

        value.add(Arrays.asList(
                "", "", "", "", "", ""
        ));
    }

    private boolean checkIntegrity(Email email) throws IOException, GeneralSecurityException {
        List<Member> members = findMemberByEmail(email);

        return members.size() <= 1;
    }

    @Override
    public boolean addMember(Member member) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        if (!checkIntegrity(new Email(member.getEmail())))
            return false;

        members.add(member);

        Collections.sort(members);

        writeJsonToRedis(members);
        writeMembers();

        return true;
    }

    @Override
    public Member findOneMemberByEmail(Email email) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();

        for (Member member : members) {
            if (member.getEmail().equals(email.getEmail())) {
                return member;
            }
        }

        return null;
    }

    @Override
    public Member findOneMemberByPhone(Phone phone) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();

        for (Member member : members) {
            if (member.getPhone().equals(phone.getPhone())) {
                return member;
            }
        }

        return null;
    }

    @Override
    public Member findOneMemberByCardinalNumberAndName(CardinalNumber cardinalNumber, Name name) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();

        for (Member member : members) {
            if (member.getCardinalNumber() == cardinalNumber.getCardinalNumber() && member.getName().equals(name.getName())) {
                return member;
            }
        }

        return null;
    }

    @Override
    public boolean updateMember(Member targetMember, CardinalNumber cardinalNumber, Name name, Position position, Phone phone, Email email, AttendingState attendingState) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();

        if (!this.checkIntegrity(email)) {
            return false;
        }

        Member updateMember = getOneMember(targetMember, members);

        if (updateMember == null) {
            return false;
        }

        updateMember.updateMember(cardinalNumber, name, position, phone, email, attendingState);

        Collections.sort(members);

        writeJsonToRedis(members);
        writeMembers();

        return true;
    }

    private void writeJsonToRedis(List<Member> members) {
        Jedis jedis = new Jedis("localhost", 6379);
        Gson gson = new Gson();

        String json = gson.toJson(members);
        jedis.set("members", json);
        jedis.expire("members", 60 * 60 * 6);
        jedis.close();
    }

    @Override
    public boolean deleteMember(Member targetMember) throws IOException, GeneralSecurityException {
        List<Member> members = findAllMembers();
        Member deleteMember = getOneMember(targetMember, members);

        if (deleteMember == null){
            return false;
        }

        writeJsonToRedis(members);
        writeMembers();

        return true;
    }

    private Member getOneMember(Member targetMember, List<Member> members) {
        for (Member member : members) {
            if (member.getEmail().equals(targetMember.getEmail()) || member.getPhone().equals(targetMember.getPhone()) ||
                    (member.getCardinalNumber() == targetMember.getCardinalNumber() && member.getName().equals(targetMember.getName()))) {
                return member;
            }
        }

        return null;
    }
}
