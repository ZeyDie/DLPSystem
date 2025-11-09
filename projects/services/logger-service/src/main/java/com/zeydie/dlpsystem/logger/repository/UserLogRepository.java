package com.zeydie.dlpsystem.logger.repository;

import com.zeydie.dlpsystem.logger.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogEntity, Long> {
}