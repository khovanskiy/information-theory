<#-- @ftlvariable name="file" type="java.io.File" -->
<#-- @ftlvariable name="archive" type="java.io.File" -->
<#-- @ftlvariable name="points1" type="java.util.List<Task1.Point>" -->
<#-- @ftlvariable name="points2" type="java.util.List<Task1.Point>" -->
<#-- @ftlvariable name="points4" type="java.util.List<Task1.Point>" -->
<#-- @ftlvariable name="results" type="java.util.List<Task1.Result>" -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name=viewport content="width=device-width, initial-scale=1">
    <title>Отчет по заданию 1</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.6.0/katex.min.css">
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.3.0/Chart.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.6.0/katex.min.js"></script>
</head>
<body>
<#setting number_format="0.############"/>
<div class='container'>
    <h1>Отчет по заданию 1 <small>Хованский Виктор Сергеевич, группа М4136с</small></h1>
    <h2>Эмпирическое распределение</h2>
    <p class="lead">
        Для подсчета распределения вероятностей файл ${file.name} был прочитан побайтово. Размер файла <span class="tex" data-value="L = ${file.length()}">L = ${file.length()}</span> байт.
        В качестве символов были выбраны последовательности размером <span class="tex" data-value="n \in N = \{1, 2, 4\}">n из N = {1, 2, 4}</span>. Далее был произведен подсчет каждого символа. При этом размер алфавита равнялся <span class="tex" data-value="256^N">256^N</span>.
    </p>
    <p class="lead">
        В качестве Х-значений графика были выбраны отсортированные вероятности для встретившихся символов.
        В качестве Y-значений использовалась арифметическая прогрессия с шагом
        <span class="tex" data-value="1 / m">1 / m</span>, где <span class="tex" data-value="m">m</span> - количество встретившихся символов.
    </p>
    <div class="width: 100%; height: 300px;">
        <h3>Последовательность n = 1</h3>
        <canvas id="chart1"></canvas>
    </div>
    <div class="width: 100%; height: 300px;">
        <h3>Последовательность n = 2</h3>
        <canvas id="chart2"></canvas>
    </div>
    <div class="width: 100%; height: 300px;">
        <h3>Последовательность n = 4</h3>
        <canvas id="chart4"></canvas>
    </div>
    <script>
        var options = {
            maintainAspectRatio: false,
            scales: {
                xAxes: [{
                    type: 'linear',
                    position: 'bottom'
                }],
                yAxes: [{
                    type: 'linear'
                }]
            }
        };
        var chart1 = new Chart(document.getElementById("chart1"), {
            type: 'line',
            data: {
                datasets: [{
                    label: 'N = 1',
                    backgroundColor: "rgba(0,0,0,0)",
                    fill: false,
                    borderColor: "#f00",
                    data: [${points1?join(", ")}]
                }]
            },
            options: options
        });
        var chart2 = new Chart(document.getElementById("chart2"), {
            type: 'line',
            data: {
                datasets: [{
                    label: 'N = 2',
                    backgroundColor: "rgba(0,0,0,0)",
                    fill: false,
                    borderColor: "#0f0",
                    data: [${points2?join(", ")}]
                }]
            },
            options: options
        });
        var chart4 = new Chart(document.getElementById("chart4"), {
            type: 'line',
            data: {
                datasets: [{
                    label: 'N = 4',
                    backgroundColor: "rgba(0,0,0,0)",
                    fill: false,
                    borderColor: "#00f",
                    data: [${points4?join(", ")}]
                }]
            },
            options: options
        });
    </script>
    <h2>Энтропия на букву источника</h2>
    <p class="lead">
        Чтобы получить из количеств каждого символа <span class="tex" data-value="K = \{k(x)\}">K = {k(x)}</span> вероятность их появления, нужно произвести нормировку.<br/>
        Пусть <span class="tex" data-value="B = L - n + 1">B = L - n + 1</span> - количество последовательностей размером <span class="tex" data-value="n">n</span> в тексте.<br/>
        Тогда <span class="tex" data-value="P(X) = \{k(x) / B : x \in X\}">P(X) = \{k(x) / B : x \in X\}</span> - искомые вероятности.
    </p>
    <p class="lead">
        Далее энтропию можно посчитать по известной формуле:
        <span class="tex" data-value="H(X) = -\sum_{x \in X}p(x)log(p(x))">H(X) = -\sum_{x \in X}p(x)log(p(x))</span>. Вероятности невстретившихся символов по условию равны 0.
    </p>
    <table class='table'>
        <tr>
            <th>Размер последовательности N</th>
            <th>Энтропия (без учета невстретившихся)</th>
        </tr>
    <#list results as result>
        <tr>
            <td>${result.n}</td>
            <td>${result.entropy}</td>
        </tr>
    </#list>
    </table>
    <h2>Сравнение результатов со сжатым файлом</h2>
    <p class="lead">
        Энтропия считается аналогично, но вероятности невстретившихся символов будут равны <span class="tex" data-value="1 / L ^ n">1 / L ^ n</span>,
        а их количество можно вычислить как размер алфавита минус количество встретившихся, т.е. <span class="tex" data-value="256^n - |K|">256^n - |K|</span>
        (будем считать, что во множество K входят только символы, которые встретились в тексте хотя бы 1 раз).
    </p>
    <p class="lead">Размер архива ${archive.name} = ${archive.length()} байт</p>
    <table class='table'>
        <tr>
            <th>Размер последовательности N</th>
            <th>Энтропия (с учетом невстретившихся)</th>
            <th>Нижняя оценка на количество байт</th>
        </tr>
    <#list results as result>
        <tr>
            <td>${result.n}</td>
            <td>${result.norm_entropy}</td>
            <td>${result.minEstimatedSize}</td>
        </tr>
    </#list>
    </table>
</div>
<script>
    $("span.tex").each(function (index, element) {
        var value = $(element).attr("data-value");
        katex.render(value, element);
    });
</script>
</body>
</html>