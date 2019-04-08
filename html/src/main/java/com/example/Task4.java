package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * victor
 */
@Controller
@RequestMapping("/task4")
public class Task4 {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        Page page = new Page();
        page.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задача 4</li></ol>");
        page.println("<h1>Сверточные коды</h1>");
        page.println("<form action='/task4/execute' method='get'>");
        page.println("<div class='form-group'><label for='n_input1'>Длина кода n</label><input type='text' class='form-control' name='n' id='n_input' /></div>");
        page.println("<div class='form-group'><label for='k_input1'>Длина слова k</label><input type='text' class='form-control' name='k' id='k_input' /></div>");
        page.println("<div class='form-group'><label for='m_input1'>Матрица в виде: 1,2,3 4,5,6</label><input type='text' class='form-control' name='m' id='m_input' /></div>");
        page.println("<button type='submit' class='btn btn-default'>Посчитать</button>");
        page.println("</form>");
        return page.toString();
    }

    private int[] convert(String value, int from, int to) {
        String bin = Integer.toString(Integer.valueOf(value, from), to);
        int[] row = new int[bin.length()];
        for (int i = 0; i < row.length; ++i) {
            row[i] = Integer.parseInt(bin.charAt(i) + "");
        }
        return row;
    }

    @RequestMapping("/execute")
    @ResponseBody
    public String execute(@RequestParam("n") int n, @RequestParam("k") int k, @RequestParam("m") String matrix) {
        Page page = new Page();
        page.println("<ol class='breadcrumb'><li><a href='/'>Задачи</a></li><li class='active'>Задача 4</li></ol>");
        page.println("<h1>Сверточные коды</h1>");

        try {
            StringTokenizer tokenizer = new StringTokenizer(matrix, " ");
            List<List<String>> rows = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                String row = tokenizer.nextToken();
                List<String> ri = new ArrayList<>();
                StringTokenizer tokenizer1 = new StringTokenizer(row, ",");
                while (tokenizer1.hasMoreTokens()) {
                    ri.add(tokenizer1.nextToken());
                }
                rows.add(ri);
            }
            Polynomial[][] G = new Polynomial[k][n];
            int maxDegree = 0;
            for (int i = 0; i < k; ++i) {
                for (int j = 0; j < n; ++j) {
                    G[i][j] = Polynomial.of("D", convert(rows.get(i).get(j), 8, 2));
                    if (G[i][j].length() > maxDegree) {
                        maxDegree = G[i][j].length();
                    }
                }
            }

            page.println("<h2>Матрица G(D)</h2>");
            printMatrix(page, G);

            Integer[][][] decomposition = new Integer[maxDegree][k][n];

            for (int m = 0; m < maxDegree; ++m) {
                for (int i = 0; i < G.length; ++i) {
                    for (int j = 0; j < G[i].length; ++j) {
                        if (G[i][j].length() > m) {
                            decomposition[m][i][j] = G[i][j].get(m);
                        } else {
                            decomposition[m][i][j] = 0;
                        }
                    }
                }
            }

            page.println("<h2>Разложение матрицы G(D) по степеням D</h2>");
            page.info("Для декомпозиции m = " + (maxDegree - 1));
            page.println("<table class='table'>");
            page.println("<tr>");
            for (int m = 0; m < maxDegree; ++m) {
                page.println("<td>");
                printMatrix(page, decomposition[m]);
                page.println("</td>");
            }
            page.println("</tr>");
            page.println("</table>");

            page.println("<h2>Полубесконечное матричное представление G</h2>");
            Integer[][] nullMatrix = new Integer[k][n];
            for (int i = 0; i < k; ++i) {
                for (int j = 0; j < n; ++j) {
                    nullMatrix[i][j] = 0;
                }
            }
            int repeat = maxDegree + 1;
            page.println("<table class='table'>");
            for (int i = 0; i < repeat; ++i) {
                page.println("<tr>");
                for (int j = 0; j < i; ++j) {
                    page.println("<td>");
                    printMatrix(page, nullMatrix);
                    page.println("</td>");
                }
                for (int m = 0; m < maxDegree; ++m) {
                    page.println("<td>");
                    printMatrix(page, decomposition[m]);
                    page.println("</td>");
                }
                for (int j = 0; j < repeat - i; ++j) {
                    page.println("<td>");
                    printMatrix(page, nullMatrix);
                    page.println("</td>");
                }
                page.println("<td class='warning'>...</td>");
                page.println("</tr>");
            }
            page.println("<tr>");
            for (int i = 0; i < maxDegree + repeat + 1; ++i) {
                page.println("<td class='warning'>...</td>");
            }
            page.println("</tr>");
            page.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
            page.fail("Ошибка: " + e);
        }
        return page.toString();
    }

    private <T> void printMatrix(Page page, T[][] a) {
        page.println("<table class='table table-bordered'>");
        //page.println("<tr><th colspan='" + a.length + "'>Матрица</th></tr>");
        for (int i = 0; i < a.length; ++i) {
            page.println("<tr>");
            for (int j = 0; j < a[i].length; ++j) {
                page.println("<td>" + a[i][j] + "</td>");
            }
            page.println("</tr>");
        }
        page.println("</table>");
    }
}
