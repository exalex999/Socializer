package socializer.core;

import java.util.Date;
import java.util.regex.Pattern;

import javax.persistence.*;

/**
 * JPA Entity representing the person
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
@NamedQueries({
	@NamedQuery(query = "Select p from Post p where p.publisher = :publisher and p.date Between :oldest and :newest and p.deleted = 0 ORDER BY p.date DESC", name = "Person.getPosts"),
	@NamedQuery(query = "Select p from Person p where p.customPersonalLink != NULL and p.customPersonalLink = :personalLink or p.customPersonalLink = NULL and (:personalLink like 'id%') and (p.id like SUBSTRING(:personalLink, 3))", name = "Person.getPerson"),
	@NamedQuery(query = "Select f from Friendship f where f.inviter = :person1 and f.invited = :person2 or f.inviter = :person2 and f.invited = :person1", name = "Person.getFriendship"),
	@NamedQuery(query = "Select p from Post p, Friendship f where p.date Between :oldest and :newest and (f.inviter = :person and p.publisher = f.invited or f.invited = :person and f.status = :friendshipstatus and p.publisher = f.inviter) and p.blockingReason = NULL and p.deleted = 0 ORDER BY p.date DESC", name = "Person.getFeed"),
	@NamedQuery(query = "Select m from Message m where (m.sender=:interlocutor1 and m.receiver=:interlocutor2 or m.sender=:interlocutor2 and m.receiver=:interlocutor1 or (:interlocutor2 is null and (m.sender=NULL and m.receiver=:interlocutor1 or m.receiver=NULL and m.sender=:interlocutor1))) and m.date Between :oldest and :newest ORDER BY m.date ASC", name = "Person.getConversation"),
	@NamedQuery(query = "Select n from Notification n where n.person = :person and n.date Between :oldest and :newest ORDER BY n.date DESC", name = "Person.getNotifications"),
	@NamedQuery(query = "Select p from Post p where p.publisher = :publisher and p.blockingReason != NULL and p.date Between :oldest and :newest and p.deleted = 0 ORDER BY p.date DESC", name = "Person.getBlockedPosts"),
	@NamedQuery(query = "Select p from Post p, Report r where r.status=:reportStatus and r.date Between :oldest and :newest and r.post = p and p.deleted = 0 and p.blockingReason = NULL ORDER BY r.date ASC", name = "Person.getPendingReportedPosts")
})
public class Person extends User {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Regular expression for checking custom personal link format
	 */
	private static final Pattern CUSTOM_PERSONAL_LINK_REGEX = Pattern.compile("\\A(?!(id[0-9]+|login|search|friends|conversations|editprofile|feed|support|reports)\\z)[a-z][a-z0-9._-]*\\z");
	
	/**
	 * Person's birthday
	 */
	private long birthday;

	/**
	 * Person's birthplace
	 */
	private String birthplace;

	/**
	 * Person's account blocking reason (null if not blocked)
	 */
	private String blockingReason;

	/**
	 * Person's name
	 */
	private String name;
	
	/**
	 * Person's custom personal link
	 */
	@Column(unique=true)
	private String customPersonalLink;

	/**
	 * Person's new notification number
	 */
	private int newNotificationsNumber;

	/**
	 * Person's residence place
	 */
	private String residencePlace;

	/**
	 * Processing status of the person's requests sent to the support team
	 */
	private int supportStatus;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Person(){}

	/**
	 * Sets person's custom personal link.
	 * @param customPersonalLink new custom personal link
	 * @throws IllegalArgumentException if custom personal link has invalid format
	 */
	void setCustomPersonalLink(String customPersonalLink) throws IllegalArgumentException {
		if(customPersonalLink == null || customPersonalLink.equals("")){
			this.customPersonalLink = null;
			return;
		}
		if(!CUSTOM_PERSONAL_LINK_REGEX.matcher(customPersonalLink.toLowerCase()).matches())
			throw new IllegalArgumentException("Invalid custom personal link format: only latin letters, digits, dots and underscores are allowed; the first symbol must be a letter; it cannot be of standard personal link format ('id&lt;digit_sequence&gt;') or some other link used by the website");
		this.customPersonalLink = customPersonalLink.toLowerCase();
	}
	
	/**
	 * Sets person's account blocking reason.
	 * @param blockingReason person's account blocking reason (null if account is supposed to be unblocked)
	 */
	void setBlockingReason(String blockingReason) throws IllegalArgumentException {
		if("".equals(blockingReason))
			throw new IllegalArgumentException("Blocking reason cannot be empty");
		this.blockingReason = blockingReason;
	}

	/**
	 * Retrieves person's new notification number.
	 * @return person's new notification number
	 */
	int getNewNotificationsNumber() {
		return this.newNotificationsNumber;
	}
	
	/**
	 * Sets person's new notification number.
	 * @param newNotificationsNumber person's new notification number
	 * @throws IllegalArgumentException if newNotificationsNumber &lt; 0
	 */
	void setNewNotificationsNumber(int newNotificationsNumber) throws IllegalArgumentException {
		if(newNotificationsNumber < 0)
			throw new IllegalArgumentException("Number of new notifications cannot be 0.");
		this.newNotificationsNumber = newNotificationsNumber;
	}
	
	/**
	 * Retrieves processing status of the person's requests sent to the support team.
	 * @return processing status of the person's requests sent to the support team
	 */
	SupportStatus getSupportStatus() {
		return SupportStatus.values()[this.supportStatus];
	}

	/**
	 * Sets rocessing status of the person's requests sent to the support team.
	 * @param supportStatus processing status of the person's requests sent to the support team
	 * @throws IllegalArgumentException is supportStatus is null
	 */
	void setSupportStatus(SupportStatus supportStatus) throws IllegalArgumentException {
		if(supportStatus == null)
			throw new IllegalArgumentException("Support status cannot be null");
		this.supportStatus = supportStatus.ordinal();
	}

	/**
	 * Sets person's birthday.
	 * @param birthday person's birthday
	 * @throws IllegalArgumentException if birthday is null
	 */
	void setBirthday(Date birthday) throws IllegalArgumentException{
		if(birthday == null)
			this.birthday = 0;
		else{
			if(birthday.after(new Date()))
				throw new IllegalArgumentException("Birthday cannot be in the future");
			this.birthday = birthday.getTime();
		}
	}

	/**
	 * Sets person's birthplace.
	 * @param birthplace person's birthplace
	 * @throws IllegalArgumentException if birthplace is null
	 */
	void setBirthplace(String birthplace) throws IllegalArgumentException {
		if(birthplace == null)
			throw new IllegalArgumentException("Birthplace cannot be null");
		this.birthplace = birthplace;
	}

	/**
	 * Sets person's name.
	 * @param name person's name
	 * @throws IllegalArgumentException if name is null/empty
	 */
	void setName(String name) throws IllegalArgumentException {
		if(name == null || name.equals(""))
			throw new IllegalArgumentException("Name cannot be empty");
		this.name = name;
	}

	/**
	 * Sets person's residence place.
	 * @param residencePlace person's residence place
	 * @throws IllegalArgumentException if residencePlace is null
	 */
	void setResidencePlace(String residencePlace) throws IllegalArgumentException {
		if(residencePlace == null)
			throw new IllegalArgumentException("Residence place cannot be null");
		this.residencePlace = residencePlace;
	}

	/**
	 * Retrieves person's account blocking reason.
	 * @return person's account blocking reason (null if account is not blocked)
	 */
	String getBlockingReason() {
		return this.blockingReason;
	}
	
	/**
	 * Creates new Person entity.
	 * @param email person's email
	 * @param password person's password
	 * @param name person's name
	 * @param birthday person's birthday
	 * @param birthplace person's birthplace
	 * @param residencePlace person's residence place
	 * @param customPersonalLink person's custom personal link
	 * @throws IllegalArgumentException if any of the arguments is illegal
	 */
	public Person(String email, String password, String name, Date birthday, String birthplace, String residencePlace, String customPersonalLink) throws IllegalArgumentException {
		super(email, password);
		setBirthday(birthday);
		setBirthplace(birthplace);
		setName(name);
		setResidencePlace(residencePlace);
		setCustomPersonalLink(customPersonalLink);
		this.newNotificationsNumber = 0;
		this.blockingReason = null;
		this.supportStatus = SupportStatus.RESOLVED.ordinal();
	}
	
	/**
	 * Retrieves person's personal link.
	 * @return person's personal link (custom if not null, otherwise of format 'id&lt;user_id&gt;')
	 */
	public String getPersonalLink(){
		if(customPersonalLink == null)
			return "id" + getId();
		return this.customPersonalLink;
	}

	/**
	 * Retrieves person's custom personal link
	 * @return person's custom personal link (null if none)
	 */
	public String getCustomPersonalLink(){
		if(customPersonalLink == null)
			return "";
		return this.customPersonalLink;
	}
	
	/**
	 * Retrieves person's birthday
	 * @return person's birthday
	 */
	public Date getBirthday() {
		if(birthday == 0)
			return null;
		return new Date(this.birthday);
	}

	/**
	 * Retrieves person's birthplace
	 * @return person's birthplace
	 */
	public String getBirthplace() {
		return this.birthplace;
	}

	/**
	 * Retrieves person's name
	 * @return person's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves person's residence place
	 * @return person's residence place
	 */
	public String getResidencePlace() {
		return this.residencePlace;
	}
	
	public boolean isBlocked(){
		return blockingReason != null;
	}

	/**
	 * Processing status of the person's requests sent to the support team
	 * @author Oleksandr Grygorenko (a1601931)
	 */
	enum SupportStatus{
		PENDING, RESOLVED
	}
}