package com.zeydie.dlpsystem.server.data.entity;

import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "computers")
public class ComputerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID computerId;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String ip;
    @Column(nullable = false)
    private boolean active;
    @Column
    private LocalDateTime lastStartup;
    @UpdateTimestamp
    @Column
    private LocalDateTime lastUpdate;
    @ManyToMany
    @Column
    private List<UserEntity> users = Lists.newArrayList();

    public ComputerEntity(@NonNull final String name, @NonNull final String ip) {
        this.name = name;
        this.ip = ip;
    }
}