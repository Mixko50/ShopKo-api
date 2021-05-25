package ml.mixko.shopkoapi.Controller.Checkout.Setting.PasswordSetting;

import ml.mixko.shopkoapi.Model.ChangePasswordDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setting")
public class ChangePassword {
    @PostMapping("/changepassword/change")
    public Map<String, Object> changepassword(@CookieValue String jwt, @RequestBody ChangePasswordDTO password){
        Map<String, Object> res = new HashMap<>();
        String userID;
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `password` = ? WHERE `user`.`id` = ?");
            userID = JWTUtil.parseToken(jwt);
            preparedStatement.setString(1,password.getNewPassword());
            preparedStatement.setInt(2,Integer.parseInt(userID));
            preparedStatement.execute();
            System.out.println("password changed!");
            res.put("isChanged",true);
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            res.put("isChanged",false);
            e.printStackTrace();
        }
        return res;
    }
}
