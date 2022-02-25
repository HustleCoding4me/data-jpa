package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
