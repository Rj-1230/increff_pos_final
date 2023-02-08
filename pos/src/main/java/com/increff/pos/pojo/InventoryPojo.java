package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "inventory", indexes = @Index(name = "index1", columnList = "productId"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"productId"})})

@Getter
@Setter

public class InventoryPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;
    @Column(nullable = false)
    private Integer productId;
    @Column(nullable = false)
    private Integer quantity;

}
