package com.purvik.jhipdemo.repository;

import com.purvik.jhipdemo.domain.Subject;
import com.purvik.jhipdemo.domain.Teacher;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the Subject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


    List<Subject> findAllByClassroomId(Long id);

    @Query("select s.teachers from Subject s where s.id = :id")
    Set<Teacher> listTeacherBySubjectId(@Param("id")Long id);

    List<Subject> findByCode(Long codePart);

    List<Subject> findByTitleContainsAllIgnoreCase(String titlePart);

    List<Subject> findByDate(LocalDate datePart);

}
