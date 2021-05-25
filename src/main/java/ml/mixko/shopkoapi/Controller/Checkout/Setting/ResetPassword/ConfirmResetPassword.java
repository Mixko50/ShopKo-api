package ml.mixko.shopkoapi.Controller.Checkout.Setting.ResetPassword;

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
public class ConfirmResetPassword {
    @PostMapping("/resetPassword/confirm")
    public Map<String, Object> confirmChangePassword(@CookieValue String jwt, @RequestBody ChangePasswordDTO changepassword){
        Map<String, Object> res = new HashMap<>();
        try {
            String userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `password` = ? WHERE `user`.`id` = ?");
            preparedStatement.setString(1,changepassword.getNewPassword());
            preparedStatement.setInt(2,Integer.parseInt(userId));
            preparedStatement.execute();
            res.put("isChanged" ,true);
            System.out.println("Reset password successfully");
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            res.put("isChanged" ,false);
            e.printStackTrace();
        }
        return res;
    }
}
