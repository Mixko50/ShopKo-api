package ml.mixko.shopkoapi.Controller.Checkout.Setting.PasswordSetting;

import ml.mixko.shopkoapi.Model.ChangePasswordDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setting")
public class CheckPassword {
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

}
