package ml.mixko.shopkoapi.Controller.Checkout.Setting.PaymentSetting;

import ml.mixko.shopkoapi.Model.PaymentDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/setting")
public class AddPayment {
    @PostMapping("/payment/add")
    public void addCard(@CookieValue String jwt, @RequestBody PaymentDTO payment){
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

}
