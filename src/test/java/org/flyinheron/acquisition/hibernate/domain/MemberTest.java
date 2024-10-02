package org.flyinheron.acquisition.hibernate.domain;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class MemberTest extends JpaTestBase {
    @Autowired
    public MemberTest(EntityManagerFactory emf) {
        super(emf);

    }

    @Test
    void can_not_create_without_email() {
        Member member = Member.builder()
                .nickname("flyin-heron")
                .phoneNumber("01012311231")
                .password("passwordExample")
                .build();

        assertThatThrownBy(() -> em.persist(member));
        transaction.rollback();
    }

    @Test
    void persistMember() {
        Member member = Member.builder()
                .email("hello@email.com")
                .nickname("heron2")
                .password("password")
                .phoneNumber("01201230123")
                .build();

        // persist(entity), 영속성 컨텍스트(1차 캐시)에 entity 저장 및 쓰기 지연 SQL 저장소에 DB 에 보낼 쿼리문을 저장.
        em.persist(member);

        // flush(), 영속성 컨텍스트의 쓰기 지연 SQL 저장소에 쌓인 쿼리를 보내고, 영속성 컨텍스트의 1차 캐시와 DB에 삽입된 엔터티의 싱크를 맞춘다(???).
        // flush() 명시하지 않으면, org.hibernate.engine.transaction.internal.TransactionImpl.commit() 시 flush().
        em.flush();

        String memberId = member.getId();

        Member found = em.find(Member.class, memberId);

        assertThat(found).isEqualTo(member);
        assertThat(found.getCreatedDate()).isEqualTo(found.getUpdatedDate());
        assertThat(found.getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(found.getId()).isNotNull();
    }

    @Test
    void updateStatus() {
        Member member = Member.builder()
                .email("hello@email3.com")
                .nickname("hero4")
                .password("password")
                .phoneNumber("01201230123")
                .build();

        em.persist(member);
        em.flush();

        String memberId = member.getId();
        LocalDateTime createdDate = member.getCreatedDate();
        Member found = em.find(Member.class, memberId);

        found.updateStatus(EntityStatus.SOFT_DELETED);
        em.flush();

        EntityStatus updatedStatus = found.getStatus();
        LocalDateTime updatedDate = found.getUpdatedDate();

        assertThat(updatedDate).isNotEqualTo(createdDate);
        assertThat(updatedStatus).isEqualTo(EntityStatus.SOFT_DELETED);
    }
}
