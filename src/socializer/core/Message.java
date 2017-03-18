package socializer.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * JPA Entity representing a personal message
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
public class Message implements Serializable {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * message ID
	 */
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * message creation datetime
	 */
	private long date;

	/**
	 * message content
	 */
	private String message;

	/**
	 * message receiver
	 */
	@ManyToOne
	private Person receiver;

	/**
	 * message sender
	 */
	@ManyToOne
	private Person sender;

	/**
	 * message subject
	 */
	private String subject;

	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Message(){}

	/**
	 * Creates a message.
	 * @param message message content.
	 * @param receiver message receiver.
	 * @param sender message sender.
	 * @param subject message subject.
	 * @throws IllegalArgumentException if message content is empty/null or message subject is null
	 */
	public Message(String message, Person receiver, Person sender, String subject) throws IllegalArgumentException {
		if(message == null || message.equals(""))
			throw new IllegalArgumentException("Message cannot be empty.");
		if(subject == null)
			throw new IllegalArgumentException("Subject cannot be null.");
		this.message = message;
		this.receiver = receiver;
		this.sender = sender;
		this.subject = subject;
		this.date = new Date().getTime();
	}

	/**
	 * Retrieves message creation date.
	 * @return message creation date
	 */
	public Date getDate() {
		return new Date(this.date);
	}

	/**
	 * Retrieves message content.
	 * @return message content
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Retrieves message id.
	 * @return message id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Retrieves message receiver.
	 * @return message receiver (null if support team)
	 */
	public Person getReceiver() {
		return this.receiver;
	}

	/**
	 * Retrieves message sender.
	 * @return message sender (null if support team)
	 */
	public Person getSender() {
		return this.sender;
	}

	/**
	 * Retrieves message subject.
	 * @return message subject
	 */
	public String getSubject() {
		return this.subject;
	}
}