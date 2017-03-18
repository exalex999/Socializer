package socializer.core;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.*;

/**
 * JPA Entity representing the user
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(query = "Select u from User u where u.email = :email and u.password = :password", name = "User.login")
public class User implements Serializable {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Regular expression for checking email format
	 */
	private static final Pattern EMAIL_REGEX = Pattern.compile("\\A[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}\\z");

	/**
	 * User ID
	 */
	@Id
	@GeneratedValue
	private long id;

	/**
	 * User email
	 */
	@Column(unique=true)
	private String email;

	/**
	 * User password
	 */
	private String password;

	/**
	 * Messages sent by the user
	 */
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE, orphanRemoval= true)
	private List<Message> outgoingMessages;

	/**
	 * Messages received by the user
	 */
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE, orphanRemoval= true)
	private List<Message> incomingMessages;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	protected User(){}

	/**
	 * Creates new user.
	 * @param email user's email
	 * @param password user's password
	 * @throws IllegalArgumentException if email or password are of invalid format
	 */
	User(String email, String password) throws IllegalArgumentException {
		setEmail(email);
		setPassword(password);
	}

	/**
	 * Retrieves user's email
	 * @return user's email
	 */
	String getEmail() {
		return this.email;
	}

	/**
	 * Sets user's email
	 * @param email user's email
	 * @throws IllegalArgumentException if email is of an invalid format
	 */
	void setEmail(String email) throws IllegalArgumentException {
		if(email == null || !EMAIL_REGEX.matcher(email.toLowerCase()).matches())
			throw new IllegalArgumentException("Invalid email.");
		this.email = email.toLowerCase();
	}

	/**
	 * Retrieves user's password
	 * @return user's password
	 */
	String getPassword() {
		return this.password;
	}

	/**
	 * Sets user's password
	 * @param password user's password
	 * @throws IllegalArgumentException if password is of an invalid format
	 */
	void setPassword(String password) throws IllegalArgumentException {
		if(password == null || password.length() < 8)
			throw new IllegalArgumentException("Password length must be at least 8.");
		this.password = password;
	}

	/**
	 * Retrieves user ID
	 * @return user ID
	 */
	public long getId() {
		return this.id;
	}

}