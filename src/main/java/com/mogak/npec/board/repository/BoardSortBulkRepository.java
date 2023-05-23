package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardSort;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardSortBulkRepository {
    private final String TABLE = "board_sorts";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void bulkInsert(List<BoardSort> boardSorts) {
        String sql = String.format("""
                INSERT INTO %s (boards_id, like_count, view_count, comment_count, created_at, updated_at)
                VALUES (:board.id, :likeCount, :viewCount, :commentCount, :createdAt, :updatedAt)
                """, TABLE);
        SqlParameterSource[] params = boardSorts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, params);
    }

    public void truncateTable() {
        String sql = String.format("""
                TRUNCATE TABLE %s
                """, TABLE);

        jdbcTemplate.getJdbcOperations().execute(sql);
    }
}
