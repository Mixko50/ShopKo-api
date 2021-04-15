package ml.mixko.shopkoapi.shopping;


import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class Cart {
    @PostMapping("/addtocart")
    public Map<String, Object> addtocart(@RequestParam int id, @RequestParam int product, @RequestParam int quantity) {
        Map<String, Object> res = new HashMap<>();
        try {
            System.out.println("Connected!");
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO cart_item (user_id, product_id, quantity) VALUES (?, ?, ?)");
            preparedStatement.setInt(1,id);
            preparedStatement.setInt(2,product);
            preparedStatement.setInt(3,quantity);
            preparedStatement.execute();
            res.put("Success",true);
        } catch (Exception e){
            e.printStackTrace();
            res.put("Success", false);
        }
        return res;
    }
}
