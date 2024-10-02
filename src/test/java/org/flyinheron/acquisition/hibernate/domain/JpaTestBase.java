package org.flyinheron.acquisition.hibernate.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
public class JpaTestBase {
    private final EntityManagerFactory emf;
    protected EntityManager em;
    protected Member embeddedMember;
    protected Board embeddedBoard;
    protected EntityTransaction transaction;

    public JpaTestBase(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        getTransactionBegin();

        persistEmbeddedMember();
        persistEmbeddedBoard();

        Member found = em.find(Member.class, embeddedMember.getId());
        assertThat(embeddedMember).isEqualTo(found);
    }

    @AfterEach
    void tearDown() {
        beginTransactionIfNotActive();

        em.createQuery("DELETE FROM Comment c").executeUpdate();
        em.createQuery("DELETE FROM Board b").executeUpdate();
        em.createQuery("DELETE FROM Member m").executeUpdate();

        var commentList = em.createQuery("SELECT c FROM Comment c").getResultList();
        var boardList = em.createQuery("SELECT b FROM Board b").getResultList();
        var memberList = em.createQuery("SELECT m FROM Member m").getResultList();

        assertThat(commentList).isEmpty();
        assertThat(boardList).isEmpty();
        assertThat(memberList).isEmpty();
        transaction.commit();
    }

    private void getTransactionBegin() {
        transaction = em.getTransaction();
        transaction.begin();
    }

    /**
     * assertThatThrownBy, transaction.Rollback 시, 트랜잭션 다시 시작.
     */
    private void beginTransactionIfNotActive() {
        if (!transaction.isActive()) {
            getTransactionBegin();
        }
    }

    void persistEmbeddedBoard() {
        embeddedBoard = Board.builder()
                .content("hello board")
                .member(embeddedMember)
                .build();
        em.persist(embeddedBoard);
        em.flush();
    }

    void persistEmbeddedMember() {
        embeddedMember = Member.builder()
                .email("flyin.heron@gmail.com")
                .nickname("heron")
                .password("some-legit-password")
                .phoneNumber("01133335555")
                .build();
        em.persist(embeddedMember);
        em.flush();
    }
}
