package no.kristiania.database;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDaoTest {
    @Test
    void ShouldListInsertedMembers() {
        MemberDao memberDao = new MemberDao();
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