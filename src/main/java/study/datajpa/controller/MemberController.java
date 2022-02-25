package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    //우리가 예상하는 구현
    @GetMapping("/getMember/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //Spring이 id로 자동으로 Member 삽입
    @GetMapping("/getMember/{id}/autoConstruct")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public List<MemberDto> memberDtoList(@PageableDefault(size=10) Pageable pageable){
        return memberRepository.findAll(pageable).stream()
                .map(m -> new MemberDto(m))
                .collect(Collectors.toList());
    }


    //마지막 파라미터로 Pageable 인터페이스 받아서 쓰면 된다.
    //파라미터 Page 넘기면 자동으로 Mapping, Paging
    @GetMapping("/membersWithTeam")
    public List<Page> list(@PageableDefault(size=10)@Qualifier("member") Pageable memberPageable, @PageableDefault(size=5)@Qualifier("team") Pageable teamPageable){
        ArrayList<Page> p = new ArrayList<>();

        Page<Member> memberAll = memberRepository.findAll(memberPageable);
        Page<Team> teamAll = teamRepository.findAll(teamPageable);

        p.add(memberAll);
        p.add(teamAll);
        return p;
    }


    @GetMapping("/teams")
    public Page<Member> list(@PageableDefault(size= 5) Pageable teamPageable){
        return memberRepository.findAll(teamPageable);
    }

    @PostConstruct
    public void init() {
        for(int i = 0; i < 100; i++){
            memberRepository.save(new Member("user" + i,i));
            teamRepository.save(new Team("team" + i));
        }
    }
}
