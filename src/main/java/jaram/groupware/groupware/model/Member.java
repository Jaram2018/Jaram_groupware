package jaram.groupware.groupware.model;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import jaram.groupware.groupware.model.value.*;
import org.springframework.stereotype.Component;
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
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by NamHyunsil on 2018. 5. 15..
 */

@Component
public class Member {
    public int cardinalNumber;
    public String name;
    public String position;
    public String phone;
    public String email;
    public String isAttending;

    public Member() {

    }
    private static final String APPLICATION_NAME = "jaram_groupware";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CLIENT_SECRET_DIR = "../../../../client_secret.json";

    public Member(int cardinalNumber, String name, String position, String phone, String email, String isAttending){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.isAttending = isAttending;
    }

    public Member(int cardinalNumber, String name, String phone, String email){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = "준회원";
        this.isAttending = "재학";
    }

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

    public static List<Member> getMembers() throws IOException, GeneralSecurityException{
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1jbZu0nQ4ru-ypfXaz4gY6_XI9JV7gVnHq_3T7i9K-1c";
        final String range = "A1:F";

        //FIXME READ Spreadsheets
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        List<Member> members = new LinkedList<>();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                members.add(new Member(Integer.parseInt((String)row.get(0)), (String)row.get(1), (String)row.get(2),
                        (String)row.get(3), (String)row.get(4), (String)row.get(5)));
            }
        }


        //FIXME WRITE Spreadsheets
        List<List<Object>> value = Arrays.asList(
                Arrays.asList(
                        "기수", "이름", "직책"
                )
        );
        ValueRange body = new ValueRange()
                .setValues(value);
        UpdateValuesResponse result =
                service.spreadsheets().values().update(spreadsheetId, range, body)
                        .setValueInputOption("RAW")
                        .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());

        return members;
    }
}
