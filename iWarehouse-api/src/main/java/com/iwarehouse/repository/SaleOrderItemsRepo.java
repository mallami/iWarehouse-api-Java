package com.iwarehouse.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iwarehouse.model.SaleOrderItems;

@Repository
@Transactional
public interface SaleOrderItemsRepo extends JpaRepository<SaleOrderItems,Integer> {
	
	// Get All Order Items by orderID
	@Query(value = "SELECT * FROM sale_order_items WHERE order_id = ? ORDER BY item_seq ASC", nativeQuery = true)
	public List<SaleOrderItems> getAllOrderItems(int orderId);
	
	// Calculate Order Items Total by orderID
	@Query(value = "SELECT SUM(subtotal) FROM sale_order_items WHERE order_id = ?", nativeQuery = true)
	public Double calculateOrderTotal(int orderId);
	
	// Calculate Order Items Cost by orderID
	@Query(value = "SELECT SUM(sold_qty * cost_price) FROM sale_order_items WHERE order_id = ?", nativeQuery = true)
	public Double calculateOrderCost(int orderId);
	
	// Get One Item from Order Items
	@Query(value = "SELECT * FROM sale_order_items WHERE item_seq = ?", nativeQuery = true)
	public SaleOrderItems getOrderItem(int itemSEQ);
	
	// UPDATE soldQty, unitPrice, salePrice & subtotal
	@Modifying
	@Query(value = "UPDATE sale_order_items SET sold_qty = ?, unit_price = ?, sale_price = ?, subtotal = ? WHERE item_seq = ?", nativeQuery = true)
	public void updateOrderItem(int soldQty, double unitPrice, double salePrice, double subtotal, int itemSEQ);

	// Remove Order Item
	@Modifying
	@Query(value = "DELETE FROM sale_order_items WHERE item_seq = ?", nativeQuery = true)
	public void removeOrderItem(int itemSEQ);

	// Remove All Order Items
	@Modifying
	@Query(value = "DELETE FROM sale_order_items WHERE order_id = ?", nativeQuery = true)
	public void removeAllOrderItems(int orderId);

}
