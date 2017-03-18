package socializer.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * JPA Entity representing a post report
 * @author Oleksandr Grygorenko (a1601931)
 */
@Entity
@Table
public class Report implements Serializable {
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Report ID
	 */
	@Id
	@GeneratedValue
	private long id;

	/**
	 * Reporter's comment
	 */
	private String comment;

	/**
	 * Datetime of placing the report
	 */
	private long date;

	/**
	 * Reported post
	 */
	@ManyToOne
	private Post post;

	/**
	 * Reporter
	 */
	@ManyToOne
	private Person reporter;

	/**
	 * Report processing status
	 */
	private int status;
	
	/**
	 * Default constructor (needed by JPA)
	 */
	@SuppressWarnings("unused")
	private Report(){}

	/**
	 * Retrieves report processing status
	 * @return report processing status
	 */
	ReportStatus getStatus() {
		return ReportStatus.values()[this.status];
	}

	/**
	 * Sets report processing status
	 * @param status new report processing status
	 * @throws IllegalArgumentException if status to be set is null
	 */
	void setStatus(ReportStatus status) throws IllegalArgumentException {
		if(status == null)
			throw new IllegalArgumentException("Report status cannot be null");
		this.status = status.ordinal();
	}
	
	/**
	 * Creates new post report.
	 * @param comment reporter's comment
	 * @param post post to be reported
	 * @param reporter reporter
	 * @throws IllegalArgumentException if comment, post or reporter are null/empty or reporter is reported post publisher
	 */
	public Report(String comment, Post post, Person reporter) throws IllegalArgumentException {
		if(comment == null || comment.equals(""))
			throw new IllegalArgumentException("Comment cannot be empty");
		if(post == null)
			throw new IllegalArgumentException("Post reference cannot be null");
		if(reporter == null)
			throw new IllegalArgumentException("Reporter reference cannot be null");
		if(post.getPublisher().getId() == reporter.getId())
			throw new IllegalArgumentException("Post publisher cannot report his own post");
		this.comment = comment;
		date = new Date().getTime();
		this.post = post;
		this.reporter = reporter;
		this.status = ReportStatus.PENDING.ordinal();
	}

	/**
	 * Retrieves report ID.
	 * @return report ID
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Retrieves reporter's comment.
	 * @return reporter's comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * Retrieves the datetime of placing report
	 * @return datetime of placing the report
	 */
	public Date getDate() {
		return new Date(this.date);
	}

	/**
	 * Retrives the reported post
	 * @return reported post
	 */
	public Post getPost() {
		return this.post;
	}

	/**
	 * Retrieves reporter.
	 * @return reporter
	 */
	public Person getReporter() {
		return this.reporter;
	}

	/**
	 * Report processing status
	 * @author Oleksandr Grygorenko (a1601931)
	 */
	enum ReportStatus{
		PENDING, PROCESSED
	}
}