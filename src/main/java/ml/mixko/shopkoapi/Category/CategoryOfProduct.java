package ml.mixko.shopkoapi.Category;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryOfProduct {
    @GetMapping("/categoryproduct")
    public Map<String, Object> category(@RequestParam String category){
        Map<String, Object> res = new HashMap<>();
        System.out.println(category);
        int id;
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE name = ?");
            preparedStatement.setString(1,category);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                id = rs.getInt("id");
                ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
                preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE category_id = ?");
                preparedStatement.setInt(1,id);
                rs = preparedStatement.executeQuery();
                res.put("isFound",true);
                while (rs.next()){
                    Map<String, Object> product = new HashMap<>();
                    product.put("id",rs.getInt("id"));
                    product.put("title",rs.getString("title"));
                    product.put("price",rs.getDouble("price"));
                    product.put("sold",rs.getInt("sold"));
                    product.put("quantity",rs.getInt("quantity"));
                    product.put("image",rs.getString("image"));
                    product.put("category_id",rs.getInt("category_id"));
                    product.put("details",rs.getString("details"));
                    arrayList.add(product);
                }
                res.put("information",arrayList);
            } else {
                res.put("isFound",false);
                return res;
            }
        } catch (Exception e){
            res.put("isFound",false);
            e.printStackTrace();
        }
        return res;
    }
}
