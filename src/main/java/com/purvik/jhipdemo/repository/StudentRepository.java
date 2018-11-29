package com.purvik.jhipdemo.repository;

import com.purvik.jhipdemo.domain.Student;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByClassroomId(Long id);

}
