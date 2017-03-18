package socializer.core;

import java.io.Serializable;
import javax.persistence.*;

/**
 * JPA Entity representing persons' friendship
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
public class Friendship implements Serializable {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Friendship ID.
	 */
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * Person invited into friendship
	 */
	@ManyToOne
	private Person invited;

	/**
	 * Person inviting into friendship
	 */
	@ManyToOne
	private Person inviter;

	/**
	 * Friendship acceptance status
	 */
	private int status;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Friendship(){}

	/**
	 * Sets friendship acceptance status.
	 * @param status friendship acceptance status
	 */
	void setStatus(FriendshipStatus status) {
		this.status = status.ordinal();
	}
	
	/**
	 * Establishes new friendship.
	 * @param invited person invited into friendship
	 * @param inviter person inviting into friendship
	 * @param status friendship acceptance status
	 * @throws IllegalArgumentException if either of inviter, invited or status are null
	 */
	Friendship(Person invited, Person inviter, FriendshipStatus status) throws IllegalArgumentException {
		if(invited == null)
			throw new IllegalArgumentException("Invited person reference cannot be null.");
		if(inviter == null)
			throw new IllegalArgumentException("Inviting person reference cannot be null.");
		if(status == null)
			throw new IllegalArgumentException("Friendship status cannot be null.");
		this.invited = invited;
		this.inviter = inviter;
		setStatus(status);
	}

	/**
	 * Retrieves person invited into friendship.
	 * @return person invited into friendship
	 */
	public Person getInvited() {
		return this.invited;
	}

	/**
	 * Retrieves person inviting into friendship.
	 * @return person inviting into friendship
	 */
	public Person getInviter() {
		return this.inviter;
	}

	/**
	 * Retrieves friendship acceptance status.
	 * @return friendship acceptance status
	 */
	public FriendshipStatus getStatus() {
		return FriendshipStatus.values()[this.status];
	}
	
	/**
	 * Friendship acceptance status
	 * @author Oleksandr Grygorenko (a1601931)
	 */
	public enum FriendshipStatus{
		PENDING, ACCEPTED
	}
}