package ml.mixko.shopkoapi.Controller.Checkout.Setting.ResetPassword;

import ml.mixko.shopkoapi.utils.JWTUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/setting")
public class AddCookieForResetPassword {
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

}
