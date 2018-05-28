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
import jaram.groupware.groupware.model.MemberModel;
import jaram.groupware.groupware.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Spreadsheets implements MemberRepository {
    public Spreadsheets(){}

    @Autowired
    MemberModel memberModel;

    private static final String APPLICATION_NAME = "jaram_groupware";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CLIENT_SECRET_DIR = "../../../../client_secret.json";
    private static final String spreadsheetId = "1jbZu0nQ4ru-ypfXaz4gY6_XI9JV7gVnHq_3T7i9K-1c";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = MemberModel.class.getResourceAsStream(CLIENT_SECRET_DIR);
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
    public List<MemberModel> findAllMembers() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = "A2:F";

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        List<MemberModel> memberModels = new LinkedList<>();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                memberModels.add(new MemberModel(Integer.parseInt((String)row.get(0)), (String)row.get(1), (String)row.get(2),
                        (String)row.get(3), (String)row.get(4), (String)row.get(5)));
            }
        }

        return memberModels;
    }

    @Override
    public boolean writeMembers(List<MemberModel> memberModels) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = "A2:F";

        List<List<Object>> value = Arrays.asList();
        for (MemberModel memberModel : memberModels) {
            value.add(Arrays.asList(
                    memberModel.cardinalNumber, memberModel.name, memberModel.position, memberModel.phone, memberModel.email, memberModel.attendingState
            ));
        }

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange body = new ValueRange()
                .setValues(value);
        UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());

        return true;
    }

    @Override
    public List<MemberModel> findMemberByCardinalNumber(int cardinalNumber) throws IOException, GeneralSecurityException {
        List<MemberModel> memberModels = this.findAllMembers();
        List<MemberModel> result = new LinkedList<>();

        for (MemberModel memberModel : memberModels) {
            if (memberModel.cardinalNumber == cardinalNumber) {
                result.add(memberModel);
            }
        }

        return result;
    }

    @Override
    public List<MemberModel> findMemberByName(String name) {
        return null;
    }

    @Override
    public List<MemberModel> findMemberByPosition(String position) {
        return null;
    }

    @Override
    public List<MemberModel> findMemberByPhone(String phone) {
        return null;
    }

    @Override
    public List<MemberModel> findMemberByEmail(String email) {
        return null;
    }

    @Override
    public List<MemberModel> findMemberByAttendingState(String attendingState) {
        return null;
    }
}
