package socializer.core;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 * Person's access point for application functionality
 * @author Oleksandr Grygorenko (a1601931)
 */
public class PersonManager extends UserManager {
	
	/**
	 * Registers new person
	 * @param email person's email
	 * @param password person's password
	 * @throws AccessException if the user is already registered
	 * @throws IllegalStateException if connection to the database has not been initialized yet
	 */
	public static void register(String email, String password) throws AccessException, IllegalStateException {
		if(emFactory == null)
			throw new IllegalStateException("UserManager is closed/not initialized");
	    EntityManager em = emFactory.createEntityManager();
	    em.getTransaction().begin();
	    try{
		    em.persist(new Person(email, password, "noname", null, "", "", null));
		    em.getTransaction().commit();
	    } catch(Exception e){
	    	try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
	    	if(e instanceof IllegalArgumentException)
	    		throw e;
	    	else
	    		throw new AccessException("Specified user is already registered");
	    } finally{
	    	try{
	    		em.close();
	    	} catch(Exception e){}
	    }
	}
	
	/**
	 * Stores new notification.
	 * @param n notification
	 */
	@SuppressWarnings("unchecked")
	private void createNotification(Notification n){
		try{
			Query query = em.createNamedQuery("Person.getNotifications");
		    query.setParameter("person", n.getPerson());
		    query.setParameter("oldest", 0);
		    query.setParameter("newest", Long.MAX_VALUE);
			List<Notification> ns = query.getResultList();
			for(int i = 0; i < n.getPerson().getNewNotificationsNumber(); ++i)
				if(ns.get(i).getNotification().equals(n.getNotification()) && ns.get(i).getReference().equals(n.getReference()))
					return;
    		em.getTransaction().begin();
    		em.persist(n);
        	n.getPerson().setNewNotificationsNumber(n.getPerson().getNewNotificationsNumber() + 1);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Performs person's login.
	 * @param email person's email
	 * @param password person's password
	 * @throws AccessException if given credentials are invalid
	 * @throws IllegalStateException if connection to the database has not been initialized yet
	 */
	public PersonManager(String email, String password) throws AccessException, IllegalStateException {
		super(email, password);
		if(!(user instanceof Person))
			throw new AccessException("Invalid credentials");
	}
	
	/**
	 * Retrieves the person, on whose behalf the current instance of PersonManager performs actions
	 * @return person, on whose behalf the current instance of PersonManager performs actions
	 */
	public Person getPerson(){
		return (Person)user;
	}
	
	/**
	 * Retrieves posts of the given person
	 * @param publisher posts' publisher
	 * @param oldest lower datetime bounder of the posts to be delivered (null if since the beginning of time)
	 * @param newest upper datetime bounder of the posts to be delivered (null if till the present moment)
	 * @return posts of the given person
	 */
    @SuppressWarnings("unchecked")
	public Post[] getPosts(Person publisher, Date oldest, Date newest) {
		Query query = em.createNamedQuery("Person.getPosts");
	    query.setParameter("publisher", publisher);
	    query.setParameter("oldest", oldest == null ? 0 : oldest);
	    query.setParameter("newest", newest == null ? Long.MAX_VALUE : newest);
		List<Post> res = query.getResultList();
		return res.toArray(new Post[res.size()]);
	}
    
    /**
     * Stores changes to the person's profile. On error no changes are applied.
     * @param birthday person's birthday
     * @param birthplace person's birthplace
     * @param customPersonalLink person's custom personal link
     * @param name person's name
     * @param residencePlace person's residence place
     */
    public void editProfile(Date birthday, String birthplace, String customPersonalLink, String name, String residencePlace){
    	try{
    		em.getTransaction().begin();
        	Person person = (Person)user;
        	person.setBirthday(birthday);
        	person.setBirthplace(birthplace);
        	person.setCustomPersonalLink(customPersonalLink);
        	person.setName(name);
        	person.setResidencePlace(residencePlace);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    		if(e instanceof RollbackException)
    			throw new IllegalArgumentException("Such custom personal link is already used.");
    		else
    			throw e;
    	}
    }
    
    /**
     * Retrieves friendship entity between the logged in person and the person given
     * @param p person, friendship with whom is to be searcher
     * @return friendship entity between the logged in person and the person given; null if none exists
     */
    @SuppressWarnings("unchecked")
	public Friendship getFriendship(Person p){
    	Query query = em.createNamedQuery("Person.getFriendship");
	    query.setParameter("person1", (Person)user);
	    query.setParameter("person2", p);
		List<Friendship> res = query.getResultList();
		if(res.isEmpty())
			return null;
		return res.get(0);
    }
    
    /**
     * Retrieves person's news feed.
	 * @param oldest lower datetime bounder of the posts to be delivered (null if since the beginning of time)
	 * @param newest upper datetime bounder of the posts to be delivered (null if till the present moment)
     * @return news feed
     */
    @SuppressWarnings("unchecked")
	public Post[] getFeed(Date oldest, Date newest){
    	Query query = em.createNamedQuery("Person.getFeed");
	    query.setParameter("person", (Person)user);
	    query.setParameter("oldest", oldest == null ? 0 : oldest);
	    query.setParameter("newest", newest == null ? Long.MAX_VALUE : newest);
	    query.setParameter("friendshipstatus", Friendship.FriendshipStatus.ACCEPTED.ordinal());
		List<Post> res = query.getResultList();
		return res.toArray(new Post[res.size()]);
    }
    
    /**
     * checks if the given post has been already reported by the person
     * @param p post
     * @return true if reported; false otherwise
     */
	public boolean isPostReported(Post p){
    	Query query = em.createNamedQuery("Post.isReported");
	    query.setParameter("reporter", (Person)user);
	    query.setParameter("post", p);
	    return ((Number)query.getSingleResult()).intValue() != 0;
    }
	
	/**
	 * sends invitation for the friendship to another person; accepts the counter invitation if one exists. On error no changes are applied.
	 * @param friend person to send invitation (or whose invitation to accept)
	 */
	public void addFriend(Person friend){
		try{
    		em.getTransaction().begin();
        	Friendship f = getFriendship(friend);
        	if(f == null){
        		f = new Friendship(friend, (Person)user, Friendship.FriendshipStatus.PENDING);
        		em.persist(f);
        		em.getTransaction().commit();
        		createNotification(new Notification("<b>" + getPerson().getName() + "</b>" + " invites you to be friends", getPerson().getPersonalLink(), friend));
        	}
        	else if(f.getInvited().getId() == user.getId()){
        		f.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        		em.getTransaction().commit();
        	}
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Removes the person from the friend list / cancels friendship invitation. On error no changes are applied.
	 * @param friend person, friendship with whom / friendship invitation to whom is to be cancelled
	 */
	public void removeFriend(Person friend){
		try{
    		em.getTransaction().begin();
        	em.remove(getFriendship(friend));
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Adds new person's post.
	 * @param p post to be added
	 */
	public void addPost(Post p){
		if(p == null)
			throw new IllegalArgumentException("Post cannot be null");
		if(p.getPublisher().getId() != ((Person)user).getId())
			throw new IllegalArgumentException("Illegal post publisher");
		try{
    		em.getTransaction().begin();
        	em.persist(p);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Removes person's post. On error no changes are applied.
	 * @param p post to be removed
	 */
	public void removePost(Post p){
		if(p == null)
			throw new IllegalArgumentException("Post cannot be null");
		if(p.getPublisher().getId() != ((Person)user).getId())
			throw new IllegalArgumentException("Illegal post publisher");
		try{
    		em.getTransaction().begin();
        	p.setDeleted(true);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Reports a post. On error no changes are applied.
	 * @param r report to be placed
	 */
	public void reportPost(Report r){
		if(r == null)
			throw new IllegalArgumentException("Report cannot be null");
		if(r.getReporter().getId() != ((Person)user).getId())
			throw new IllegalArgumentException("Illegal reporter");
		if(isPostReported(r.getPost()))
			throw new IllegalArgumentException("Post is already reported");
		try{
    		em.getTransaction().begin();
        	em.persist(r);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Retrieves person's conversation with a friend/support team.
	 * @param interlocutor person's interlocutor or null if support team
	 * @param oldest lower datetime bounder of the messages to be delivered (null if since the beginning of time)
	 * @param newest upper datetime bounder of the messages to be delivered (null if till the present moment)
	 * @return conversation
	 * @throws IllegalArgumentException if interlocutor is a person who is not a friend
	 */
	@SuppressWarnings("unchecked")
	public Message[] getConversation(Person interlocutor, Date oldest, Date newest) throws IllegalArgumentException{
		Friendship f = getFriendship(interlocutor);
		if(interlocutor != null && (f == null || f.getStatus() == Friendship.FriendshipStatus.PENDING))
			throw new IllegalArgumentException("Interlocutor is not a friend");
    	Query query = em.createNamedQuery("Person.getConversation");
	    query.setParameter("interlocutor1", (Person)user);
	    query.setParameter("interlocutor2", interlocutor);
	    query.setParameter("oldest", oldest == null ? 0 : oldest);
	    query.setParameter("newest", newest == null ? Long.MAX_VALUE : newest);
		List<Message> res = query.getResultList();
		return res.toArray(new Message[res.size()]);
	}
	
	/**
	 * Sends message to a friend/support team
	 * @param m message to be sent
	 * @throws IllegalArgumentException on attempt to send a message on behalf of another person or to a person, who is not a friend
	 */
	public void sendMessage(Message m) throws IllegalArgumentException {
		Friendship f = getFriendship(m.getReceiver());
		if(m.getReceiver() != null && (f == null || f.getStatus() == Friendship.FriendshipStatus.PENDING))
			throw new IllegalArgumentException("Interlocutor is not a friend");
		if(m.getSender().getId() != user.getId())
			throw new IllegalArgumentException("Illegal sender");
		try{
    		em.getTransaction().begin();
        	em.persist(m);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
		if(m.getReceiver()!=null)
			createNotification(new Notification("<b>" + getPerson().getName() + "</b>" + " has sent you a message", "conversations/" + getPerson().getPersonalLink(), m.getReceiver()));
	}
	
	/**
	 * Retrieves person's notification number
	 * @return person's notification number
	 */
	public int getNewNotificationsNumber(){
		return getPerson().getNewNotificationsNumber();
	}
	
	public void resetNewNotificationsNumber(){
		try{
    		em.getTransaction().begin();
    		getPerson().setNewNotificationsNumber(0);
    		em.getTransaction().commit();
    	} catch(Exception e){
    		try{
	    		em.getTransaction().rollback();
	    	} catch (Exception ex){}
    		try{
	    		em.close();
	    	} catch (Exception ex){}
    		em = UserManager.emFactory.createEntityManager();
    	}
	}
	
	/**
	 * Retrieves person's notifications
	 * @param oldest lower datetime bounder of the notifications to be delivered (null if since the beginning of time)
	 * @param newest upper datetime bounder of the notifications to be delivered (null if till the present moment)
	 * @return person's notifications
	 */
	@SuppressWarnings("unchecked")
	public Notification[] getNotifications(Date oldest, Date newest){
		Query query = em.createNamedQuery("Person.getNotifications");
	    query.setParameter("person", (Person)user);
	    query.setParameter("oldest", oldest == null ? 0 : oldest);
	    query.setParameter("newest", newest == null ? Long.MAX_VALUE : newest);
		List<Notification> res = query.getResultList();
		return res.toArray(new Notification[res.size()]);
	}
	
	/**
	 * Searches for a person by his name.
	 * @param query person's name pattern to be compared with during the search; pattern is considered to be compatible with person's name if there is an injective function from the set of words in the pattern to the set of words in person's name, whose arguments are substrings of the corresponding values
	 * @param isFriend flag indicating if the searched people must be friends
	 * @return hashmap of pairs "found person - their name with highlighted recognized pattern compatibilities"
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Person, String> search(String query, boolean isFriend){
		HashMap<Person, String> res = new HashMap<>();
		final String[] patterns = query.split(" ");
		for(Person p : (List<Person>)em.createQuery("Select p from Person p ORDER BY p.name ASC").getResultList())
			if(!isFriend && p.getId() != getPerson().getId() && !p.isBlocked() || getFriendship(p) != null && getFriendship(p).getStatus() == Friendship.FriendshipStatus.ACCEPTED){
				final ArrayList<String> names = new ArrayList<>(Arrays.asList(p.getName().split(" ")));
				if(names.size() >= patterns.length && new Object(){
					public boolean match(int firstPattern){
						if(firstPattern == patterns.length)
							return true;
						for(int i = 0; i < names.size(); ++i){
							String match = names.get(i);
							if(match.toLowerCase().contains(patterns[firstPattern].toLowerCase())){
								names.remove(i);
								if(match(firstPattern+1)){
									int patternStart = match.toLowerCase().indexOf(patterns[firstPattern].toLowerCase());
									names.add(i, match.substring(0,patternStart)+"<font style='color:black; background-color:orange;'>"+match.substring(patternStart, patternStart+patterns[firstPattern].length())+"</font>"+match.substring(patternStart+patterns[firstPattern].length()));
									return true;
								}
								names.add(i, match);
							}
						}
						return false;
					}
				}.match(0))
					res.put(p, String.join(" ", names));
			}
		return res;
	}
	
	/**
	 * Gets account blocking reason
	 * @return account blocking reason (null if not blocked)
	 */
	public String getBlockingReason(){
		return getPerson().getBlockingReason();
	}
}
