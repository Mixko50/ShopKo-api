package ml.mixko.shopkoapi.Controller.Checkout.Cart;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/cart")
public class DeleteItemInCart {

    @DeleteMapping("/delete")
    public void deleteCart(@RequestParam int id){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement =  connection.prepareStatement("DELETE FROM cart_item WHERE id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            System.out.println("Delete item in cart successfully");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
