package cyse7125.fall2022.group03.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Task;

@Service
public interface TaskService {
    
    ResponseEntity<JSONObject> createTask(Task task);
    ResponseEntity<JSONObject> getAllTasksUnderAList(String listId);
    ResponseEntity<JSONObject> getAllTasks(); //self
    Task getATask(String listId, String taskId);
}
