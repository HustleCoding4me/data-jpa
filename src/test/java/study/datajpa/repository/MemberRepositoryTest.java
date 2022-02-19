package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member m = new Member("hw");
        //when
        Member save = memberRepository.save(m);
        Optional<Member> findMember = memberRepository.findById(save.getId());
        findMember.orElseGet(()->new Member("default"));
        //then
        assertThat(findMember.get().getUsername()).isEqualTo(save.getUsername());
        assertThat(findMember.get().getId()).isEqualTo(save.getId());
        assertThat(findMember.get()).isEqualTo(save);

    }

    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단기 조회건
        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //카운트 검증

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCnt = memberRepository.count();
        assertThat(deletedCnt).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA",20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        //when
        List<Member> result = memberRepository.findByusernameAndAgeGreaterThan("AAA",15);
        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void testQuery() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA",20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        //when
        List<Member> result = memberRepository.findUser("AAA",10);
        //when
    assertThat(result.get(0)).isEqualTo(aaa);
        //then
    }

    @Test
    public void findUserNameListTest() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA",20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        //when
        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("username : " +      s);
        }        //then



    }
    @Test
    void findMemberDto() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);
        Member memberA = new Member("AAA", 10);
        memberA.setTeam(teamA);
        memberRepository.save(memberA);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("memberDto = :" + dto);
        }
    }

    @Test
    public void findByNames() throws Exception {
        //given
        getTestMembers(new Member("AAA", 10), new Member("AAA", 20));
        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        //then
        for (Member member : result) {
            System.out.println("members : " +  member);
        }
    }

    private void getTestMembers(Member AAA, Member AAA1) {
        Member aaa = AAA;
        Member bbb = AAA1;
        memberRepository.save(aaa);
        memberRepository.save(bbb);
    }


    @Test
    public void returnType() throws Exception {
        getTestMembers(new Member("AAA", 10), new Member("BBB", 20));

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        //좋은 점 null일 경우(유저가 없을 겨우) EmptyColection을 return 해준다.
        //절대 null로 return하지 않는다. != null체크 안해도 됨

        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        //단건 조회일 경우 없으면 null이다.
        //JPA는 결과가 없을 경우 noResultAcception이 뜬다.
        //SpringDataJPA는 그냥 null로 반환 해버린다.
        //사실 그냥 Optional로 받으면 된다.
        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");
        //만약 한 건 조회인데 2건이상이 조회가 될경우 IncorrectResultSizeDataAccessException이 터진다.
        //원래 NonUniqueDataException이 터지면, Spring 예외로 변환해서 준다.
        // why? client가 여러 DB를 사용해도 spring프레임워크가 한번 감싸서 주면
        //동일한 상황에 동일한 Exception이 넘어올텐데데 그럼 개발자가 동일한 처리를
        //유지해도 되어서 좋다.
}