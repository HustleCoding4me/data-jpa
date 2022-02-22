package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() throws Exception {
        //given
        Member m = new Member("hw");
        //when
        Member save = memberRepository.save(m);
        Optional<Member> findMember = memberRepository.findById(save.getId());
        findMember.orElseGet(() -> new Member("default"));
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
        Member bbb = new Member("AAA", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        //when
        List<Member> result = memberRepository.findByusernameAndAgeGreaterThan("AAA", 15);
        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void testQuery() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        //when
        List<Member> result = memberRepository.findUser("AAA", 10);
        //when
        assertThat(result.get(0)).isEqualTo(aaa);
        //then
    }

    @Test
    public void findUserNameListTest() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        //when
        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("username : " + s);
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
            System.out.println("members : " + member);
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

    @Test
    public void paging() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 10);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        Member ccc = new Member("CCC", 10);
        Member ddd = new Member("DDD", 10);
        memberRepository.save(ccc);
        memberRepository.save(ddd);
        Member eee = new Member("EEE", 10);
        Member fff = new Member("FFF", 10);
        memberRepository.save(eee);
        memberRepository.save(fff);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //when
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
        //then
        //내부 실제 Data 꺼내기
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("Member : " + member);
        }
        System.out.println("totalElements : " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);//전체 페이지 번호
        assertThat(page.isFirst()).isTrue();//첫번째 페이지인가
        assertThat(page.hasNext()).isTrue();//다음이 있는가 (더보기 같은 기능 구현시 좋음)
    }

    @Test
    public void slicing() throws Exception {
        //given
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 10);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        Member ccc = new Member("CCC", 10);
        Member ddd = new Member("DDD", 10);
        memberRepository.save(ccc);
        memberRepository.save(ddd);
        Member eee = new Member("EEE", 10);
        Member fff = new Member("FFF", 10);
        memberRepository.save(eee);
        memberRepository.save(fff);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);
        //then
        //내부 실제 Data 꺼내기
        List<Member> content = page.getContent();
        //long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("Member : " + member);
        }
        //System.out.println("totalElements : " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        //assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0);//페이지 번호
        //assertThat(page.getTotalPages()).isEqualTo(2);//전체 페이지 번호
        assertThat(page.isFirst()).isTrue();//첫번째 페이지인가
        assertThat(page.hasNext()).isTrue();//다음이 있는가 (더보기 같은 기능 구현시 좋음)

        page.map(member -> new MemberDto(member.getId(), member.getUsername(), "teamA"));
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        Member aaa = new Member("AAA", 11);
        Member bbb = new Member("BBB", 12);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        Member ccc = new Member("CCC", 13);
        Member ddd = new Member("DDD", 14);
        memberRepository.save(ccc);
        memberRepository.save(ddd);
        Member eee = new Member("EEE", 15);
        Member fff = new Member("FFF", 16);
        memberRepository.save(eee);
        memberRepository.save(fff);
        //when
        //10살 이상 모두 +1
        int resultCount = memberRepository.bulkAgePlus(10);
        //then
        assertThat(resultCount).isEqualTo(6);
    }

    @Test
    public void checkBulkDirtyChecking() throws Exception {
        //given
        Member aaa = new Member("AAA", 11);
        Member bbb = new Member("BBB", 12);
        memberRepository.save(aaa);
        memberRepository.save(bbb);
        Member ccc = new Member("CCC", 13);
        Member ddd = new Member("DDD", 14);
        memberRepository.save(ccc);
        memberRepository.save(ddd);
        Member eee = new Member("EEE", 15);
        Member fff = new Member("FFF", 100);
        memberRepository.save(eee);
        memberRepository.save(fff);

        //when
        List<Member> findMemberBeforeBulk = memberRepository.findByUsername("FFF");
        int resultCount = memberRepository.bulkAgePlus(10);
        em.flush();
        em.clear();
        List<Member> findAfterBulkMember = memberRepository.findByUsername("FFF");

        //DB는 101인데, 둘 다 100으로 출력된다.
        //then
        System.out.println("findMemberBeforeBulk : " + findMemberBeforeBulk.get(0).getAge());
        System.out.println("findAfterBulkMember : " + findAfterBulkMember.get(0).getAge());

    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //fetchType LAZY라 member를 조회할 때 ,Team은 최초에 proxy로 가져온다.
        //지연로딩.

        em.flush();
        em.clear();

        //when

      /*  List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("memberNm = " + member.getUsername());
            System.out.println("memberTeam = " + member.getTeam().getName());
        }*/
        List<Member> memberFetchJoin = memberRepository.findMemberFetchJoin();
        for (Member member : memberFetchJoin) {
            System.out.println("memberNm = " + member.getUsername());
            System.out.println("TeamClass : " + member.getTeam().getClass());
            System.out.println("memberTeam = " + member.getTeam().getName());
        }
        //then
    }

    @Test
    public void queryHint() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();
        //flush까지는 영속성 Context가 남아있고, clear 할 때 날아간다.
        //when
        Member findMember = memberRepository.findById(member1.getId()).get();
        findMember.setUsername("member2");
        em.flush();
        //변경 감지가 발생해서 update가 됨.
        //변경 하려면 원본과 비교할 가상 Entity를 셋팅해놓는다.
        //그래서 Select만 할거라고 Hint를 주는거다. Hint는 Hibernate만 있음.


        Member findMemberReadOnly = memberRepository.findReadOnlyByUsername("member2");
        findMember.setUsername("hihi");
        em.flush();

        //update문이 나가지 않는다. 이미 readOnly로 애초에 Update를 염두에 두지 않는다.

    }

    @Test
    public void lock() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();
        //when
        List<Member> findMember = memberRepository.findLockByUsername("member1");
        //lockTest
        //then
    }

    @Test
    public void callCustom() throws Exception {
        //given
        List<Member> memberCustom = memberRepository.findMemberCustom();
        //when

        //then
    }
    

}