package com.purvik.jhipdemo.repository;

import com.purvik.jhipdemo.domain.Subject;
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
 * Spring Data  repository for the Teacher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query(value = "select distinct teacher from Teacher teacher left join fetch teacher.subjects",
        countQuery = "select count(distinct teacher) from Teacher teacher")
    Page<Teacher> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct teacher from Teacher teacher left join fetch teacher.subjects")
    List<Teacher> findAllWithEagerRelationships();

    @Query("select teacher from Teacher teacher left join fetch teacher.subjects where teacher.id =:id")
    Optional<Teacher> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select t.subjects from Teacher t where t.id = :id")
    HashSet<Subject> findAllByTeacherId(@Param("id")Long id);

}
