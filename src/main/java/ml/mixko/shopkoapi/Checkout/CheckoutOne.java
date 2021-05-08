package ml.mixko.shopkoapi.Checkout;

import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/checkout")
public class CheckoutOne {
    public static final SimpleDateFormat time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    @GetMapping("/one/product")
    public Map<String, Object> checkoutOne(@RequestParam int id) {
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Checkout one");
                res.put("isFound", true);
                res.put("title", rs.getString("title"));
                res.put("price", rs.getDouble("price"));
                res.put("image", rs.getString("image"));
            } else {
                res.put("isFound", false);
            }
        } catch (Exception e) {
            res.put("isFound", false);
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/one/confirm")
    public Map<String, Object> confirmOne(@CookieValue String jwt, @RequestParam int addressId, @RequestParam int paymentId, @RequestParam int discount, @RequestParam int productId, @RequestParam int quantity, @RequestParam double price) {
        Map<String, Object> res = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            String userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (id, user_id, timestamp, address_id, payment_id, discount) VALUES (NULL, ?, ?, ?, ? , ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, Integer.parseInt(userId));
            preparedStatement.setString(2, time.format(timestamp));
            preparedStatement.setInt(3, addressId);
            preparedStatement.setInt(4, paymentId);
            preparedStatement.setInt(5, discount);
            preparedStatement.execute();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int generatedKey = rs.getInt(1);
            preparedStatement = connection.prepareStatement("INSERT INTO order_item (id, product_id, quantity, price, order_id, user_id) VALUES (NULL, ?, ?, ?, ?, ? )");
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, generatedKey);
            preparedStatement.setInt(5,Integer.parseInt(userId));
            preparedStatement.execute();
            System.out.println("Confirm order one successfully");

            preparedStatement = connection.prepareStatement("SELECT sold FROM product WHERE id = ?");
            preparedStatement.setInt(1,productId);
            rs = preparedStatement.executeQuery();
            rs.next();
            int sold = rs.getInt("sold");
            preparedStatement = connection.prepareStatement("UPDATE product SET sold = ? WHERE id = ?");
            preparedStatement.setInt(1,sold+quantity);
            preparedStatement.setInt(2,productId);
            preparedStatement.execute();
            res.put("order",true);
            return res;
        } catch (Exception e) {
            res.put("order",false);
            e.printStackTrace();
        }
        return res;
    }
}
