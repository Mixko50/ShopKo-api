package ml.mixko.shopkoapi.Controller.Checkout.Setting.ResetPassword;

import ml.mixko.shopkoapi.Model.ChangePasswordDTO;
import ml.mixko.shopkoapi.Model.ProfileDTO;
import ml.mixko.shopkoapi.utils.JWTUtil;
import ml.mixko.shopkoapi.utils.MySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setting")
public class ResetPasswordSendEmail {
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/resetPassword")
    public Map<String, Object> ResetPassword(@RequestBody ProfileDTO profile, HttpServletResponse response){
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
}
