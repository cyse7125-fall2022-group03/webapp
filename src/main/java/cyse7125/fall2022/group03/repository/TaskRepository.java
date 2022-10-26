package cyse7125.fall2022.group03.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Task;
import cyse7125.fall2022.group03.model.TaskIdentity;

@Repository
public interface TaskRepository extends JpaRepository<Task, TaskIdentity> {

    List<Task> findByUserId(String userId);
    Task findByTaskId(String taskId);
}
