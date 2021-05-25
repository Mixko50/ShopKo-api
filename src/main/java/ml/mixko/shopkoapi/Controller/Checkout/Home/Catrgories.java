package ml.mixko.shopkoapi.Controller.Checkout.Home;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class Catrgories {
    @GetMapping("/categories")
    public Map<String, Object> category(){
        Map<String, Object> res = new HashMap<>();
        ArrayList<Map<String, Object>> arrCategories = new ArrayList<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM category");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Map<String, Object> user = new HashMap<>();
                user.put("id",rs.getInt("id"));
                user.put("name",rs.getString("name"));
                user.put("image",rs.getString("image"));
                arrCategories.add(user);
            }
            System.out.println("Categories appeared!");
            res.put("information",arrCategories);
            connection.close();
            preparedStatement.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
