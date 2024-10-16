package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class CrudController {

    private final UserService userService;

    private final RoleService roleService;

    private final UserValidator userValidator;

    @Autowired
    public CrudController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "table_of_users";
    }

    @GetMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getListOfRoles());
        return "form_for_user";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getListOfRoles());
            return "form_for_user";
        }
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/updateInfo")
    public String updateUser(@RequestParam(name = "userId") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getListOfRoles());
        return "form_for_user";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(name = "userId") Long id) {
        userService.remove(id);
        return "redirect:/admin";
    }
}
