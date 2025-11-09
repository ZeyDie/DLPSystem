package com.zeydie.dlpsystem.logger.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "logs_computers")
public class ComputerLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private @NotNull UUID computerId;
    @Column(nullable = false)
    private @NotNull String ip;
    @Column(nullable = false)
    private @NotNull String log;
    @CreationTimestamp
    @Column(nullable = false)
    private @Nullable LocalDateTime createdAt;
}