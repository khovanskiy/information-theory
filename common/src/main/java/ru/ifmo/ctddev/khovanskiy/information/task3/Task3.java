package ru.ifmo.ctddev.khovanskiy.information.task3;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * victor
 */
@Slf4j
public class Task3 implements Runnable {
    public static void main(String[] args) {
        new Task3().run();
    }

    @Override
    public void run() {
        String source = "IF_WE_CANNOT_DO_AS_WE_WOULD_WE_SHOULD_DO_AS_WE_CAN";
        //String source = "After dinner sit a while, after supper walk a mile";
        //String source = "abaaaababa";
        Map<String, Algorithm> algorithms = new HashMap<>();
        //algorithms.put("huffman", new HuffmanAlgorithm());
        //algorithms.put("adaptive", new AdaptiveArithmeticAlgorithm());
        //algorithms.put("numeric", new NumericAlgorithm());
        //algorithms.put("lz77", new LZ77Algorithm());
        algorithms.put("lz78", new LZ78Algorithm());

        Map<String, Object> parameters = new HashMap<>();
        for (Map.Entry<String, Algorithm> entry : algorithms.entrySet()) {
            String key = entry.getKey();
            Algorithm algorithm = entry.getValue();
            AlgorithmResult result = algorithm.encode(source, true);
            parameters.put(key, result);
        }

        Configuration configuration = new Configuration(new Version("2.3.25"));
        configuration.setTemplateLoader(new ClassTemplateLoader());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task3.html"))) {
            Template template = configuration.getTemplate("template/report3.ftl", Locale.US);
            template.process(parameters, writer);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
