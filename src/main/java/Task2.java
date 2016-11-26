import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * @author victor
 */
@Slf4j
public class Task2 implements Runnable {
    public static void main(String[] args) {
        new Task2().run();
    }

    @Override
    public void run() {
        Map<String, Object> parameters = new HashMap<>();
        double[][] M = new double[][]{
                {1d/3, 1d/3, 1d/3},
                {0, 1d/2, 1d/2},
                {1d/4, 0, 3d/4},
        };
        {
            List<List<Double>> matrix = new ArrayList<>();
            for (int i = 0; i < M.length; ++i) {
                List<Double> row = new ArrayList<>();
                for (int j = 0; j < M.length; ++j) {
                    row.add(M[i][j]);
                }
                matrix.add(row);
            }
            parameters.put("matrix", matrix);
        }

        for (int i = 0; i < M.length; ++i) {
            System.out.println("Row #" + i + " validated: " + validateRowSum(M[i]));
        }
        double[] p = new double[]{3d/13, 2d/13, 8d/13};
        {
            List<Double> pList = new ArrayList<>();
            for (int i = 0; i < p.length; ++i) {
                pList.add(p[i]);
            }
            parameters.put("p", p);
        }
        System.out.println("P-vector validated: " + validateRowSum(p));
        double HX = 0d;
        for (int i = 0; i < p.length; ++i) {
            HX += -p[i] * Utils.log(p[i], 2);
        }
        log.info("H(X) = " + HX);
        parameters.put("HX", HX);

        double HXXinf = 0d;
        for (int i = 0; i < p.length; ++i) {
            for (int j = 0; j < p.length; ++j) {
                if (M[i][j] != 0) {
                    HXXinf += -p[i] * M[i][j] * Utils.log(M[i][j], 2);
                }
            }
        }
        log.info("H(X|X^inf) = " + HXXinf);
        parameters.put("HXXinf", HXXinf);

        double H2X = HXXinf + (HX - HXXinf) / 2;
        log.info("H_2(X) = " + H2X);
        parameters.put("H2X", H2X);
        log.info("H_n(X) = " + HXXinf + " + (" + (HX - HXXinf) + ") / " + 2);

        {
            int N = 1;
            log.info("### N = 1");
            Map<String, Double> probabilities = new HashMap<>();
            for (int i = 0; i < p.length; ++i) {
                String label = label(i);
                double priority = p[i];
                probabilities.put(label, priority);
            }
            probabilities.forEach((key, value) -> {
                log.info(key + " = " + value);
            });
            parameters.put("probs1", probabilities);
            Map<String, String> codes = buildCodes(probabilities);
            log.info("Codes");
            codes.forEach((key, value) -> {
                log.info(key + " = " + value);
            });
            parameters.put("codes1", codes);
            double Kn = 0d;
            for (int i = 0; i < p.length; ++i) {
                double priority = p[i];
                if (priority > 0) {
                    String label = label(i);
                    assert codes.containsKey(label) : "No label: " + label;
                    Kn += priority * codes.get(label).length() / N;
                }
            }
            System.out.println("K_n = " + Kn);
            parameters.put("K1X", Kn);
        }
        {
            int N = 2;
            log.info("### N = 2");
            log.info("Probabilities");
            Map<String, Double> probabilities = new HashMap<>();
            for (int i = 0; i < p.length; ++i) {
                for (int j = 0; j < p.length; ++j) {
                    String label = label(i, j);
                    double priority = p[i] * M[i][j];
                    probabilities.put(label, priority);
                }
            }
            probabilities.forEach((key, value) -> {
                log.info(key + " = " + value);
            });
            parameters.put("probs2", probabilities);
            Map<String, String> codes = buildCodes(probabilities);
            log.info("Codes");
            codes.forEach((key, value) -> {
                log.info(key + " = " + value);
            });
            parameters.put("codes2", codes);
            double Kn = 0d;
            for (int i = 0; i < p.length; ++i) {
                for (int j = 0; j < p.length; ++j) {
                    double priority = p[i] * M[i][j];
                    if (priority > 0) {
                        //System.out.print(a + "\t\t");
                        String label = label(i, j);
                        assert codes.containsKey(label) : "No label: " + label;
                        Kn += priority * codes.get(label).length() / N;
                    }
                }
                //System.out.println();
            }
            System.out.println("K_n = " + Kn);
            parameters.put("K2X", Kn);
        }
        Configuration configuration = new Configuration(new Version("2.3.25"));
        configuration.setTemplateLoader(new ClassTemplateLoader());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task2.html"))) {
            Template template = configuration.getTemplate("template/report2.ftl", Locale.US);
            template.process(parameters, writer);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private static boolean validateRowSum(double[] row) {
        double sum = 0d;
        for (int i = 0; i < row.length; ++i) {
            sum += row[i];
        }
        return sum == 1;
    }

    private String label(Integer... numbers) {
        String label = "";
        for (int i = 0; i < numbers.length; ++i) {
            label += (char) (numbers[i] + 'a');
        }
        return label;
    }

    private Map<String, String> buildCodes(Map<String, Double> probabilities) {
        ComparatorChain<Node> comparatorChain = new ComparatorChain<>();
        comparatorChain.addComparator((o1, o2) -> Double.compare(o1.priority, o2.priority));
        comparatorChain.addComparator((o1, o2) -> o1.label.compareTo(o2.label));
        PriorityQueue<Node> nodes = new PriorityQueue<>(comparatorChain);
        probabilities.forEach((label, priority) -> {
            if (priority > 0) {
                nodes.add(new Node(label, priority));
            }
        });
        while (nodes.size() > 1) {
            Node left = nodes.poll();
            Node right = nodes.poll();
            nodes.add(new Node(left, right));
        }
        assert nodes.size() == 1;
        Node root = nodes.poll();
        Map<String, String> codes = new TreeMap<>(String::compareTo);
        dfs(root, "", codes);
        return codes;
    }

    private void dfs(Node current, String path, Map<String, String> codes) {
        if (current.isLeaf()) {
            codes.put(current.label, path);
            return;
        }
        dfs(current.left, path + "0", codes);
        dfs(current.right, path + "1", codes);
    }

    @Data
    private class Node {
        String label = "";
        Node left;
        Node right;
        double priority;

        public Node(String label, double priority) {
            this.label = label;
            this.priority = priority;
        }

        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.priority = left.priority + right.priority;
        }

        public boolean isLeaf() {
            return !Objects.equals(label, "");
        }
    }
}
