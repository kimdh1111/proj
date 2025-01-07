package edu.du.proj1120.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MemberDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Member selectByEmail(String email) {
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where EMAIL = ?",
				new RowMapper<Member>() {
					@Override
					public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
						// 'REGDATE'가 null일 경우 LocalDateTime을 null로 처리
						Timestamp regDateTimestamp = rs.getTimestamp("REGDATE");
						LocalDateTime regDateTime = null;
						if (regDateTimestamp != null) {
							regDateTime = regDateTimestamp.toLocalDateTime();
						}

						// 'REGDATE'가 null일 경우를 처리하여 member 객체 생성
						Member member = new Member(
								rs.getString("EMAIL"),
								rs.getString("PASSWORD"),
								rs.getString("NAME"),
								regDateTime); // regDateTime이 null일 수 있음
						member.setId(rs.getLong("ID"));
						return member;
					}
				}, email);

		return results.isEmpty() ? null : results.get(0);
	}



	public void insert(Member member) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) " +
								"values (?, ?, ?, ?)",
						new String[] { "ID" });
				// 인덱스 파라미터 값 설정
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4,
						Timestamp.valueOf(member.getRegisterDateTime()));
				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
		}, keyHolder);
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	}

	public void update(Member member) {
		jdbcTemplate.update(
				"update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?",
				member.getName(), member.getPassword(), member.getEmail());
	}

	public List<Member> selectAll() {
		List<Member> results = jdbcTemplate.query("select * from MEMBER",
				(ResultSet rs, int rowNum) -> {
					Member member = new Member(
							rs.getString("EMAIL"),
							rs.getString("PASSWORD"),
							rs.getString("NAME"),
							rs.getTimestamp("REGDATE").toLocalDateTime());
					member.setId(rs.getLong("ID"));
					return member;
				});
		return results;
	}

	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from MEMBER", Integer.class);
		return count;
	}

	public List<Member> selectByRegdate(LocalDateTime from, LocalDateTime to) {
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where REGDATE between ? and ? " +
						"order by REGDATE desc",
				memRowMapper,
				from, to);
		return results;
	}

	private RowMapper<Member> memRowMapper =
			new RowMapper<Member>() {
				@Override
				public Member mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Member member = new Member(rs.getString("EMAIL"),
							rs.getString("PASSWORD"),
							rs.getString("NAME"),
							rs.getTimestamp("REGDATE").toLocalDateTime());
					member.setId(rs.getLong("ID"));
					return member;
				}
			};

	public Member selectById(Long memId) {
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where ID = ?",
				memRowMapper, memId);

		return results.isEmpty() ? null : results.get(0);
	}

}