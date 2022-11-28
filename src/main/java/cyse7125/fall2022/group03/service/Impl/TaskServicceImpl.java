package cyse7125.fall2022.group03.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class TaskServicceImpl implements TaskService {
	private static final Logger logger = LoggerFactory.getLogger(TaskServicceImpl.class);

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
		logger.info("Service - create a task");

		ResponseEntity<JSONObject> result;
		try {

			User user = userServiceImpl.getCurrentUser();

			//is the newlist of same user
			result = listsServiceImpl.getAList(newTask.getListId());

			if (result.getStatusCode() != HttpStatus.OK) {
				return result;
			}

			newTask.setUserId(user.getUserId());
			//newTask.setListId(newTask.getListId());

			newTask.setAccountCreated(LocalDateTime.now());
			newTask.setAccountUpdated(LocalDateTime.now());

			newTask.setStatus(Task.Status.TODO);

			if (newTask.getPriority() == null) {
				newTask.setPriority(Task.Priority.low);	//default
			}

			//all check before saving
			if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {
				if (newTask.getTagList().size() > 10) {
					logger.error("createTask - A task can have only upto 10 tags");

					return generateResponse("{\"error\":\"A task can have only upto 10 tags\"}", HttpStatus.BAD_REQUEST);
				}

				List<Tag> tagList = newTask.getTagList();
				for (Tag tag: tagList) {
					if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
						logger.error("createTask - Better have empty tagList rather than having a tag with no name");

						return generateResponse("{\"error\":\"Better have empty tagList rather than having a tag with no name\"}", HttpStatus.BAD_REQUEST);
					}
				}
			}            
			if( newTask.getRemainderList() != null  && !newTask.getRemainderList().isEmpty()) {
				if (newTask.getRemainderList().size() > 5) {
					logger.error("createTask - A task can have only upto 5 remainders");

					return generateResponse("{\"error\":\"A task can have only upto 5 remainders\"}", HttpStatus.BAD_REQUEST);
				}
			}

			if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {

				List<Tag> tagList = newTask.getTagList();
				for (Tag tag: tagList) {

					//if tag available use it, else new one
					List<Tag> existTagList = tagRepository.findTagByTagnameAndUserId(tag.getTagname(), user.getUserId());
					//Optional<Tag> existTagList = tagRepository.findById(new TagIdentity(user.getUserId(), tag.getTagname()));

					if( existTagList == null || existTagList.isEmpty()) {
						//create new tag
						tag.setUseri(user.getUserId());
						//tagName pre-exists

						tag.setTagCreated(LocalDateTime.now());
						tag.setTagUpdated(LocalDateTime.now());

						//tag.setTaskObject(newTask); //?

						tagRepository.save(tag);
					} else {
						//List<Tag> actualLists = existTagList.isPresent() ? Collections.singletonList(existTagList.get()) : Collections.emptyList();
						Tag existTag = existTagList.get(0);

						tag.setUseri(user.getUserId());
						//need not, but good, as tag is being updated by linking to new task

						//existTag.setTagUpdated(String.valueOf(new Date()));     //will get error, because models should have all variables have value while save method is called in 117
						tag.setTagUpdated(LocalDateTime.now());
						tag.setTagCreated(existTag.getTagCreated());

						//tag.setTaskObject(newTask);

						//tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), user.getUserId());		//update tagUpdated in multiple taskid rows
						//tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), existTag.getTaskObject().getTaskId());		//update tagUpdated in multiple taskid rows
						tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), tag.getUseri());	
						tagRepository.save(tag);
					}               
				}
			}

			if( newTask.getCommentList() != null  && !newTask.getCommentList().isEmpty()) {

				List<Comment> commentList = newTask.getCommentList();
				for (Comment comment : commentList) {

					comment.setCommentCreated(LocalDateTime.now());
					comment.setCommentUpdated(LocalDateTime.now());

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

					remainder.setRemainderCreated(LocalDateTime.now());
					remainder.setRemainderUpdated(LocalDateTime.now());

					remainderRepository.save(remainder);
				}
			}

			//Task taskToPut = new Task(user.getId(), newTask.getListId(), newTask.getSummary(), newTask.getName(), newTask.getDueDate(), String.valueOf(new Date()), String.valueOf(new Date()));

			newTask = taskRepository.save(newTask);


		} catch (Exception e){
			logger.error("createTask - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return generateResponse(newTask, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<JSONObject> getAllTasksUnderAList(String listId) {
		logger.info("Service - getAllTasksUnderAList");

		try {

			User user = userServiceImpl.getCurrentUser();
			List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());

			if( listOfTasks == null || listOfTasks.isEmpty()) {
				logger.error("getAllTasksUnderAList - You have no tasks, start creating");

				return generateResponse("{\"error\":\"You have no tasks, start creating\"}", HttpStatus.OK);
			}

			// List<Task> actualListOfTasks = listOfTasks.isPresent() ? Collections.singletonList(listOfTasks.get()) : Collections.emptyList();
			List<Task> finalListOfTasks = new ArrayList<Task>();
			for (Task t : listOfTasks) {
				if (t.getListId().equals(listId)) {
					finalListOfTasks.add(t);
				}
			}
			if (finalListOfTasks.isEmpty()) {
				logger.error("getAllTasksUnderAList - You dont have such a list");

				return generateResponse("{\"error\":\"You dont have such a list\"}", HttpStatus.NOT_FOUND);
			}

			return generateResponse(finalListOfTasks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAllTasksUnderAList - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	//self
	@Override
	public ResponseEntity<JSONObject> getAllTasks() {
		logger.info("Service - get all task");
		try {

			User user = userServiceImpl.getCurrentUser();
			List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());

			if( listOfTasks == null || listOfTasks.isEmpty()) {
				logger.error("getAllTasks - You have no tasks, start creating");

				return generateResponse("{\"success\":\"You have no tasks, start creating\"}", HttpStatus.OK);
			}

			return generateResponse(listOfTasks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAllTasks - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<JSONObject> getATask(String listId, String taskId) {
		logger.info("Service - get a task");
		try {

			User user = userServiceImpl.getCurrentUser();
			Optional<Task> task = taskRepository.findById(new TaskIdentity(user.getUserId(), listId, taskId));

			if( task == null || task.isEmpty()) {
				logger.error("getATask - You have no tasks or You dont have such a list/task");

				return generateResponse("{\"error\":\"You have no tasks or You dont have such a list/task\"}", HttpStatus.NOT_FOUND);
			}

			List<Task> actualLists = task.isPresent() ? Collections.singletonList(task.get()) : Collections.emptyList();

			System.out.println(actualLists.toString());
			return generateResponse(actualLists, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getATask - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Task getATask(String taskId) {
		logger.info("Service - get a task");

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
		logger.info("Service - update a Task");
		try {

			User user = userServiceImpl.getCurrentUser();
			Task existingTaskToUpdate = taskRepository.findTaskByTaskIdAndUserId(newTask.getTaskId(), user.getUserId());


			if (existingTaskToUpdate == null) {
				logger.error("updateTask - You dont have such a Task");

				return generateResponse("{\"error\":\"You dont have such a Task\"}", HttpStatus.NOT_FOUND);
			}

			if (newTask.getAccountCreated() != null) {
				logger.error("updateTask - Can't update Creation Date");

				return generateResponse("{\"error\":\"Can't update Creation Date\"}", HttpStatus.BAD_REQUEST);
			}
			if (newTask.getAccountUpdated() != null) {
				logger.error("updateTask - Can't update Updated Date");

				return generateResponse("{\"error\":\"Can't update Updated Date\"}", HttpStatus.BAD_REQUEST);
			}

			//all check before saving
			if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {
				if (newTask.getTagList().size() > 10) {
					logger.error("updateTask - A task can have only upto 10 tags");

					return generateResponse("{\"error\":\"A task can have only upto 10 tags\"}", HttpStatus.BAD_REQUEST);
				}

				List<Tag> tagList = newTask.getTagList();
				for (Tag tag: tagList) {
					if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
						logger.error("updateTask - Better have empty tagList rather than having a tag with no name");

						return generateResponse("{\"error\":\"Better have empty tagList rather than having a tag with no name\"}", HttpStatus.BAD_REQUEST);
					}
				}
			}            
			if( newTask.getRemainderList() != null  && !newTask.getRemainderList().isEmpty()) {
				if (newTask.getRemainderList().size() > 5) {
					logger.error("updateTask - A task can have only upto 5 remainders");

					return generateResponse("{\"error\":\"A task can have only upto 5 remainders\"}", HttpStatus.BAD_REQUEST);
				}
			}

			if (newTask.getListId() != null && existingTaskToUpdate.getListId() != newTask.getListId()) {
				logger.error("updateTask - Changing to another list is a different api - try that");

				return generateResponse("{\"error\":\"Changing to another list is a different api - try that.\"}", HttpStatus.BAD_REQUEST);
			}

			/* Changing 1 Primary key - have to delete entire row and reproduce with new that pk. - so moving to another separate api.
            if (newTask.getListId() != null && existingTaskToUpdate.getListId() != newTask.getListId()) {
            	// moving to other list

            	//is the newlist of same user
            	result = listsServiceImpl.getAList(newTask.getListId());
                if (result.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    return result;
                }
                //nothing to remove from Lists table, as we maintain only in Tasks table.
            	//taskRepository.updateListId(newTask.getListId(), existingTaskToUpdate.getTaskId(), existingTaskToUpdate.getUserId());

//            	if (updateResult != 1) {
//            		return generateResponse("{\"error\":\"Error during moving to another listid\"}", HttpStatus.BAD_REQUEST);
//            	}

            	existingTaskToUpdate.setListId(newTask.getListId());
            	//existingTaskToUpdate = taskRepository.findTaskByTaskIdAndUserId(newTask.getTaskId(), user.getUserId());

            	taskRepository.save(existingTaskToUpdate);

            	existingTaskToUpdate = taskRepository.findTaskByTaskIdAndUserId(newTask.getTaskId(), user.getUserId());
            }*/


			//have to change if datatype of duedate is not string ?
			// compare due is future from now ?
			if (newTask.getDueDate() != null && !newTask.getDueDate().equals(existingTaskToUpdate.getDueDate())) {            	

				existingTaskToUpdate.setDueDate(newTask.getDueDate());
				if (existingTaskToUpdate.getStatus() == Task.Status.OVERDUE &&
						existingTaskToUpdate.getDueDate().isAfter(LocalDateTime.now())) {
					existingTaskToUpdate.setStatus(Task.Status.TODO);
				}
				if (existingTaskToUpdate.getDueDate().isBefore(LocalDateTime.now()) && existingTaskToUpdate.getStatus()!=Task.Status.COMPLETE) {
					existingTaskToUpdate.setStatus(Task.Status.OVERDUE);
				}

				
			}

			if (newTask.getSummary() != null) {
				existingTaskToUpdate.setSummary(newTask.getSummary());
			}
			if (newTask.getName() != null) {
				existingTaskToUpdate.setName(newTask.getName());
			}
			if (newTask.getStatus() != null) {
				if (newTask.getStatus() != Task.Status.OVERDUE) {
					if (existingTaskToUpdate.getDueDate().isBefore(LocalDateTime.now())) {
						logger.error("updateTask - Cannot update status other than overdue when having due date passed");

						return generateResponse("{\"error\":\"Cannot update status other than overdue when having due date passed\"}", HttpStatus.BAD_REQUEST);
					}else {
						existingTaskToUpdate.setStatus(newTask.getStatus());
					}
				} else {
					if (existingTaskToUpdate.getDueDate().isAfter(LocalDateTime.now())) {
						logger.error("updateTask - Cannot update status to overdue when having due date in future");

						return generateResponse("{\"error\":\"Cannot update status to overdue when having due date in future\"}", HttpStatus.BAD_REQUEST);
					}else {
						existingTaskToUpdate.setStatus(newTask.getStatus());
					}
				}
				
			}
			if (newTask.getPriority() != null) {
				existingTaskToUpdate.setPriority(newTask.getPriority());
			}

			/*
            if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {

            	List<Tag> tagList = newTask.getTagList();
                for (Tag tag: tagList) {

                	if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
                		return generateResponse("{\"error\":\"Better have empty tagList rather than having a tag with no name\"}", HttpStatus.BAD_REQUEST);
                	}
                    //if tag available use it, else new one
                	List<Tag> existTagList = tagRepository.findTagByTagnameAndUserId(tag.getTagname(), user.getUserId());	//? this userid or from object id  ?
                	//Optional<Tag> existTagList = tagRepository.findById(new TagIdentity(user.getUserId(), tag.getTagname()));

                	if( existTagList == null || existTagList.isEmpty()) {
                        //create new tag
                		tag.setUseri(user.getUserId());
                		//tagName pre-exists

                        tag.setTagCreated(String.valueOf(new Date()));
                        tag.setTagUpdated(String.valueOf(new Date()));

                        tag.setTaskObject(existingTaskToUpdate); //?

                        tagRepository.save(tag);
                    } else {
                    	//List<Tag> actualLists = existTagList.isPresent() ? Collections.singletonList(existTagList.get()) : Collections.emptyList();
                    	Tag existTagForTagTime = existTagList.get(0);	//only 1 will be there for user-tagName combination - no many will be there

                    	List<Tag> existTagOfTaskList = tagRepository.findTagByTagnameAndTaskId(tag.getTagname(), existingTaskToUpdate.getTaskId());
                    	Tag exactTag = null;
                    	if(!existTagOfTaskList.isEmpty()) {
                    		exactTag = existTagOfTaskList.get(0);
                    	}

                    	tag.setUseri(user.getUserId());

                    	//need not, but good, as tag is being updated by linking to new task

                        //existTag.setTagUpdated(String.valueOf(new Date()));     //will get error, because models should have all variables have value while save method is called in 117
                        tag.setTagUpdated(String.valueOf(new Date()));
                        tag.setTagCreated(existTagForTagTime.getTagCreated());	// already user had a similar tag

                        tag.setTaskObject(existingTaskToUpdate);

                        //tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), user.getUserId());		//update tagUpdated in multiple taskid rows
                        if (exactTag != null) {
                        	tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), tag.getUseri());		//update tagUpdated in multiple taskid rows
                        } else {
                        	tagRepository.updateTagUpdated(tag.getTagUpdated(), tag.getTagname(), tag.getUseri());	
                        	tagRepository.save(tag);
                        }
                    }
                }
                existingTaskToUpdate.setTagList(tagList);
            } */

			if( newTask.getTagList() != null  && !newTask.getTagList().isEmpty()) {

				Map<String, LocalDateTime> oldMapping = new HashMap<String,LocalDateTime>();

				Iterator<Tag> tagIterator = existingTaskToUpdate.getTagList().iterator();
				while(tagIterator.hasNext()){
					Tag tag = tagIterator.next();

					oldMapping.put(tag.getTagname(), tag.getTagCreated());

					//remainderRepository.deleteByRemainderId(remainder.getRemainderId());
					tagRepository.delete(tag);
				}

				// mapping of old value vs new values ?

				List<Tag> tagLists = newTask.getTagList();
				for (Tag tag : tagLists) {
					tag.setUseri(user.getUserId());
					//tag.setTaskObject(existingTaskToUpdate);

					boolean toUpdate = false;

					if (oldMapping.containsKey(tag.getTagname())) {
						tag.setTagCreated(oldMapping.get(tag.getTagname()));
						toUpdate = true;
					} else {
						tag.setTagCreated(LocalDateTime.now());
					}

					tag.setTagUpdated(LocalDateTime.now());

					tagRepository.save(tag);

					//after saving, updating for all tasks of that tagname of user
					if (toUpdate) {
						tagRepository.updateAllTOnUpdate(LocalDateTime.now(), tag.getTagname(), user.getUserId());
					}
				}
				existingTaskToUpdate.setTagList(tagLists);
			}

			if( newTask.getRemainderList() != null  && !newTask.getRemainderList().isEmpty()) {
				Map<LocalDateTime, LocalDateTime> oldMapping = new HashMap<LocalDateTime,LocalDateTime>();

				Iterator<Remainder> remainderIterator = existingTaskToUpdate.getRemainderList().iterator();
				while(remainderIterator.hasNext()){
					Remainder remainder = remainderIterator.next();

					oldMapping.put(remainder.getDateTime(), remainder.getRemainderCreated());

					//remainderRepository.deleteByRemainderId(remainder.getRemainderId());
					remainderRepository.delete(remainder);
				}
				List<Remainder> remainderLists = newTask.getRemainderList();
				for (Remainder remainder : remainderLists) {

					if (oldMapping.containsKey(remainder.getDateTime())) {
						remainder.setRemainderCreated(oldMapping.get(remainder.getDateTime()));
					} else {
						remainder.setRemainderCreated(LocalDateTime.now());
					}

					remainder.setRemainderUpdated(LocalDateTime.now());
					remainderRepository.save(remainder);
				}
				existingTaskToUpdate.setRemainderList(remainderLists);
			}


			if( newTask.getCommentList() != null  && !newTask.getCommentList().isEmpty()) {
				Map<String, LocalDateTime> oldMapping = new HashMap<String,LocalDateTime>();

				Iterator<Comment> commentIterator = existingTaskToUpdate.getCommentList().iterator();
				while(commentIterator.hasNext()){
					Comment comment = commentIterator.next();

					oldMapping.put(comment.getComment(), comment.getCommentCreated());

					//commentRepository.deleteById(null);
					commentRepository.delete(comment);
					commentRepository.deleteByCommentId(comment.getCommentId());

				}
				List<Comment> commentLists = newTask.getCommentList();
				for (Comment comment : commentLists) {

					if (oldMapping.containsKey(comment.getComment())) {
						comment.setCommentCreated(oldMapping.get(comment.getComment()));
					} else {
						comment.setCommentCreated(LocalDateTime.now());
					}

					comment.setCommentUpdated(LocalDateTime.now());

					commentRepository.save(comment);
				}
				existingTaskToUpdate.setCommentList(commentLists);
			}

			existingTaskToUpdate.setAccountUpdated(LocalDateTime.now());

			taskRepository.save(existingTaskToUpdate);

		} catch (Exception e){
			logger.error("updateTask - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return generateResponse("{\"success\":\"Update is done!\"}", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<JSONObject>  deleteTask(String taskId) {
		logger.info("Service - delete a Task");
		try {
			User user = userServiceImpl.getCurrentUser();
			Task existingTaskToDelete = taskRepository.findTaskByTaskIdAndUserId(taskId, user.getUserId());

			if (existingTaskToDelete == null) {
				logger.error("deleteTask - You dont have such a Task");

				return generateResponse("{\"error\":\"You dont have such a Task\"}", HttpStatus.NOT_FOUND);
			}		

			taskRepository.delete(existingTaskToDelete);

		} catch (Exception e) {
			logger.error("deleteTask - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return generateResponse("{\"success\":\"Task is deleted!\"}", HttpStatus.OK);
	}


	//self/{tagname}
	@Override
	public ResponseEntity<JSONObject> getAllTasksByTagName(String tagname) {
		logger.info("Service - getAllTasksByTagName");
		try {

			User user = userServiceImpl.getCurrentUser();
			List<Task> listOfTasks = taskRepository.findByUserId(user.getUserId());

			if( listOfTasks == null || listOfTasks.isEmpty()) {
				logger.error("getAllTasksByTagName - You have no tasks, start creating");

				return generateResponse("{\"success\":\"You have no tasks, start creating\"}", HttpStatus.OK);
			}

			List<Task> listOfTasksByTagName = new ArrayList<Task>();
			for (Task task : listOfTasks) {
				if (task.getTagList() != null || !task.getTagList().isEmpty()) {
					List<Tag> listOfTags = task.getTagList();

					for (Tag tag : listOfTags) {
						if (tagname.equals(tag.getTagname())) {
							listOfTasksByTagName.add(task);
						}
					}
				}
			}

			if (listOfTasksByTagName.isEmpty()) {
				logger.error("getAllTasksByTagName - You have no tasks with that tagname, start creating");

				return generateResponse("{\"success\":\"You have no tasks with that tagname, start creating\"}", HttpStatus.OK);
			}

			return generateResponse(listOfTasksByTagName, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAllTasksByTagName - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<JSONObject> changeTaskToNewList (Task newTask) {
		logger.info("Service - changeTaskToNewList");
		try {

			User user = userServiceImpl.getCurrentUser();
			Task existingTaskToUpdate = taskRepository.findTaskByTaskIdAndUserId(newTask.getTaskId(), user.getUserId());


			if (existingTaskToUpdate == null) {
				logger.error("changeTaskToNewList - You dont have such a Task");

				return generateResponse("{\"error\":\"You dont have such a Task\"}", HttpStatus.NOT_FOUND);
			}

			if (newTask.getListId() == null) {
				logger.error("changeTaskToNewList - provide a new list");

				return generateResponse("{\"error\":\"provide a new list\"}", HttpStatus.BAD_REQUEST);
			}

			if (existingTaskToUpdate.getListId() == newTask.getListId()) {
				logger.error("changeTaskToNewList - Both are same lists- nothing to updatet");

				return generateResponse("{\"success\":\"Both are same lists- nothing to update\"}", HttpStatus.OK);
			}

			// moving to other list

			//is the newlist of same user
			ResponseEntity<JSONObject> result = listsServiceImpl.getAList(newTask.getListId());
			if (result.getStatusCode() != HttpStatus.OK) {
				return result;
			}

			//taskId and listId alone it has
			//for update taskId should be also give. else will be create
			newTask.setTaskId(existingTaskToUpdate.getTaskId());

			newTask.setUserId(user.getUserId());
			newTask.setAccountCreated(existingTaskToUpdate.getAccountCreated());
			newTask.setAccountUpdated(existingTaskToUpdate.getAccountUpdated());
			newTask.setCommentList(existingTaskToUpdate.getCommentList());
			newTask.setDueDate(existingTaskToUpdate.getDueDate());
			newTask.setName(existingTaskToUpdate.getName());
			newTask.setPriority(existingTaskToUpdate.getPriority());
			newTask.setRemainderList(existingTaskToUpdate.getRemainderList());
			newTask.setStatus(existingTaskToUpdate.getStatus());
			newTask.setSummary(existingTaskToUpdate.getSummary());
			newTask.setTagList(existingTaskToUpdate.getTagList());


			taskRepository.delete(existingTaskToUpdate);

			newTask = taskRepository.save(newTask);

			//existingTaskToUpdate = taskRepository.findTaskByTaskIdAndUserId(newTask.getTaskId(), user.getUserId());


			return generateResponse(newTask, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("changeTaskToNewList - Exception");
			e.printStackTrace();
			return generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
