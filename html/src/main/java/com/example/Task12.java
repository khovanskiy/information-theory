package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * victor
 */
@Controller
@RequestMapping("/task12")
public class Task12 {

    private static final Map<String, Session> sessions = new HashMap<>();
    private static final AtomicInteger keygen = new AtomicInteger(0);

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        Page writer = new Page();
        printIU(writer, "k" + keygen.incrementAndGet());
        return writer.toString();
    }

    private void printIU(Page writer, String key) {
        writer.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задачи 1 и 2</li></ol>");
        writer.println("<h1>Линейные коды</h1>");
        writer.println("<form action='/task12/execute' method='get'>");
        writer.println("<div class='form-group'><label for='command_input1'>Команда</label><input type='text' class='form-control' name='command' id='command_input' /></div>");
        writer.println("<div class='form-group'><label for='key_input1'>Ключ сессии</label><input type='text' class='form-control' name='key' id='key_input' value='" + key + "'/></div>");
        writer.println("<div class='form-group'><label for='q_input1'>Основание кода q</label><input type='text' class='form-control' name='q' id='q_input' value='2' /></div>");
        writer.println("<button type='submit' class='btn btn-default'>Выполнить команду</button>");
        writer.println("</form>");
    }

    private void printMatrix(Page writer, int[][] a) {
        writer.println("<table class='table'>");
        writer.println("<tr><th colspan='" + a.length + "'>Матрица</th></tr>");
        for (int i = 0; i < a.length; ++i) {
            writer.println("<tr>");
            for (int j = 0; j < a[i].length; ++j) {
                writer.println("<td>" + a[i][j] + "</td>");
            }
            writer.println("</tr>");
        }
        writer.println("</table>");
    }

    @RequestMapping("/execute")
    @ResponseBody
    public String execute(@RequestParam("key") String key, @RequestParam("command") String command, @RequestParam("q") int q) {
        Session session;
        synchronized (sessions) {
            session = sessions.get(key);
            if (session == null) {
                session = new Session();
                sessions.put(key, session);
            }
        }
        Page writer = new Page();
        printIU(writer, key);
        synchronized (session) {
            try {
                StringTokenizer tokenizer = new StringTokenizer(command, " ");
                if (session.matrix == null) {
                    if (tokenizer.nextToken().toLowerCase().equals("set")) {
                        List<String> rows = new ArrayList<>();
                        while (tokenizer.hasMoreTokens()) {
                            String row = tokenizer.nextToken();
                            rows.add(row);
                        }
                        if (rows.isEmpty()) {
                            writer.fail("Пустая матрица");
                            return writer.toString();
                        } else {
                            int length = rows.get(0).length();
                            for (int i = 1; i < rows.size(); ++i) {
                                if (rows.get(i).length() != length) {
                                    writer.fail("Строки разной длины");
                                    return writer.toString();
                                }
                            }
                            int[][] matrix = new int[rows.size()][length];
                            for (int i = 0; i < rows.size(); ++i) {
                                for (int j = 0; j < length; ++j) {
                                    matrix[i][j] = rows.get(i).charAt(j) - '0';
                                }
                            }
                            session.matrix = matrix;
                            writer.success("Установлена матрица размером " + matrix.length + "*" + matrix[0].length + "");
                            printMatrix(writer, session.matrix);
                        }
                    } else {
                        writer.fail("Матрица не задана: команды кроме <strong>set</strong> недопустимы");
                        return writer.toString();
                    }
                } else {
                    switch (tokenizer.nextToken().toLowerCase()) {
                        case "help": {
                            writer.info("Поменять местами i и j строки: <> i j ");
                            writer.info("Прибавить строку j к строке i: + i j ");
                            writer.info("Вычесть строку j к строке i: - i j ");
                            writer.info("Получить информацию о коде: ?");
                            writer.info("Построить таблицу синдромов: $");
                            writer.info("Конвертировать (P<sup>T</sup> i) <-> (I P): ->");
                            writer.info("Сгенерировать кодовые слова: #");
                            writer.info("Печать матриы на экран: p");
                            break;
                        }
                        case "<>":
                        case "sw":
                        case "swap": {
                            int i = Integer.parseInt(tokenizer.nextToken());
                            int j = Integer.parseInt(tokenizer.nextToken());
                            session.matrix = Matrix.swap(session.matrix, i - 1, j - 1);
                            writer.success("Поменяли местами строки " + i + " " + j + "");
                            printMatrix(writer, session.matrix);
                            break;
                        }
                        case "+":
                        case "add": {
                            int i = Integer.parseInt(tokenizer.nextToken());
                            int j = Integer.parseInt(tokenizer.nextToken());
                            session.matrix = Matrix.add(session.matrix, i - 1, j - 1, q);
                            printMatrix(writer, session.matrix);
                            break;
                        }
                        case "-":
                        case "sub": {
                            int i = Integer.parseInt(tokenizer.nextToken());
                            int j = Integer.parseInt(tokenizer.nextToken());
                            session.matrix = Matrix.sub(session.matrix, i - 1, j - 1, q);
                            printMatrix(writer, session.matrix);
                            break;
                        }
                        case "?":
                        case "info": {
                            writer.info("Скорость кода <strong>R</strong> = " + ((float) session.matrix.length / (float) session.matrix[0].length));
                            writer.info("Минимальное расстояние кода <strong>d</strong> = " + Utils.minDistance(session.matrix));
                            boolean isMCF = Utils.checkMCF(session.matrix);
                            writer.info("Находится ли в МСФ: " + (isMCF ? "да" : "нет"));
                            boolean isIPF = Utils.checkIP(session.matrix);
                            writer.info("Матрица в форме (I P): " + (isIPF ? "да" : "нет"));
                            boolean isPIF = Utils.checkPI(session.matrix);
                            writer.info("Матрица в форме (P<sup>T</sup> I): " + (isPIF ? "да" : "нет"));
                            break;
                        }
                        case "p":
                        case "print": {
                            printMatrix(writer, session.matrix);
                            break;
                        }
                        case "$":
                        case "syndroms": {
                            Map<List<Integer>, List<Integer>> map = Utils.syndroms(session.matrix);
                            printMap(writer, map, "Синдром", "Ошибка");
                            break;
                        }
                        case "->": {
                            session.matrix = Utils.convert(session.matrix);
                            writer.println("<h2>Результат после конвертации</h2>");
                            printMatrix(writer, session.matrix);
                            break;
                        }
                        case "#":
                        case "gen": {
                            int[][] x = Utils.words(session.matrix.length);
                            //int[][] r = new int[x.length][session.matrix.length];
                            //writer.println("<h2>Слова</h2>");
                            //printMatrix(writer, x);
                            /*for (int i = 0; i < x.length; ++i) {
                                r[i] = Utils.mul(x[i], session.matrix);
                            }*/
                            //print(r);
                            /*for (int i = 0; i < r.length; ++i) {
                                System.out.println(Arrays.toString(r[i]) + " " + b(r[i]) + ", " + (e(r[i]) - 1));
                            }*/
                            writer.println("<table class='table'>");
                            writer.println("<tr><th>Слово</th><th>Кодовое слово</th><th>Вес</th></tr>");
                            for (int i = 0; i < x.length; ++i) {
                                int[] word = x[i];
                                int[] code = Utils.mul(word, session.matrix);
                                int weight = Utils.w(code);
                                writer.println("<tr><td>" + Arrays.toString(word) + "</td><td>" + Arrays.toString(code) + "</td><td>" + weight + "</td></tr>");
                            }
                            writer.println("</table>");
                        }
                    }
                }

                //print(a);
            } catch (Exception e) {
                writer.fail(e.getMessage());
            }
        }
        return writer.toString();
    }

    private <K, V> void printMap(Page writer, Map<K, V> map, String key, String value) {
        writer.println("<table class='table'>");
        writer.println("<tr><th>" + key + "</th><th>" + value + "</th></tr>");
        map.forEach((k, v) -> writer.println("<tr><td>" + k + "</td><td>" + v + "</td></tr>"));
        writer.println("</table>");
    }
}
