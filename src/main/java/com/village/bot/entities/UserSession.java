package com.village.bot.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_sessions")
@Data
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "chat_id")
    private long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "last_user_action")
    private String lastUserAction;

    @Column(name = "user_auth_status")
    private boolean isAuthorized = false;
}
