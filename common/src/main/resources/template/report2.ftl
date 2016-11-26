<#-- @ftlvariable name="file" type="java.io.File" -->
<#-- @ftlvariable name="archive" type="java.io.File" -->
<#-- @ftlvariable name="matrix" type="java.util.List<java.util.List<java.lang.Double>>" -->
<#-- @ftlvariable name="p" type="java.util.List<java.lang.Double>" -->
<#-- @ftlvariable name="HX" type="java.lang.Double" -->
<#-- @ftlvariable name="HXXinf" type="java.lang.Double" -->
<#-- @ftlvariable name="H2X" type="java.lang.Double" -->
<#-- @ftlvariable name="probs1" type="java.util.Map<java.util.String, java.lang.Double>" -->
<#-- @ftlvariable name="codes1" type="java.util.Map<java.util.String, java.lang.String>" -->
<#-- @ftlvariable name="K1X" type="java.lang.Double" -->
<#-- @ftlvariable name="probs2" type="java.util.Map<java.util.String, java.lang.Double>" -->
<#-- @ftlvariable name="codes2" type="java.util.Map<java.util.String, java.lang.String>" -->
<#-- @ftlvariable name="K2X" type="java.lang.Double" -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name=viewport content="width=device-width, initial-scale=1">
    <title>Отчет по заданию 2</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.6.0/katex.min.css">
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.3.0/Chart.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.6.0/katex.min.js"></script>
</head>
<body>
<#setting number_format="0.######"/>
<div class='container'>
    <h1>Отчет по заданию 2
        <small>Хованский Виктор Сергеевич, группа М4136с</small>
    </h1>
    <h2>Матрица <span class="tex" data-value="M">M</span></h2>
    <p class="lead">
        Дан марковский источник с заданной матрицей переходных вероятностей:
    </p>
    <table class="table table-bordered">
    <#list matrix as row>
        <tr>
            <#list row as value>
                <td>${value}</td>
            </#list>
        </tr>
    </#list>
    </table>
    <h2>Вектор <span class="tex" data-value="p">p</span></h2>
    <p class="lead">
        Найдем вектор <span class="tex" data-value="p">p</span>: такой, что <span class="tex" data-value="p = p * M">p = p * M</span>.
        Это можно сделать, если решить систему уравнений:
    </p>
    <div>
        <ul>
            <li><span class="tex" data-value="p = p * M">p = p * M</span></li>
            <li><span class="tex" data-value="p_1 + p_2 + p_3 = 1">p_1 + p_2 + p_3 = 1</span></li>
        </ul>
    </div>
    <p class="lead">Тогда получаются следующие значения:</p>
    <table class="table table-bordered">
        <tr>
        <#list p as value>
            <td>${value}</td>
        </#list>
        </tr>
    </table>
    <h2>Энтропия <span class="tex" data-value="H(X)">H(X)</span></h2>
    <p class="lead">
        Для нахождения <span class="tex" data-value="H(X)">H(X)</span> используем формулу:
        <span class="tex" data-value="H(X) = -\sum_{x \in X} p(x) * \log_2(p(x))">H(X) = -\Sum_{x \in X} p(x) * \log_2(p(x))</span>
    </p>
    <div class="alert alert-success">Тогда <span class="tex" data-value="H(X) = ${HX}">H(X) = ${HX}</span></div>
    <h2>Энтропия <span class="tex" data-value="H(X|X^{\infty})"></span></h2>
    <p class="lead">
        Для нахождения <span class="tex" data-value="H(X|X^{\infty})"></span> используем формулу:
        <span class="tex" data-value="H(X|X^{\infty}) = H(X|X^s)"></span>, где <span class="tex" data-value="s"></span>
        - связность, а
        <span class="tex" data-value="H(X|X^s) = -\sum_i \sum_j p_i * M_{i,j} * \log_2(M_{i,j})"></span>
    </p>
    <div class="alert alert-success">Тогда <span class="tex" data-value="H(X|X^{\infty}) = ${HXXinf}"></span></div>
    <h2>Энтропии <span class="tex" data-value="H_2(X)"></span> и <span class="tex" data-value="H_n(X)"></span></h2>
    <p class="lead">
        Для нахождения <span class="tex" data-value="H_2(X)"></span> и <span class="tex" data-value="H_n(X)"></span>
        используем формулу:
        <span class="tex" data-value="H_n(X) = \frac{H(X)}{n}"></span>, где <span class="tex" data-value="n"></span> -
        размер слова, а
        <span class="tex" data-value="\frac{H(X)}{n} = \frac{s*H_s(X) + (n - s)*H(X|X^s)}{n}"></span>
    </p>
    <div class="alert alert-success">Тогда <span class="tex" data-value="H_2(X) = ${H2X}"></span> и
        <span class="tex" data-value="H_n(X) = ${HXXinf} + \frac{${HX - HXXinf}}{2}"></span>
    </div>
    <h2>Код Хаффмана для <span class="tex" data-value="n = 1"></span></h2>
    <h3>Вероятности</h3>
    <table class="table table-bordered">
        <tr>
            <th class="col-xs-3">Слово</th>
            <th>Вероятность</th>
        </tr>
    <#list probs1 as key, value>
        <tr>
            <td>${key}</td>
            <td>${value}</td>
        </tr>
    </#list>
    </table>

    <h3>Коды</h3>
    <table class="table table-bordered">
        <tr>
            <th class="col-xs-3">Слово</th>
            <th>Код</th>
        </tr>
    <#list codes1 as key, value>
        <tr>
            <td>${key}</td>
            <td>${value}</td>
        </tr>
    </#list>
    </table>
    <div class="alert alert-success">И количество бит на символ равно <span class="tex"
                                                                            data-value="K_1(X) = ${K1X}"></span>
    </div>
    <h2>Код Хаффмана для <span class="tex" data-value="n = 2"></span></h2>
    <h3>Вероятности</h3>
    <table class="table table-bordered">
        <tr>
            <th class="col-xs-3">Слово</th>
            <th>Вероятность</th>
        </tr>
    <#list probs2 as key, value>
        <tr>
            <td>${key}</td>
            <td>${value}</td>
        </tr>
    </#list>
    </table>

    <h3>Коды</h3>
    <table class="table table-bordered">
        <tr>
            <th class="col-xs-3">Слово</th>
            <th>Код</th>
        </tr>
    <#list codes2 as key, value>
        <tr>
            <td>${key}</td>
            <td>${value}</td>
        </tr>
    </#list>
    </table>
    <div class="alert alert-success">И количество бит на символ равно <span class="tex"
                                                                            data-value="K_2(X) = ${K2X}"></span>
    </div>
    <h2>Алгоритм кодирования</h2>
    <ul class="lead">
        <li>Первый символ кодируем с помощью алгоритма Хаффмана;</li>
        <li>Последующие символы будем кодировать относительно предыдущего с помощью таблицы:
            <table class="table table-bordered">
                <tr>
                    <td>Предыдущий символ</td>
                    <td colspan="3">a</td>
                    <td colspan="2">b</td>
                    <td colspan="2">c</td>
                </tr>
                <tr>
                    <td>Следующий символ</td>
                    <td>a</td>
                    <td>b</td>
                    <td>c</td>
                    <td>b</td>
                    <td>c</td>
                    <td>a</td>
                    <td>c</td>
                </tr>
                <tr>
                    <td>Кодовый символ</td>
                    <td>0</td>
                    <td>10</td>
                    <td>11</td>
                    <td>0</td>
                    <td>1</td>
                    <td>0</td>
                    <td>1</td>
                </tr>
            </table>
        </li>
    </ul>
    <div class="alert alert-success">И количество бит на символ равно <span class="tex"
                                                                            data-value="K^*(X) = 1.153846"></span>

    </div>
    <h2>Дополнение к заданию</h2>
    <p class="lead">
        Первые <span class="tex" data-value="s"></span> символов кодируются с помошью алгоритма Хаффмана в соответствии
        с
        финальными
        вероятностями,
        а последующие с применением марковской цепи. В качестве символа выбирается последовательность разумной длины в
        зависимости от алфавита, которые дают блоки размера <span class="tex" data-value="N"></span>.
    </p>
    <script>
        $("span.tex").each(function (index, element) {
            var value = $(element).attr("data-value");
            katex.render(value, element);
        });
    </script>
</body>
</html>