package cyse7125.fall2022.group03.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.model.TaskIdentity;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.TaskRepository;
import cyse7125.fall2022.group03.service.TaskService;


@Service
public class TaskServicceImpl implements TaskService {
    
    @Autowired
    TaskRepository taskRepository;
    
    @Autowired
    UserServiceImpl userServiceImpl;
    
    @Autowired
    ListsServiceImpl listsServiceImpl;

    @Override
    public ResponseEntity<JSONObject> createTask(Task newTask) {
        
        ResponseEntity<JSONObject> result;
        try {
            
            User user = userServiceImpl.getCurrentUser();
            
            result = listsServiceImpl.getAList(newTask.getListId());
            
            if (result.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return result;
            }
            
            Task taskToPut = new Task(user.getId(), newTask.getListId(), newTask.getSummary(), newTask.getName(), newTask.getDueDate(), String.valueOf(new Date()), String.valueOf(new Date()));
            
            newTask = taskRepository.save(taskToPut);
            
    
        }catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);
        }
        
        return generateResponse(newTask, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JSONObject> getAllTasksUnderAList(String listId) {
        
        User user = userServiceImpl.getCurrentUser();
        List<Task> listOfTasks = taskRepository.findByUserId(user.getId());
        
        if( listOfTasks == null || listOfTasks.isEmpty()) {
            return generateResponse("{\"error\":\"You have no tasks, start creating\"}", HttpStatus.BAD_REQUEST);
        }
        
       // List<Task> actualListOfTasks = listOfTasks.isPresent() ? Collections.singletonList(listOfTasks.get()) : Collections.emptyList();
        List<Task> finalListOfTasks = new ArrayList<Task>();
        for (Task t : listOfTasks) {
            if (t.getListId().equals(listId)) {
                finalListOfTasks.add(t);
            }
        }
        if (finalListOfTasks.isEmpty()) {
            return generateResponse("{\"error\":\"You dont have such a list\"}", HttpStatus.BAD_REQUEST);
        }
        
        return generateResponse(finalListOfTasks, HttpStatus.CREATED);
    }

    //self
    @Override
    public ResponseEntity<JSONObject> getAllTasks() {

        User user = userServiceImpl.getCurrentUser();
        List<Task> listOfTasks = taskRepository.findByUserId(user.getId());
        
        if( listOfTasks == null || listOfTasks.isEmpty()) {
            return generateResponse("{\"success\":\"You have no tasks, start creating\"}", HttpStatus.CREATED);
        }
        
        return generateResponse(listOfTasks, HttpStatus.CREATED);
    }
    
    @Override
    public ResponseEntity<JSONObject> getATask(String listId, String taskId) {
        
        User user = userServiceImpl.getCurrentUser();
        Optional<Task> task = taskRepository.findById(new TaskIdentity(user.getId(), listId, taskId));
        
        if( task == null || task.isEmpty()) {
            return generateResponse("{\"error\":\"You have no tasks or You dont have such a list/task\"}", HttpStatus.BAD_REQUEST);
        }
        
        List<Task> actualLists = task.isPresent() ? Collections.singletonList(task.get()) : Collections.emptyList();
        
        System.out.println(actualLists.toString());
        return generateResponse(actualLists, HttpStatus.CREATED);
    }
    
    @Override
    public Task getATask(String taskId) {
        
        User user = userServiceImpl.getCurrentUser();
        Task task = taskRepository.findByTaskId(taskId);
        
        return task;
    }
    
    //validate isapttaskuser

    public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {
        if(messageObject instanceof List<?>) {
            if (((List<?>) messageObject).size()==0) {
                messageObject = ((List<?>) messageObject).get(0);
            } else {
                JSONObject obj2=new JSONObject();
                obj2.put("List", messageObject);
                messageObject = obj2;
            }
        }
        
        return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);
    }
    public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
    }

    
}
