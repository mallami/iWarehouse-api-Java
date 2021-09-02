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

import com.electronics.model.SaleOrder;
import com.electronics.service.SaleOrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "order")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@AllArgsConstructor
public class SaleOrderController {
	
	private SaleOrderService saleOrderService;
	
	// ADD NEW SALE ORDER
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "addNewOrder.iwh")
	public SaleOrder addSaleOrder(HttpServletRequest req, @RequestBody SaleOrder saleOrder) {
		return saleOrderService.addSaleOrder(saleOrder);
	}
	
	// GET ALL SALE ORDERS
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listAllOrders.iwh")
	public List<SaleOrder> getAllSaleOrders(HttpServletRequest req) {
		return saleOrderService.getAllSaleOrders();
	}
	
	// GET ALL SALE ORDERS FOR CURRENT USER
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listCurrentUserOrders.iwh")
	public List<SaleOrder> getUserSaleOrders(HttpServletRequest req) {
		return saleOrderService.getUserSaleOrders(UserController.uName);
	}
	
	// GET SALE ORDER BY ID
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getOrder.iwh:Id={orderId}")
	public SaleOrder getOrderById(HttpServletRequest req, @PathVariable int orderId) {
		return saleOrderService.getOrderById(orderId);
	}
	
	// GET SALE ORDER BY CUSTOMER NAME (OR PART OF THE NAME)
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getOrder.iwh:customerName={customerName}")
	public List<SaleOrder> getOrderByCustomer(HttpServletRequest req, @PathVariable String customerName) {
		return saleOrderService.getOrderByCustomer(customerName);
	}
	
	// UPDATE SALE ORDER
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "updateOrder.iwh:Id={orderId}")
	public SaleOrder updateOrder(HttpServletRequest req, @RequestBody SaleOrder saleOrder, @PathVariable int orderId) {
		return saleOrderService.updateOrder(saleOrder);
	}
	
	// CANCEL SALE ORDER
	@DeleteMapping(value = "cancelOrder.iwh:Id={orderId}")
	public SaleOrder cancelOrder(HttpServletRequest req, HttpServletResponse resp, @PathVariable int orderId) {
		SaleOrder saleOrder = null;
		
		if(saleOrderService.cancelOrder(orderId)) {
			resp.setStatus(200);  // Order cancelled successfully
			saleOrder = saleOrderService.getOrderById(orderId);
			
			if(!saleOrderService.getUserPermissions().getOrderCost()) {
				saleOrder.setOrderCost(null);
			}
		} else {
			resp.setStatus(406);  // Cannot cancel without deleting items - will change to cannot cancel due to payment or pick up
		}		
		
		return saleOrder;
	}
	
}
