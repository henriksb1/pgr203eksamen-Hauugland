package no.kristiania.database;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberDao {

    private DataSource datasource;

    public MemberDao(DataSource dataSource) {
        this.datasource = dataSource;
    }


    public void insert(Member member) throws SQLException {
        try (Connection connection = datasource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO members (member_name, email) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )){
                statement.setString(1, member.getName());
                statement.setString(2, member.getEmail());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    member.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public Member retrieve(Long id) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM members WHERE id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToMember(rs);
                    }else{
                        return null;
                    }

                }
            }
        }
    }

    private Member mapRowToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("member_name"));
        member.setEmail(rs.getString("email"));
        return member;
    }


    public List<Member> list() throws SQLException {
        List<Member>  members = new ArrayList<>();
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM members")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        Member member = new Member();
                        member.setName(rs.getString("member_name"));
                        members.add(mapRowToMember(rs));
                    }
                }
            }
        }
        return members;
    }
}
