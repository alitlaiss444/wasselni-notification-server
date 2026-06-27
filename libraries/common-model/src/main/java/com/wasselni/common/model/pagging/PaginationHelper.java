
package com.wasselni.common.model.pagging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;


public class PaginationHelper<E> {

	public Page<E> fetchPage(final JdbcTemplate jt, final String sqlFetchRows, final Object args[], final int pageNo,
			final int pageSize, final RowMapper<E> rowMapper) {

		// determine how many rows are available
		final int rowCount = jt.queryForObject(buildCountQuery(sqlFetchRows), Integer.class, args);

		// calculate the number of pages
		int pageCount = rowCount / pageSize;
		if (rowCount > pageSize * pageCount) {
			pageCount++;
		}

		// create the page object
		final Page<E> page = new Page<E>();
		page.setPageNumber(pageNo);
		page.setPagesAvailable(pageCount);
		page.setTotalCount(rowCount);

		// fetch a single page of results
		final int startRow = (pageNo - 1) * pageSize;
		jt.query(sqlFetchRows, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				final List<E> pageItems = page.getPageItems();
				int currentRow = 0;
				while (rs.next() && currentRow < startRow + pageSize) {
					if (currentRow >= startRow) {
						pageItems.add(rowMapper.mapRow(rs, currentRow));
					}
					currentRow++;
				}
				return page;
			}
		}, args);
		return page;
	}

	public String buildCountQuery(String query) {
		return "SELECT count(1) FROM (" + query + ") T";
	}

	public static int getPage(int start, int pageSize) {
		return (start / pageSize) + 1;
	}
}