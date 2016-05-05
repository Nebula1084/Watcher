package em.watcher.conroller;

import em.watcher.ManageStatus;
import em.watcher.Sessionable;
import em.watcher.user.PassMatcher;
import em.watcher.user.User;
import em.watcher.user.UserService;
import em.watcher.user.UserValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;


import javax.validation.Valid;

@Controller
public class UserController implements Sessionable {
    @Autowired
    UserService userService;
    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    PassMatcher passMatcher;

    @InitBinder({"user"})
    public void initBinder(DataBinder binder) {
        binder.addValidators(new UserValidator());
    }

    @RequestMapping(value = {"/login.do"}, method = {RequestMethod.POST}
    )
    public String login(@Valid @ModelAttribute("user") User user, BindingResult result,
                        @ModelAttribute(Sessionable.Status) ManageStatus status, Model model) {
        if (result.hasErrors()) {
            this.logger.info(result);
            status.status = 2;
            return "redirect:/login.html";
        } else {
            User temp = this.userService.login(user);
            if (temp != null) {
                this.logger.info(temp + " login sucessed");
                status.status = 0;
                model.addAttribute(Sessionable.User, temp);
                return "redirect:/index.html";
            } else {
                this.logger.info(user + " login failed");
                status.status = 1;
                return "redirect:/login.html";
            }
        }
    }

    @RequestMapping(value = {"/register.do"}, method = {RequestMethod.POST})
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result,
                           @ModelAttribute(Sessionable.Status) ManageStatus status, Model model) {
        if (result.hasErrors()) {
            this.logger.info(result);
            status.status = 2;
            return "redirect:/register.html";
        } else {
            User temp = this.userService.register(user);
            if (temp != null) {
                this.logger.info(temp + " register successes");
                status.status = 0;
                model.addAttribute(Sessionable.User, temp);
                return "redirect:/index.html";
            } else {
                this.logger.info(user + " register failed");
                status.status = ManageStatus.account_aldeady_exist;
                return "redirect:/register.html";
            }
        }
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logout(@ModelAttribute(Sessionable.User) User user, SessionStatus status) {
        status.setComplete();
        return "redirect:/";
    }


    @RequestMapping(value = "/modify.do", method = RequestMethod.POST)
    public String modify(@ModelAttribute(Sessionable.User) User user, @ModelAttribute(Sessionable.Status) ManageStatus status,
                         @RequestParam("org") String orgP, @RequestParam("new") String newP) {
        if (passMatcher.validate(orgP)) {
            status.status = ManageStatus.invalid_char;
            return "redirect:/profile.html";
        }
        if (passMatcher.validate(orgP)) {
            status.status = ManageStatus.invalid_char;
            return "redirect:/profile.html";
        }
        if (user.getPassword().equals(orgP)) {
            user.setPassword(newP);
            userService.save(user);
            this.logger.info(user + " modify password successes");
        } else {
            status.status = ManageStatus.account_or_password_error;
            this.logger.info(user + " modify password failed");
        }
        return "redirect:/profile.html";
    }
}
