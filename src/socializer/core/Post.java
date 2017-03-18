package socializer.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * JPA Entity representing the post
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
@NamedQuery(query="Select Count(r) from Report r where r.post=:post and r.reporter=:reporter", name="Post.isReported")
public class Post implements Serializable {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Post ID
	 */
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * Blocking reason of the post, blocked by admin (null if not blocked)
	 */
	private String blockingReason;

	/**
	 * Post content
	 */
	private String content;

	/**
	 * Post posting date
	 */
	private long date;
	
	/**
	 * Flag indicating if the post has been deleted by the publisher
	 */
	private int deleted;

	/**
	 * Post publisher
	 */
	@ManyToOne
	private Person publisher;

	@OneToMany(mappedBy="post")
	private List<Report> reports;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Post(){}
	
	/**
	 * Sets a flag indicating if the post has been deleted by the publisher.
	 * @param deleted flag indicating if the post has been deleted by the publisher
	 */
	void setDeleted(boolean deleted){
		this.deleted = deleted ? 1 : 0;
	}
	
	/**
	 * Sets post content.
	 * @param content post content
	 * @throws IllegalArgumentException if content is null/empty
	 */
	void setContent(String content) throws IllegalArgumentException {
		if(content == null || content.equals(""))
			throw new IllegalArgumentException("Post content cannot be empty");
		this.content = content;
	}
	
	/**
	 * Sets post blocking reason.
	 * @param blockingReason blocking reason (null if not blocked, blocked otherwise)
	 * @throws IllegalArgumentException if blocking reason is empty
	 */
	void setBlockingReason(String blockingReason) throws IllegalArgumentException {
		if("".equals(blockingReason))
			throw new IllegalArgumentException("Blocking reason cannot be empty");
		this.blockingReason = blockingReason;
	}

	Report[] getReports() {
		return reports.toArray(new Report[reports.size()]);
	}
	
	/**
	 * Creates new post.
	 * @param content post content
	 * @param publisher post publisher
	 * @throws IllegalArgumentException if publisher reference is null
	 */
	public Post(String content, Person publisher) throws IllegalArgumentException {
		if(publisher == null)
			throw new IllegalArgumentException("Post publisher reference cannot be null");
		setContent(content);
		this.publisher = publisher;
		blockingReason = null;
		date = new Date().getTime();
		deleted = 0;
	}

	/**
	 * Retrives post ID.
	 * @return post ID
	 */
	public long getId(){
		return id;
	}

	/**
	 * Retrieves post blocking reason.
	 * @return post blocking reason
	 */
	public String getBlockingReason() {
		return this.blockingReason;
	}
	
	/**
	 * Retrieves post content.
	 * @return post content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Retrieves post posting date.
	 * @return post posting date
	 */
	public Date getDate() {
		return new Date(this.date);
	}

	/**
	 * Retrieves post publisher.
	 * @return post publisher
	 */
	public Person getPublisher() {
		return this.publisher;
	}
	
	/**
	 * Checks if the post is deleted by the publisher.
	 * @return true if deleted, false otherwise
	 */
	public boolean isDeleted(){
		return this.deleted != 0;
	}
}