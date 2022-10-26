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
@IdClass(value = TagIdentity.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Tag {
    
    @Id
    private String userId;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id",length = 32)
    private String tagId;
    
    // wont have task id
    private String name;
    
    @Column(name = "tag_created", updatable = false, nullable = false)
    private String tagCreated;
    @Column(name = "tag_updated", nullable = false)
    private String tagUpdated;
    
    public Tag() {
        super();
    }
    public Tag(String userId, String name, String tagCreated, String tagUpdated) {
        super();
        this.userId = userId;
        this.name = name;
        this.tagCreated = tagCreated;
        this.tagUpdated = tagUpdated;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTagId() {
        return tagId;
    }
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTagCreated() {
        return tagCreated;
    }
    public void setTagCreated(String tagCreated) {
        this.tagCreated = tagCreated;
    }
    public String getTagUpdated() {
        return tagUpdated;
    }
    public void setTagUpdated(String tagUpdated) {
        this.tagUpdated = tagUpdated;
    }
       
}
