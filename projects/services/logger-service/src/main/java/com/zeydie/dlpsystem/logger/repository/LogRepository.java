package com.zeydie.dlpsystem.logger.repository;

import com.zeydie.dlpsystem.logger.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}