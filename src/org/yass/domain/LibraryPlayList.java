package org.yass.domain;

import java.util.Collection;


public class LibraryPlayList extends PlayList {

	private Collection<String> genres;
	private Collection<String> artists;
	private Collection<String> albums;

	/**
	 * @param genres
	 *          the genres to set
	 */
	public final void setGenres(final Collection<String> genres) {
		this.genres = genres;
	}

	/**
	 * @param artists
	 *          the artists to set
	 */
	public final void setArtists(final Collection<String> artists) {
		this.artists = artists;
	}

	/**
	 * @param albums
	 *          the albums to set
	 */
	public final void setAlbums(final Collection<String> albums) {
		this.albums = albums;
	}

	/**
	 * @return the genres
	 */
	public final Collection<String> getGenres() {
		return genres;
	}

	/**
	 * @return the artists
	 */
	public final Collection<String> getArtists() {
		return artists;
	}

	/**
	 * @return the albums
	 */
	public final Collection<String> getAlbums() {
		return albums;
	}
}
