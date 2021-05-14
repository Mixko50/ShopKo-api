package ml.mixko.shopkoapi.Controller.Checkout.Setting.PaymentSetting;

import ml.mixko.shopkoapi.Model.PaymentDTO;
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
public class PaymentPage {
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
}
