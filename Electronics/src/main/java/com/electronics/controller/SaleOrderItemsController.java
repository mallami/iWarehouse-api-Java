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

import com.electronics.model.SaleOrderItems;
import com.electronics.service.SaleOrderItemsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "orderItems")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@AllArgsConstructor
public class SaleOrderItemsController {

	private SaleOrderItemsService saleOrderItemsService;

	// ADD NEW ITEM TO SALE ORDER
	@PostMapping(value = "addNewOrderItem.iwh")
	public SaleOrderItems addOrderItem(HttpServletRequest req, HttpServletResponse resp, @RequestBody SaleOrderItems saleOrderItems) {
		if(saleOrderItemsService.addOrderItem(saleOrderItems)) {
			resp.setStatus(201);  // Item added successfully
			
			if(!saleOrderItemsService.getUserPermissions().getOrderCost()) {
				saleOrderItems.setCostPrice(null);
				saleOrderItems.getSaleOrder().setOrderCost(null);
				saleOrderItems.getStock().setCostPrice(null);
			}
		} else {
			resp.setStatus(406);  // Item not added due to break the logic (sold qty>0, sold qty<=stock qty, unit price>=cost price)
		}
		
		return saleOrderItems;
	}
	
	// GET ALL SALE ORDER ITEMS BY ORDER ID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getAllOrderItems.iwh:orderId={orderId}")
	public List<SaleOrderItems> getAllOrderItems(HttpServletRequest req, @PathVariable int orderId) {
		return saleOrderItemsService.getAllOrderItems(orderId);
	}
	
	// GET ONE ORDER ITEM
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getOrderItem.iwh:Seq={itemSEQ}")
	public SaleOrderItems getOrderItem(HttpServletRequest req, @PathVariable int itemSEQ) {
		return saleOrderItemsService.getOrderItem(itemSEQ);
	}
	
	// RETRIEVE CURRENT UNIT PRICE FOR ALL ORDER ITEMS BY ORDER ID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "retrieveUnitPrice.iwh:Id={orderId}")
	public void retrieveUnitPrice(HttpServletRequest req, @PathVariable int orderId) {
		saleOrderItemsService.retrieveUnitPrice(orderId);
	}
	
	// RETRIEVE CURRENT SALE PRICE FOR ALL ORDER ITEMS BY ORDER ID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "retrieveSalePrice.iwh:Id={orderId}")
	public void retrieveSalePrice(HttpServletRequest req, @PathVariable int orderId) {
		saleOrderItemsService.retrieveSalePrice(orderId);
	}
	
	// UPDATE ORDER ITEM (SOLD QTY, UNIT PRICE, SALE PRICE & SUBTOTAL)
	@PutMapping(value = "updateOrderItem.iwh:Seq={itemSEQ}&soldQty={soldQty}&unitPrice={unitPrice}&salePrice={salePrice}")
	public SaleOrderItems updateOrderItem(HttpServletRequest req, HttpServletResponse resp, @PathVariable int soldQty, @PathVariable double unitPrice, @PathVariable double salePrice, @PathVariable int itemSEQ) {
		SaleOrderItems saleOrderItems = null;
		
		if(saleOrderItemsService.updateOrderItem(soldQty, unitPrice, salePrice, itemSEQ)) {
			resp.setStatus(200);  // Item updated successfully
			saleOrderItems = saleOrderItemsService.getOrderItem(itemSEQ);
			
			if(!saleOrderItemsService.getUserPermissions().getOrderCost()) {
				saleOrderItems.setCostPrice(null);
				saleOrderItems.getSaleOrder().setOrderCost(null);
				saleOrderItems.getStock().setCostPrice(null);
			}
		} else {
			resp.setStatus(406);  // Item not updated due to break the logic (sold qty>0, sold qty<=stock qty, unit price>=cost price)
		}
		
		return saleOrderItems;
	}
	
	// REMOVE ORDER ITEM
	@ResponseStatus(value = HttpStatus.OK)
	@DeleteMapping(value = "removeOrderItem.iwh:Seq={itemSEQ}")
	public SaleOrderItems removeOrderItem(HttpServletRequest req, @PathVariable int itemSEQ) {
		SaleOrderItems saleOrderItems = saleOrderItemsService.getOrderItem(itemSEQ);
		saleOrderItemsService.removeOrderItem(itemSEQ);
		
		if(!saleOrderItemsService.getUserPermissions().getOrderCost()) {
			saleOrderItems.setCostPrice(null);
			saleOrderItems.getSaleOrder().setOrderCost(null);
			saleOrderItems.getStock().setCostPrice(null);
		}

		return saleOrderItems;
	}

}
