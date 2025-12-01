package com.java.m3.demo.controller;

import com.java.m3.demo.model.Menu;
import com.java.m3.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/api/menu")
    @ResponseBody
    public List<Menu> getAllMenu() {
        return restaurantService.getAllMenu();
    }

    @GetMapping(value = {"/", "/index", "/home"})
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "page", defaultValue = "1") int page) {

        int pageSize = 12;

        Page<Menu> menuPage = restaurantService.searchMenus(keyword, category, page, pageSize);
        
        List<String> categories = restaurantService.getAllCategories();

        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("categories", categories);
        
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", menuPage.getTotalPages());
        model.addAttribute("totalItems", menuPage.getTotalElements());
        
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentCategory", category);

        return "shop";
    }
}