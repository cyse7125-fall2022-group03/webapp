package cyse7125.fall2022.group03.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cyse7125.fall2022.group03.model.Comment;
import cyse7125.fall2022.group03.model.Remainder;
import cyse7125.fall2022.group03.model.Tag;
import cyse7125.fall2022.group03.model.TagIdentity;
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

    //@Transactional
    @Override
    public ResponseEntity<JSONObject> createTask(Task newTask) {
        
        ResponseEntity<JSONObject> result;
        try {
            
            User user = userServiceImpl.getCurrentUser();
            
            //is the newlist of same user
            result = listsServiceImpl.getAList(newTask.getListId());
            
            if (result.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return result;
            }
            
            newTask.setUserId(user.getUserId());
            //newTask.setListId(newTask.getListId());
            
            newTask.setAccountCreated(String.valueOf(new Date()));
            newTask.setAccountUpdated(String.valueOf(new Date()));
            
            newTask.setStatus(Task.Status.TODO);
            
            if (newTask.getPriority() == null) {
            	newTask.setPriority(Task.Priority.low);	//default
            }
            
            if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {
            	List<Tag> tagList = newTask.getTagList();
            	for (Tag tag: tagList) {
            		
            		if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
                		return generateResponse("{\"error\":\"Better have empty tagList rather than having a tag with no name\"}", HttpStatus.BAD_REQUEST);
                	}
                
            		//if tag available use it, else new one
            		List<Tag> existTagList = tagRepository.findTagByTagnameAndUserId(tag.getTagname(), user.getUserId());
            		//Optional<Tag> existTagList = tagRepository.findById(new TagIdentity(user.getUserId(), tag.getTagname()));
            	
            		if( existTagList == null || existTagList.isEmpty()) {
            			//create new tag
            			tag.setUseri(user.getUserId());
            			//tagName pre-exists
                    
            			tag.setTagCreated(String.valueOf(new Date()));
            			tag.setTagUpdated(String.valueOf(new Date()));
                    
            			tagRepository.save(tag);
            		} else {
            			//List<Tag> actualLists = existTagList.isPresent() ? Collections.singletonList(existTagList.get()) : Collections.emptyList();
            			Tag existTag = existTagList.get(0);
            			System.out.println(existTag.toString());
            			//need not, but good, as tag is being updated by linking to new task
                    
            			//existTag.setTagUpdated(String.valueOf(new Date()));     //will get error, because models should have all variables have value while save method is called in 117
            			tag.setTagUpdated(String.valueOf(new Date()));
            			tag.setTagCreated(String.valueOf(new Date()));
                    
            			//tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), user.getUserId());		//update tagUpdated in multiple taskid rows
            			tagRepository.save(tag);
            		}               
            	}
            }
            
            if( newTask.getCommentList() != null  && !newTask.getCommentList().isEmpty()) {
            	List<Comment> commentList = newTask.getCommentList();
            	for (Comment comment : commentList) {
                
            		comment.setCommentCreated(String.valueOf(new Date()));
            		comment.setCommentUpdated(String.valueOf(new Date()));
                
            		commentRepository.save(comment);
            	}
            }
            
            if( newTask.getRemainderList() != null  && !newTask.getRemainderList().isEmpty()) {
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
    
    //validate isapttaskuser ?
    
    

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

    //@Transactional
    @Override
    public ResponseEntity<JSONObject> updateTask (Task newTask) {
    	
    	ResponseEntity<JSONObject> result;
        try {
            
            User user = userServiceImpl.getCurrentUser();
            Task existingTask = taskRepository.findTaskByTaskIdAndUserId(newTask.getTaskId(), user.getUserId());
            
            if (existingTask == null) {
            	return generateResponse("{\"error\":\"You dont have such a Task\"}", HttpStatus.BAD_REQUEST);
            }
            
            if (newTask.getAccountCreated() != null) {
            	return generateResponse("{\"error\":\"Can't update Creation Date\"}", HttpStatus.BAD_REQUEST);
            }
            if (newTask.getAccountUpdated() != null) {
            	return generateResponse("{\"error\":\"Can't update Updated Date\"}", HttpStatus.BAD_REQUEST);
            }
            
            if (newTask.getListId() != null && existingTask.getListId() != newTask.getListId()) {
            	// moving to other list
            	
            	//is the newlist of same user
            	result = listsServiceImpl.getAList(newTask.getListId());
                if (result.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    return result;
                }
                
            	//nothing to remove from Lists table, as we maintain only in Tasks table.
            	taskRepository.updateListId(newTask.getListId(), existingTask.getTaskId(), existingTask.getUserId(), existingTask.getListId());
            	
//            	if (updateResult != 1) {
//            		return generateResponse("{\"error\":\"Error during moving to another listid\"}", HttpStatus.BAD_REQUEST);
//            	}
            		
            	existingTask.setListId(newTask.getListId());
            }
            
            //have to change if datatype of duedate is not string
            // compare due is future from now ?
            if (newTask.getDueDate() != null && !newTask.getDueDate().equals(existingTask.getDueDate())) {            	
            	
            	existingTask.setDueDate(newTask.getDueDate());
            	
            	if (existingTask.getStatus() == Task.Status.OVERDUE) {
            		existingTask.setStatus(Task.Status.TODO);
            	}
            }
            
            if (newTask.getSummary() != null) {
            	existingTask.setSummary(newTask.getSummary());
            }
            if (newTask.getName() != null) {
            	existingTask.setName(newTask.getName());
            }
            if (newTask.getStatus() != null) {
            	existingTask.setStatus(newTask.getStatus());
            }
            if (newTask.getPriority() != null) {
            	existingTask.setPriority(newTask.getPriority());
            }
            
            if( newTask.getRemainderList() != null  && !newTask.getRemainderList().isEmpty()) {
            	Iterator<Remainder> remainderIterator = existingTask.getRemainderList().iterator();
            	while(remainderIterator.hasNext()){
            		Remainder remainder = remainderIterator.next();
            		//remainderRepository.deleteByRemainderId(remainder.getRemainderId());
            		remainderRepository.delete(remainder);
            	}
            	List<Remainder> remainderLists = newTask.getRemainderList();
            	for (Remainder remainder : remainderLists) {
            		remainder.setRemainderCreated(String.valueOf(new Date()));
            		remainder.setRemainderUpdated(String.valueOf(new Date()));
            		remainderRepository.save(remainder);
            	}
            	existingTask.setRemainderList(remainderLists);
            }
            
            
            if( newTask.getCommentList() != null  && !newTask.getCommentList().isEmpty()) {
            	Iterator<Comment> commentIterator = existingTask.getCommentList().iterator();
            	while(commentIterator.hasNext()){
            		Comment comment = commentIterator.next();
            		//commentRepository.deleteByCommentId(comment.getCommentId());
            		commentRepository.delete(comment);
            	}
            	List<Comment> commentLists = newTask.getCommentList();
            	for (Comment comment : commentLists) {
            		comment.setCommentCreated(String.valueOf(new Date()));
            		comment.setCommentUpdated(String.valueOf(new Date()));
            		commentRepository.save(comment);
            	}
            	existingTask.setCommentList(commentLists);
            }
            
            if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {
            	
            	List<Tag> tagList = newTask.getTagList();
                for (Tag tag: tagList) {
                    
                	if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
                		return generateResponse("{\"error\":\"Better have empty tagList rather than having a tag with no name\"}", HttpStatus.BAD_REQUEST);
                	}
                    //if tag available use it, else new one
                	List<Tag> existTagList = tagRepository.findTagByTagnameAndUserId(tag.getTagname(), user.getUserId());
                	//Optional<Tag> existTagList = tagRepository.findById(new TagIdentity(user.getUserId(), tag.getTagname()));
                	
                	if( existTagList == null || existTagList.isEmpty()) {
                        //create new tag
                		tag.setUseri(user.getUserId());
                		//tagName pre-exists
                        
                        tag.setTagCreated(String.valueOf(new Date()));
                        tag.setTagUpdated(String.valueOf(new Date()));
                        
                        tagRepository.save(tag);
                    } else {
                    	//List<Tag> actualLists = existTagList.isPresent() ? Collections.singletonList(existTagList.get()) : Collections.emptyList();
                    	Tag existTag = existTagList.get(0);	//only 1 will be there for user-tagName combination
                    	
                    	//need not, but good, as tag is being updated by linking to new task
                        
                        //existTag.setTagUpdated(String.valueOf(new Date()));     //will get error, because models should have all variables have value while save method is called in 117
                        tag.setTagUpdated(String.valueOf(new Date()));
                        tag.setTagCreated(existTag.getTagCreated());
                        
                        tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), user.getUserId());		//update tagUpdated in multiple taskid rows
                    }
                }
                existingTask.setTagList(tagList);
            }
            
            existingTask.setAccountUpdated(String.valueOf(new Date()));
            
            taskRepository.save(existingTask);
        
        } catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);
        }
        
        return generateResponse("{\"success\":\"Update is done!\"}", HttpStatus.CREATED);
    }
    
    
}
