package org.yass.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jaudiotagger.tag.id3.valuepair.GenreTypes;
import org.yass.lucene.Constants;

public class TrackProperty implements Constants {

	public static int UID_COUNTER = 0;
	public String value;
	public int id;
	public String type;
	private final static Map<String, TrackProperty> instances = new LinkedHashMap<String, TrackProperty>();

	public TrackProperty(final int id, final String value, final String type) {
		this.type = type;
		this.value = value;
		this.id = UID_COUNTER++;
		if (ARTIST.equals(type) && "".equals(value))
			this.value = UNKNOWN_ARTIST;
		if (ALBUM.equals(type) && "".equals(value))
			this.value = UNKNOWN_ALBUM;
		if (GENRE.equals(type) && value.startsWith("("))
			this.value = GenreTypes.getInstanceOf().getValueForId(Integer.parseInt(value.substring(1, value.indexOf(')'))))
					.trim();
		if (GENRE.equals(type) && "".equals(value))
			this.value = UNKNOWN_GENRE;
	}

	public static TrackProperty get(final String value, final String type) {
		TrackProperty prop = instances.get(type + "_" + value);
		if (prop == null)
			instances.put(type + "_" + value, prop = new TrackProperty(0, value, type));
		return prop;
	}
}
