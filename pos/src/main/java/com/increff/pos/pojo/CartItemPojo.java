package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cart_items", indexes = @Index(name = "multiIndex", columnList = "productId, counterId"),
        uniqueConstraints={@UniqueConstraint(columnNames={"productId","counterId"})})

@Getter
@Setter

public class CartItemPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;
    @Column( nullable=false)
    private Integer quantity;
    @Column(nullable=false)
    private Integer productId;
    @Column(nullable=false)
    private Double sellingPrice ;
    @Column(nullable=false)
    private Integer counterId;


}
