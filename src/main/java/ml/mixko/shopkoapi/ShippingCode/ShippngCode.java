package ml.mixko.shopkoapi.ShippingCode;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shippingcode")
public class ShippngCode {
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
        } catch (Exception e){
            res.put("isFound", false);
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/update")
    public void updateCode(@RequestParam String shippingCode){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shipping_code WHERE code = ?");
            preparedStatement.setString(1,shippingCode);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int quantity = rs.getInt("quantity");
            preparedStatement = connection.prepareStatement("UPDATE shipping_code SET quantity = ? WHERE code = ?");
            preparedStatement.setInt(1,quantity-1);
            preparedStatement.setString(2,shippingCode);
            preparedStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
