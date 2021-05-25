package ml.mixko.shopkoapi.Controller.Checkout.Setting.PaymentSetting;

import ml.mixko.shopkoapi.Model.PaymentDTO;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/setting")
public class DeletePayment {
    @PostMapping("/payment/delete")
    public void deleteCard(@RequestBody PaymentDTO payment){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM payment WHERE payment.id = ?");
            preparedStatement.setInt(1,payment.getId());
            preparedStatement.execute();
            System.out.println("Delete payment successfully");
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
