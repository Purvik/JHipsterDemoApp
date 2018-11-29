package com.purvik.jhipdemo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Classroom entity. This class is used in ClassroomResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /classrooms?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassroomCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter roomNo;

    private LongFilter roomStandard;

    private LongFilter teacherId;

    public ClassroomCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(LongFilter roomNo) {
        this.roomNo = roomNo;
    }

    public LongFilter getRoomStandard() {
        return roomStandard;
    }

    public void setRoomStandard(LongFilter roomStandard) {
        this.roomStandard = roomStandard;
    }

    public LongFilter getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(LongFilter teacherId) {
        this.teacherId = teacherId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassroomCriteria that = (ClassroomCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(roomNo, that.roomNo) &&
            Objects.equals(roomStandard, that.roomStandard) &&
            Objects.equals(teacherId, that.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        roomNo,
        roomStandard,
        teacherId
        );
    }

    @Override
    public String toString() {
        return "ClassroomCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (roomNo != null ? "roomNo=" + roomNo + ", " : "") +
                (roomStandard != null ? "roomStandard=" + roomStandard + ", " : "") +
                (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
            "}";
    }

}
