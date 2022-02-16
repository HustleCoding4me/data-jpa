package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member m = new Member("hw");
        //when
        Member save = memberRepository.save(m);
        Optional<Member> findMember = memberRepository.findById(save.getId());
        findMember.orElseGet(()->new Member("default"));
        //then
        assertThat(findMember.get().getUserName()).isEqualTo(save.getUserName());
        assertThat(findMember.get().getId()).isEqualTo(save.getId());
        assertThat(findMember.get()).isEqualTo(save);

    }

}