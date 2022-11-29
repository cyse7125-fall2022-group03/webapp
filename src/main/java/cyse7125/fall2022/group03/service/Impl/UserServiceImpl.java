package cyse7125.fall2022.group03.service.Impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Lists;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.ListsRepository;
import cyse7125.fall2022.group03.repository.UserRepository;
import cyse7125.fall2022.group03.service.UserService; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.prometheus.client.Histogram;
import io.prometheus.client.CollectorRegistry;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final Histogram requestLatency_userDb;
	
	public UserServiceImpl(CollectorRegistry registry) {
        requestLatency_userDb = Histogram.build()
                .name("requests_latency_seconds_userDb").help("userDb Request latency in seconds").register(registry);
    }
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	ListsRepository listsRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@SuppressWarnings("rawtypes")
	ResponseEntity NoContent = new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(null)), HttpStatus.NO_CONTENT);


	@Override
	public ResponseEntity<JSONObject> createUser(User user) {
		logger.info("Service - create a user");

		user.setAccountCreated(LocalDateTime.now());
		user.setAccountUpdated(LocalDateTime.now());

		if (!checkEmailFormat(user.getEmail())){
			logger.error("createUser - Email Constraints not met");

			return generateResponse("{\"error\":\"Email Constraints not met\"}", HttpStatus.BAD_REQUEST);
		}

		Histogram.Timer requestTimer = requestLatency_userDb.startTimer();
		//check email already exists
		if (userRepository.existsByEmail(user.getEmail())){
			logger.error("createUser - Email Already Exists");

			return  generateResponse("{\"error\":\"Email Already Exists\"}", HttpStatus.BAD_REQUEST);
		}
		requestTimer.observeDuration();

		//password constraints
		String password = user.getPassword();
		if (!checkPassword(password)){
			logger.error("createUser - Password Constraints not met");

			return generateResponse("{\"error\":\"Password Constraints not met\"}", HttpStatus.BAD_REQUEST);
		}

		//encode
		//String newPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		String newPassword = bCryptPasswordEncoder.encode(password);
		user.setPassword(newPassword);

		Histogram.Timer requestTimer2 = requestLatency_userDb.startTimer();
		try{
			//System.out.println(JSON.toJSONString(user));

			userRepository.save(user);

			//Lists newList = new Lists(new ListsIdentity(user.getId()), "List1", String.valueOf(new Date()), String.valueOf(new Date()));
			Lists newList = new Lists(user.getUserId(),"List1", LocalDateTime.now(), LocalDateTime.now());

			listsRepository.save(newList);

		}catch (Exception e){
			logger.error("createUser - Exception");
			e.printStackTrace();
			return generateResponse(null, HttpStatus.BAD_REQUEST);
		} finally {
			requestTimer2.observeDuration();
		}

		return generateResponse(user, HttpStatus.CREATED);
	}

	public static boolean checkPassword(String password){
		String pattern = "^[0-9A-Za-z]{8,16}$";
		boolean isMatch = Pattern.matches(pattern,password);
		return isMatch;
	}

	public static boolean checkEmailFormat(String email){
		String pattern = "^(.+)@(.+)$";
		boolean isMatch = Pattern.matches(pattern,email);
		return isMatch;
	}

	public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {

		return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);

	}
	public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {

		return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
	}


	public static String getCurrentUserEmail(){
		String currentUserEmail = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			currentUserEmail = ((UserDetails)principal).getUsername();
		}else {
			currentUserEmail = principal.toString();
		}
		return currentUserEmail;
	}
	public static String getCurrentUserEmail(Object principal){
		String currentUserEmail = "";
		if(principal instanceof UserDetails) {
			currentUserEmail = ((UserDetails)principal).getUsername();
		}else {
			currentUserEmail = principal.toString();
		}
		return currentUserEmail;
	}

	public String getCurrentUserID(){
		String currentUserEmail = UserServiceImpl.getCurrentUserEmail();
		User user = userRepository.findByEmail(currentUserEmail);
		return user.getUserId();
	}
	public User getCurrentUser() throws Exception{
		String currentUserEmail = UserServiceImpl.getCurrentUserEmail();
		User user = userRepository.findByEmail(currentUserEmail);
		//check for password ?
		return user;
	} 

	public ResponseEntity<JSONObject> checkValidUser(User user){
		logger.info("Service - check valid user");

		if (user == null) {
			logger.error("checkValidUser - Email not registered");

			return generateResponse("{\"error\":\"Email not registered\"}", HttpStatus.BAD_REQUEST);
		}

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//System.out.println(((UserDetails)principal).toString());
		@SuppressWarnings("unused")
		String pwd = ((UserDetails)principal).getPassword();
		//System.out.println(pwd);
		//System.out.println(user.getPassword());
		//String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());

		//if (user.getPassword().equals(pwd)) {
		//    return generateResponse(user, HttpStatus.FOUND);
		//}
		//return generateResponse("{\"error\":\"Email & password do not match\"}", HttpStatus.BAD_REQUEST);
		return null;
	}

	@Override
	public ResponseEntity<JSONObject> getUserDetails() {   
		try {
			User user = getCurrentUser();

			ResponseEntity<JSONObject> result = checkValidUser(user);
			if (result != null) {
				return result;
			}
			//password to be removed from json
			return generateResponse(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getUserDetails - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<JSONObject> updateEmail(Map<String, String> request) {
		logger.info("Service - update user email");
		
		try {
			User user = getCurrentUser();
			ResponseEntity<JSONObject> result = checkValidUser(user);
			if (result != null) {
				return result;
			}

			String newEmail = request.get("email");
			if (newEmail == null) {
				logger.error("updateEmail - Please enter a valid value to key email");

				return generateResponse("{\"error\":\"Please enter a valid value to key email\"}", HttpStatus.BAD_REQUEST);
			}
			if (!checkEmailFormat(newEmail)){
				logger.error("updateEmail - Email Constraints not met");

				return generateResponse("{\"error\":\"Email Constraints not met\"}", HttpStatus.BAD_REQUEST);
			}

			Histogram.Timer requestTimer2 = requestLatency_userDb.startTimer();
			User testUser = userRepository.findByEmail(newEmail);
			requestTimer2.observeDuration();
			if (testUser != null) {
				logger.error("updateEmail - Email id already exists in DB");

				return generateResponse("{\"error\":\"Email id already exists in DB\"}", HttpStatus.BAD_REQUEST);
			}

			user.setEmail(newEmail);
			user.setAccountUpdated(LocalDateTime.now());

			Histogram.Timer requestTimer = requestLatency_userDb.startTimer();
			userRepository.save(user);
			requestTimer.observeDuration();

			return generateResponse(user, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("updateEmail - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<JSONObject> updateUser(User newUserValues) {
		logger.info("Service - update user details");

		try {
			User user = getCurrentUser();

			ResponseEntity<JSONObject> result = checkValidUser(user);
			if (result != null) {
				return result;
			}
			if (newUserValues.getAccountCreated()!=null || newUserValues.getAccountUpdated()!=null || newUserValues.getUserId()!=null) {
				logger.error("updateUser - Request can not have UserId, Account created or updated");

				return generateResponse("{\"error\":\"Request can not have UserId, Account created or updated\"}", HttpStatus.BAD_REQUEST);
			}
			if (newUserValues.getEmail() != null && !newUserValues.getEmail().equals(user.getEmail())) {
				logger.error("updateUser - Email should be updated through another API");

				return generateResponse("{\"error\":\"Email should be updated through another API\"}", HttpStatus.BAD_REQUEST);
			}		

			if(newUserValues.getFirstName() != null) {
				user.setFirstName(newUserValues.getFirstName());
			}
			if(newUserValues.getMiddleName() != null) {
				user.setMiddleName(newUserValues.getMiddleName());
			}
			if(newUserValues.getLastName() != null) {
				user.setLastName(newUserValues.getLastName());
			}
			if(newUserValues.getPassword() != null) {
				user.setPassword(newUserValues.getPassword());
			}
			
			Histogram.Timer requestTimer = requestLatency_userDb.startTimer();
			userRepository.save(user);
			requestTimer.observeDuration();

			return generateResponse(user, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("updateUser - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
