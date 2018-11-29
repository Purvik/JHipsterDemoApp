package com.purvik.jhipdemo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Classroom entity.
 */
public class ClassroomDTO implements Serializable {

    private Long id;

    @NotNull
    private Long roomNo;

    @NotNull
    private Long roomStandard;

    private Set<TeacherDTO> teachers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Long roomNo) {
        this.roomNo = roomNo;
    }

    public Long getRoomStandard() {
        return roomStandard;
    }

    public void setRoomStandard(Long roomStandard) {
        this.roomStandard = roomStandard;
    }

    public Set<TeacherDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<TeacherDTO> teachers) {
        this.teachers = teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassroomDTO classroomDTO = (ClassroomDTO) o;
        if (classroomDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classroomDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassroomDTO{" +
            "id=" + getId() +
            ", roomNo=" + getRoomNo() +
            ", roomStandard=" + getRoomStandard() +
            "}";
    }
}
