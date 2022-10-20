package cyse7125.fall2022.group03.service;

import org.springframework.http.ResponseEntity;
import com.alibaba.fastjson.JSONObject;
import cyse7125.fall2022.group03.model.User;

public interface UserService {
    
    ResponseEntity<JSONObject> createUser(User user);
}
