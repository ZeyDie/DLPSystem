package com.zeydie.dlpsystem.logger.repository;

import com.zeydie.dlpsystem.logger.entity.ComputerLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerLogRepository extends JpaRepository<ComputerLogEntity, Long> {
}