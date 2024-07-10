package org.flyinheron.hibernate.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityBase {
    @Enumerated(value = EnumType.STRING)
    private EntityStatus status;

    @Column(nullable = false)
//    @CreationTimestamp
    @CreatedDate
    private LocalDateTime createdDate;

//    @UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime updatedDate;

    private LocalDateTime deletedDate;

    @PrePersist
    private void prePersistWork() {
        this.status = EntityStatus.ACTIVE;
    }

    protected void updateStatus(EntityStatus updatedStatus) {
        this.status = updatedStatus;
    }

    protected void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    protected void setDeletedDate() {
        setDeletedDate(LocalDateTime.now());
    }

    protected boolean isSoftDeleted() {
        return this.status.equals(EntityStatus.SOFT_DELETED);
    }
}
