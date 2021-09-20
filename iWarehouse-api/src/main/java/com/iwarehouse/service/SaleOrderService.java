package com.iwarehouse.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.iwarehouse.controller.UserController;
import com.iwarehouse.model.AccessGroup;
import com.iwarehouse.model.SaleOrder;
import com.iwarehouse.repository.SaleOrderRepo;
import com.iwarehouse.repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleOrderService {
	
	private SaleOrderRepo saleOrderRepo;
	private SaleOrderItemsService saleOrderItemsService;
	private UserRepo userRepo;
	
	// GET USER PERMISSIONS
	public AccessGroup getUserPermissions() {
		return userRepo.getUserByUsername(UserController.uEmail).getAccessGroup();
	}
	
	// ADD NEW SALE ORDER
	public SaleOrder addSaleOrder(SaleOrder saleOrder) {
		saleOrder.setCreatedUser(UserController.uName);
		saleOrder.setCreatedBy(UserController.uName + " - Sale Order Created - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		saleOrderRepo.save(saleOrder);
		
		if(!getUserPermissions().getOrderCost()) {
			saleOrder.setOrderCost(null);
		}

		return saleOrder;
	}
	
	// GET ALL SALE ORDERS
	public List<SaleOrder> getAllSaleOrders() {
		List<SaleOrder> ordersList = saleOrderRepo.getAllOrders();
		
		if(!getUserPermissions().getOrderCost()) {
			for(int i = 0; i < ordersList.size(); i++) {
				ordersList.get(i).setOrderCost(null);
			}
		}
		
		return ordersList;
	}
	
	// GET ALL SALE ORDERS FOR CURRENT USER
	public List<SaleOrder> getUserSaleOrders(String uName) {
		List<SaleOrder> ordersList = saleOrderRepo.getUserOrders(uName);
		
		if(!getUserPermissions().getOrderCost()) {
			for(int i = 0; i < ordersList.size(); i++) {
				ordersList.get(i).setOrderCost(null);
			}
		}
		
		return ordersList;
	}
	
	// GET SALE ORDER BY ID
	public SaleOrder getOrderById(int orderId) {
		SaleOrder saleOrder = saleOrderRepo.getOrderById(orderId);
		
		if(!getUserPermissions().getOrderCost()) {
			saleOrder.setOrderCost(null);
		}
		
		return saleOrder;
	}
	
	// GET SALE ORDER BY CUSTOMER NAME
	public List<SaleOrder> getOrderByCustomerName(String customerName) {
		List<SaleOrder> ordersList = saleOrderRepo.getOrderByCustomerName(customerName);
		
		if(!getUserPermissions().getOrderCost()) {
			for(int i = 0; i < ordersList.size(); i++) {
				ordersList.get(i).setOrderCost(null);
			}
		}
		
		return ordersList;
	}
	
	// GET SALE ORDER BY CREATED USER
	public List<SaleOrder> getOrderByCreatedUser(String createdUser) {
		List<SaleOrder> ordersList = saleOrderRepo.getOrderByCreatedUser(createdUser);
		
		if(!getUserPermissions().getOrderCost()) {
			for(int i = 0; i < ordersList.size(); i++) {
				ordersList.get(i).setOrderCost(null);
			}
		}
		
		return ordersList;
	}
	
	// GET SALE ORDER BY CUSTOMER NAME & CREATED USER
	public List<SaleOrder> getOrderByCustomerNameAndCreatedUser(String customerName, String createdUser) {
		List<SaleOrder> ordersList = saleOrderRepo.getOrderByCustomerNameAndCreatedUser(customerName, createdUser);
		
		if(!getUserPermissions().getOrderCost()) {
			for(int i = 0; i < ordersList.size(); i++) {
				ordersList.get(i).setOrderCost(null);
			}
		}
		
		return ordersList;
	}
	
	// UPDATE SALE ORDER
	public SaleOrder updateOrder(SaleOrder saleOrder) {
		saleOrder.setUpdatedBy(UserController.uName + " - Order Summary Updated - Date: " + LocalDate.now() + ", Time: " + LocalTime.now());
		saleOrderRepo.save(saleOrder);
		
		if(!getUserPermissions().getOrderCost()) {
			saleOrder.setOrderCost(null);
		}
		
		return saleOrder;
	}
	
	// UPDATE SALE ORDER TOTAL
	public void updateOrderTotal(double orderTotal, int orderId) {
		saleOrderRepo.updateOrderTotal(orderTotal, orderId);
	}
	
	// CANCEL SALE ORDER
	public Boolean cancelOrder(int orderId) {
		boolean resp = false;
		
		double total = saleOrderRepo.getOrderById(orderId).getOrderTotal();
		if(total >= 0) {  // It will be changed to paid==0 & picked==0
			saleOrderItemsService.removeAllOrderItems(orderId);
			saleOrderRepo.cancelOrder(orderId);
			resp = true;
		}
		
		return resp;
	}
	
}
