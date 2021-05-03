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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `cart_item` INNER JOIN product ON product.id = product_id WHERE user_id = ?");
            preparedStatement.setInt(1,Integer.parseInt(id));
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
            int count = 0;
            System.out.println("Cart counting!");
            double total = 0;
            while (rs.next()){
                res.put("isFound",true);
                Map<String, Object> product = new HashMap<>();
                product.put("title",rs.getString("title"));
                product.put("total_price",rs.getDouble("price")*rs.getInt("quantity_pick"));
                product.put("price",rs.getDouble("price"));
                product.put("quantity_pick",rs.getInt("quantity_pick"));
                product.put("id",rs.getInt("cart_item.id"));
                product.put("image",rs.getString("image"));
                arrayList.add(product);
                total+=rs.getDouble("price")*rs.getInt("quantity_pick");
                count++;
            }
            System.out.println(count);
            res.put("productInCart",count);
            res.put("total",total);
            res.put("information",arrayList);
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }

    @DeleteMapping("/delete")
    public void deleteCart(@RequestParam int id){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement =  connection.prepareStatement("DELETE FROM cart_item WHERE id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            System.out.println("Delete item in cart successfully");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
