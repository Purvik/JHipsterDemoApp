package com.purvik.jhipdemo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SchoolBus entity.
 */
public class SchoolBusDTO implements Serializable {

    private Long id;

    @NotNull
    private Long busNo;

    private String busType;

    @NotNull
    private String busDriverName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNo() {
        return busNo;
    }

    public void setBusNo(Long busNo) {
        this.busNo = busNo;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusDriverName() {
        return busDriverName;
    }

    public void setBusDriverName(String busDriverName) {
        this.busDriverName = busDriverName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SchoolBusDTO schoolBusDTO = (SchoolBusDTO) o;
        if (schoolBusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schoolBusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SchoolBusDTO{" +
            "id=" + getId() +
            ", busNo=" + getBusNo() +
            ", busType='" + getBusType() + "'" +
            ", busDriverName='" + getBusDriverName() + "'" +
            "}";
    }
}
