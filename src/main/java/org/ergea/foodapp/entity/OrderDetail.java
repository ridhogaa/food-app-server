package org.ergea.foodapp.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ergea.foodapp.entity.base.BaseDate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class OrderDetail extends BaseDate {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private Double totalPrice;
}


