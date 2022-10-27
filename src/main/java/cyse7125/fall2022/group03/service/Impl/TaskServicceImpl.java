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

import cyse7125.fall2022.group03.model.Comment;
import cyse7125.fall2022.group03.model.Remainder;
import cyse7125.fall2022.group03.model.Tag;
import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.model.TaskIdentity;
import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.CommentRepository;
import cyse7125.fall2022.group03.repository.RemainderRepository;
import cyse7125.fall2022.group03.repository.TagRepository;
import cyse7125.fall2022.group03.repository.TaskRepository;
import cyse7125.fall2022.group03.service.TaskService;



@Service
public class TaskServicceImpl implements TaskService {
    
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    RemainderRepository remainderRepository;
    
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
            
            newTask.setUserId(user.getUserId());
            //newTask.setListId(newTask.getListId());
            
            newTask.setAccountCreated(String.valueOf(new Date()));
            newTask.setAccountUpdated(String.valueOf(new Date()));
            
            List<Tag> tagList = newTask.getTagList();
            for (Tag tag: tagList) {
                
                //if tag available use it, else new one
                Tag existTag = tagRepository.findTagByTagnameAndUserId(tag.getTagname(), newTask.getUserId());
                if (existTag != null) {
                    //need not, but good, as tag is being updated by linking to new task
                    
                    //existTag.setTagUpdated(String.valueOf(new Date()));     //will get error, because models should have all variables have value while save method is called in 117
                    tag.setTagUpdated(String.valueOf(new Date()));
                    tag.setTagCreated(existTag.getTagCreated());
                } 
                else {
                    tag.setUseri(user.getUserId());
                    
                    tag.setTagCreated(String.valueOf(new Date()));
                    tag.setTagUpdated(String.valueOf(new Date()));
                    
                    tagRepository.save(tag);
                }                
            }
            
            List<Comment> commentList = newTask.getCommentList();
            for (Comment comment : commentList) {
                
                comment.setCommentCreated(String.valueOf(new Date()));
                comment.setCommentUpdated(String.valueOf(new Date()));
                
                commentRepository.save(comment);
            }
            
            List<Remainder> remainderList = newTask.getRemainderList();
            for (Remainder remainder : remainderList) {
                
                //only in format yyyy-MM-dd HH:mm 
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                //LocalDateTime dateTime = LocalDateTime.parse(remainder.getDateTime(), formatter);
                
                remainder.setDateTime(remainder.getDateTime());
                
                remainder.setRemainderCreated(String.valueOf(new Date()));
                remainder.setRemainderUpdated(String.valueOf(new Date()));
                
                remainderRepository.save(remainder);
            }
            
            //Task taskToPut = new Task(user.getId(), newTask.getListId(), newTask.getSummary(), newTask.getName(), newTask.getDueDate(), String.valueOf(new Date()), String.valueOf(new Date()));
            
            newTask = taskRepository.save(newTask);
            
    
        }catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);
        }
        
        return generateResponse(newTask, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JSONObject> getAllTasksUnderAList(String listId) {
        
        User user = userServiceImpl.getCurrentUser();
        List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());
        
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
        List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());
        
        if( listOfTasks == null || listOfTasks.isEmpty()) {
            return generateResponse("{\"success\":\"You have no tasks, start creating\"}", HttpStatus.CREATED);
        }
        
        return generateResponse(listOfTasks, HttpStatus.CREATED);
    }
    
    @Override
    public ResponseEntity<JSONObject> getATask(String listId, String taskId) {
        
        User user = userServiceImpl.getCurrentUser();
        Optional<Task> task = taskRepository.findById(new TaskIdentity(user.getUserId(), listId, taskId));
        
        if( task == null || task.isEmpty()) {
            return generateResponse("{\"error\":\"You have no tasks or You dont have such a list/task\"}", HttpStatus.BAD_REQUEST);
        }
        
        List<Task> actualLists = task.isPresent() ? Collections.singletonList(task.get()) : Collections.emptyList();
        
        System.out.println(actualLists.toString());
        return generateResponse(actualLists, HttpStatus.CREATED);
    }
    
    @Override
    public Task getATask(String taskId) {
        
        //User user = userServiceImpl.getCurrentUser();
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
