package com.iwarehouse.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.iwarehouse.controller.UserController;
import com.iwarehouse.model.AccessGroup;
import com.iwarehouse.model.SaleOrderItems;
import com.iwarehouse.repository.SaleOrderItemsRepo;
import com.iwarehouse.repository.SaleOrderRepo;
import com.iwarehouse.repository.StockRepo;
import com.iwarehouse.repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleOrderItemsService {
	
	private StockRepo stockRepo;
	private SaleOrderRepo saleOrderRepo;
	private SaleOrderItemsRepo saleOrderItemsRepo;
	private UserRepo userRepo;
	
	// GET USER PERMISSIONS
	public AccessGroup getUserPermissions() {
		return userRepo.getUserByUsername(UserController.uEmail).getAccessGroup();
	}
	
	// ADD NEW ITEM TO SALE ORDER
	public Boolean addOrderItem(SaleOrderItems saleOrderItems) {
		boolean resp = false;
		int orderId = saleOrderItems.getSaleOrder().getOrderId();
		int soldItemId = saleOrderItems.getStock().getItemId();
		String soldItemName = saleOrderItems.getStock().getItemName();
		double unitPrice = saleOrderItems.getUnitPrice();
		double salePrice = saleOrderItems.getSalePrice();
		double costPrice = stockRepo.getItemById(soldItemId).getCostPrice();
		
		// Save cost price from Stock to cost price in SaleOrderItems 
		saleOrderItems.setCostPrice(costPrice);
		
		// Save sale price from Stock to sale price in SaleOrderItems 
		saleOrderItems.setSalePrice(salePrice);
		
		int stockQty = stockRepo.getItemById(soldItemId).getItemQTY();
		int soldQty = saleOrderItems.getSoldQTY();
		
		double stockSalePrice = stockRepo.getItemById(soldItemId).getSalePrice();	
		if(stockSalePrice == 0) salePrice = unitPrice;
		
		if(soldQty > 0 & soldQty <= stockQty & unitPrice >= costPrice) {			
			// Calculate Subtotal
			if(salePrice > 0) {
				saleOrderItems.setSubtotal(soldQty * salePrice);
			} else {
				saleOrderItems.setSubtotal(soldQty * unitPrice);
			}

			// SAVE NEW ITEM
			if(stockSalePrice == 0) saleOrderItems.setSalePrice(0.0);
			saleOrderItemsRepo.save(saleOrderItems);
			
			// Update stock QTY
			int newQty = stockQty - soldQty;
			stockRepo.updateStockQty(newQty, soldItemId);
			
			// Update sale order total & cost
			double orderTotal = saleOrderItemsRepo.calculateOrderTotal(orderId);
			double orderCost = saleOrderItemsRepo.calculateOrderCost(orderId);			
			saleOrderRepo.updateOrderTotal(orderTotal, orderId);
			saleOrderRepo.updateOrderCost(orderCost, orderId);
			
			// Update sale order updatedBy
			saleOrderRepo.changeUpdated(UserController.uName + " - Item [" + soldItemName + "] Added - Date: " + LocalDate.now() + ", Time: " + LocalTime.now(), orderId);
			
			resp = true;
		}
		
		return resp;
	}
	
	// GET ALL SALE ORDER ITEMS BY ORDER ID
	public List<SaleOrderItems> getAllOrderItems(int orderId) {
		List<SaleOrderItems> itemsList = saleOrderItemsRepo.getAllOrderItems(orderId);
		
		if(!getUserPermissions().getOrderCost()) {
			for(int i = 0; i < itemsList.size(); i++) {
				itemsList.get(i).setCostPrice(null);
				itemsList.get(i).getSaleOrder().setOrderCost(null);
				itemsList.get(i).getStock().setCostPrice(null);
			}
		}
		
		return itemsList;
	}
	
	// GET ONE ORDER ITEM
	public SaleOrderItems getOrderItem(int itemSEQ) {
		SaleOrderItems saleOrderItems = saleOrderItemsRepo.getOrderItem(itemSEQ);
		
		if(!getUserPermissions().getOrderCost()) {
			saleOrderItems.setCostPrice(null);
		}

		return saleOrderItems;
	}
	
	// CALCULATE TOTAL OF ORDER ITEMS BY ORDER ID
	public Double calculateOrderTotal(int orderId) {
		return saleOrderItemsRepo.calculateOrderTotal(orderId);
	}
	
	// RETRIEVE CURRENT UNIT PRICE FOR ALL ORDER ITEMS BY ORDER ID
	public void retrieveUnitPrice(int orderId) {
		List<SaleOrderItems> itemsList = new ArrayList<>();
		itemsList = saleOrderItemsRepo.getAllOrderItems(orderId);
		
		int itemId = 0;
		double unitPrice = 0;
		for(SaleOrderItems item : itemsList) {
			itemId = item.getStock().getItemId();
			unitPrice = stockRepo.getItemById(itemId).getSellingPrice();
			updateOrderItem(item.getSoldQTY(), unitPrice, item.getSalePrice(), item.getItemSEQ());
		}
	}

	// RETRIEVE CURRENT SALE PRICE FOR ALL ORDER ITEMS BY ORDER ID
	public void retrieveSalePrice(int orderId) {
		List<SaleOrderItems> itemsList = new ArrayList<>();
		itemsList = saleOrderItemsRepo.getAllOrderItems(orderId);
		
		int itemId = 0;
		double salePrice = 0;
		for(SaleOrderItems item : itemsList) {
			itemId = item.getStock().getItemId();
			salePrice = stockRepo.getItemById(itemId).getSalePrice();
			updateOrderItem(item.getSoldQTY(), item.getUnitPrice(), salePrice, item.getItemSEQ());
		}
	}

	// UPDATE ORDER ITEM (SOLD QTY, UNIT PRICE & SALE PRICE)
	public Boolean updateOrderItem(int soldQty, double unitPrice, double salePrice, int itemSEQ) {
		boolean resp = false;
		double subtotal = 0;
		int orderId = saleOrderItemsRepo.getOrderItem(itemSEQ).getSaleOrder().getOrderId();
		int itemId = saleOrderItemsRepo.getOrderItem(itemSEQ).getStock().getItemId();
		String itemName = saleOrderItemsRepo.getOrderItem(itemSEQ).getStock().getItemName();
		int stockQty = stockRepo.getItemById(itemId).getItemQTY();
		int orderQty = saleOrderItemsRepo.getOrderItem(itemSEQ).getSoldQTY();
		int differenceQty = soldQty - orderQty;	
		double costPrice = stockRepo.getItemById(itemId).getCostPrice();
		
		double stockSalePrice = stockRepo.getItemById(itemId).getSalePrice();	
		if(stockSalePrice == 0) salePrice = unitPrice;
		
		if(soldQty > 0 & differenceQty <= stockQty & unitPrice >= costPrice) {
			// Update stock QTY
			stockRepo.updateStockQty(stockQty - differenceQty, itemId);
			
			// Calculate Subtotal
			if(salePrice > 0) {
				subtotal = soldQty * salePrice;
			} else {
				subtotal = soldQty * unitPrice;
			}

			// UPDATE SOLD QTY, UNIT PRICE, SALE PRICE & SUBTOTAL
			if(stockSalePrice == 0) salePrice = 0;
			saleOrderItemsRepo.updateOrderItem(soldQty, unitPrice, salePrice, subtotal, itemSEQ);
			
			// Update sale order total & cost
			double orderTotal = 0;
			double orderCost = 0;
			if(saleOrderItemsRepo.calculateOrderTotal(orderId) != null) {
				orderTotal = saleOrderItemsRepo.calculateOrderTotal(orderId);
				orderCost = saleOrderItemsRepo.calculateOrderCost(orderId);
				saleOrderRepo.updateOrderTotal(orderTotal, orderId);
				saleOrderRepo.updateOrderCost(orderCost, orderId);
			} 
			
			// Update sale order updatedBy
			saleOrderRepo.changeUpdated(UserController.uName + " - Item [" + itemName + "] Updated - Date: " + LocalDate.now() + ", Time: " + LocalTime.now(), orderId);
			
			resp = true;
		}

		return resp;
	}
	
	// REMOVE ORDER ITEM
	public void removeOrderItem(int itemSEQ) {
		int orderId = saleOrderItemsRepo.getOrderItem(itemSEQ).getSaleOrder().getOrderId();
		int itemId = saleOrderItemsRepo.getOrderItem(itemSEQ).getStock().getItemId();
		String itemName = saleOrderItemsRepo.getOrderItem(itemSEQ).getStock().getItemName();

		// Update stock QTY
		int newQty = stockRepo.getItemById(itemId).getItemQTY() + saleOrderItemsRepo.getOrderItem(itemSEQ).getSoldQTY();
		stockRepo.updateStockQty(newQty, itemId);
		
		// REMOVE ORDER ITEM
		saleOrderItemsRepo.removeOrderItem(itemSEQ);
		
		// Update sale order total & cost
		double orderTotal = 0;
		double orderCost = 0;
		if(saleOrderItemsRepo.calculateOrderTotal(orderId) != null) {
			orderTotal = saleOrderItemsRepo.calculateOrderTotal(orderId);
			orderCost = saleOrderItemsRepo.calculateOrderCost(orderId);
		} 
		saleOrderRepo.updateOrderTotal(orderTotal, orderId);
		saleOrderRepo.updateOrderCost(orderCost, orderId);
		
		// Update sale order updatedBy
		saleOrderRepo.changeUpdated(UserController.uName + " - Item [" + itemName + "] Removed - Date: " + LocalDate.now() + ", Time: " + LocalTime.now(), orderId);
	}

	// REMOVE ALL ORDER ITEMS BY ORDER ID - VOID SALE ORDER
	public void removeAllOrderItems(int orderId) {
		List<SaleOrderItems> itemsList = new ArrayList<>();
		itemsList = saleOrderItemsRepo.getAllOrderItems(orderId);
		
		// Update stock QTY
		int itemId = 0;
		int newQty = 0;
		for(SaleOrderItems item : itemsList) {
			itemId = item.getStock().getItemId();
			newQty = stockRepo.getItemById(itemId).getItemQTY() + item.getSoldQTY();
			stockRepo.updateStockQty(newQty, itemId);			
		}
		
		// REMOVE ALL ORDER ITEMS
		saleOrderItemsRepo.removeAllOrderItems(orderId);		
	}

}
