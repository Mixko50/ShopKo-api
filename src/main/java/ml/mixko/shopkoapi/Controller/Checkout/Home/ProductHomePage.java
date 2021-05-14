package ml.mixko.shopkoapi.Controller.Checkout.Home;

import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.PostMapping;
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
public class ProductHomePage {
    @PostMapping("/product")
    public Map<String, Object> productHomePage() {
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from product");
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Map<String, Object>> arrProduct = new ArrayList<>();
            while (rs.next()){
                Map<String, Object> product = new HashMap<>();
                product.put("id",rs.getInt("id"));
                product.put("title",rs.getString("title"));
                product.put("price",rs.getString("price"));
                product.put("sold",rs.getString("sold"));
                product.put("img",rs.getString("image"));
                arrProduct.add(product);
            }
            res.put("product",arrProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
