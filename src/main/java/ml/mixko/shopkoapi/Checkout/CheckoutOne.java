package ml.mixko.shopkoapi.Checkout;

import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/checkout")
public class CheckoutOne {
    @GetMapping("/one/product")
    public Map<String, Object> checkoutOne(@RequestParam int id){
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                System.out.println("Checkout one");
                res.put("isFound", true);
                res.put("title",rs.getString("title"));
                res.put("price",rs.getDouble("price"));
                res.put("image",rs.getString("image"));
            } else {
                res.put("isFound",false);
            }
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/one/confirm")
    public Map<String, Object> confirmOne(@CookieValue String jwt, @RequestParam int addressId, @RequestParam int paymentId, @RequestParam int discount, @RequestParam int productId, @RequestParam int quantity, @RequestParam double price){
        Map<String, Object> res = new HashMap<>();
        System.out.println(jwt);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            String userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (id, user_id, timestamp, address_id, payment_id, discount) VALUES (NULL, ?, ?, ?, ? , ?)");
            preparedStatement.setInt(1,Integer.parseInt(userId));
            preparedStatement.setString(2,time.format(timestamp));
            preparedStatement.setInt(3,addressId);
            preparedStatement.setInt(4,paymentId);
            preparedStatement.setInt(5,discount);
            preparedStatement.execute();

            preparedStatement = connection.prepareStatement("SELECT id FROM orders WHERE user_id = ? ORDER BY timestamp DESC LIMIT 1");
            preparedStatement.setInt(1,Integer.parseInt(userId));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                res.put("id",rs.getInt("id"));
                int order_id = rs.getInt("id");
                preparedStatement = connection.prepareStatement("INSERT INTO order_item (id, product_id, quantity, price, order_id) VALUES (NULL, ?, ?, ?, ? )");
                preparedStatement.setInt(1,productId);
                preparedStatement.setInt(2,quantity);
                preparedStatement.setDouble(3,price);
                preparedStatement.setInt(4,order_id);
                preparedStatement.execute();
                System.out.println("Confirm order one successfully");
                return res;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static final SimpleDateFormat time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
}
