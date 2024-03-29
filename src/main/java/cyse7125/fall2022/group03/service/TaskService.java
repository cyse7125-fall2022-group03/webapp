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
    ResponseEntity<JSONObject> getATask(String listId, String taskId);
    Task getATask(String taskId);
	ResponseEntity<JSONObject> updateTask(Task newTask);
	ResponseEntity<JSONObject> deleteTask(String taskId);
	//void deleteTasksOfList(String listId, String userID) throws Exception;
	ResponseEntity<JSONObject> getAllTasksByTagName(String tagname); //self/{tagname}
	ResponseEntity<JSONObject> changeTaskToNewList (Task newTask);
}
