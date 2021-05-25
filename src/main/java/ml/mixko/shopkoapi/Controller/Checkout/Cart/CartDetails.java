package ml.mixko.shopkoapi.Controller.Checkout.Cart;

import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartDetails {

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
            res.put("productInCart",count);
            res.put("total",total);
            res.put("information",arrayList);
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }

}
