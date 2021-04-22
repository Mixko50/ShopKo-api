package ml.mixko.shopkoapi.account;

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
                System.out.println("Login Success");
                login.put("isLoginSuccess", true);
                login.put("token",jwt);
                Cookie cookie = new Cookie("jwt",jwt);
                response.addCookie(cookie);
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
            if (rs.getString("user_pic") == "" || rs.getString("user_pic") == null){
                res.put("profilepic","https://storage.googleapis.com/shopko/user/pink_default_img.jpeg");
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

    @PostMapping("/address")
    public Map<String, Object> address(@CookieValue String jwt){
        Map<String, Object> res = new HashMap<>();
        String userid;
        try {
            userid = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM address WHERE user_id = ?");
            preparedStatement.setInt(1,Integer.parseInt(userid));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            if (rs.getString("name") == null){
                res.put("address",false);
            } else {
                res.put("address",true);
                res.put("name",rs.getString("name"));
                res.put("province",rs.getString("province"));
                res.put("district",rs.getString("district"));
                res.put("sub_district",rs.getString("sub_district"));
                res.put("post",rs.getString("post"));
                res.put("house_number",rs.getString("house_number"));
                res.put("details",rs.getString("details"));
                res.put("phone",rs.getString("phone_number"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

}
