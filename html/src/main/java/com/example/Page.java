package com.example;

/**
 * victor
 */
public class Page {
    private final StringBuilder builder = new StringBuilder();

    public <T> Page println(T t) {
        builder.append(t.toString()).append("\n");
        return this;
    }

    public <T> Page print(T t) {
        builder.append(t.toString());
        return this;
    }

    public <T> Page success(T t) {
        builder.append("<div class=\"alert alert-success\" role=\"alert\">").append(t).append("</div>");
        return this;
    }

    public <T> Page info(T t) {
        builder.append("<div class=\"alert alert-info\" role=\"alert\">").append(t).append("</div>");
        return this;
    }

    public <T> Page fail(T t) {
        builder.append("<div class=\"alert alert-danger\" role=\"alert\">").append(t).append("</div>");
        return this;
    }

    public Page formula(String formula) {
        /*println("<div class='element'></div>");
        println("<script>");
        println("var element = document.getElementById('element');");
        println("katex.render(\"" + formula + "\", element);");
        println("</script>");*/
        //println("<div data-expr=\"\\\\displaystyle{" + formula + "}\"></div>");
        println("<div class='tex' data-expr=\"\\displaystyle {" + formula + "}\"></div>");
        return this;
    }

    @Override
    public String toString() {
        String js = "<script>window.startup = function() {\n" +
                "    var tex = document.getElementsByClassName(\"tex\");\n" +
                "    Array.prototype.forEach.call(tex, function(el) {\n" +
                "        katex.render(el.getAttribute(\"data-expr\"), el);\n" +
                "    });\n" +
                "};startup();</script>";
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta name=viewport content='width=device-width, initial-scale=1'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'/>" +
                "<link rel='stylesheet' href='//cdnjs.cloudflare.com/ajax/libs/KaTeX/0.5.1/katex.min.css'>" +
                "<script src='//cdnjs.cloudflare.com/ajax/libs/KaTeX/0.5.1/katex.min.js'></script>" +
                "<style>.tex { margin: 5px 0; }</style>" +
                "</head><body>" +
                "<div class='container'>" +
                builder.toString() +
                "</div>" +
                js +
                "</body></html>";
    }
}
