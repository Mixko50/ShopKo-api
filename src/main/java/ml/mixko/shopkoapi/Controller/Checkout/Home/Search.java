package ml.mixko.shopkoapi.Controller.Checkout.Home;

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
@RequestMapping("/home")
public class Search {
    @GetMapping("/product/search")
    public Map<String, Object> Search(@RequestParam String search) {
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            String sql = "SELECT * FROM `product` WHERE title LIKE "+"'%"+search+"%'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Map<String, Object>> arrProduct = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("id", rs.getInt("id"));
                product.put("title", rs.getString("title"));
                product.put("price", rs.getString("price"));
                product.put("sold", rs.getString("sold"));
                product.put("img", rs.getString("image"));
                arrProduct.add(product);
            }
            System.out.println("Product found!");
            res.put("product", arrProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
