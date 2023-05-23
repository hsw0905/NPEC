package com.mogak.npec.member.repository;

import com.mogak.npec.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberBulkRepository {
    private final String TABLE = "members";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void bulkInsert(List<Member> members) {
        String sql = String.format("""
                INSERT INTO %s (nickname, email, password, is_out, image_url, created_at, updated_at)
                VALUES (:nickname, :email, :password, :isOut, :imageUrl, :createdAt, :updatedAt)
                """, TABLE);
        SqlParameterSource[] params = members
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
