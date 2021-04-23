package ml.mixko.shopkoapi.setting;

import ml.mixko.shopkoapi.DTO.ChangePasswordDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class ChangePassword {
    @PostMapping("/changepassword/check")
    public Map<String, Object> checkpassword(@CookieValue String jwt, @RequestBody ChangePasswordDTO password){
        Map<String, Object> res = new HashMap<>();
        String userID;
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ? AND password = ?");
            userID = JWTUtil.parseToken(jwt);
            preparedStatement.setInt(1,Integer.parseInt(userID));
            preparedStatement.setString(2,password.getOldPassword());
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("password checked!");
            if (rs.next()){
                System.out.println("password correct");
                res.put("isChecked",true);
            } else {
                System.out.println("password incorrect");
                res.put("isChecked",false);
            }

        } catch (Exception e){
            res.put("isChecked",false);
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/changepassword/change")
    public Map<String, Object> changepassword(@CookieValue String jwt, @RequestBody ChangePasswordDTO password){
        Map<String, Object> res = new HashMap<>();
        String userID;
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `password` = ? WHERE `user`.`id` = ?");
            userID = JWTUtil.parseToken(jwt);
            System.out.println(userID);
            preparedStatement.setString(1,password.getNewPassword());
            preparedStatement.setInt(2,Integer.parseInt(userID));
            preparedStatement.execute();
            System.out.println("password changed!");
            res.put("isChanged",true);

        } catch (Exception e){
            res.put("isChanged",false);
            e.printStackTrace();
        }
        return res;
    }
}
