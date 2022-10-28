package cyse7125.fall2022.group03.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

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
    @JSONField(serialize = false)
    private String remainderId;
    
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    
    String dateTime;
    
    //@Column(name = "remainder_created", updatable = false, nullable = false)
    @Column(name = "remainder_created")
    private String remainderCreated = String.valueOf(new Date());
    @Column(name = "remainder_updated")
    private String remainderUpdated = String.valueOf(new Date());
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
