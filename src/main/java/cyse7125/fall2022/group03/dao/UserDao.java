package cyse7125.fall2022.group03.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cyse7125.fall2022.group03.model.User;

@Repository
@Transactional
public class UserDao {
	
	@Autowired
	private SessionFactory factory;
	
	private Session getSession() {
		Session session = factory.getCurrentSession();
		if (session == null) {
			session = factory.openSession();
		}
		return session;
	}
	
	public void createUser(User user) {
		getSession().save(user);
	}
	
	
	public void validateLogin() {
		
	}
	

}
