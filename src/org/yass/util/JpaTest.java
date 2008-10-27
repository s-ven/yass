/**
 * 
 */
package org.yass.util;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.User;

/**
 * @author svenduzont
 * 
 */
public class JpaTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("all")
	public static void main(final String[] args) {
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("yass");
		final EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("SELECT l FROM Library l WHERE l.id = 1");
		final List<Library> lst = q.getResultList();
		for (final Library lib : lst) {
			System.out.println(lib);
			final Collection<Track> tracks = lib.getTracks();
			System.out.println(tracks.size());
			for (final Track track : tracks)
				System.out.println(track + " " + track.getPath());
			final Query q1 = em.createNamedQuery("getTrackByPath");
			q1
					.setParameter(1,
							"/Volumes/Arc - 1/Music/Rock-Pop/Sting/1985 - the dream of the blue turtle/10 - moon over bourbon street.mp3");
			final List resultList = q1.getResultList();
			for (final Object object : resultList) {
				System.out.println(object);
				System.out.println(((Track) object).getTrackInfo("artist"));
			}
		}
		q = em.createQuery("Select u FROM User u where u.id = 1");
		final User user = (User) q.getSingleResult();
		System.out.println(user);
	}
}
