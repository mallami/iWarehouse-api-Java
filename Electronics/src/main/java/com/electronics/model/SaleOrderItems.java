package com.electronics.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderItems {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_seq")
	private Integer itemSEQ;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "order_id")
	private SaleOrder saleOrder;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "item_id")	
	private Stock stock;
	
	@Column(name = "sold_qty", nullable = false)
	private Integer soldQTY;
	
	@Column(name = "cost_price")
	private Double costPrice;
	
	@Column(name = "unit_price", nullable = false)
	private Double unitPrice;

	@Column(name = "sale_price")
	private Double salePrice;
	
	@Column(name = "subtotal")
	private Double subtotal;
	
}
