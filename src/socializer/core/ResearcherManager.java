package socializer.core;

import java.rmi.AccessException;
import java.util.Date;
import java.util.List;

/**
 * Researcher access point for application functionality
 * @author Oleksandr Grygorenko (a1601931)
 */
public class ResearcherManager extends UserManager {

	/**
	 * Performs researcher's login.
	 * @param email researcher's email
	 * @param password researcher's password
	 * @throws AccessException if given credentials are invalid
	 * @throws IllegalStateException if connection to the database has not been initialized
	 */
	public ResearcherManager(String email, String password) throws AccessException, IllegalStateException {
		super(email, password);
		if(!(user instanceof Researcher))
			throw new AccessException("Invalid credentials");
	}
	
	/**
	 * Counts average sent message number
	 * @return average message number
	 */
	public double getAverageMessageNumber(){
		try{
			double res = (double)(long)em.createQuery("Select Count(m) from Message m where m.sender != NULL and m.receiver != null").getSingleResult() / (long)em.createQuery("Select Count(p) from Person p").getSingleResult();
			if(Double.isFinite(res))
				return res;
		} catch(Exception e){e.printStackTrace();}
		return 0.0;
	}
	
	/**
	 * Counts average sent message frequency
	 * @return average sent message frequency
	 */
	@SuppressWarnings("unchecked")
	public double getAverageMessageFrequency(){
		try{
			double res = 0.0;
			List<Person> people = em.createQuery("Select p from Person p").getResultList();
			for(Person p : people){
				res += (double)(long)em.createQuery("Select Count(m) from Message m where m.sender.id="+p.getId()+" and m.receiver != NULL").getSingleResult() * 3600000 / (new Date().getTime() - (long)em.createQuery("Select min(m.date) from Message m where m.sender.id="+p.getId()+" and m.receiver != NULL").getSingleResult());
			}
			if(Double.isFinite(res / people.size()))
				return res / people.size();
		} catch(Exception e){e.printStackTrace();}
		return 0.0;
	}
}
