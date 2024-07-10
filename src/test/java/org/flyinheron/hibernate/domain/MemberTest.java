package org.flyinheron.hibernate.domain;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Slf4j
public class MemberTest {
    private final EntityManager entityManager;

    @Autowired
    public MemberTest(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    void can_not_create_without_email() {
        Member member = Member.builder()
                .nickname("flyin-heron")
                .phoneNumber("01012311231")
                .password("passwordExample")
                .build();

        assertThatThrownBy(() -> entityManager.persist(member));
    }

    @Test
    void persistMember() {
        Member member = Member.builder()
                .email("hello@email.com")
                .nickname("heron")
                .password("password")
                .phoneNumber("01201230123")
                .build();
        Member member2 = Member.builder()
                .email("hello@email2.com")
                .nickname("heron2")
                .password("password")
                .phoneNumber("01201230123")
                .build();

        // persist(entity), 영속성 컨텍스트(1차 캐시)에 entity 저장 및 쓰기 지연 SQL 저장소에 DB 에 보낼 쿼리문을 저장.
        entityManager.persist(member);
        entityManager.persist(member2);

        // flush(), 영속성 컨텍스트의 쓰기 지연 SQL 저장소에 쌓인 쿼리를 보내고, 영속성 컨텍스트의 1차 캐시와 DB에 삽입된 엔터티의 싱크를 맞춘다(???).
        // flush() 명시하지 않으면, org.hibernate.engine.transaction.internal.TransactionImpl.commit() 시 flush().
        entityManager.flush();
        entityManager.refresh(member);

        String memberId = member.getId();
        LocalDateTime createdDate = member.getCreatedDate();

        String member2Id = member2.getId();
        LocalDateTime member2CreatedDate = member2.getCreatedDate();

        Member found = entityManager.find(Member.class, memberId);
        Member found2 = entityManager.find(Member.class, member2Id);
    }

    @Test
    void updateStatus() {
        Member member = Member.builder()
                .email("hello@email.com")
                .nickname("heron")
                .password("password")
                .phoneNumber("01201230123")
                .build();

        entityManager.persist(member);
        entityManager.flush();

        String memberId = member.getId();
        LocalDateTime createdDate = member.getCreatedDate();
        Member found = entityManager.find(Member.class, memberId);

        found.updateStatus(EntityStatus.SOFT_DELETED);
        entityManager.flush();

        EntityStatus updatedStatus = found.getStatus();
        LocalDateTime updatedDate = found.getUpdatedDate();

        assertThat(updatedDate).isNotEqualTo(createdDate);
        assertThat(updatedStatus).isEqualTo(EntityStatus.SOFT_DELETED);
    }
}
