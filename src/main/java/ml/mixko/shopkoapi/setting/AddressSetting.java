package ml.mixko.shopkoapi.setting;

import ml.mixko.shopkoapi.DTO.AddressSettingDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/setting")
public class AddressSetting {
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

        } catch (Exception e){
            e.printStackTrace();
        }
    }

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