package ml.mixko.shopkoapi.account;


import ml.mixko.shopkoapi.database.MySQL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class Signup {
    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String username, @RequestParam String phone, @RequestParam String password, @RequestParam String email){
        Map<String, Object> res = new HashMap<>();

        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `user` (`id`, `firstname`, `lastname`, `username`, `phone_number`, `gender`, `birthdate`, `password`, `email`, `user_pic`, `type`) VALUES (NULL, ?, ?, ?, ?, NULL, NULL, ?, ?, NULL, 'normal') ");
            preparedStatement.setString(1,firstname);
            preparedStatement.setString(2,lastname);
            preparedStatement.setString(3,username);
            preparedStatement.setString(4,phone);
            preparedStatement.setString(5,password);
            preparedStatement.setString(6,email);
            res.put("Success",true);
        } catch (Exception e){
            e.printStackTrace();
            res.put("Success",false);
        }

        return res;
    }
}
