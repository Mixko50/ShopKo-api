package ml.mixko.shopkoapi.Controller.Checkout.Setting.AddressSetting;

import ml.mixko.shopkoapi.Model.AddressSettingDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/setting")
public class AddAddress {
    @PostMapping("/address/add")
    public void addAddress(@CookieValue String jwt, @RequestBody AddressSettingDTO address){
        String userId;
        try {
            userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `address` (`id`, `name`, `province`, `district`, `sub_district`, `post`, `house_number`, `details`, `phone_number`, `user_id`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1,address.getName());
            preparedStatement.setString(2,address.getProvince());
            preparedStatement.setString(3,address.getDistrict());
            preparedStatement.setString(4,address.getSub_district());
            preparedStatement.setString(5,address.getPost());
            preparedStatement.setString(6,address.getHouse_number());
            preparedStatement.setString(7,address.getDetails());
            preparedStatement.setString(8,address.getPhone());
            preparedStatement.setInt(9,Integer.parseInt(userId));
            preparedStatement.execute();
            System.out.println("Add address successfully");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
