package org.yass.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

public class SearchQuery implements Constants {

	private BooleanQuery artistsFilter;
	private BooleanQuery albumsFilter;
	private BooleanQuery genresFilter;
	private String keywords;
	private String keywordsFields;
	final static MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] { ALBUM, ARTIST, TITLE },
			new StandardAnalyzer());
	final static QueryParser albumParser = new QueryParser(ALBUM, new StandardAnalyzer());
	final static QueryParser artistParser = new QueryParser(ARTIST, new StandardAnalyzer());
	final static QueryParser titleParser = new QueryParser(TITLE, new StandardAnalyzer());
	static {
		queryParser.setDefaultOperator(Operator.AND);
		albumParser.setDefaultOperator(Operator.AND);
		artistParser.setDefaultOperator(Operator.AND);
		titleParser.setDefaultOperator(Operator.AND);
	}

	/**
	 * @return the keywords
	 */
	public final String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setKeywords(final String keywords) {
		setAlbumsFilter(null);
		setArtistsFilter(null);
		setGenresFilter(null);
		this.keywords = keywords;
	}

	/**
	 * @return the filter
	 */
	public final QueryWrapperFilter getFilter() {
		final BooleanQuery bq = new BooleanQuery();
		if (artistsFilter != null)
			bq.add(artistsFilter, Occur.MUST);
		if (genresFilter != null)
			bq.add(genresFilter, Occur.MUST);
		if (albumsFilter != null)
			bq.add(albumsFilter, Occur.MUST);
		if (bq.getClauses().length != 0)
			return new QueryWrapperFilter(bq);
		return null;
	}

	/**
	 * @return the query
	 */
	public final Query getQuery() {
		if (keywords != null && !"".equals(keywords)) {
			final BooleanQuery query = new BooleanQuery();
			Query keywQuery = null;
			try {
				if ("ALL".equals(keywordsFields) || keywordsFields == null)
					keywQuery = queryParser.parse(keywords);
				else if (ARTIST.equals(keywordsFields))
					keywQuery = artistParser.parse(keywords);
				else if (ALBUM.equals(keywordsFields))
					keywQuery = albumParser.parse(keywords);
				else if (TITLE.equals(keywordsFields))
					keywQuery = titleParser.parse(keywords);
			} catch (final ParseException e) {
				return null;
			}
			query.add(keywQuery, Occur.SHOULD);
			return query;
		}
		return null;
	}

	/**
	 * @param artistsFilter
	 *          the artistsFilter to set
	 */
	public final void setArtistsFilter(final String[] artistsFilter) {
		this.artistsFilter = createFilterQuery(artistsFilter, FACET_ARTIST);
	}

	/**
	 * @param albumsFilter
	 *          the albumsFilter to set
	 */
	public final void setAlbumsFilter(final String[] albumsFilter) {
		this.albumsFilter = createFilterQuery(albumsFilter, FACET_ALBUM);
	}

	/**
	 * @param genresFilter
	 *          the genresFilter to set
	 */
	public final void setGenresFilter(final String[] genresFilter) {
		this.genresFilter = createFilterQuery(genresFilter, FACET_GENRE);
	}

	private BooleanQuery createFilterQuery(final String[] facets, final String fieldName) {
		if (facets != null && facets.length != 0) {
			final BooleanQuery query = new BooleanQuery();
			for (final String facet : facets)
				query.add(new BooleanClause(new TermQuery(new Term(fieldName, facet)), Occur.SHOULD));
			return query;
		}
		return null;
	}

	/**
	 * @return the artistsFilter
	 */
	public final BooleanQuery getArtistsFilter() {
		return artistsFilter;
	}

	/**
	 * @return the albumsFilter
	 */
	public final BooleanQuery getAlbumsFilter() {
		return albumsFilter;
	}

	/**
	 * @return the genresFilter
	 */
	public final BooleanQuery getGenresFilter() {
		return genresFilter;
	}

	/**
	 * @param keywordsFields
	 *          the keywordsFields to set
	 */
	public final void setKeywordsFields(final String keywordsFields) {
		this.keywordsFields = keywordsFields;
	}
}
