package com.example;

import javafx.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * victor
 */
@Controller
@RequestMapping(value = "/task3")
public class Task3 {

    private static final Map<Integer, Polynomial> simple = new HashMap<>();

    static {
        simple.put(2, Polynomial.fill(2, 1, 0));
        simple.put(3, Polynomial.fill(3, 1, 0));
        simple.put(4, Polynomial.fill(4, 1, 0));
        simple.put(5, Polynomial.fill(5, 2, 0));
        simple.put(6, Polynomial.fill(6, 1, 0));

        simple.put(7, Polynomial.fill(7, 3, 0));
        simple.put(8, Polynomial.fill(8, 4, 3, 2, 0));
        simple.put(9, Polynomial.fill(9, 4, 0));
        simple.put(10, Polynomial.fill(10, 3, 0));
        simple.put(11, Polynomial.fill(11, 2, 0));

        simple.put(12, Polynomial.fill(12, 6, 4, 1, 0));
        simple.put(13, Polynomial.fill(13, 4, 3, 1, 0));
        simple.put(14, Polynomial.fill(14, 10, 6, 1, 0));
        simple.put(15, Polynomial.fill(15, 1, 0));
        simple.put(16, Polynomial.fill(16, 12, 3, 1, 0));

        simple.put(17, Polynomial.fill(17, 3, 0));
        simple.put(18, Polynomial.fill(18, 7, 0));
        simple.put(19, Polynomial.fill(19, 5, 2, 1, 0));
        simple.put(20, Polynomial.fill(20, 3, 0));
        simple.put(21, Polynomial.fill(21, 2, 0));

        simple.put(22, Polynomial.fill(22, 1, 0));
        simple.put(23, Polynomial.fill(23, 5, 0));
        simple.put(24, Polynomial.fill(24, 7, 2, 1, 0));
        simple.put(25, Polynomial.fill(25, 3, 0));
        simple.put(26, Polynomial.fill(26, 6, 2, 1, 0));
        simple.put(27, Polynomial.fill(27, 5, 2, 1, 0));
        simple.put(28, Polynomial.fill(28, 3, 0));
        simple.put(29, Polynomial.fill(29, 2, 0));
    }

