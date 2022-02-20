package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void save() throws Exception {
        //given
        Member member = new Member("m1");
        //when
        Member save = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(save.getId());
        //then
        assertThat(findMember.getId()).isEqualTo(save.getId());
        assertThat(findMember.getUsername()).isEqualTo(save.getUsername());
        assertThat(findMember).isEqualTo(save);
    }

    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단기 조회건
        //when
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //카운트 검증

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCnt = memberJpaRepository.count();
        assertThat(deletedCnt).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA",20);
        memberJpaRepository.save(aaa);
        memberJpaRepository.save(bbb);
        //when
        List<Member> result = memberJpaRepository.findByusernameAndAgeGreaterThan("AAA",15);
        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void paging() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB",10);
        memberJpaRepository.save(aaa);
        memberJpaRepository.save(bbb);
        Member ccc = new Member("CCC", 10);
        Member ddd = new Member("DDD",10);
        memberJpaRepository.save(ccc);
        memberJpaRepository.save(ddd);
        Member eee = new Member("EEE", 10);
        Member fff = new Member("FFF",10);
        memberJpaRepository.save(eee);
        memberJpaRepository.save(fff);
        int age = 10;
        int offset = 3;
        int limit = 3;
        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);
        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(6);

    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        Member aaa = new Member("AAA", 11);
        Member bbb = new Member("BBB",12);
        memberJpaRepository.save(aaa);
        memberJpaRepository.save(bbb);
        Member ccc = new Member("CCC", 13);
        Member ddd = new Member("DDD",14);
        memberJpaRepository.save(ccc);
        memberJpaRepository.save(ddd);
        Member eee = new Member("EEE", 15);
        Member fff = new Member("FFF",16);
        memberJpaRepository.save(eee);
        memberJpaRepository.save(fff);

        //when
        //10살 이상 모두 +1
        int resultCount = memberJpaRepository.bulkAgePlus(10);
        //then
        assertThat(resultCount).isEqualTo(6);
    }
}