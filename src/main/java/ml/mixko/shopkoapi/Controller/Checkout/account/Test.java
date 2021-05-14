package ml.mixko.shopkoapi.Controller.Checkout.account;


import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test1")

public class Test {

    @GetMapping(path = "/login")
    public Map<String, Object> _hello()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");
        ArrayList<Object> arr = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("nickname","Mixko");
        map1.put("firstname","Apisit");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("nickname","hereJiw");
        map2.put("firstname","Jirasin");

        arr.add(map1);
        arr.add(map2);

        arr.add("mixko");
        arr.add("hereJiw");
        map.put("name",arr);
        return map;
    }

    @GetMapping(path = "/list")
    public Map<String, Object> _list() throws SQLException {

        Map<String, Object> listName = new HashMap<>();

        try {


        Connection connection = MySQL.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM user");
        listName.put("success", true);

        ArrayList<Map<String,Object>> arrUserId = new ArrayList<>();

        while (resultSet.next()){
            Map<String, Object> user = new HashMap<>();
            user.put("id",resultSet.getString("id"));
            user.put("firstname",resultSet.getString("firstname"));
            user.put("lastname",resultSet.getString("lastname"));
            user.put("username",resultSet.getString("username"));
            user.put("phone_number",resultSet.getString("phone_number"));
            user.put("gender",resultSet.getString("gender"));
            user.put("birthdate",resultSet.getDate("birthdate"));
            user.put("password",resultSet.getString("password"));
            arrUserId.add(user);
        }
            listName.put("user",arrUserId);

        } catch (SQLException e){
            listName.put("success",false);
        }
        return listName;
    }


}
