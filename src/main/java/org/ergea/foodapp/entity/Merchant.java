package org.ergea.foodapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Table(name = "merchant")
@NoArgsConstructor
@Data
public class Merchant {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "merchant_name")
    private String name;

    @Column(name = "merchant_location")
    private String location;

    @Column(name = "open")
    private Boolean isOpen;
}
