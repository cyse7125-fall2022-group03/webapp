package cyse7125.fall2022.group03.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class User {
	
	@Id
	@GeneratedValue
	private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
	
}
