package ml.mixko.shopkoapi.Controller.Checkout.Setting.ProfileSetting;

import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class ProfilePage {

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
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
