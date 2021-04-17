package ml.mixko.shopkoapi.account;


import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class Signup {
    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String username, @RequestParam String phone, @RequestParam String password, @RequestParam String email) {
        Map<String, Object> res = new HashMap<>();

        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `user` (`id`, `firstname`, `lastname`, `username`, `phone_number`, `gender`, `birthdate`, `password`, `email`, `user_pic`, `type`) VALUES (NULL, ?, ?, ?, ?, NULL, NULL, ?, ?, NULL, 'normal') ");
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, email);
            preparedStatement.execute();
            res.put("success", true);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                res.put("success", false);
                res.put("note", "Duplicate entry");
            } else {
                e.printStackTrace();
                res.put("success", false);
                res.put("note", "Error");
            }

        }
        return res;
    }

    @PostMapping("/checkusername")
    public Map<String, Object> checkusername(@RequestParam String username) {
        Map<String, Object> check = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                if (rs.getString("username").equals(username)){
                    check.put("checkUsername",true);
                }
            } else {
                check.put("checkUsername",false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            check.put("checkUsername", false);
        }
        return check;
    }

    @PostMapping("/checkemail")
    public Map<String, Object> checkemail(@RequestParam String email) {
        Map<String, Object> check = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                if (rs.getString("email").equals(email)){
                    check.put("checkEmail", true);
                }
            } else {
                check.put("checkEmail", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            check.put("checkEmail", false);
        }
        return check;
    }
}
