package com.live.chat_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;
@Entity
@Table(name = "chat_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String emailId;

    @Column(name = "password")
    private String password;

    @Lob
    @Column(name = "user_image")
    private byte[] image;

    @Lob
    @Column(name = "cover_image")
    private byte[] coverImage;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="title")
    private String title;

    @Column(name="display_name")
    private String displayName;

    @Column(name="status")
    private String status;

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

    @OneToOne
    @JoinColumn(name="role_id_fk")
    private Role role;




}
