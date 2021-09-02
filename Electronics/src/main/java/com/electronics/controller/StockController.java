package com.electronics.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.electronics.model.Stock;
import com.electronics.service.StockService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "stock")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@AllArgsConstructor
public class StockController {
	
	private StockService stockService;
	
	// ADD NEW STOCK ITEM
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "addNewStockItem.iwh")
	public Stock addStockItem(HttpServletRequest req, @RequestBody Stock stock) {		
		stock = stockService.addStockItem(stock);
		return stock;
	}
	
	// GET ALL STOCK ITEMS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listAllStockItems.iwh")
	public List<Stock> getAllStockItems(HttpServletRequest req) {
		return stockService.getAllStockItems();
	}
	
	// GET ACTIVE STOCK ITEMS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listActiveStockItems.iwh")
	public List<Stock> getActiveStockItems(HttpServletRequest req) {
		return stockService.getActiveStockItems();
	}	
	
	// GET INACTIVE STOCK ITEMS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listInactiveStockItems.iwh")
	public List<Stock> getInactiveStockItems(HttpServletRequest req) {
		return stockService.getInactiveStockItems();
	}	
	
	// GET STOCK ITEM BY ID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getStockItem.iwh:Id={itemId}")
	public Stock getItemById(HttpServletRequest req, @PathVariable int itemId) {
		return stockService.getItemById(itemId);
	}
	
	// GET STOCK ITEM BY NAME (OR PART OF THE NAME)
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listAllStockItems.iwh:Contains={itemName}")
	public List<Stock> getItemByName(HttpServletRequest req, @PathVariable String itemName) {
		return stockService.getItemByName(itemName);
	}
	
	// GET STOCK ITEM BY NAME (OR PART OF THE NAME) - ACTIVE ONLY
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listActiveStockItems.iwh:Contains={itemName}")
	public List<Stock> getActiveItemByName(HttpServletRequest req, @PathVariable String itemName) {
		return stockService.getActiveItemByName(itemName);
	}
	
	// GET HOW MANY ORDERS OF STOCK ITEM BY ID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "count/{itemId}")
	public Integer getItemCount(HttpServletRequest req, @PathVariable int itemId) {
		return stockService.getItemCount(itemId);
	}
	
	// UPDATE STOCK ITEM
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "updateStockItem.iwh:Id={itemId}")
	public Stock updateStockItem(HttpServletRequest req, @RequestBody Stock stock, @PathVariable int itemId) {
		stock = stockService.updateStockItem(stock);
		return stock;
	}

	// UPDATE STOCK ITEM SALE PRICE BY PERCENT & ITEM ID
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "updateSalePrice.iwh:Id={itemId}&Percent={percent}")
	public Stock updateSalePrice(HttpServletRequest req, @PathVariable int itemId, @PathVariable double percent) {
		double salePercent = 0.0;
		if(percent > 0) salePercent = percent / 100.0;

		return stockService.updateSalePrice(salePercent, itemId);
	}

	// SAVE STOCK ITEM SALE PRICE BY ITEM ID
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "saveSalePrice.iwh:Id={itemId}&Sale={salePrice}")
	public Stock saveSalePrice(HttpServletRequest req, @PathVariable int itemId, @PathVariable double salePrice) {
		return stockService.saveSalePrice(salePrice, itemId);
	}

	// DELETE STOCK ITEM
	@DeleteMapping(value = "deleteStockItem.iwh:Id={itemId}")
	public Stock deleteStockItem(HttpServletRequest req, HttpServletResponse resp, @PathVariable int itemId) {
		Stock stock = null;
		
		if(stockService.deleteStockItem(itemId)) {
			resp.setStatus(200);  // Stock Item deleted successfully
			stock = stockService.getItemById(itemId);
		} else {
			resp.setStatus(406);  // Stock item not deleted because it is associated with some sale orders
		}
		
		return stock;
	}

}
