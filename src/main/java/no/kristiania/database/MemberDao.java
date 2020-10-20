package no.kristiania.database;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Member;
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

        memberDao.insert(memberName);
        for (String member : memberDao.list()) {
            System.out.println(member);
        }

    }

    public void insert(String member) throws SQLException {
        try (Connection connection = datasource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO members (member_name) values (?)")){
                statement.setString(1, member);
                statement.executeUpdate();
            }
        }
    }

    public List<String> list() throws SQLException {
        List<String>  members = new ArrayList<>();
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM members")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        members.add(rs.getString("member_name"));
                    }
                }
            }
        }
        return members;
    }
}