    @RequestMapping(value = "/")
    @ResponseBody
    public String index() {
        Page writer = new Page();
        writer.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задача 3</li></ol>");
        writer.println("<h1>Построение БЧХ-кода</h1>");
        writer.println("<form action='/task3/execute' method='get'>");
        writer.println("<div class='form-group'><label for='n_input1'>Длина кода n</label><input type='text' class='form-control' name='n' id='n_input' /></div>");
        writer.println("<div class='form-group'><label for='d_input1'>Минимальное расстоние d</label><input type='text' class='form-control' name='d' id='d_input' /></div>");
        writer.println("<div class='form-group'><label for='t_input1'>Количество исправляемых ошибок t</label><input type='text' class='form-control' name='t' id='t_input' /></div>");
        writer.println("<div class='form-group'><label for='q_input1'>Основание кода q</label><input type='text' class='form-control' name='q' id='q_input' value='2' /></div>");
        writer.println("<button type='submit' class='btn btn-default'>Построить код</button>");
        writer.println("</form>");
        return writer.toString();
    }

    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    @ResponseBody
    public String execute(@RequestParam("n") int n, @RequestParam("d") int d, @RequestParam("t") int t, @RequestParam("q") int q) {
        if (n > 100 || d > 100 || q > 32) {
            return "Некорректные параметры";
        }

        List<Group> groups = new ArrayList<>();
        Set<Integer> used = new HashSet<>();
        for (int s = 0; s < n; ++s) {
            if (!used.contains(s)) {
                Group group = new Group();
                group.add(s);
                int b = s * q % n;
                while (!group.contains(b)) {
                    if (!used.contains(b)) {
                        group.add(b);
                    }
                    used.add(b);
                    b = b * q % n;
                }
                groups.add(group);
            }
        }
        Page writer = new Page();
        writer.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задача 3</li></ol>");
        writer.println("<h1>Построение БЧХ-кода</h1>");
        writer.println("<p>Требуется построить БЧХ " + q + "-ичный код с параметрами n = " + n + ", d = " + d + ", исправляющий t = " + t + " ошибок.</p>");
        {
            writer.println("<h2>Циклотомические классы по модулю " + n + "</h2>");
            writer.println("<ul>");
            for (Group group : groups) {
                writer.println("<li>C<sub>" + group.get(0) + "</sub> = " + group + "</li>");
            }
            writer.println("</ul>");
        }
        {
            writer.println("<h2>Минимальные многочлены корней</h2>");
            writer.println("<ul>");
            for (Group group : groups) {
                if (group.get(0) == 0) {
                    writer.println("<li>M<sub>0</sub>(x) = x + 1</li>");
                } else {
                    writer.println("<li>M<sub>" + group.get(0) + "</sub>(x) = " + Utils.concat(group.stream().map(i -> "(x - a<sup>" + i + "</sup>)").collect(Collectors.toList()), "*") + "</li>");
                }
            }
            writer.println("</ul>");
        }
        List<Code> codes = new ArrayList<>();
        {
            writer.println("<h2>БЧХ-коды длины " + n + "</h2>");
            writer.println("<table class='table'>");
            writer.println("<tr><th>Показатели</th><th>Порождающий</th><th>Размерность<br/>n-deg g(x)</th><th>Расстояние</th><th>b</th></tr>");
            int[][] map = Utils.words(groups.size());
            for (int i = 0; i < map.length - 1; ++i) {
                Group g = new Group();
                List<Integer> polynomial = new ArrayList<>();
                for (int j = 0; j < map[i].length; ++j) {
                    if (map[i][j] == 1) {
                        g.addAll(groups.get(j));
                        polynomial.add(groups.get(j).get(0));
                    }
                }
                if (!g.isEmpty()) {
                    Collections.sort(g);
                    Pair<Integer, Integer> result = Utils.seqLength(g);
                    int distance = result.getKey() + 1;
                    int b = result.getValue();
                    assert distance <= n;
                    codes.add(new Code(g, polynomial, n - g.size(), distance, b));
                }
            }
            codes = codes.stream().sorted((lhs, rhs) -> Integer.compare(lhs.distance, rhs.distance)).collect(Collectors.toList());
            for (Code code : codes) {
                printCode(writer, code);
            }
            writer.println("</table>");
        }

        int m = 0;
        int s = 1;
        Polynomial px;
        {
            int acc = 1;
            for (int i = 1; i <= 29; ++i) {
                //System.out.println("m = " + i);
                acc *= 2;
                if (acc - 1 == n) {
                    writer.println("<p>Число n = " + n + " равно числу 2<sup>m</sup> - 1 = " + (acc - 1) + " при m = " + i + "</p>");
                    writer.println("<p>Можно построить примитивный БЧХ код.</p>");
                    m = i;
                    break;
                } else if (Utils.primes(acc - 1).contains(n)) {
                    writer.println("<p>Число n = " + n + " является делителем числа 2<sup>m</sup> - 1 = " + (acc - 1) + " = " + Utils.concat(Utils.primes(acc - 1), "*") + " при m = " + i + "</p>");
                    writer.println("<p>Можно построить непримитивный БЧХ код.</p>");
                    m = i;
                    writer.fail("В непримитивном коде что-то будет считать, но неправильно, т.к. x^n - 1 не делится на подобранный g(x)");

                    boolean foundS = false;
                    for (int j = 1; j <= 29; ++j) {
                        int temp = (int)Math.pow(2, m) - 1;
                        if (temp % n == 0) {
                            foundS = true;
                            s = temp / n;
                        }
                    }
                    if (foundS) {
                        writer.success("<p>Нашли s = " + s + "</p>");
                    } else {
                        writer.success("<p>Не найдено подходящего s. Для корректность оставлено s = 1.</p>");
                    }
                    break;
                }
            }
            if (m != 0) {
                px = simple.get(m);
                assert px != null;
                //boolean isPrimitive = Polynomial.isPrimitive(px, q, n);
                //System.out.println(isPrimitive);
            } else {
                writer.fail("В таблице 6.4 примитивного многочлена нет.");
                return writer.toString();
            }
        }

        Optional<Code> optional = codes.stream()
                .sorted((lhs, rhs) -> Integer.compare(lhs.distance, rhs.distance))
                .filter(c -> c.distance >= d)
                .findFirst();
        if (!optional.isPresent()) {
            writer.fail("Подходящего БЧХ кода с минимальным расстояние d = " + d + " не найдено.");
            return writer.toString();
        } else {
            Code code = optional.get();
            writer.success("Подходящий БЧХ код с минимальным расстоянием d = " + d + " найден.");

            writer.println("<table class='table'>");
            writer.println("<tr><th>Показатели</th><th>Порождающий</th><th>Размерность<br/>n-deg g(x)</th><th>Расстояние</th><th>b</th></tr>");
            printCode(writer, code);
            writer.println("</table>");
            writer.println("<ul>");

            writer.println("<li>Примитивный полином <strong>p(x)</strong> = " + px + "</li>");
            //writer.println("<li>Порождающий многочлен <strong>g(x)</strong> =" + Utils.concat(code.group.stream().map(i -> "(x - a<sup>" + i + "</sup>)").collect(Collectors.toList()), "*") + "</li>");
            writer.println("<li>Примитивный элемент поля <strong>a</strong> = " + Polynomial.of(0, 1) + "</li>");
            writer.println("</ul>");

            int mod = (int)Math.pow(2, m) - 1;
            Polynomial[] field = new Polynomial[mod];
            Map<Polynomial, Integer> fieldMap = new HashMap<>();
            {
                writer.println("<h2>Поле GF(2<sup>" + m + "</sup>), порожденное многочленом p(x) = " + px + "</h2>");
                Polynomial current = Polynomial.one();
                Polynomial x = Polynomial.x();
                writer.println("<table class='table'>");
                writer.println("<tr><th>Степень</th><th>Многочлен</th><th>Последовательность</th></tr>");
                //writer.println("<tr><td></td><td></td><td></td></tr>");
                for (int i = 0; i < mod; ++i) {
                    field[i] = current;
                    fieldMap.put(current, i);
                    //System.out.println(i + " | " + current);
                    writer.println("<tr><td>" + i + "</td><td>" + current + "</td><td>" + current.toBitString(m) + "</td></tr>");
                    current = current.mul(x, q).mod(px, q);
                }
                writer.println("</table>");
            }

            Polynomial[][] h = new Polynomial[d - 1][n];
            for (int i = 0; i < d - 1; ++i) {
                int b = i + code.left;
                //h[i][0] = Polynomial.of(1);
                for (int j = 0; j < n; ++j) {
                    h[i][j] = Polynomial.fill((b * j % n) * s);
                }
            }

            /*h = new Polynomial[2][15];
            h[0][0] = Polynomial.fill(0);
            h[0][1] = Polynomial.fill(1);
            h[0][2] = Polynomial.fill(2);
            h[0][3] = Polynomial.fill(3);
            h[0][4] = Polynomial.fill(4);
            h[0][5] = Polynomial.fill(5);
            h[0][6] = Polynomial.fill(6);
            h[0][7] = Polynomial.fill(7);
            h[0][8] = Polynomial.fill(8);
            h[0][9] = Polynomial.fill(9);
            h[0][10] = Polynomial.fill(10);
            h[0][11] = Polynomial.fill(11);
            h[0][12] = Polynomial.fill(12);
            h[0][13] = Polynomial.fill(13);
            h[0][14] = Polynomial.fill(14);

            h[1][0] = Polynomial.fill(0);
            h[1][1] = Polynomial.fill(3);
            h[1][2] = Polynomial.fill(6);
            h[1][3] = Polynomial.fill(9);
            h[1][4] = Polynomial.fill(12);
            h[1][5] = Polynomial.fill(0);
            h[1][6] = Polynomial.fill(3);
            h[1][7] = Polynomial.fill(6);
            h[1][8] = Polynomial.fill(9);
            h[1][9] = Polynomial.fill(12);
            h[1][10] = Polynomial.fill(0);
            h[1][11] = Polynomial.fill(3);
            h[1][12] = Polynomial.fill(6);
            h[1][13] = Polynomial.fill(9);
            h[1][14] = Polynomial.fill(12);*/

            writer.println("<h2>Проверочная матрица H</h2>");
            writer.println("<table class='table'>");
            for (int i = 0; i < h.length; ++i) {
                writer.println("<tr>");
                for (int j = 0; j < h[i].length; ++j) {
                    int degree = h[i][j].length() - 1;
                    h[i][j] = field[degree];
                    writer.println("<td>a<sup>" + degree + "</sup>=" + h[i][j] + "</td>");
                }
                writer.println("</tr>");
            }
            writer.println("</table>");


            writer.println("<h2>Декодирование</h2>");

            int[] y = new int[n];
            for (int i : Utils.random(n, t)) {
                y[i] = 1;
            }
            //y = new int[]{1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1};
            writer.println("<p>Пусть <strong>y</strong> = " + Arrays.toString(y) + " - принятая последовательность</p>");

            Polynomial[] ss = Utils.mul(y, Utils.transpose(h), q);
            writer.println("<h3>Полученные синдромы</h3>");
            writer.println("<table class='table'>");
            writer.println("<tr><th>Номер уравнения</th><th>Синдром</th><th>Соответствующий элемент поля</th></tr>");
            for (int i = 0; i < ss.length; i++) {
                writer.print("<tr><td>" + i + "</td><td>" + ss[i] + " </td><td>a<sup>");
                Integer assigned = fieldMap.get(ss[i]);
                if (assigned != null) {
                    writer.print(fieldMap.get(ss[i]));
                } else {
                    writer.print("-inf");
                }
                writer.println("</sup></td></tr>");
            }
            writer.println("</table>");

            /*writer.println("<h3>Таблица синдромов</h3>");
            Map<Polynomial[], int[]> syndroms = Utils.syndroms(h, q);
            writer.println("<table class='table'>");
            writer.println("<tr><td>Синдром</td><td>Ошибка</td></tr>");
            syndroms.forEach((k, v) -> {
                writer.println("<tr><td>" + k + "</td><td>" + </td></tr>");
            });
            writer.println("</table>");*/

            /*List<Polynomial> polynomials = code.group.stream().map(i -> Polynomial.fill(i, 1)).collect(Collectors.toList());
            polynomials.forEach(System.out::println);
            Polynomial temp = polynomials.get(0);
            for (int i = 1; i < polynomials.size(); ++i) {
                temp = temp.mul(polynomials.get(i), q);
            }
            System.out.println(temp);
            Polynomial gx = temp.mod(prim, q);
            Polynomial xn_1 = Polynomial.kxm(1, 0).add(Polynomial.kxm(1, n), q);
            //Polynomial mod = Polynomial.mod(xn_1, prim, q);
            writer.println("<p>Таким образом, порождающий многочлен <strong>g(x)</strong> = " + gx + "</p>");

            writer.println("<h2>Порождающая матрица G</h2>");
            writer.println("<table class='table'>");
            //System.out.println("N = " + n + " |g(x)| = " + gx.length());
            int r = gx.length();
            int h = n - r + 1;
            int[][] g = new int[h][n];
            for (int i = 0; i < h; ++i) {
                for (int j = 0; j < gx.length(); ++j) {
                    g[i][i + j] = gx.get(j);
                }
            }
            Utils.print(g);
            System.out.println(Utils.minDistance(g));
            for (int i = 0; i < h; ++i) {
                writer.println("<tr>");
                for (int j = 0; j < i; ++j) {
                    writer.println("<td>0</td>");
                }
                for (int j = 0; j < gx.length(); ++j) {
                    writer.print("<td class='success'>" + gx.get(j) + "</td>");
                }
                for (int j = 0; j < h - i - 1; ++j) {
                    writer.println("<td>0</td>");
                }
                writer.println("</tr>");
            }
            writer.println("</table>");

            Polynomial mod = xn_1.mod(gx, q);
            if (mod.equals(Polynomial.zero())) {
                writer.success("Остаток от деления x^n - 1 / g(x) = " + mod);
            } else {
                writer.fail("Остаток от деления x^n - 1 / g(x) = " + mod);
                return writer.toString();
            }
            */
        }
        return writer.toString();
    }

    private void printCode(Page writer, Code code) {
        writer.println("<tr>");
        writer.println("<td>" + code.group + "</td>");
        String tm = "";
        for (int i : code.polynomial) {
            tm += "M<sub>" + i + "</sub>";
        }
        writer.println("<td>" + tm + "</td>");
        writer.println("<td>" + code.size + "</td>");
        writer.println("<td>" + code.distance + "</td>");
        writer.println("<td>" + code.left + "</td>");
        writer.println("</tr>");
    }
}
