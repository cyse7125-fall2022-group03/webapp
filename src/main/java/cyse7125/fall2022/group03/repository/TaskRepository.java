package cyse7125.fall2022.group03.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.model.TaskIdentity;

@Repository
public interface TaskRepository extends JpaRepository<Task, TaskIdentity> {

    List<Task> findByUserId(String userId);
    Task findByTaskId(String taskId);
    
    @Query("Select task from Task task where task.taskId=?1 and task.userId=?2" )
    Task findTaskByTaskIdAndUserId(String taskId, String userId);
    
    @Query("Select task from Task task where task.listId=?1 and task.userId=?2" )
    List<Task> findTaskByListIdAndUserId(String listId, String userId);
    
    @Modifying
    @Transactional
    //@Query("update Task task set task.listId = ?1 where task.taskId=?2 and task.userId=?3 and task.listId=?4")
    @Query("update Task task set task.listId = ?1 where task.taskId=?2 and task.userId=?3")
    void updateListId(String newListId, String taskId, String userId);
}
