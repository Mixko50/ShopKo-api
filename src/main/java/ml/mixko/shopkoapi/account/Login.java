package ml.mixko.shopkoapi.account;


import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class Login {
    @GetMapping(path = "/login")
    public Map<String, Object> login(@RequestParam String username, String password){
        Map<String, Object> login = new HashMap<>();

        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
            statement.setString(1,username);
            statement.setString(2,password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                System.out.println("Success");
                login.put("isLoginSuccess", true);
                login.put("username", username);
                login.put("password", password);
                login.put("email",rs.getString("email"));
                login.put("phone_number",rs.getString("phone_number"));
                login.put("firstname",rs.getString("firstname"));
                login.put("lastname",rs.getString("lastname"));
                login.put("gender",rs.getString("gender"));
                login.put("birthdate",rs.getDate("birthdate"));
            }else {
                System.out.println("Fail");
                login.put("isLoginSuccess", false);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        return login;
    }
}
