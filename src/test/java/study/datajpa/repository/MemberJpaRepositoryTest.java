package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @Rollback(value = false)
    public void save() throws Exception {
        //given
        Member member = new Member("m1");
        //when
        Member save = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(save.getId());
        //then
        assertThat(findMember.getId()).isEqualTo(save.getId());
        assertThat(findMember.getUserName()).isEqualTo(save.getUserName());
        assertThat(findMember).isEqualTo(save);
    }

    @Test
    public void find() throws Exception {
        //given

        //when

        //then
    }
}