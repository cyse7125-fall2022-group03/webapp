package cyse7125.fall2022.group03.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
//@IdClass(value = RemainderIdentity.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Remainder {
    
    //@Id
    //private String taskId;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id",length = 32)
    private String remainderId;
    
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    
    String dateTime;
    
    @Column(name = "tag_created", updatable = false, nullable = false)
    private String remainderCreated;
    @Column(name = "tag_updated", nullable = false)
    private String remainderUpdated;
    public Remainder() {
        super();
    }
    //public Remainder(String taskId, LocalDateTime dateTime, String remainderCreated, String remainderUpdated) {
    public Remainder(String dateTime, String remainderCreated, String remainderUpdated) {
        super();
        //this.taskId = taskId;
        this.dateTime = dateTime;
        this.remainderCreated = remainderCreated;
        this.remainderUpdated = remainderUpdated;
    }
//    public String getTaskId() {
//        return taskId;
//    }
//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }
    public String getRemainderId() {
        return remainderId;
    }
    public void setRemainderId(String remainderId) {
        this.remainderId = remainderId;
    }
    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getRemainderCreated() {
        return remainderCreated;
    }
    public void setRemainderCreated(String remainderCreated) {
        this.remainderCreated = remainderCreated;
    }
    public String getRemainderUpdated() {
        return remainderUpdated;
    }
    public void setRemainderUpdated(String remainderUpdated) {
        this.remainderUpdated = remainderUpdated;
    }
    
}
