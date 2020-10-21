package no.kristiania.database;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberDao {

    private DataSource datasource;

    public MemberDao(DataSource dataSource) {
        this.datasource = dataSource;
    }

    public static void main(String[] args) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/teammembers");
        dataSource.setUser("memberadmin");
        dataSource.setPassword("V0E5!M@7eaM!");

        MemberDao memberDao = new MemberDao(dataSource);


        System.out.println("Whats the new member name?");
        Scanner scanner = new Scanner(System.in);
        String memberName = scanner.nextLine();

        Member member = new Member();
        member.setName(memberName);
        memberDao.insert(member);
        for (Member m : memberDao.list()) {
            System.out.println(m);
        }

    }

    public void insert(Member member) throws SQLException {
        try (Connection connection = datasource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO members (member_name) values (?)")){
                statement.setString(1, member.getName());
                statement.executeUpdate();
            }
        }
    }

    public Member retrieve(Long id) {
        return null;
    }


    public List<Member> list() throws SQLException {
        List<Member>  members = new ArrayList<>();
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM members")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        Member member = new Member();
                        member.setName(rs.getString("member_name"));
                        members.add(member);
                    }
                }
            }
        }
        return members;
    }
}
