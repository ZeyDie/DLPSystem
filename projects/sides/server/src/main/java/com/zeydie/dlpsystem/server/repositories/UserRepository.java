package com.zeydie.dlpsystem.server.repositories;

import com.zeydie.dlpsystem.server.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}