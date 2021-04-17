package ml.mixko.shopkoapi.account;


import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class Login {
    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
        Map<String, Object> login = new HashMap<>();

        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
            statement.setString(1,username);
            statement.setString(2,password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                String jwt = JWTUtil.generateToken(rs.getInt("id")+"");
                System.out.println("Success");
                login.put("isLoginSuccess", true);
                login.put("token",jwt);
                Cookie cookie = new Cookie("jwt",jwt);
                response.addCookie(cookie);
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
