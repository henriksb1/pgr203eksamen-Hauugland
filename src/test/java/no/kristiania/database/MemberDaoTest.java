package no.kristiania.database;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDaoTest {
    @Test
    void ShouldListInsertedMembers() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");

        try(Connection connection = dataSource.getConnection()){
            connection.prepareStatement("CREATE TABLE members (member_name varchar)").executeUpdate();
        }

        MemberDao memberDao = new MemberDao(dataSource);
        String member = exampleMember();
        memberDao.insert(member);
        assertThat(memberDao.list()).contains(member);
    }

    private String exampleMember() {
        String[] names = {"Petter", "Marius", "Thomine", "Oda"};
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }


}