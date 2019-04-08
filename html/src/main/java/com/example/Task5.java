package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * victor
 */
@Controller
@RequestMapping(value = "/task5")
public class Task5 {

    @RequestMapping(value = "/")
    @ResponseBody
    public String index() {
        Page writer = new Page();
        writer.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задача 5</li></ol>");
        writer.println("<h1>Проверка линейного кода на корректность</h1>");
        writer.println("<form action='/task5/execute' method='get'>");
        writer.println("<div class='form-group'><label for='n_input1'>n</label><input type='text' class='form-control' name='n' id='n_input' /></div>");
        writer.println("<div class='form-group'><label for='k_input1'>k</label><input type='text' class='form-control' name='k' id='k_input' /></div>");
        writer.println("<div class='form-group'><label for='d_input1'>d</label><input type='text' class='form-control' name='d' id='d_input' /></div>");
        writer.println("<div class='form-group'><label for='q_input1'>q</label><input type='text' class='form-control' name='q' id='q_input' value='2' /></div>");
        writer.println("<button type='submit' class='btn btn-default'>Проверить код</button>");
        writer.println("</form>");
        return writer.toString();
    }

    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    @ResponseBody
    public String execute(@RequestParam("n") int n, @RequestParam("k") int k, @RequestParam("d") int d, @RequestParam("q") int q) {
        Page writer = new Page();
        writer.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задача 5</li></ol>");
        writer.println("<h1>Проверка линейного кода на корректность</h1>");
        writer.println("<p>Требуется проверить существование линейного " + q + "-ичного кода с параметрами n = " + n + ", k = " + k + ", d = " + d + ".</p>");
        {
            writer.println("<h2>Граница Грайсмера</h2>");
            writer.formula("N(k, d) \\geq \\sum_{i=0}^{k-1} \\bigg \\lceil \\frac{d}{2^i} \\bigg \\rceil");
            int sum = 0;
            for (int i = 0; i <= k - 1; ++i) {
                sum += Math.ceil(d / Math.pow(2, i));
            }
            boolean ok = n >= sum;

            if (ok) {
                writer.success("Верно: " + n + " >= " + sum + "");
            } else {
                writer.fail("Неверно: " + n + " >= " + sum + "");
            }
        }

        {
            writer.println("<h2>Условие для всех линейных кодов</h2>");
            writer.formula("\\frac{n2^{k-1}}{2^k-1} \\geq d");
            double left = n * Math.pow(2, k - 1) / (Math.pow(2, k) - 1);
            boolean ok = n * Math.pow(2, k - 1) / (Math.pow(2, k) - 1) >= d;
            if (ok) {
                writer.success("Верно: " + left + " >= " + d + "");
            } else {
                writer.fail("Неверно: " + left + " >= " + d + "");
            }
        }

        {
            writer.println("<h2>Граница Варшамова-Гилберта</h2>");
            writer.formula("q^{n-k} > \\sum_{i=0}^{d-2}{\\binom{n-1}{i}(q-1)^i}");
            long left = (long) Math.pow(q, n - k);
            long sum = 0;
            for (int i = 0; i <= d - 2; ++i) {
                sum += Utils.C(n - 1, i) * Math.pow(q - 1, i);
            }
            boolean ok = left > sum;
            //writer.println("Граница Варшамова-Гилберта: " + Math.pow(q, n - k) + " >= " + sum + " " + ok);
            if (ok) {
                writer.success("Верно: " + left + " >= " + sum + "");
            } else {
                writer.fail("Неверно: " + left + " >= " + sum + "");
            }
        }

        {
            writer.println("<h2>Граница Хэмминга</h2>");
            writer.formula("q^k \\leq \\frac{q^n}{\\displaystyle  \\sum_{i=0}^{t}{\\binom{n}{i}(q-1)^i}}, t=\\bigg\\lfloor\\frac{d-1}{2}\\bigg\\rfloor");
            int t = (d - 1) / 2;
            long left = (long) Math.pow(q, k);
            long sum = 0;
            for (int i = 0; i <= t; ++i) {
                sum += Utils.C(n, i) * Math.pow(q - 1, i);
            }
            double right = Math.pow(q, n) / sum;
            boolean ok = left <= right;
            if (ok) {
                writer.success("Верно: " + left + " <= " + right + "");
            } else {
                writer.fail("Неверно: " + left + " <= " + right + "");
            }
        }
        return writer.toString();
    }
}
