package org.ergea.orders.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ergea.orders.entity.base.BaseDate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
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

    @Column(name = "user_id")
    private String userId;
}
