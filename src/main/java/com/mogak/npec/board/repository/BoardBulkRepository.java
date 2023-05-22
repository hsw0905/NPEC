package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardBulkRepository {
    private final String TABLE = "boards";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void bulkInsert(List<Board> boards) {
        String sql = String.format("""
                INSERT INTO %s (members_id, title, content, is_deleted, modified_at, created_at, updated_at)
                VALUES (:member.id, :title, :content, :isDeleted, :modifiedAt, :createdAt, :updatedAt)
                """, TABLE);
        SqlParameterSource[] params = boards
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
