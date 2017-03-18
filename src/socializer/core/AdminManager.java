package socializer.core;

import java.rmi.AccessException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.Query;

/**
 * Admin access point for application functionality
 * @author Oleksandr Grygorenko (a1601931)
 */
public class AdminManager extends UserManager {
	
	/**
	 * Performs admin's login.
	 * @param email admin's email
	 * @param password admin's password
	 * @throws AccessException if given credentials are invalid
	 * @throws IllegalStateException if connection to the database has not been initialized
	 */
	public AdminManager(String email, String password) throws AccessException, IllegalStateException {
		super(email, password);
		if(!(user instanceof Admin))
			throw new AccessException("Invalid credentials");
	}

	/**
	 * Retrieves posts, reported with not yet processed reports, issued between "oldest" and "newest" time points
	 * @param oldest oldest report taken into consideration
	 * @param newest newest report taken into consideration
	 * @return posts, reported with not yet processed reports
	 */
	@SuppressWarnings("unchecked")
	public Post[] getPendingReportedPosts(Date oldest, Date newest){
		Query query = em.createNamedQuery("Person.getPendingReportedPosts");
	    query.setParameter("oldest", oldest == null ? 0 : oldest);
	    query.setParameter("newest", newest == null ? Long.MAX_VALUE : newest);
	    query.setParameter("reportStatus", Report.ReportStatus.PENDING.ordinal());
	    LinkedHashSet<Post> res = new LinkedHashSet<Post>(query.getResultList());
		return res.toArray(new Post[res.size()]);
	}
	
	/**
	 * Retrieves all the blocked posts of the specified person, issued between "oldest" and "newest" time points
	 * @param publisher post author
	 * @param oldest oldest report taken into consideration
	 * @param newest newest report taken into consideration
	 * @return all the blocked posts of the specified person, issued between "oldest" and "newest" time points
	 */
	@SuppressWarnings("unchecked")
	public Post[] getBlockedPosts(Person publisher, Date oldest, Date newest){
		Query query = em.createNamedQuery("Person.getBlockedPosts");
	    query.setParameter("publisher", publisher);
	    query.setParameter("oldest", oldest == null ? 0 : oldest);
	    query.setParameter("newest", newest == null ? Long.MAX_VALUE : newest);
		List<Post> res = query.getResultList();
		return res.toArray(new Post[res.size()]);
	}
	
	/**
	 * Retrieves all the reports of the specified post
	 * @param post post, whose reports are searched for
	 * @return array of all the reports of the specified post
	 */
	public Report[] getReports(Post post){
		System.err.println(post == null);
		return post.getReports();
	}
	
	/**
	 * Retrieves a blocking reason of the person account (null if not blocked)
	 * @param p person
	 * @return blocking reason of the person account (null if not blocked)
	 */
	public String getBlockingReason(Person p){
		return p.getBlockingReason();
	}

	/**
	 * Locks person's account (attaching also a reason for that). Does nothing if already
	 * @param account person's account
	 * @param blockingReason blocking reason to attach
	 * @throws IllegalArgumentException on null/empty arguments
	 */
	public void lockAccount(Person account, String blockingReason) throws IllegalArgumentException {
		if(account == null || blockingReason == null || blockingReason.equals(""))
			throw new IllegalArgumentException("Person's account cannot be null, account blocking reason cannot be null or empty");
		try{
    		em.getTransaction().begin();
        	account.setBlockingReason(blockingReason);
        	em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    		e.printStackTrace();
    	}
	}

	/**
	 * Unlocks the specified account. Does nothing if already
	 * @param account person, whose account is to be unlocked
	 */
	public void unlockAccount(Person account) {
		try{
    		em.getTransaction().begin();
        	account.setBlockingReason(null);
        	em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    		e.printStackTrace();
    	}
	}

	/**
	 * Locks the specified post. Does nothing if already
	 * @param post post to block
	 * @param blockingReason blocking reason
	 * @throws IllegalArgumentException on empty/null values of the arguments
	 */
	public void lockPost(Post post, String blockingReason) throws IllegalArgumentException {
		if(post == null || blockingReason == null || blockingReason.equals(""))
			throw new IllegalArgumentException("Post cannot be null, account blocking reason cannot be null or empty");
		try{
    		em.getTransaction().begin();
    		post.setBlockingReason(blockingReason);
        	em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    		e.printStackTrace();
    	}
	}

	/**
	 * Unlocks the specified post. Does nothing if already
	 * @param post post to unlock
	 */
	public void unlockPost(Post post) {
		try{
    		em.getTransaction().begin();
    		post.setBlockingReason(null);
        	em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    		e.printStackTrace();
    	}
	}

	/**
	 * marks all the reports, attached to the post specified, as processed
	 * @param post post, whose reports are to be ignored
	 */
	public void ignorePost(Post post) {
		try{
    		em.getTransaction().begin();
    		for(Report r : post.getReports())
    			r.setStatus(Report.ReportStatus.PROCESSED);
        	em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    		e.printStackTrace();
    	}
	}
}
