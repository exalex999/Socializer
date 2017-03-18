package socializer.core;

import javax.persistence.*;

/**
 * JPA Entity representing the researcher
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
class Researcher extends User {
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Researcher(){}
	
	/**
	 * Creates new researcher.
	 * @param email researcher's email
	 * @param password researcher's password
	 * @throws IllegalArgumentException if email or password are of invalid format
	 */
	Researcher(String email, String password) throws IllegalArgumentException{
		super(email, password);
	}
}