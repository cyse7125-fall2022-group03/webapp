package cyse7125.fall2022.group03.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Data
@Entity
@IdClass(value = TaskIdentity.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Task {
    
    @Id
    private String userId;
    @Id
    private String listId;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id",length = 32)
    private String taskId;
    
    private String summary;
    private String name;
    private String dueDate;
    @Column(name = "account_created", updatable = false, nullable = false)
    private String accountCreated;
    @Column(name = "account_updated", nullable = false)
    private String accountUpdated;
      
    
    public Task() {
        super();
    }
    public Task(String userId, String listId, String summary, String name, String dueDate, String accountCreated,
            String accountUpdated) {
        super();
        this.userId = userId;
        this.listId = listId;
        this.summary = summary;
        this.name = name;
        this.dueDate = dueDate;
        this.accountCreated = accountCreated;
        this.accountUpdated = accountUpdated;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getListId() {
        return listId;
    }
    public void setListId(String listId) {
        this.listId = listId;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getAccountCreated() {
        return accountCreated;
    }
    public void setAccountCreated(String accountCreated) {
        this.accountCreated = accountCreated;
    }
    public String getAccountUpdated() {
        return accountUpdated;
    }
    public void setAccountUpdated(String accountUpdated) {
        this.accountUpdated = accountUpdated;
    }
    
    
}
