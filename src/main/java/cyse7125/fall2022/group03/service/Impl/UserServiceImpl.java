package cyse7125.fall2022.group03.service.Impl;

import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Lists;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.ListsRepository;
import cyse7125.fall2022.group03.repository.UserRepository;
import cyse7125.fall2022.group03.service.UserService; 

@Service("userService")
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ListsRepository listsRepository;

    @SuppressWarnings("rawtypes")
    ResponseEntity NoContent = new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(null)), HttpStatus.NO_CONTENT);
    
   
    @Override
    public ResponseEntity<JSONObject> createUser(User user) {
        
        user.setAccountCreated(String.valueOf(new Date()));
        user.setAccountUpdated(String.valueOf(new Date()));
        
        if (!checkEmailFormat(user.getEmail())){
            return generateResponse("{\"error\":\"Email Constraints not met\"}", HttpStatus.BAD_REQUEST);
        }
        
        //check email already exists
        if (userRepository.existsByEmail(user.getEmail())){
            return  generateResponse("{\"error\":\"Email Already Exists\"}", HttpStatus.BAD_REQUEST);
        }
        
        //password constraints
        String password = user.getPassword();
        if (!checkPassword(password)){
            return generateResponse("{\"error\":\"Password Constraints not met\"}", HttpStatus.BAD_REQUEST);
        }

        //encode
        String newPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(newPassword);
        
        try{
            System.out.println(JSON.toJSONString(user));
            
            userRepository.save(user);
            
            //Lists newList = new Lists(new ListsIdentity(user.getId()), "List1", String.valueOf(new Date()), String.valueOf(new Date()));
            Lists newList = new Lists(user.getId(),"List1", String.valueOf(new Date()), String.valueOf(new Date()));
            
            listsRepository.save(newList);

        }catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);
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
        return user.getId();
    }
    public User getCurrentUser(){
        String currentUserEmail = UserServiceImpl.getCurrentUserEmail();
        User user = userRepository.findByEmail(currentUserEmail);
        return user;
    }
    
 
    
}
