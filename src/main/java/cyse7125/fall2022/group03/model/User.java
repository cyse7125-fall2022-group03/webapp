package cyse7125.fall2022.group03.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class User {
	
	@Id
	@GeneratedValue(generator = "jpa-uuid")
	@Column(name = "userId", length = 32)
	private String userId;
	@Column(name = "first_name")
    private String firstName;
	@Column(name = "middle_name")
    private String middleName;
	@Column(name = "last_name")
    private String lastName;
	@Column(unique=true) 
    private String email;
    @JSONField(serialize = false)
    private String password;
    

    @Column(name = "account_created", updatable = false, nullable = false)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = Shape.STRING)
    private LocalDateTime accountCreated;
    
    @Column(name = "account_updated", nullable = false)
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", shape = Shape.STRING)
    private LocalDateTime accountUpdated;
    
    public User() {
        super();
    }
    public User(String firstName, String middleName, String lastName, String email, String password,
    		LocalDateTime accountCreated, LocalDateTime accountUpdated) {
        super();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountCreated = accountCreated;
        this.accountUpdated = accountUpdated;
    }
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
