package com.iwarehouse.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "access_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private Integer groupId;
	
	@Column(name = "group_name", nullable = false, unique = true)
	private String groupName;

	// SETTINGS
	@Column(name = "settings")
	private Boolean settings;
	
	@Column(name = "users")
	private Boolean users;
	
	@Column(name = "users_readonly")
	private Boolean usersReadOnly;
	
	@Column(name = "groups")
	private Boolean groups;
	
	@Column(name = "groups_readonly")
	private Boolean groupsReadOnly;
	
	// STOCK
	@Column(name = "stock")
	private Boolean stock;
	
	@Column(name = "new_stock")
	private Boolean newStock;
	
	@Column(name = "cost_price")
	private Boolean costPrice;
	
	@Column(name = "cost_price_readonly")
	private Boolean costPriceReadOnly;
	
	@Column(name = "stock_delete")
	private Boolean stockDelete;
	
	@Column(name = "stock_update")
	private Boolean stockUpdate;
	
	@Column(name = "stock_info")
	private Boolean stockInfo;
	
	// SALE ORDER
	@Column(name = "sale_order")
	private Boolean saleOrder;
	
	@Column(name = "all_orders")
	private Boolean allOrders;
	
	@Column(name = "order_cost")
	private Boolean orderCost;
	
	@Column(name = "order_change_price")
	private Boolean orderChangePrice;
	
	@Column(name = "order_delete")
	private Boolean orderDelete;
	
	@Column(name = "order_update")
	private Boolean orderUpdate;
	
	@Column(name = "new_order")
	private Boolean newOrder;

	@Column(name = "order_current_user")
	private Boolean orderCurrentUser;

	@Column(name = "order_other_user_readonly")
	private Boolean orderOtherUserReadOnly;

	@Column(name = "order_other_user_open")
	private Boolean orderOtherUserOpen;

	@Column(name = "order_other_user_find")
	private Boolean orderOtherUserFind;

	@Column(name = "order_info")
	private Boolean orderInfo;
	
}
