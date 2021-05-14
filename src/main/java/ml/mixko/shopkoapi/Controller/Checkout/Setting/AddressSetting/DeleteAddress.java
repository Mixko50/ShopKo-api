package ml.mixko.shopkoapi.Controller.Checkout.Setting.AddressSetting;

import ml.mixko.shopkoapi.Model.AddressSettingDTO;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/setting")
public class DeleteAddress {
    @PostMapping("/address/delete")
    public void deleteAddress(@RequestBody AddressSettingDTO address){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM address WHERE address.id = ?");
            preparedStatement.setInt(1,address.getId());
            preparedStatement.execute();
            System.out.println("Delete address successfully");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
