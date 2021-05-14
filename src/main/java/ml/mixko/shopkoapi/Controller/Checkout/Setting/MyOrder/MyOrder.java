package ml.mixko.shopkoapi.Controller.Checkout.Setting.MyOrder;

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
@RequestMapping("setting")
public class MyOrder {
    @GetMapping("/myorder")
    public Map<String, Object> MyOrder(@CookieValue String jwt){
        Map<String, Object> res = new HashMap<>();
        try {
            String userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product INNER JOIN order_item ON product.id = product_id WHERE order_item.user_id = ?");
            preparedStatement.setInt(1,Integer.parseInt(userId));
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Map<String, Object>> productArrayList = new ArrayList<>();
            while (rs.next()){
                res.put("isFound",true);
                Map<String, Object> product = new HashMap<>();
                product.put("order_item_id",rs.getInt("order_item.id"));
                product.put("image",rs.getString("image"));
                product.put("title",rs.getString("title"));
                product.put("quantity",rs.getInt("order_item.quantity"));
                product.put("total_price",rs.getDouble("order_item.price"));
                product.put("price",rs.getString("product.price"));
                productArrayList.add(product);
            }
            res.put("information",productArrayList);
            return res;
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }
}
