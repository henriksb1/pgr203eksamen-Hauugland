package no.kristiania.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDaoTest {

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        memberDao = new MemberDao(dataSource);
    }

    @Test
    void ShouldListInsertedMembers() throws SQLException {
        Member member = exampleMember();
        memberDao.insert(member);
        assertThat(memberDao.list())
                .extracting(Member::getName)
                .contains(member.getName());
    }

    @Test
    void shouldRetrieveAllMemberProperties() throws SQLException {
        Member member = exampleMember();
        memberDao.insert(member);
        assertThat(memberDao.retrieve(member.getId()))
                .isEqualTo(member);
    }

    private Member exampleMember() {
        Member member = new Member();
        member.setName(exampleMemberName());
        return member;
    }

    private String exampleMemberName() {
        String[] names = {"Petter", "Marius", "Thomine", "Oda"};
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }


}