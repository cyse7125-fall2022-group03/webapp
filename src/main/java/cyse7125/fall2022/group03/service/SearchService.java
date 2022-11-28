package cyse7125.fall2022.group03.service;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.alibaba.fastjson.JSONObject;

@Service
public interface SearchService {

	ResponseEntity<JSONObject> getSearchTasks(String keyword);
}
