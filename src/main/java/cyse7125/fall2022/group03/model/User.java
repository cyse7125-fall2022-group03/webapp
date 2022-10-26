package cyse7125.fall2022.group03.model;

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
	@Column(length = 32)
	private String id;
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
    private String accountCreated;
    @Column(name = "account_updated", nullable = false)
    private String accountUpdated;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
