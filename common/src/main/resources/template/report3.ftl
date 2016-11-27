<#-- @ftlvariable name="huffman" type="ru.ifmo.ctddev.khovanskiy.information.task3.HuffmanResult" -->
<#-- @ftlvariable name="adaptive" type="ru.ifmo.ctddev.khovanskiy.information.task3.AdaptiveArithmeticResult" -->
<#-- @ftlvariable name="numeric" type="ru.ifmo.ctddev.khovanskiy.information.task3.NumericResult" -->
<#-- @ftlvariable name="lz77" type="ru.ifmo.ctddev.khovanskiy.information.task3.LZ77Result" -->
<#-- @ftlvariable name="lz78" type="ru.ifmo.ctddev.khovanskiy.information.task3.LZ78Result" -->
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
    <h1>Отчет по заданию 3
        <small>Хованский Виктор Сергеевич, группа М4136с</small>
    </h1>
<#if huffman??>
    <h2>Алгоритм Хаффмана</h2>
    <h3>Подсчет количества битов на передачу дерева</h3>
    <table class="table table-bordered">
        <tr>
            <th>Ярус</th>
            <th>Общее число вершин</th>
            <th>Число концевых вершин</th>
            <th>Диапозон значений</th>
            <th>Затраты в битах</th>
        </tr>
        <#list huffman.levels as level>
            <tr>
                <td>${level.ordinal}</td>
                <td>${level.numberOfNodes}</td>
                <td>${level.numberOfLeafs}</td>
                <td>0...${level.numberOfNodes}</td>
                <td>${level.bits}</td>
            </tr>
        </#list>
    </table>
    <h3>Регулярный код Хаффмена</h3>
    <table class="table table-bordered">
        <tr>
            <th>Буква</th>
            <th>Число появлений</th>
            <th>Номер яруса (длина кодового слова)</th>
            <th>Кодовое слово</th>
        </tr>
        <#list huffman.stepResults as step>
            <tr>
                <td>${step.symbol}</td>
                <td>${step.numberOfOccurrence}</td>
                <td>${step.code?length}</td>
                <td>${step.code}</td>
            </tr>
        </#list>
    </table>
    <div class="alert alert-info">
        Затраты на передачу служебной информации = ${huffman.treeCost} бит
    </div>
    <div class="alert alert-info">
        Затраты на передачу сообщений = ${huffman.messageCost} бит
    </div>
    <div class="alert alert-info">
        Итого = ${huffman.treeCost + huffman.messageCost} бит
    </div>
</#if>
<#if adaptive??>
    <h2>Адаптивное кодирование с применением арифметического кодирования</h2>
    <table class="table table-bordered">
        <tr>
            <th>Шаг</th>
            <th>x</th>
            <th>p(x)</th>
            <th>q(x)</th>
            <th>F</th>
            <th>G</th>
        </tr>
        <#list adaptive.stepResults as step>
            <tr>
                <td>${step.ordinal}</td>
                <td>${step.x}</td>
                <td>${step.p?string("#0.########")}</td>
                <td>${step.q?string("#0.########")}</td>
                <td>${step.f?string("#0.#############################################")}</td>
                <td>${step.g?string("#0.#############################################")}</td>
            </tr>
        </#list>
    </table>
    <div class="alert alert-info">
        Итого = ${adaptive.bits} бит
    </div>
</#if>
<#if numeric??>
    <h2>Нумерационное кодирование</h2>
    <div class="alert alert-info">
        Количество различных композиций = ${numeric.numberOfCompositions}
    </div>
    <div class="alert alert-info">
        Длина части кодового слова для передачи номера композиции = ${numeric.l1} бит
    </div>
    <div class="alert alert-info">
        Номер композиции = ${numeric.positionOfComposition}
    </div>
    <div class="alert alert-info">
        Количество последовательностей с фиксированной композицией = ${numeric.numberOfSequences}
    </div>
    <div class="alert alert-info">
        Длина части кодового слова для передачи номера композиции = ${numeric.l2} бит
    </div>
    <div class="alert alert-info">
        Номер последовательности = ${numeric.positionOfSequence}
    </div>
    <div class="alert alert-info">
        Итого = ${numeric.l1 + numeric.l2} бит
    </div>
</#if>
<#if lz77??>
    <h2>Алгоритм LZ77</h2>
    <table class="table table-bordered">
        <tr>
            <th>Шаг</th>
            <th>Флаг</th>
            <th>Последовательность букв</th>
            <th>d</th>
            <th>l</th>
            <th>Кодовая последовательность</th>
            <th>Биты</th>
        </tr>
        <#list lz77.stepResults as step>
            <tr>
                <td>${step.ordinal}</td>
                <td>${step.found?string("1", "0")}</td>
                <td>${step.sequence}</td>
                <td>${step.d}</td>
                <td>${step.l}</td>
                <td>${step.code}</td>
                <td>${step.bits}</td>
            </tr>
        </#list>
        <tr>
            <td colspan="6">Итого</td>
            <td>${lz77.bits}</td>
        </tr>
    </table>
</#if>
<#if lz78??>
    <h2>Алгоритм LZ78</h2>
    <table class="table table-bordered">
        <tr>
            <th>Шаг</th>
            <th>Словарь</th>
            <th>Номер слова</th>
            <th>Кодовые символы</th>
            <th>Биты</th>
        </tr>
        <#list lz78.stepResults as step>
            <tr>
                <td>${step.ordinal}</td>
                <td>${step.sequence}</td>
                <td>${step.wordNumber}</td>
                <td>${step.code}</td>
                <td>${step.bits}</td>
            </tr>
        </#list>
        <tr>
            <td colspan="6">Итого</td>
            <td>${lz78.bits}</td>
        </tr>
    </table>
</#if>
    <script>
        $("span.tex").each(function (index, element) {
            var value = $(element).attr("data-value");
            katex.render(value, element);
        });
    </script>
</body>
</html>