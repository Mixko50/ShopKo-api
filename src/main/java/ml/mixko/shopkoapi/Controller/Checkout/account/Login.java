package ml.mixko.shopkoapi.Controller.Checkout.account;

import ml.mixko.shopkoapi.Model.LoginDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class Login {
    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestBody LoginDTO loginInformation, HttpServletResponse response){
        Map<String, Object> login = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
            statement.setString(1,loginInformation.getUsername());
            statement.setString(2,loginInformation.getPassword());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                String jwt = JWTUtil.generateToken(rs.getInt("id")+"");
                System.out.println("Login Success");
                login.put("isLoginSuccess", true);
                login.put("token",jwt);
                Cookie cookie = new Cookie("jwt",jwt);
                cookie.setPath("/");
                response.addCookie(cookie);
            }else {
                System.out.println("Login Fail");
                login.put("isLoginSuccess", false);
            }
            connection.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return login;
    }

    @PostMapping("/fetch")
    public Map<String, Object> fetch(@CookieValue String jwt){
        Map<String, Object> res = new HashMap<>();
        String userid;
        try {
            userid = JWTUtil.parseToken(jwt);
        } catch (Exception e){
            res.put("isLoggedIn",false);
            return res;
        }
        res.put("isLoggedIn",true);
        res.put("userId",userid);
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            preparedStatement.setInt(1,Integer.parseInt(userid));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            res.put("username",rs.getString("username"));
            res.put("firstname",rs.getString("firstname"));
            res.put("lastname",rs.getString("lastname"));
            res.put("email",rs.getString("email"));
            res.put("type",rs.getString("type"));
            if (rs.getString("user_pic") == "" || rs.getString("user_pic") == null){
                res.put("profilepic","https://storage.googleapis.com/shopko/user/default_img.JPG");
            } else {
                res.put("profilepic",rs.getString("user_pic"));
            }
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}