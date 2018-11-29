package com.purvik.jhipdemo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SchoolBus.
 */
@Entity
@Table(name = "school_bus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolBus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bus_no", nullable = false)
    private Long busNo;

    @Column(name = "bus_type")
    private String busType;

    @NotNull
    @Column(name = "bus_driver_name", nullable = false)
    private String busDriverName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNo() {
        return busNo;
    }

    public SchoolBus busNo(Long busNo) {
        this.busNo = busNo;
        return this;
    }

    public void setBusNo(Long busNo) {
        this.busNo = busNo;
    }

    public String getBusType() {
        return busType;
    }

    public SchoolBus busType(String busType) {
        this.busType = busType;
        return this;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusDriverName() {
        return busDriverName;
    }

    public SchoolBus busDriverName(String busDriverName) {
        this.busDriverName = busDriverName;
        return this;
    }

    public void setBusDriverName(String busDriverName) {
        this.busDriverName = busDriverName;
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
        SchoolBus schoolBus = (SchoolBus) o;
        if (schoolBus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schoolBus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SchoolBus{" +
            "id=" + getId() +
            ", busNo=" + getBusNo() +
            ", busType='" + getBusType() + "'" +
            ", busDriverName='" + getBusDriverName() + "'" +
            "}";
    }
}
