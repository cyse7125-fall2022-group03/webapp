package cyse7125.fall2022.group03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.service.TaskService;

@RestController
@RequestMapping("/v1/user")
public class TasksController {
    
    @Autowired
    TaskService taskService;
    
    @GetMapping("/self")
    public ResponseEntity<JSONObject> getAllTasks() {
        
        return taskService.getAllTasks();
    }
    
    @GetMapping("/task/{listId}/{taskId}")
    public ResponseEntity<JSONObject> getATask(@PathVariable String listId, @PathVariable String taskId) {
        
        if (listId == null || listId.isBlank() || taskId == null || taskId.isBlank()) {
            return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
        }
        
        return taskService.getATask(listId, taskId);
        
    }
    
    @GetMapping("/task/{listId}")
    public ResponseEntity<JSONObject> getAllTasksUnderAList(@PathVariable String listId) {
        
        if (listId == null || listId.isBlank()) {
            return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
        }
        
        return taskService.getAllTasksUnderAList(listId);
    }
    
    @PostMapping("/task/create")
    public ResponseEntity<JSONObject> createTask(@RequestBody Task newTask) {
        
        if (newTask == null) {
            return generateResponse("{\"error\":\"Invalid request body\"}", HttpStatus.BAD_REQUEST);
        }
        // And other task variable validations - left
        
        if (newTask.getTaskId() != null) {
            return generateResponse("{\"error\":\"Request cant hold Task ID\"}", HttpStatus.BAD_REQUEST);
        }
        
        if (newTask.getUserId() != null) {
            return generateResponse("{\"error\":\"Request cant hold User ID - Not allowed to see others\"}", HttpStatus.BAD_REQUEST);
        }
        
        return taskService.createTask(newTask);
    }
    
    public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);
    }
    public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
    }

}
