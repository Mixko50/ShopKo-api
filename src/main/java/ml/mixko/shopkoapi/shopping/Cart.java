package ml.mixko.shopkoapi.shopping;


import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    @GetMapping("/details")
    public Map<String, Object> details(@CookieValue String jwt){
        Map<String, Object> res = new HashMap<>();
        try {
            String id = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cart_item WHERE user_id = ?");
            preparedStatement.setInt(1,Integer.parseInt(id));
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
            int count = 0;
            System.out.println("Cart counting!");
            while (rs.next()){
                res.put("isFound",true);
                count++;
            }
            System.out.println(count);
            res.put("productInCart",count);
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }
}
