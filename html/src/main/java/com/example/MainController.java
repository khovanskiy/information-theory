package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * victor
 */
@Controller
public class MainController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        Page sb = new Page();
        sb.println("<h1>Решение билета по теории кодирования</h1>");
        sb.println("<ul>");
        sb.println("<li><a href='/task12/'>Задания 1 и 2</a></li>");
        sb.println("<li><a href='/task3/'>Задание 3</a></li>");
        sb.println("<li><a href='/task4/'>Задание 4</a></li>");
        sb.println("<li><a href='/task5/'>Задание 5</a></li>");
        sb.println("</ul>");
        return sb.toString();
    }
}
