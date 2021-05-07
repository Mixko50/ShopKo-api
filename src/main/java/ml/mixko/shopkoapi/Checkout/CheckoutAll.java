package ml.mixko.shopkoapi.Checkout;

import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/checkout")
public class CheckoutAll {
    public static final SimpleDateFormat time = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    @PostMapping("/all/confirm")
    public Map<String, Object> confirmOrderAll(@CookieValue String jwt, @RequestParam int addressId, @RequestParam int paymentId, @RequestParam int discount){
        Map<String, Object> res = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            String userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (id, user_id, timestamp , address_id, payment_id, discount) VALUES (NULL, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, Integer.parseInt(userId));
            preparedStatement.setString(2, time.format(timestamp));
            preparedStatement.setInt(3, addressId);
            preparedStatement.setInt(4, paymentId);
            preparedStatement.setInt(5, discount);
            preparedStatement.execute();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int generatedKey = rs.getInt(1);
            preparedStatement = connection.prepareStatement("SELECT * FROM cart_item WHERE user_id = ?");
            preparedStatement.setInt(1,Integer.parseInt(userId));
            rs = preparedStatement.executeQuery();
            PreparedStatement preparedStatementPrice;
            PreparedStatement preparedStatementInsertProduct;
            ResultSet rsPrice;
            while (rs.next()) {
                preparedStatementPrice = connection.prepareStatement("SELECT * FROM `cart_item` INNER JOIN product ON product.id = product_id");
                rsPrice = preparedStatementPrice.executeQuery();
                rsPrice.next();
                preparedStatementInsertProduct = connection.prepareStatement("INSERT INTO order_item (id, product_id, quantity, price, order_id, user_id) VALUES (NULL, ?, ?, ?, ?, ?)");
                preparedStatementInsertProduct.setInt(1,rs.getInt("product_id"));
                preparedStatementInsertProduct.setInt(2,rs.getInt("quantity_pick"));
                double totalPrice = rsPrice.getDouble("price")*rs.getInt("quantity_pick");
                preparedStatementInsertProduct.setDouble(3,totalPrice);
                preparedStatementInsertProduct.setInt(4,generatedKey);
                preparedStatementInsertProduct.setInt(5,Integer.parseInt(userId));
                preparedStatementInsertProduct.execute();
            }
            preparedStatement = connection.prepareStatement("DELETE FROM cart_item WHERE user_id = ?");
            preparedStatement.setInt(1,Integer.parseInt(userId));
            preparedStatement.execute();
            res.put("order",true);
            System.out.println("Confirm order all successfully");
            return res;
        } catch (Exception e){
            res.put("order",false);
            e.printStackTrace();
        }
        return res;
    }
}
