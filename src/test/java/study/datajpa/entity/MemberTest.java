package study.datajpa.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        
        //when
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear();//db 쿼리 날리고 영속성 cache 날려버림.

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        members.stream().forEach(m -> System.out.println("member = " + m +"\n -> member.team = " + m.getTeam()));
    }

    @Test
    public void mappedSuperClassTest() throws Exception {
        //given
        Member m1 = new Member("user1");
        memberRepository.save(m1); //@PrePersist 작동
        Thread.sleep(100);
        m1.setUsername("user2222");
        //when
        em.flush();
        em.clear();
        Member member = memberRepository.findById(m1.getId()).get();
        //then
        System.out.println("getCreatedDate : " + member.getCreatedDate());
        System.out.println("getLastModifiedDate : " + member.getLastModifiedDate());
        System.out.println("getCreatedBy : " +  member.getCreatedBy());
        System.out.println("getLastModifiedBy : " +  member.getLastModifiedBy());
    }
    
}