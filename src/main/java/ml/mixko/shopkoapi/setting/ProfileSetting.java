package ml.mixko.shopkoapi.setting;

import ml.mixko.shopkoapi.DTO.ProfileDTo;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/setting")
public class ProfileSetting {
    @PostMapping( "/profile/firstname")
    public void changeFirstname(@CookieValue String jwt, @RequestBody ProfileDTo profile){
        String userId;
        try {
            userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `firstname` = ? WHERE `user`.`id` = ?");
            preparedStatement.setString(1,profile.getFirstname());
            preparedStatement.setInt(2,Integer.parseInt(userId));
            preparedStatement.execute();
            System.out.println("Change firstname success");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping(path = "/profile/lastname")
    public void changeLastname(@CookieValue String jwt, @RequestBody ProfileDTo profile){
        String userId;
        try {
            userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET lastname = ? WHERE user.id = ?");
            preparedStatement.setString(1,profile.getLastname());
            preparedStatement.setInt(2,Integer.parseInt(userId));
            preparedStatement.executeUpdate();
            System.out.println("Change lastname success");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}