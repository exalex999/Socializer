package socializer.core;

import javax.persistence.*;

/**
 * JPA Entity representing the admin
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
class Admin extends User {
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Admin(){}
	
	/**
	 * Creates new admin.
	 * @param email admin's email
	 * @param password admin's password
	 * @throws IllegalArgumentException if email or password are of invalid format
	 */
	Admin(String email, String password) throws IllegalArgumentException{
		super(email, password);
	}
}