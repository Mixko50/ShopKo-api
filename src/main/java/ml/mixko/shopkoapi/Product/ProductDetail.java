package ml.mixko.shopkoapi.Product;

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
@RequestMapping("/products")
public class ProductDetail {
    @GetMapping("/details")
    public Map<String, Object> productDetails(@RequestParam int id){
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                System.out.println(rs.getString("title"));
                System.out.println("Found product!");
                res.put("isFound",true);
                res.put("id",rs.getInt("id"));
                res.put("title",rs.getString("title"));
                res.put("price",rs.getDouble("price"));
                res.put("sold",rs.getInt("sold"));
                res.put("quantity",rs.getInt("quantity"));
                res.put("image",rs.getString("image"));
                res.put("category_id",rs.getInt("category_id"));
                res.put("details",rs.getString("details"));
            } else {
                res.put("isFound",false);
            }
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }
}
