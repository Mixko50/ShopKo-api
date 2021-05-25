package ml.mixko.shopkoapi.Controller.Checkout.Setting.AddressSetting;

import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setting")
public class AddressPage {
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
            ArrayList<Map<String,Object>> arrAddress = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString("name") == null) {
                    res.put("address", false);
                    return res;
                } else {
                    Map<String, Object> user = new HashMap<>();
                    res.put("address", true);
                    user.put("name", rs.getString("name"));
                    user.put("province", rs.getString("province"));
                    user.put("district", rs.getString("district"));
                    user.put("sub_district", rs.getString("sub_district"));
                    user.put("post", rs.getString("post"));
                    user.put("house_number", rs.getString("house_number"));
                    user.put("details", rs.getString("details"));
                    user.put("phone", rs.getString("phone_number"));
                    user.put("id",rs.getInt("id"));
                    arrAddress.add(user);
                }
                res.put("information",arrAddress);
            }
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
