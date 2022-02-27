package study.datajpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class initDB {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PostConstruct
    public void init() {
        /*for(int i = 0; i < 100; i++){
            memberRepository.save(new Member("user" + i,i));
            teamRepository.save(new Team("team" + i));
        }*/
    }
}
