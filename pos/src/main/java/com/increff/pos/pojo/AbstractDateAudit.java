package com.increff.pos.pojo;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;

@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractDateAudit {
    //    updation time stamp lagane se creation time stamp nhi kaam kar rha hai
//    @CreationTimestamp
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @Version
    private Long version;
}