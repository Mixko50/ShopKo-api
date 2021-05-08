package ml.mixko.shopkoapi.setting;

import ml.mixko.shopkoapi.DTO.ChangePasswordDTO;
import ml.mixko.shopkoapi.DTO.ProfileDTo;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.PagedResultsControl;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setting")
public class ResetPassword {
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/resetPassword")
    public Map<String, Object> ResetPassword(@RequestBody ProfileDTo profile, HttpServletResponse response){
        Map<String, Object> res = new HashMap<>();
        try {
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM user WHERE email = ?");
            preparedStatement.setString(1, profile.getEmail());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                System.out.println("Confirm email checked!");
                res.put("email", true);
                String jwt = JWTUtil.generateResetPasswordToken(rs.getInt("id")+"");
                Cookie cookie = new Cookie("jwt",jwt);
                response.addCookie(cookie);
                cookie.setPath("/resetpassword");
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom("mixko@mixko.ml");
                msg.setTo(profile.getEmail());
                msg.setSubject("Reset password for ShopKo");
                msg.setText("http://localhost:8080/setting/resetPassword/"+jwt);
                javaMailSender.send(msg);
            } else {
                res.put("email", false);
                return res;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    @GetMapping("/resetPassword/{token}")
    public void fromEmail(@PathVariable("token") String jwt, HttpServletResponse response){
        String userId = JWTUtil.parseResetPasswordToken(jwt);
        Cookie cookie = new Cookie("resetPassword",jwt);
        System.out.println("HEllO");
        response.addCookie(cookie);
        response.setHeader("Location", "http://localhost:3000/resetpassword");
        response.setStatus(302);
        cookie.setPath("/");
    }

    @PostMapping("/resetPassword/confirm")
    public Map<String, Object> confirmChangePassword(@CookieValue String jwt, @RequestBody ChangePasswordDTO changepassword){
        Map<String, Object> res = new HashMap<>();
        try {
            String userId = JWTUtil.parseToken(jwt);
            Connection connection = MySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `password` = ? WHERE `user`.`id` = ?");
            preparedStatement.setString(1,changepassword.getNewPassword());
            preparedStatement.setInt(2,Integer.parseInt(userId));
            preparedStatement.execute();
            res.put("isChanged" ,true);
            System.out.println("Reset password successfully");
        } catch (Exception e){
            res.put("isChanged" ,false);
            e.printStackTrace();
        }
        return res;
    }
}
