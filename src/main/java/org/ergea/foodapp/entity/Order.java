package org.ergea.foodapp.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ergea.foodapp.entity.base.BaseDate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "orders")
@AllArgsConstructor
@Data
@Where(clause = "deleted_date is null")
public class Order extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "completed")
    private Boolean isComplete;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
