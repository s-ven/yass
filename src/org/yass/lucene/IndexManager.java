/**
 * 
 */
package org.yass.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.FieldSelectorResult;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.FieldCache.StringIndex;
import org.apache.lucene.store.LockObtainFailedException;
import org.yass.domain.PlayList;
import org.yass.domain.Track;
import org.yass.util.FileUtils;

/**
 * @author svenduzont
 * 
 */
public final class IndexManager implements org.yass.YassConstants {

	private final String mediaFilesRoot;
	private final String indexRoot;
	private final static Log LOG = LogFactory.getLog(IndexManager.class);
	private int currentMediaFileIndex = 0;
	private int mediaFilesCount;
	private String mediaFilesExtensions = "mp3";
	private long lastUpdateTime;
	private final long updateInterval = 3600000 * 24;
	private IndexSearcher indexSearcher;

	/**
	 * 
	 * @param root
	 * @param indexRoot
	 */
	public IndexManager(final String root, final String indexRoot) {
		mediaFilesRoot = root;
		this.indexRoot = indexRoot;
		final Timer updateTimer = new Timer(true);
		updateTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				updateIndex();
			}
		}, updateInterval - lastUpdateTime, updateInterval);
	}

	private void updateIndex() {
	}

	/**
	 * 
	 */
	public void createIndex() {
		try {
			if (!IndexReader.indexExists(indexRoot)) {
				final IndexWriter iw = new IndexWriter(indexRoot, new StandardAnalyzer());
				final File rootFile = new File(mediaFilesRoot);
				final Collection<File> mp3Files = FileUtils.getFiles(rootFile, FileUtils
						.getExtensionFilter(mediaFilesExtensions));
				mediaFilesCount = mp3Files.size();
				for (final File file : mp3Files) {
					final Document doc = new Document();
					try {
						final Track track = new Track(file);
						// Add the fields for the search
						doc.add(new Field(ARTIST, track.getTrackInfo(ARTIST).getValue(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(ALBUM, track.getTrackInfo(ALBUM).getValue(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(GENRE, track.getTrackInfo(GENRE).getValue(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(TITLE, track.getTitle(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(TRACK, track.getTrackNr() + "", Store.YES, Index.TOKENIZED));
						doc.add(new Field(PATH, track.getPath(), Store.YES, Index.UN_TOKENIZED));
						// TODO : Use a formatter
						doc.add(new Field(LENGTH, "" + track.getLength(), Store.YES, Index.UN_TOKENIZED));
						doc.add(new Field(ARTIST, track.getTrackInfo(ARTIST).getId() + "", Store.YES, Index.UN_TOKENIZED));
						doc.add(new Field(ALBUM, track.getTrackInfo(ALBUM).getId() + "", Store.YES, Index.UN_TOKENIZED));
						doc.add(new Field(GENRE, track.getTrackInfo(GENRE).getId() + "", Store.YES, Index.UN_TOKENIZED));
						LOG.info("Indexing MP3 File : " + ++currentMediaFileIndex + "/" + mediaFilesCount);
						iw.addDocument(doc);
					} catch (final Exception e) {
						LOG.warn("Error while refreshing metadata index", e);
					}
				}
				iw.close();
			}
			indexSearcher = new IndexSearcher(IndexReader.open(indexRoot));
			lastUpdateTime = IndexReader.lastModified(indexRoot);
		} catch (final CorruptIndexException e) {
			LOG.fatal("Error while refreshing metadata index", e);
		} catch (final LockObtainFailedException e) {
			LOG.fatal("Error while refreshing metadata index", e);
		} catch (final IOException e) {
			LOG.fatal("Error while refreshing metadata index", e);
		}
	}

	/**
	 * 
	 * @param query
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	public PlayList search(final SearchQuery searchQuery, final PlayList plst) throws IOException {
		final BitSet bitSet = new BitSet(indexSearcher.getIndexReader().numDocs());
		final Query query = searchQuery.getQuery();
		final QueryWrapperFilter filter = searchQuery.getFilter();
		if (query != null) {
			final HitCollector hitCollector = new HitCollector() {

				@Override
				public void collect(int doc, float score) {
					bitSet.set(doc);
				}
			};
			indexSearcher.search(query, filter, hitCollector);
		} else
			// no query hence will set all the bitset
			bitSet.set(0, indexSearcher.getIndexReader().numDocs());
		// Filter the results
		if (filter != null)
			bitSet.and(filter.bits(indexSearcher.getIndexReader()));
		// Iterate through the bitset and create MediaFiles objects
		int docIndex = -1;
		while ((docIndex = bitSet.nextSetBit(docIndex + 1)) > -1)
			plst.add(new Track(indexSearcher.doc(docIndex, new FieldSelector() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -85682246480645765L;

				public FieldSelectorResult accept(final String fieldName) {
					if (ALBUM_ID.equals(fieldName) || ARTIST_ID.equals(fieldName) || GENRE_ID.equals(fieldName)
							|| ALBUM.equals(fieldName) || ARTIST.equals(fieldName) || GENRE.equals(fieldName)
							|| PATH.equals(fieldName) || TITLE.equals(fieldName) || TRACK.equals(fieldName)
							|| LENGTH.equals(fieldName))
						return FieldSelectorResult.LOAD_FOR_MERGE;
					return FieldSelectorResult.NO_LOAD;
				}
			})));
		return plst;
	}

	/**
	 * 
	 * @return
	 */
	public final String getMediaFilesExtensions() {
		return mediaFilesExtensions;
	}

	public final String getMediaFilesRoot() {
		return mediaFilesRoot;
	}

	/**
	 * 
	 * @return
	 */
	public final String getIndexRoot() {
		return indexRoot;
	}

	/**
	 * 
	 * @return
	 */
	public final int getCurrentMediaFileIndex() {
		return currentMediaFileIndex;
	}

	/**
	 * 
	 * @return
	 */
	public final int getMediaFilesCount() {
		return mediaFilesCount;
	}

	/**
	 * 
	 * @param mediaFilesExtensions
	 */
	public final void setMediaFilesExtensions(final String mediaFilesExtensions) {
		this.mediaFilesExtensions = mediaFilesExtensions;
	}

	public PlayList searchFromUIDS(final List<String> uids) throws IOException {
		final PlayList pl = new PlayList();
		final String[] uidsArr = uids.toArray(new String[] {});
		Arrays.sort(uidsArr, 0, uidsArr.length - 1);
		final Collection<String> col = Arrays.asList(uidsArr);
		final Iterator<String> uidit = col.iterator();
		String uid = uidit.next();
		final StringIndex strIndex = FieldCache.DEFAULT.getStringIndex(indexSearcher.getIndexReader(), UUID);
		int i = 0;
		for (final String look : strIndex.lookup) {
			i += 1;
			if (look != null) {
				if (uid.compareTo(look) > 0)
					continue;
				if (look.equals(uid)) {
					final Filter qf = new QueryFilter(new TermQuery(new Term(UUID, uid)));
					pl.add(new Track(indexSearcher.doc(qf.bits(indexSearcher.getIndexReader()).nextSetBit(0))));
					if (!uidit.hasNext())
						break;
					uid = uidit.next();
				}
			}
		}
		return pl;
	}
}
