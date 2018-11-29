package com.purvik.jhipdemo.repository;

import com.purvik.jhipdemo.domain.SchoolBus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SchoolBus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolBusRepository extends JpaRepository<SchoolBus, Long> {

}
