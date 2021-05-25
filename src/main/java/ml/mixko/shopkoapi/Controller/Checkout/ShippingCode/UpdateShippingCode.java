package ml.mixko.shopkoapi.Controller.Checkout.ShippingCode;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
@RequestMapping("/shippingcode")
public class UpdateShippingCode {
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
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
