package ml.mixko.shopkoapi.Controller.Checkout.ShippingCode;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shippingcode")
public class CheckShippngCode {
    @GetMapping("/check")
    public Map<String, Object> checkShippingCode(@RequestParam String shippingCode){
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shipping_code WHERE code = ?");
            preparedStatement.setString(1,shippingCode);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                if (rs.getInt("quantity") <= 0){
                    res.put("isFound", false);
                    return res;
                }
                res.put("isFound", true);
            } else {
                res.put("isFound", false);
            }
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            res.put("isFound", false);
            e.printStackTrace();
        }
        return res;
    }
}
