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
//@IdClass(value = CommentIdentity.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Comment {
    
    //@Id
    //private String taskId;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id",length = 32)
    @JSONField(serialize = false)
    private String commentId;
    
    private String comment;
    
    //@Column(name = "comment_created", updatable = false, nullable = false)
    @Column(name = "comment_created")
    private String commentCreated = String.valueOf(new Date());
    //@Column(name = "comment_updated", nullable = false)
    @Column(name = "comment_updated")
    private String commentUpdated = String.valueOf(new Date());
    
    public Comment() {
        super();
    }
    //public Comment(String taskId, String comment, String commentCreated, String commentUpdated) {
    public Comment(String comment, String commentCreated, String commentUpdated) {
        super();
        //this.taskId = taskId;
        this.comment = comment;
        this.commentCreated = commentCreated;
        this.commentUpdated = commentUpdated;
    }
//    public String getTaskId() {
//        return taskId;
//    }
//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }
    public String getCommentId() {
        return commentId;
    }
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getCommentCreated() {
        return commentCreated;
    }
    public void setCommentCreated(String commentCreated) {
        this.commentCreated = commentCreated;
    }
    public String getCommentUpdated() {
        return commentUpdated;
    }
    public void setCommentUpdated(String commentUpdated) {
        this.commentUpdated = commentUpdated;
    }
    
    

}
