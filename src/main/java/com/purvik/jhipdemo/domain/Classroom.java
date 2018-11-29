package com.purvik.jhipdemo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Classroom.
 */
@Entity
@Table(name = "classroom")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "room_no", nullable = false)
    private Long roomNo;

    @NotNull
    @Column(name = "room_standard", nullable = false)
    private Long roomStandard;

    //owner
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "classroom_teacher",
               joinColumns = @JoinColumn(name = "classrooms_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "teachers_id", referencedColumnName = "id"))
    private Set<Teacher> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomNo() {
        return roomNo;
    }

    public Classroom roomNo(Long roomNo) {
        this.roomNo = roomNo;
        return this;
    }

    public void setRoomNo(Long roomNo) {
        this.roomNo = roomNo;
    }

    public Long getRoomStandard() {
        return roomStandard;
    }

    public Classroom roomStandard(Long roomStandard) {
        this.roomStandard = roomStandard;
        return this;
    }

    public void setRoomStandard(Long roomStandard) {
        this.roomStandard = roomStandard;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public Classroom teachers(Set<Teacher> teachers) {
        this.teachers = teachers;
        return this;
    }

    public Classroom addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getClassrooms().add(this);
        return this;
    }

    public Classroom removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getClassrooms().remove(this);
        return this;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Classroom classroom = (Classroom) o;
        if (classroom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classroom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Classroom{" +
            "id=" + getId() +
            ", roomNo=" + getRoomNo() +
            ", roomStandard=" + getRoomStandard() +
            "}";
    }
}
