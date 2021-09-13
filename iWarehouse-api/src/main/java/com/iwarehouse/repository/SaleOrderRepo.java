package com.iwarehouse.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iwarehouse.model.SaleOrder;

@Repository
@Transactional
public interface SaleOrderRepo extends JpaRepository<SaleOrder,Integer> {
	
	// Get All Sale Orders
	@Query(value = "SELECT * FROM sale_order ORDER BY order_date DESC, order_id DESC", nativeQuery = true)
	public List<SaleOrder> getAllOrders();

	// Get All Sale Orders for Current User
	@Query(value = "SELECT * FROM sale_order WHERE created_user = ? ORDER BY order_date DESC, order_id DESC", nativeQuery = true)
	public List<SaleOrder> getUserOrders(String uName);

	// Get Sale Order by orderID
	@Query(value = "SELECT * FROM sale_order WHERE order_id = ?", nativeQuery = true)
	public SaleOrder getOrderById(int orderId);

	// Get Sale Order by customerName (or if contains part of the name)
	@Query(value = "SELECT * FROM sale_order WHERE LOWER(customer_name) LIKE %?% ORDER BY customer_name ASC, order_id ASC", nativeQuery = true)
	public List<SaleOrder> getOrderByCustomer(String customerName);

	// Update Sale Order Total by orderID
	@Modifying
	@Query(value = "UPDATE sale_order SET order_total = ? WHERE order_id = ?", nativeQuery = true)
	public void updateOrderTotal(double orderTotal, int orderId);

	// Update Sale Order Cost by orderID
	@Modifying
	@Query(value = "UPDATE sale_order SET order_cost = ? WHERE order_id = ?", nativeQuery = true)
	public void updateOrderCost(double orderCost, int orderId);

	// Update Sale Order createdUser & createdBy by orderID
	@Modifying
	@Query(value = "UPDATE sale_order SET created_user = ?, created_by = ? WHERE order_id = ?", nativeQuery = true)
	public void changeCreated(String createdUser, String createdBy, int orderId);

	// Update Sale Order updatedBy by orderID
	@Modifying
	@Query(value = "UPDATE sale_order SET updated_by = ? WHERE order_id = ?", nativeQuery = true)
	public void changeUpdated(String updatedBy, int orderId);

	// Delete Sale Order by orderID
	@Modifying
	@Query(value = "DELETE FROM sale_order WHERE order_id = ?", nativeQuery = true)
	public void cancelOrder(int orderId);
	
}
