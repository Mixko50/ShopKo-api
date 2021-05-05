package ml.mixko.shopkoapi.account;

import ml.mixko.shopkoapi.DTO.Logindto;
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
    public Map<String, Object> login(@RequestBody Logindto loginInformation, HttpServletResponse response){
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
            }else {
                System.out.println("Login Fail");
                login.put("isLoginSuccess", false);
            }
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
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/profile")
    public Map<String, Object> profile(@CookieValue String jwt){
        Map<String, Object> res = new HashMap<>();
        String userid;
        try {
            userid = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            preparedStatement.setInt(1,Integer.parseInt(userid));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            res.put("username",rs.getString("username"));
            res.put("firstname",rs.getString("firstname"));
            res.put("lastname",rs.getString("lastname"));
            res.put("phone",rs.getString("phone_number"));
            res.put("email",rs.getString("email"));
            res.put("gender",rs.getString("gender"));
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            res.put("birthdate", sdf.format(rs.getDate("birthdate")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response){
        try {
            Cookie cookie = new Cookie("jwt",null);
            cookie.setPath("/");
            response.addCookie(cookie);
            System.out.println("Logout!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}