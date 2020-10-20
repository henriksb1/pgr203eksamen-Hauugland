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
    private ArrayList<String> members = new ArrayList<>();

    public MemberDao(DataSource dataSource) {
        this.datasource = dataSource;
    }

    public static void main(String[] args) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/teammembers");
        dataSource.setUser("memberadmin");
        dataSource.setPassword("V0E5!M@7eaM!");


        System.out.println("Whats the new member name?");
        Scanner scanner = new Scanner(System.in);
        String memberName = scanner.nextLine();

        try (Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO members (member_name) values (?)")){
                statement.setString(1, memberName);
                statement.executeUpdate();
            }
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM members")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(rs.getString("member_name"));
                    }
                }
            }
        }
    }

    public void insert(String member) throws SQLException {
        try (Connection connection = datasource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO members (member_name) values (?)")){
                statement.setString(1, member);
                statement.executeUpdate();
            }
        }
        members.add(member);
    }

    public List<String> list() {
        return members;
    }
}
