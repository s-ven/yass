package org.yass.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yass.domain.PlayList;

public class FilePlayList extends PlayList {

	public FilePlayList(final File file, final IndexManager im) {
		final List<String> uids = new ArrayList<String>();
		try {
			final BufferedReader br = new BufferedReader(new FileReader(file));
			name = br.readLine();
			String line;
			while ((line = br.readLine()) != null)
				uids.add(line);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		if (uids.size() > 0)
			try {
				add(im.searchFromUIDS(uids));
				type = "user";
			} catch (final IOException e) {
				e.printStackTrace();
			}
		id = java.util.UUID.nameUUIDFromBytes(name.getBytes()).toString();
	}

	public FilePlayList(final String name) {
		this.name = name;
	}

	public void save() {
		id = java.util.UUID.nameUUIDFromBytes(name.getBytes()).toString();
		final File file = new File(PLAYLIST_ROOT, id + ".txt");
		try {
			file.createNewFile();
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(name);
			bw.newLine();
			for (final String id : tracks.keySet()) {
				bw.write(id);
				bw.newLine();
			}
			bw.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
