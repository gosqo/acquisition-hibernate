package org.flyinheron.acquisition.hibernate.domain;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class CommentTest extends JpaTestBase {
    @Autowired
    public CommentTest(EntityManagerFactory emf) {
        super(emf);
    }

    @Test
    @DisplayName("안녕하세요?")
    void can_not_create_without_content() {
        var comment = Comment.builder()
                .member(embeddedMember)
                .board(embeddedBoard)
                .build();

        assertThatThrownBy(() -> em.persist(comment));
        transaction.rollback();
    }

    @Test
    void can_not_create_without_member() {
        var comment = Comment.builder()
                .content("hello.")
                .board(embeddedBoard)
                .build();

        assertThatThrownBy(() -> em.persist(comment));
        transaction.rollback();
    }

    @Test
    void can_not_create_without_board() {
        var comment = Comment.builder()
                .content("hello.")
                .member(embeddedMember)
                .build();

        assertThatThrownBy(() -> em.persist(comment));
        transaction.rollback();
    }

    @Test
    void persistComment() {
        var comment = Comment.builder()
                .content("hello.")
                .board(embeddedBoard)
                .member(embeddedMember)
                .build();
        em.persist(comment);
        em.flush();

        var found = em.find(Comment.class, comment.getId());

        assertThat(found).isEqualTo(comment);
    }
}