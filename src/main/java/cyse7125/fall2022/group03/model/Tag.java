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
//@IdClass(value = TagIdentity.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Tag {
    
    //@Id
    @Column(name = "useri")
    private String useri;
    
    @Id
    @Column(name = "tagId")
    @GeneratedValue(generator = "jpa-uuid")
    private String tagId;
    
    @Column(name = "tagname")
    private String tagname;
    
    @Column(name = "tag_created", updatable = false, nullable = false)
    private String tagCreated;
    @Column(name = "tag_updated", nullable = false)
    private String tagUpdated;
    
    //cant have task id as would not know while creating
    //so better is using foreign key concept, which auto populates
    /*@ManyToOne
    @JoinColumns({
        @JoinColumn(name = "task_id", referencedColumnName = "task_id"),
        @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        @JoinColumn(name = "list_id", referencedColumnName = "list_id")
    })
    private Task taskObject;*/
    
    
    public Tag() {
        super();
    }


    public Tag(String useri, String tagname, String tagCreated, String tagUpdated) {
        super();
        this.useri = useri;
        this.tagname = tagname;
        this.tagCreated = tagCreated;
        this.tagUpdated = tagUpdated;
    }

    public String getUseri() {
        return useri;
    }

    public void setUseri(String useri) {
        this.useri = useri;
    }

    public String getTagname() {
        return tagname;
    }


    public void setTagname(String tagname) {
        this.tagname = tagname;
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


	public String getTagId() {
		return tagId;
	}


	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

   
}
