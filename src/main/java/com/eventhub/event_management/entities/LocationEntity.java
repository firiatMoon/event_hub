package com.eventhub.event_management.entities;

import jakarta.persistence.*;


import java.util.Objects;

@Entity
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "description")
    private String description;

    public LocationEntity() {
    }

    public LocationEntity(Long id, String name, String address, Integer capacity, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Location{name=%s, address=%s, capacity=%s}", name, address, capacity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LocationEntity locationEntity = (LocationEntity) obj;
        return Objects.equals(locationEntity.getName(), name) &&
                Objects.equals(locationEntity.getAddress(), address) &&
                capacity.compareTo(locationEntity.capacity) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(name, address, capacity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
