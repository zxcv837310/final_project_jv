package com.java.m3.demo.controller;

import com.java.m3.demo.model.Cart;
import com.java.m3.demo.model.Order;
import com.java.m3.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    // 1. Hiển thị trang Checkout
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/shop";
        }

        Order order = new Order();
        order.setOrderCode(orderService.generateOrderCode());

        model.addAttribute("order", order);
        model.addAttribute("cart", cart);
        
        return "checkout";
    }

    // 2. Xử lý nút Place Order
    @PostMapping("/place-order")
    public String placeOrder(@ModelAttribute("order") Order order, 
                             HttpSession session, 
                             RedirectAttributes redirectAttributes) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/shop";
        }

        orderService.saveOrder(order, cart);

        session.removeAttribute("cart");

        redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully! Your Order ID is " + order.getOrderCode());

        return "redirect:/shop";
    }
}