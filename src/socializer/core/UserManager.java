package socializer.core;

import java.rmi.AccessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.persistence.*;

/**
 * User access point for application functionality
 * @author Oleksandr Grygorenko (a1601931)
 */
public abstract class UserManager {

	/**
	 * JPA Entity Manager Factory for generating Entity Managers for the class instances
	 */
	protected static EntityManagerFactory emFactory = null;
	
	private static void testInit(){
		EntityManager em = null;
	    try{
			em = emFactory.createEntityManager();
		    em.getTransaction().begin();
		    em.persist(new Admin("admin@gmail.com", "adminadmin"));
		    em.persist(new Researcher("researcher@gmail.com", "researcherresearcher"));
		    em.getTransaction().commit();
	    } catch(Exception e){
	    	e.printStackTrace();
	    	try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
	    } finally{
	    	try{
	    		em.close();
	    	} catch(Exception e){}
	    }
	}
	
	/**
	 * Initializes connection to the application database. Must be (once) executed prior to instantiating the class.
	 */
	public static void init(String contextPath){
		if(emFactory == null){
			Map<String, Object> configOverrides = new HashMap<String, Object>();
			configOverrides.put("javax.persistence.jdbc.url", "jdbc:sqlite:"+contextPath+"socializerDB.db");
			emFactory = Persistence.createEntityManagerFactory("socializerDB", configOverrides);
		}
		testInit();
	}
	
	/**
	 * Terminates the connection to the application database. Must be executed after usage of all class instances. After that, no instances should be created.
	 */
	public static void destroy(){
		if(emFactory == null){
			emFactory.close();
			emFactory = null;
		}
	}
	
	/**
	 * The user, on whose behalf the current UserManager instance performs actions
	 */
	protected User user;
	
	/**
	 * JPA Entity Manager for accessing application database
	 */
	protected EntityManager em;
	
	/**
	 * Performs user's login.
	 * @param email user's email
	 * @param password user's password
	 * @throws AccessException if given credentials are invalid
	 * @throws IllegalStateException if connection to the database has not been initialized
	 */
	@SuppressWarnings("unchecked")
	protected UserManager(String email, String password) throws AccessException, IllegalStateException {
		try{
			em = emFactory.createEntityManager();
		} catch(NullPointerException e){
			throw new IllegalStateException("UserManager is closed/not initialized");
		}
		Query query = em.createNamedQuery("User.login");
	    query.setParameter("email", email);
	    query.setParameter("password", password);
	    List<User> res = query.getResultList();
	    if(res.isEmpty())
	    	throw new AccessException("Invalid credentials");
	    this.user = res.get(0);
	}
	
	/**
	 * Terminates current UserManager instance session
	 */
	public void close(){
		try{
			em.close();
			em = null;
		} catch(Exception e){}
	}

	/**
	 * Retrieves a person of the given personal link
	 * @param personalLink person's personal link
	 * @return person with the given personal link
	 * @throws NoSuchElementException if no person of such a personal link is registered
	 */
	@SuppressWarnings("unchecked")
	public Person getPerson(String personalLink) throws NoSuchElementException{
		Query query = em.createNamedQuery("Person.getPerson");
	    query.setParameter("personalLink", personalLink);
		List<Person> res = query.getResultList();
		if(res.isEmpty())
			throw new NoSuchElementException("No person with a link '" + personalLink + "'");
		return res.get(0);
	}

	/**
	 * Retrieves a post of the given ID
	 * @param id post ID
	 * @return post of the given ID
	 */
	public Post getPost(long id){
		try{
			return em.find(Post.class, id);
		} catch(Exception e){}
		return null;
	}
}
