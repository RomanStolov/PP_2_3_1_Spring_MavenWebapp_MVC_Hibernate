package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping(value = "/people")
public class PeopleController {
    private UserService userService;

    public PeopleController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображение списка всех пользователей
     */
    @GetMapping()
    public String readAllUsers(Model model) {
        List<User> usersList = userService.getAllUsers();
        model.addAttribute("allUsersList", usersList);
        return "index";
    }

    /**
     * Отображение одного пользователя с подробными данными на отдельной форме
     */
    @GetMapping(value = "/{id}")
    public String readUserById(@PathVariable("id") long id, Model model) {
        model.addAttribute("userById", userService.getUser(id));
        return "index-user-by-id";
    }

    /**
     * Добавление нового пользователя с подробными данными на отдельной форме - Шаг 1
     * Создание модели пользователя и вызов отдельной формы для ввода подробных данных нового пользователя
     */
    @GetMapping(value = "/new")
    public String newUser(@ModelAttribute("newUser") User user) {
        return "index-user-new";
    }

    /**
     * Добавление нового пользователя с подробными данными на отдельной форме - Шаг 2
     * Приём введённых подробныхданных нового пользователя с формы и вызов метода из сервиса для
     * сохранения этого пользователя в БД, а затем редирект на форму со списком всех пользователей
     */
    @PostMapping()
    public String createUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/people";
    }

    /**
     * Редактирование пользователя - Шаг 1
     * Приём с формы id редактируемого пользователя, сохранение этого пользователя в модель с
     * последующей передачей на форму редактирования пользователя
     */
    @GetMapping(value = "/{id}/edit")
    public String editUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("editUser", userService.getUser(id));
        return "index-user-edit";
    }

    /**
     * Редактирование пользователя - Шаг 2
     * Приём в запросе с формы id редактируемого пользователя, приём с формы новых данных для этого
     * пользователя, отправка этих данных в метод сервиса для изменения записи в БД и последующий
     * редирект на страницу со списком всех пользователей
     */
    @PatchMapping(value = "/{id}")
    public String updateUser(@PathVariable("id") long id, @ModelAttribute("editUser") User newUser) {
        userService.updateUser(id, newUser);
        return "redirect:/people";
    }

    /**
     * Удаление пользователя и последующий редирект на страницу со списком всех пользователей
     */
    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/people";
    }

}
