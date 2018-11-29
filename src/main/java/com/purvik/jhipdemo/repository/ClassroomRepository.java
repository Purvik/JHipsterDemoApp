package com.purvik.jhipdemo.repository;

import com.purvik.jhipdemo.domain.Classroom;
import com.purvik.jhipdemo.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Classroom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>, JpaSpecificationExecutor<Classroom> {

    @Query(value = "select distinct classroom from Classroom classroom left join fetch classroom.teachers",
        countQuery = "select count(distinct classroom) from Classroom classroom")
    Page<Classroom> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct classroom from Classroom classroom left join fetch classroom.teachers")
    List<Classroom> findAllWithEagerRelationships();

    @Query("select classroom from Classroom classroom left join fetch classroom.teachers where classroom.id =:id")
    Optional<Classroom> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select c.teachers from Classroom c where c.id = :id")
    HashSet<Teacher> listTeacherOfClassId(@Param("id")Long id);




}
