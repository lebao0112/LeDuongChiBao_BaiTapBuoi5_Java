package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String index(Model model){
        model.addAttribute("listProduct",productService.GetAll());
        return "products/index";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("product", new Product());
        return "products/create";
    }

    @PostMapping("create")
    public  String create(@Valid Product newProduct,@RequestParam MultipartFile imageProduct , BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("product", new Product());
            return "products/create";
        }

        //save img to static file folder

        if(imageProduct != null && imageProduct.getSize() > 0){
            try{

                File saveFile = new ClassPathResource("static/images").getFile();
                String newImageFile = UUID.randomUUID() + ".png";
                Path path= Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newImageFile);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Product find = productService.get(id);
        model.addAttribute("product", find);
        return "products/edit";
    }
    @PostMapping("/edit")
    public String edit(@Valid Product editProduct, @RequestParam("imageProduct") MultipartFile imageProduct, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("product", editProduct);
            int id = editProduct.getId();
            return "products/edit/" + id;
        }

        if(imageProduct != null && imageProduct.getSize() > 0){

            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + editProduct.getImage());
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        productService.edit(editProduct);
        return "redirect:/products";
    }


}
