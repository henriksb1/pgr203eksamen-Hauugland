package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MemberDao extends AbstractDao<Member> {

    public MemberDao(DataSource dataSource) {
        super(dataSource);

    }

    public void insert(Member member) throws SQLException {
        insert(member, "INSERT INTO members (member_name, email) values (?, ?)");
    }


    public Member retrieve(Long id) throws SQLException {
        return retrieve(id, "SELECT * FROM members WHERE id = ?");
    }

    public List<Member> list() throws SQLException {
        return list("SELECT * FROM members");
    }

    @Override
    protected Member mapRow(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("member_name"));
        member.setEmail(rs.getString("email"));
        return member;
    }

}
