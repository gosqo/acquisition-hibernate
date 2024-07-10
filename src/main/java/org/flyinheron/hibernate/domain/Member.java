package org.flyinheron.hibernate.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@Table(name = "member")
public class Member extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 24)
    private String password;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String nickname;

    @PreUpdate
    protected void setDeleteDateIfSoftDeleted() {
        if (isSoftDeleted()) {
            setDeletedDate();
        }
    }
}
