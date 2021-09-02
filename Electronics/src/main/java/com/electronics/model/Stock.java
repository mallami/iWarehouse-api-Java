package com.electronics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
	
	@Id
	@SequenceGenerator(name = "stockseq", sequenceName = "stock_seq", initialValue = 101, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockseq")
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "item_name", nullable = false, unique = true)
	private String itemName;
	
	@Column(name = "item_size")
	private Double itemSize;
	
	@Column(name = "size_unit")
	private String sizeUnit;
	
	@Column(name = "item_qty", nullable = false)
	private Integer itemQTY;
	
	@Column(name = "cost_price", nullable = false)
	private Double costPrice;
	
	@Column(name = "selling_price", nullable = false)
	private Double sellingPrice;
	
	@Column(name = "sale_price")
	private Double salePrice;
	
	@Column(name = "item_status", nullable = false)
	private Boolean itemStatus;
	
	@Column(name = "item_comment")
	private String itemComment;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
}
