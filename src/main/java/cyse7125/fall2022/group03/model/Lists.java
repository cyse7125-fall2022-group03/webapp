package cyse7125.fall2022.group03.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@IdClass(value = ListsIdentity.class)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Lists {
    
    @Id
    private String userId;
    
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "listId",length = 32)
    private String listId;
    
    private String name;
    
    @Column(name = "account_created", updatable = false, nullable = false)
    private LocalDateTime accountCreated;
    @Column(name = "account_updated", nullable = false)
    private LocalDateTime accountUpdated;
    
    public Lists() {
        
    }

    public Lists(String userId, String name, LocalDateTime accountCreated, LocalDateTime accountUpdated) {
        super();
        this.userId = userId;
        this.name = name;
        this.accountCreated = accountCreated;
        this.accountUpdated = accountUpdated;
    }
    

    public Lists(String userId, LocalDateTime accountCreated, LocalDateTime accountUpdated) {
        super();
        this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(LocalDateTime accountCreated) {
        this.accountCreated = accountCreated;
    }

    public LocalDateTime getAccountUpdated() {
        return accountUpdated;
    }

    public void setAccountUpdated(LocalDateTime accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    
}
