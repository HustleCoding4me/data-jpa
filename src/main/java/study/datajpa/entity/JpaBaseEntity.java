package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass //실제 상속관계는 아니고 그냥 값만 내려서 사용할 수 있게 해주는 class
//이걸 선언해야 이후 상속한 Entity Table 생성시 변수값이 추가된다.
@Getter @Setter
public class JpaBaseEntity {

    @Column(updatable = false) //DB의 값이 변경되지 않게 고정해주는 어노테이션
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //persist (등록) 하기전에 발동
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate //update 전에 발동
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }

}
