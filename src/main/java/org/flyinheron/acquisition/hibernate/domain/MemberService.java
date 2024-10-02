package org.flyinheron.acquisition.hibernate.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final Logger log = LoggerFactory.getLogger(MemberService.class);
    @PersistenceContext
    private final EntityManager entityManager;

    public MemberService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void persistEmbeddedMember() {
        Member embededMember = Member.builder()
                .email("flyin.heron@gmail.com")
                .password("someLegitPassword")
                .nickname("heron")
                .phoneNumber("01133338888")
                .build();

        entityManager.persist(embededMember);
        entityManager.flush();
        log.info("persisted.");
    }
}
