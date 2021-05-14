package ml.mixko.shopkoapi.Controller.Checkout.account;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/account")
public class Logout {
    @PostMapping("/logout")
    public void logout(HttpServletResponse response){
        try {
            Cookie cookie = new Cookie("jwt",null);
            cookie.setPath("/");
            response.addCookie(cookie);
            System.out.println("Logout!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
