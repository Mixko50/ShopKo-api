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
public class UpdateAddress {
    @PostMapping("/address/update")
    public void addressSetting(@RequestBody AddressSettingDTO address){
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE address SET name = ?, province = ?, district = ?, sub_district = ?, post = ?, house_number = ?, details = ?, phone_number = ? WHERE address.id = ?");
            preparedStatement.setString(1,address.getName());
            preparedStatement.setString(2,address.getProvince());
            preparedStatement.setString(3,address.getDistrict());
            preparedStatement.setString(4,address.getSub_district());
            preparedStatement.setString(5,address.getPost());
            preparedStatement.setString(6,address.getHouse_number());
            preparedStatement.setString(7,address.getDetails());
            preparedStatement.setString(8,address.getPhone());
            preparedStatement.setInt(9,address.getId());
            preparedStatement.execute();
            System.out.println(address.getId());
            System.out.println("Update address successfully");
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
