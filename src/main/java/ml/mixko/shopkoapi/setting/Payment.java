package ml.mixko.shopkoapi.setting;

import ml.mixko.shopkoapi.DTO.PaymentDTO;
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
@RequestMapping("/setting")
public class Payment {
    @PostMapping("/payment")
    public Map<String, Object> payment(@CookieValue String jwt) {
        Map<String, Object> res = new HashMap<>();
        String userId;
        try {
            userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM payment WHERE user_id = ?");
            preparedStatement.setInt(1, Integer.parseInt(userId));
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Map<String, Object>> arrPayment = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString("name")==null){
                    res.put("payment", false);
                    return res;
                } else {
                    res.put("payment", true);
                    Map<String, Object> user = new HashMap<>();
                    user.put("card_number", rs.getString("card_number"));
                    user.put("type", rs.getString("type"));
                    user.put("id", rs.getString("id"));
                    arrPayment.add(user);
                }
                res.put("information", arrPayment);
            }
        } catch (Exception e) {
            res.put("payment", false);
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/payment/add")
    public void addCard(@CookieValue String jwt,@RequestBody PaymentDTO payment){
        String userId;
        try {
            userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `payment` (`id`, `name`, `card_number`, `type`, `expired_month`, `expired_year`, `user_id`) VALUES (NULL, ? , ? , ? , ? , ?, ?)");
            preparedStatement.setString(1,payment.getName());
            preparedStatement.setString(2, payment.getCardNumber());
            preparedStatement.setString(3,payment.getType());
            preparedStatement.setString(4, payment.getMonth());
            preparedStatement.setString(5,payment.getYear());
            preparedStatement.setInt(6,Integer.parseInt(userId));
            preparedStatement.execute();
            System.out.println("Add payment successfully");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/payment/delete")
    public void deleteCard(@RequestBody PaymentDTO payment){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM payment WHERE payment.id = ?");
            preparedStatement.setInt(1,payment.getId());
            preparedStatement.execute();
            System.out.println("Delete payment successfully");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
