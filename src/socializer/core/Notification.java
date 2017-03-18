package socializer.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * JPA Entity representing person's notification
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
public class Notification implements Serializable {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Notification ID
	 */
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * Notification creation datetime
	 */
	private long date;

	/**
	 * Notification content
	 */
	private String notification;
	
	/**
	 * Notification hyperlink
	 */
	private String reference;

	/**
	 * Notification owner
	 */
	@ManyToOne
	private Person person;

	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Notification(){}
	
	/**
	 * Creates new notification.
	 * @param notification notification content
	 * @param reference notification hyperlink
	 * @param person notification owner
	 * @throws IllegalArgumentException if notification content/owner is empty/null
	 */
	public Notification(String notification, String reference, Person person) throws IllegalArgumentException {
		if(notification == null || notification.equals(""))
			throw new IllegalArgumentException("Notification cannot be empty.");
		if(person == null)
			throw new IllegalArgumentException("Person reference cannot be null.");
		this.notification = notification;
		this.person = person;
		this.reference = reference;
		this.date = new Date().getTime();
	}

	/**
	 * Retrieves notification ID.
	 * @return notification ID
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Retrieves notification creation datetime.
	 * @return notification creation datetime
	 */
	public Date getDate() {
		return new Date(this.date);
	}

	/**
	 * Retrieves notification content.
	 * @return notification content
	 */
	public String getNotification(){
		return this.notification;
	}

	/**
	 * Retrieves notification hyperlink.
	 * @return notification hyperlink
	 */
	public String getReference(){
		return this.reference;
	}
	
	/**
	 * Retrieves notification owner.
	 * @return notification owner
	 */
	public Person getPerson(){
		return this.person;
	}
}