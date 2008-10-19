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
		final Query q = em.createQuery("SELECT l FROM Library l WHERE l.id = 1");
		final List<Library> lst = q.getResultList();
		for (final Library lib : lst) {
			System.out.println(lib);
			final Collection<Track> tracks = lib.getTracks();
			System.out.println(tracks.size());
			for (final Track track : tracks)
				System.out.println(track);
		}
	}
}
