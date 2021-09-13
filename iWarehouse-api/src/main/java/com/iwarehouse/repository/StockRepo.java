package com.iwarehouse.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iwarehouse.model.Stock;

@Repository
@Transactional
public interface StockRepo extends JpaRepository<Stock,Integer> {

	// Get List of All Stock Items
	@Query(value = "SELECT * FROM stock ORDER BY item_name ASC", nativeQuery = true)
	public List<Stock> getAllItems();
	
	// Get List of Inactive Stock Items
	@Query(value = "SELECT * FROM stock WHERE item_status = false ORDER BY item_name ASC", nativeQuery = true)
	public List<Stock> getInactiveItems();
	
	// Get List of Active Stock Items
	@Query(value = "SELECT * FROM stock WHERE item_status = true ORDER BY item_name ASC", nativeQuery = true)
	public List<Stock> getActiveItems();

	// Get Stock Item by itemID
	@Query(value = "SELECT * FROM stock WHERE item_id = ?", nativeQuery = true)
	public Stock getItemById(int itemId);

	// Get Stock Item by itemName (or if contains part of the name)
	@Query(value = "SELECT * FROM stock WHERE LOWER(item_name) LIKE %?% ORDER BY item_name ASC", nativeQuery = true)
	public List<Stock> getItemByName(String itemName);

	// Get Stock Item by itemName (or if contains part of the name) - Active Only
	@Query(value = "SELECT * FROM stock WHERE LOWER(item_name) LIKE %?% AND item_status = true ORDER BY item_name ASC", nativeQuery = true)
	public List<Stock> getActiveItemByName(String itemName);

	// Get How Many Orders of Stock Item by itemID
	@Query(value = "SELECT COUNT(*) FROM sale_order_items WHERE item_id = ?", nativeQuery = true)
	public Integer getItemCount(int itemId);

	// Update Stock Item QTY by itemID
	@Modifying
	@Query(value = "UPDATE stock SET item_qty = ? WHERE item_id = ?", nativeQuery = true)
	public void updateStockQty(int itemQTY, int itemId);

	// Update Stock Item sellingPrice by itemID
	@Modifying
	@Query(value = "UPDATE stock SET selling_price = ? WHERE item_id = ?", nativeQuery = true)
	public void updateSellingPrice(double sellingPrice, int itemId);

	// Update Stock Item salePrice by itemID
	@Modifying
	@Query(value = "UPDATE stock SET sale_price = ? WHERE item_id = ?", nativeQuery = true)
	public void updateSalePrice(double salePrice, int itemId);

	// Delete Stock Item by itemID
	@Modifying
	@Query(value = "DELETE FROM stock WHERE item_id = ?", nativeQuery = true)
	public void deleteStockItem(int itemId);

}
