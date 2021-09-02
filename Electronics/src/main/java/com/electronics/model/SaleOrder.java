package com.electronics.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrder {
	
	@Id
	@SequenceGenerator(name = "orderseq", sequenceName = "order_seq", initialValue = 6101, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderseq")
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "order_date", nullable = false)
	private LocalDate orderDate;
	
	@Column(name = "customer_name", nullable = false)
	private String customerName;
	
	@Column(name = "order_comment")
	private String orderComment;
	
	@Column(name = "pickup_comment")
	private String pickupComment;
	
	@Column(name = "invoice_comment")
	private String invoiceComment;
	
	@Column(name = "order_total")
	private Double orderTotal;
	
	@Column(name = "order_cost")
	private Double orderCost;

	@Column(name = "created_user")
	private String createdUser;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
}
