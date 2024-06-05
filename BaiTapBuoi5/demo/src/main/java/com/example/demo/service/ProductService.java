package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    private List<Product> listProd = new ArrayList<>();

    public ProductService(){

   /*     this.listProd.add(new Product(1,"san pham 1","1.jpg",20000));
        this.listProd.add(new Product(2,"san pham 2","2.jpg",40000));*/

    }
    public int getMaxId() {
        return listProd.stream()
                .map(Product::getId) // Map each Product to its id
                .max(Comparator.naturalOrder()) // Get the max id
                .orElse(0); // Provide a default value if the list is empty
    }



    public void add(Product product){
        int id = getMaxId() + 1;
        product.setId(id);
        listProd.add(product);
    }

    public List<Product> GetAll(){
        return listProd;

    }

    public Product get(int id){
        return listProd.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

    }

    public void edit(Product editProduct) {
        Product find = listProd.get(editProduct.getId());
        if(find != null){
            find.setName(editProduct.getName());
            find.setImage(editProduct.getImage());
            find.setPrice(editProduct.getPrice());
        }


    }
}
