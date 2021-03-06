package com.iwarehouse.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

import com.iwarehouse.model.SaleOrder;
import com.iwarehouse.service.SaleOrderService;

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
	
	// GET SALE ORDERS FOR TODAY DATE
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listTodayOrders.iwh")
	public List<SaleOrder> getTodayOrders(HttpServletRequest req) {
		return saleOrderService.getTodayOrders();
	}
	
	// GET SALE ORDERS FOR DATE RANGE
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "listDateRangeOrders.iwh:From={dateFrom}&To={dateTo}")
	public List<SaleOrder> getDateRangeOrders(HttpServletRequest req, @PathVariable String dateFrom, @PathVariable String dateTo) {
		try {
			return saleOrderService.getDateRangeOrders(LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
		} catch(DateTimeParseException e) {
			return null;
		}
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
	@GetMapping(value = "getOrders.iwh:customerName={customerName}")
	public List<SaleOrder> getOrdersByCustomerName(HttpServletRequest req, @PathVariable String customerName) {
		return saleOrderService.getOrdersByCustomerName(customerName);
	}
	
	// GET SALE ORDER BY CREATED USER
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getOrders.iwh:createdUser={createdUser}")
	public List<SaleOrder> getOrdersByCreatedUser(HttpServletRequest req, @PathVariable String createdUser) {
		return saleOrderService.getOrdersByCreatedUser(createdUser);
	}
	
	// GET SALE ORDER BY CUSTOMER NAME & CREATED USER   // *** NOT USED
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "getOrders.iwh:customerName={customerName}&createdUser={createdUser}")
	public List<SaleOrder> getOrdersByCustomerNameAndCreatedUser(HttpServletRequest req, @PathVariable String customerName, @PathVariable String createdUser) {
		return saleOrderService.getOrdersByCustomerNameAndCreatedUser(customerName, createdUser);
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
			resp.setStatus(406);  // Will be changed to cannot cancel due to payment or pick up
		}		
		
		return saleOrder;
	}
	
}
