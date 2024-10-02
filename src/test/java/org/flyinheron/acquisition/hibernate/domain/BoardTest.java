package org.flyinheron.acquisition.hibernate.domain;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class BoardTest extends JpaTestBase {
    @Autowired
    public BoardTest(EntityManagerFactory emf) {
        super(emf);
    }

    @Test
    void persistBoard() {
        var board = Board.builder()
                .content("hello")
                .member(embeddedMember)
                .build();

        em.persist(board);
        em.flush();

        var found = em.find(Board.class, board.getId());

        assertThat(found.getId()).isNotNull();
        assertThat(found.getCreatedDate()).isNotNull();
    }

    @Test
    void can_not_create_without_content() {
        var board = Board.builder()
                .member(embeddedMember)
                .build();

        assertThatThrownBy(() -> em.persist(board));
        transaction.rollback();
    }

    @Test
    void can_not_create_without_member() {
        var board = Board.builder()
                .content("hello")
                .build();

        assertThatThrownBy(() -> em.persist(board));
        transaction.rollback();
    }
}