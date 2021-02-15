package com.example.demo.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.controller.GoogleCalendarContoller;
import com.example.demo.dto.AuthProvider;
import com.example.demo.dto.GoogleToken;
import com.example.demo.dto.GoogleVO;
import com.example.demo.dto.Member;
import com.example.demo.dto.Schedule;
import com.example.demo.error.OAuth2AuthenticationProcessingException;
import com.example.demo.repository.GoogleRepository;
import com.example.demo.repository.MemberRepository;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

@Component
@RequiredArgsConstructor
@Log4j2
public class GoogleUtils {
	
	private static final String clientId="367773692104-ldevrvvo0gghju43m8cc1m6as95nkenn.apps.googleusercontent.com";
	private static final String clientScret="coRQQGpdBTxYDrjdy1NxI7g5";
	private static final String APPLICATION_NAME = "aaa";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static String TOKENS_DIRECTORY_PATH = "tokens";
	GoogleAuthorizationCodeFlow flow;
	GoogleClientSecrets clientSecrets;
	Credential credential;
	private static HttpTransport httpTransport;
	private static com.google.api.services.calendar.Calendar client;
	private final static Log logger = LogFactory.getLog(GoogleCalendarContoller.class);
	private CalMonth calMonth=new CalMonth();
	
	    /**
	     * Global instance of the scopes required by this quickstart.
	     * If modifying these scopes, delete your previously saved tokens/ folder.
	     */
	 private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
	 private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private GoogleRepository googleRepository;
	
	public Member checkMember(GoogleToken googleToken) throws IOException, GeneralSecurityException {
		GoogleTokenResponse tokenResponse =
		          new GoogleAuthorizationCodeTokenRequest(
		              new NetHttpTransport(),
		              JacksonFactory.getDefaultInstance(),
		              "https://www.googleapis.com/oauth2/v4/token",
		              clientId,
		              clientScret,
		              googleToken.getCode(),
		              googleToken.getRedirect_uri())  
		              .execute();
		String accessToken = tokenResponse.getAccessToken();		
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();
		String userId = payload.getSubject();  // Use this value as a key to identify a user.
		String email = payload.getEmail();
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get("name");
		String pictureUrl = (String) payload.get("picture");
		String locale = (String) payload.get("locale");
		String familyName = (String) payload.get("family_name");
		String givenName = (String) payload.get("given_name");
		
		GoogleVO user = new GoogleVO();
    	Member member=new Member();     
        user.setProviderId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setImageUrl(pictureUrl);
        
        member.setUserId(email);
        member.setName(name);
        List<String> groups=new ArrayList();
        member.setGroups(groups);
        member.setBirthDay(new Date());
        member.setPw("");
        System.out.println("user:"+user.toString());
        System.out.println("member:"+member.toString());
        Optional<GoogleVO> userOptional = googleRepository.findByEmail(email);     
      
        if(userOptional.isPresent()) {           
            member=memberRepository.findByUserId(email);
        } else {
            member=memberRepository.save(member);
            googleRepository.save(user);            
        }
       
		return member;
	}

	 public List<Schedule>  getCalInfo(GoogleToken googleToken) throws GeneralSecurityException, IOException {
		 com.google.api.services.calendar.model.Events eventList;
		 final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		 List<Schedule>list=new ArrayList<>();
			String message="";
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientScret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
					Collections.singleton(CalendarScopes.CALENDAR)).build();
			flow.newAuthorizationUrl().setRedirectUri("http://localhost:8081/add");
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				TokenResponse response = flow.newTokenRequest(googleToken.getCode()).setRedirectUri(googleToken.getRedirect_uri()).execute();
				credential = flow.createAndStoreCredential(response, "userID");
				client = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(APPLICATION_NAME).build();				
				//final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
				final DateTime now = new DateTime(new Date());
				Events events = client.events().list("primary")
		                .setMaxResults(30)
		                .setTimeMin(now)
		                .setOrderBy("startTime")
		                .setSingleEvents(true)
		                .execute();				
				List<Event> items = events.getItems();			
				
		        if (items.isEmpty()) {
		            System.out.println("No upcoming events found.");
		        } else {
		            System.out.println("Upcoming events");
		            for (Event event : items) {
		                DateTime start = event.getStart().getDateTime();
		                if (start == null) {
		                    start = event.getStart().getDate();
		                }
		                System.out.printf("%s (%s)\n", event.getSummary(), start);
		                Schedule schedule=new Schedule();
		                schedule.setColor("#F5A9E1");
		                schedule.setAllDay(true);
		                schedule.setTitle(event.getSummary()+"(google)");
		                schedule.setDescription(event.getDescription());
		                schedule.setStart(transFormat.parse(event.getStart().get("date")+""));
		                schedule.setEnd(transFormat.parse(event.getEnd().get("date")+""));
		                schedule.setSid("google");
		                schedule.setGroup("google");
		                System.out.println("schedule:"+schedule.toString());
		                list.add(schedule);
		                message+=event.getSummary();
		            }
		        }			
				System.out.println("My:" + events.getItems());
				
			} catch (Exception e) {
				logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
						+ " Redirecting to google connection status page.");
				message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
						+ " Redirecting to google connection status page.";			
			}

			System.out.println("cal message:" + message);
			return list;
			
	 }
	
}
