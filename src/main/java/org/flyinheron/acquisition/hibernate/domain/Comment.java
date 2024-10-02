package org.flyinheron.acquisition.hibernate.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4000)
    private String content;

    @ManyToOne
    @JoinColumn(
            name="member_id"
            , nullable = false
            , referencedColumnName = "id"
            , foreignKey = @ForeignKey(name = "fk_comment_member_member_id"))
    private Member member;

    @ManyToOne
    @JoinColumn(
            name="board_id"
            , nullable = false
            , referencedColumnName = "id"
            , foreignKey = @ForeignKey(name = "fk_comment_board_board_id"))
    private Board board;
}
