/**
 * 
 */
package org.yass.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.apache.lucene.index.TermEnum;
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
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.yass.domain.LibraryPlayList;
import org.yass.domain.MediaFile;
import org.yass.domain.PlayList;
import org.yass.util.FileUtils;

/**
 * @author svenduzont
 * 
 */
public final class IndexManager implements Constants {

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
						final MediaFile mediaFile = new MediaFile(file);
						// Add the fields for the search
						doc.add(new Field(ARTIST, mediaFile.getArtist(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(ALBUM, mediaFile.getAlbum(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(GENRE, mediaFile.getGenre(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(TITLE, mediaFile.getTitle(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(TRACK, mediaFile.getTrack(), Store.YES, Index.TOKENIZED));
						doc.add(new Field(PATH, mediaFile.getPath(), Store.YES, Index.UN_TOKENIZED));
						// TODO : Use a formatter
						doc.add(new Field(LENGTH, "" + mediaFile.getLength(), Store.YES, Index.UN_TOKENIZED));
						// Add the fields for the facets and faceted search
						doc.add(new Field(FACET_ARTIST, mediaFile.getArtist(), Store.NO, Index.UN_TOKENIZED));
						doc.add(new Field(FACET_ALBUM, mediaFile.getAlbum(), Store.NO, Index.UN_TOKENIZED));
						doc.add(new Field(FACET_GENRE, mediaFile.getGenre(), Store.NO, Index.UN_TOKENIZED));
						doc.add(new Field(UUID, mediaFile.getUuid(), Store.YES, Index.UN_TOKENIZED));
						LOG.info("Indexing MP3 File : " + ++currentMediaFileIndex + "/" + mediaFilesCount);
						iw.addDocument(doc);
					} catch (final InvalidAudioFrameException e) {
						LOG.warn("Error while parsing file " + file, e);
					} catch (final ReadOnlyFileException e) {
						LOG.warn("Error while rereshing metadata index", e);
					} catch (final TagException e) {
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
	public PlayList search(final SearchQuery searchQuery) throws IOException {
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
		final LibraryPlayList searchResults = new LibraryPlayList();
		while ((docIndex = bitSet.nextSetBit(docIndex + 1)) > -1)
			searchResults.add(new MediaFile(indexSearcher.doc(docIndex, new FieldSelector() {

				public FieldSelectorResult accept(final String fieldName) {
					if (ALBUM.equals(fieldName) || ARTIST.equals(fieldName) || GENRE.equals(fieldName) || PATH.equals(fieldName)
							|| TITLE.equals(fieldName) || TRACK.equals(fieldName) || LENGTH.equals(fieldName))
						return FieldSelectorResult.LOAD_FOR_MERGE;
					return FieldSelectorResult.NO_LOAD;
				}
			})));
		searchResults.setGenres(doFacets(FACET_GENRE, bitSet));
		searchResults.setArtists(doFacets(FACET_ARTIST, bitSet));
		searchResults.setAlbums(doFacets(FACET_ALBUM, bitSet));
		return searchResults;
	}

	/**
	 * 
	 * @param fieldName
	 * @param resultBitSet
	 * @return
	 * @throws IOException
	 */
	private Collection<String> doFacets(final String fieldName, final BitSet resultBitSet) throws IOException {
		final TermEnum te = indexSearcher.getIndexReader().terms(new Term(fieldName, ""));
		final Collection<String> facets = new ArrayList<String>();
		Term term;
		do {
			if ((term = te.term()) == null || !term.field().equals(fieldName))
				break;
			if (te.docFreq() > 0) {
				final BitSet filtered = (BitSet) new QueryWrapperFilter(new TermQuery(term)).bits(
						indexSearcher.getIndexReader()).clone();
				filtered.and(resultBitSet);
				if (filtered.cardinality() > 0)
					facets.add(term.text());
			}
		} while (te.next());
		te.close();
		return facets;
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final String root = "/Volumes/Arc - 1/Music/";
		final String indexRoot = "MP3METADATA";
		final IndexManager mib = new IndexManager(root, indexRoot);
		System.out.println(StringEscapeUtils.unescapeHtml("aaaaaaaaa&#0;"));
		mib.createIndex();
		final long start = System.currentTimeMillis();
		final PlayList results = new PlayList();
		// mib.search(results);
		System.out.println(results);
		System.out.println(System.currentTimeMillis() - start);
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
			if (!uidit.hasNext())
				break;
			i += 1;
			if (look != null) {
				if (uid.compareTo(look) > 0)
					continue;
				if (look.equals(uid)) {
					final Filter qf = new QueryFilter(new TermQuery(new Term(UUID, uid)));
					pl.add(new MediaFile(indexSearcher.doc(qf.bits(indexSearcher.getIndexReader()).nextSetBit(0))));
					uid = uidit.next();
				}
			}
		}
		return pl;
	}
}
