package com.iwarehouse.service;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.iwarehouse.controller.UserController;
import com.iwarehouse.model.AccessGroup;
import com.iwarehouse.model.Stock;
import com.iwarehouse.repository.StockRepo;
import com.iwarehouse.repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StockService {
	
	private StockRepo stockRepo;
	private UserRepo userRepo;
	
	// GET USER PERMISSIONS
	public AccessGroup getUserPermissions() {
		return userRepo.getUserByUsername(UserController.uEmail).getAccessGroup();
	}
	
	// ADD NEW STOCK ITEM
	public Stock addStockItem(Stock stock) {
		stock.setSizeUnit(stock.getSizeUnit().toUpperCase());
		stock.setCreatedBy(UserController.uName + " - Stock Item Added - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		stockRepo.save(stock);
		
		if(!getUserPermissions().getCostPrice()) {
			stock.setCostPrice(null);
		}

		return stock;
	}
	
	// GET ALL STOCK ITEMS
	public List<Stock> getAllStockItems() {
		List<Stock> stockList = stockRepo.getAllItems();
		
		if(!getUserPermissions().getCostPrice()) {
			for(int i = 0; i < stockList.size(); i++) {
				stockList.get(i).setCostPrice(null);
			}
		}
		
		return stockList;
	}
	
	// GET ACTIVE STOCK ITEMS
	public List<Stock> getActiveStockItems() {
		List<Stock> stockList = stockRepo.getActiveItems();
		
		if(!getUserPermissions().getCostPrice()) {
			for(int i = 0; i < stockList.size(); i++) {
				stockList.get(i).setCostPrice(null);
			}
		}
		
		return stockList;
	}
		
	// GET INACTIVE STOCK ITEMS
	public List<Stock> getInactiveStockItems() {
		List<Stock> stockList = stockRepo.getInactiveItems();
		
		if(!getUserPermissions().getCostPrice()) {
			for(int i = 0; i < stockList.size(); i++) {
				stockList.get(i).setCostPrice(null);
			}
		}
		
		return stockList;
	}
		
	// GET STOCK ITEM BY ID
	public Stock getItemById(int itemId) {
		Stock stock = stockRepo.getItemById(itemId);
		
		if(!getUserPermissions().getCostPrice()) {
			stock.setCostPrice(null);
		}

		return stock;
	}
		
	// GET STOCK ITEM BY NAME
	public List<Stock> getItemByName(String itemName) {
		List<Stock> stockList = stockRepo.getItemByName(itemName);
		
		if(!getUserPermissions().getCostPrice()) {
			for(int i = 0; i < stockList.size(); i++) {
				stockList.get(i).setCostPrice(null);
			}
		}
		
		return stockList;
	}
	
	// GET STOCK ITEM BY NAME (OR PART OF THE NAME) - ACTIVE ONLY
	public List<Stock> getActiveItemByName(String itemName) {
		List<Stock> stockList = stockRepo.getActiveItemByName(itemName);
		
		if(!getUserPermissions().getCostPrice()) {
			for(int i = 0; i < stockList.size(); i++) {
				stockList.get(i).setCostPrice(null);
			}
		}
		
		return stockList;
	}
	
	// GET HOW MANY ORDERS OF STOCK ITEM BY ID
	public Integer getItemCount(int itemId) {
		return stockRepo.getItemCount(itemId);
	}
		
	// UPDATE STOCK ITEM
	public Stock updateStockItem(Stock stock) {
		stock.setSizeUnit(stock.getSizeUnit().toUpperCase());
		stock.setUpdatedBy(UserController.uName + " - Stock Item Updated - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		stockRepo.save(stock);
		return stock;
	}

	// UPDATE STOCK ITEM QTY
	public void updateStockQty(int itemQTY, int itemId) {
		stockRepo.updateStockQty(itemQTY, itemId);
	}
	
	// UPDATE STOCK ITEM SELLING PRICE BY PERCENT & ITEM ID
	public Stock updateSellingPrice(double sellingPercent, int itemId) {
		double costPrice = stockRepo.getItemById(itemId).getCostPrice();
		double sellingPrice = stockRepo.getItemById(itemId).getSellingPrice();
		double salePrice = stockRepo.getItemById(itemId).getSalePrice();
		double salePercent = 1 - (salePrice / sellingPrice);
		
		double profitAmount = costPrice * sellingPercent;
		sellingPrice = costPrice + profitAmount;		
		if(sellingPercent == 0) sellingPrice = costPrice;
		
		// Update Selling Price
		sellingPrice = Math.round(sellingPrice * 100.0) / 100.0;
		stockRepo.updateSellingPrice(sellingPrice, itemId);
		
		double saleAmount = sellingPrice * salePercent;
		salePrice = sellingPrice - saleAmount;		
		if(salePercent == 0) salePrice = 0;
		
		// Update Sale Price based on new value of Selling Price
		salePrice = Math.round(salePrice * 100.0) / 100.0;
		stockRepo.updateSalePrice(salePrice, itemId);

		Stock stock = stockRepo.getItemById(itemId);		
		stock.setUpdatedBy(UserController.uName + " - Set Selling Price as " + (sellingPercent * 100) + "% - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		stockRepo.save(stock);

		if(!getUserPermissions().getCostPrice()) {
			stock.setCostPrice(null);
		}

		return stock;
	}
	
	// UPDATE STOCK ITEM SALE PRICE BY PERCENT & ITEM ID
	public Stock updateSalePrice(double salePercent, int itemId) {
		double sellingPrice = stockRepo.getItemById(itemId).getSellingPrice();
		double saleAmount = sellingPrice * salePercent;
		double salePrice = sellingPrice - saleAmount;		
		if(salePercent == 0) salePrice = 0;
		
		salePrice = Math.round(salePrice * 100.0) / 100.0;
		stockRepo.updateSalePrice(salePrice, itemId);
		
		Stock stock = stockRepo.getItemById(itemId);		
		stock.setUpdatedBy(UserController.uName + " - Set Sale Price as " + (salePercent * 100) + "% - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		stockRepo.save(stock);

		if(!getUserPermissions().getCostPrice()) {
			stock.setCostPrice(null);
		}

		return stock;
	}
	
	// SAVE STOCK ITEM SELLING PRICE BY ITEM ID
	public Stock saveSellingPrice(double sellingPrice, int itemId) {
		sellingPrice = Math.round(sellingPrice * 100.0) / 100.0;
		stockRepo.updateSellingPrice(sellingPrice, itemId);
		
		Stock stock = stockRepo.getItemById(itemId);		
		stock.setUpdatedBy(UserController.uName + " - Selling Price Adjusted - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		stockRepo.save(stock);

		if(!getUserPermissions().getCostPrice()) {
			stock.setCostPrice(null);
		}

		return stock;
	}

	// SAVE STOCK ITEM SALE PRICE BY ITEM ID
	public Stock saveSalePrice(double salePrice, int itemId) {
		salePrice = Math.round(salePrice * 100.0) / 100.0;
		stockRepo.updateSalePrice(salePrice, itemId);
		
		Stock stock = stockRepo.getItemById(itemId);		
		stock.setUpdatedBy(UserController.uName + " - Sale Price Adjusted - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		stockRepo.save(stock);

		if(!getUserPermissions().getCostPrice()) {
			stock.setCostPrice(null);
		}

		return stock;
	}

	// DELETE STOCK ITEM
	public Boolean deleteStockItem(int itemId) {
		boolean resp = false;
		if(stockRepo.getItemCount(itemId) == 0) {
			stockRepo.deleteStockItem(itemId);
			resp = true;
		}
		
		return resp;
	}
	
}
