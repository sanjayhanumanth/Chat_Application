package com.live.chat_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "chat_group")
@NoArgsConstructor
@AllArgsConstructor
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Lob
    @Column(name = "group_image")
    private byte[] image;

    @Lob
    @Column(name = "cover_image")
    private byte[] coverImage;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "modified_at")
    private Timestamp modifiedAT;

    @Column(name = "modified_by")
    private Long modifiedBy;
}
