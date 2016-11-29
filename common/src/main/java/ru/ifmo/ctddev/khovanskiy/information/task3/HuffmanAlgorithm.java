package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.comparators.ComparatorChain;
import ru.ifmo.ctddev.khovanskiy.information.util.Utils;

import java.math.BigInteger;
import java.util.*;

/**
 * @author Victor Khovanskiy
 */
@Slf4j
public class HuffmanAlgorithm extends Algorithm {

    @Override
    public HuffmanResult encode(String source1, String source2, boolean showDebugInfo) {
        HuffmanResult result = new HuffmanResult();
        String input = source2;
        result.setInput(source2);
        char[] x = input.toCharArray();

        // Композиция
        Map<Character, Integer> tau = tau(x);

        // Сортируем по частоте появлений, затем по алфавиту
        ComparatorChain<Node> comparatorChain = new ComparatorChain<>();
        comparatorChain.addComparator(Comparator.comparingDouble(o -> o.priority));
        comparatorChain.addComparator(Comparator.<Node, String>comparing(o -> o.label));
        PriorityQueue<Node> queue = new PriorityQueue<>(comparatorChain);

        // Строим дерево
        List<Node> nodes = new ArrayList<>();
        tau.forEach((label, priority) -> {
            Node node = new Node(label + "", priority);
            queue.add(node);
            nodes.add(node);
        });
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            queue.add(new Node(left, right));
        }
        assert queue.size() == 1;
        Node root = queue.poll();

        // Обходим дерево и строим коды
        dfs(root, "");

        // Вычисляем количество различных вершин на ярусах
        Map<Integer, HuffmanLevel> levels = new TreeMap<>();
        // Обходим дерево
        level(root, 0, levels);
        // Вычисляем требуемое количество битов
        levels.forEach((ordinal, level) -> {
            int bits = (int) Math.ceil(Utils.log(level.getNumberOfNodes() + 1, 2));
            level.setBits(bits);
        });
        result.setLevels(new ArrayList<>(levels.values()));

        // Запись результатов шагов
        List<HuffmanStepResult> stepResults = new ArrayList<>();
        for (Node node : nodes) {
            HuffmanStepResult stepResult = new HuffmanStepResult();
            stepResult.setSymbol(node.label);
            stepResult.setNumberOfOccurrence(node.priority);
            stepResult.setCode(node.code);
            stepResults.add(stepResult);
        }
        result.setStepResults(stepResults);

        // Количество битов для передачи ярусов
        int levelsCost = levelsCost(levels.values());
        // Количество битов для передачи символов
        int symbolsCost = symbolsCost(nodes);
        // Количество битов для передачи дерева
        int treeCost = levelsCost + symbolsCost;
        result.setTreeCost(treeCost);

        // Количество битов для передачи сообщения
        int messageCost = 0;
        Map<String, String> codes = new HashMap<>();
        for (Node node : nodes) {
            codes.put(node.label, node.code);
            messageCost += node.priority * node.code.length();
        }
        result.setMessageCost(messageCost);

        StringBuilder sb = new StringBuilder();
        for (char c : x) {
            sb.append(codes.get(c + ""));
        }
        String output = sb.toString();
        result.setOutput(output);

        /*if (showDebugInfo) {
            out.println("строка = " + input);
            levels.forEach((ordinal, level) -> {
                out.println(level.getOrdinal() + " " + level.getNumberOfNodes() + " " + level.getNumberOfLeafs() + " 0..." + level.getNumberOfNodes() + " " + level.getBits());
            });
            out.println("Буква\tЧисло появлений\tДлинна кодового слова\tКодовое слово");
            for (HuffmanStepResult stepResult : stepResults) {
                out.println(stepResult.getSymbol() + "\t" + stepResult.getNumberOfOccurrence() + "\t" + stepResult.getCode().length() + "\t" + stepResult.getCode());
            }
            out.println("Количество бит для кодирования дерева = " + (nodes.size() * 2 - 1) + " + 8 * " + nodes.size() + " = " + (nodes.size() * 10 - 1));

            out.println("стоимость кодирования сообщения = " + messageCost);
            out.println("общая стоимость = " + (treeCost + messageCost));
            out.println("стоимость кодирования регулярным кодом (без сжатия) = 8 * " + input.length() + " = " + (8 * input.length()));
        }*/
        return result;
    }

    /**
     * Вычисляет и возвращает количество битов для передачи ярусов
     *
     * @param levels ярусы
     * @return количество битов
     */
    private int levelsCost(Iterable<HuffmanLevel> levels) {
        int total = 0;
        for (HuffmanLevel level : levels) {
            total += level.getBits();
        }
        return total;
    }

    /**
     * Вычисляет и возвращает количество битов для передачи символов
     *
     * @param nodes узлы
     * @return количество битов
     */
    private int symbolsCost(Iterable<Node> nodes) {
        int total = 0;
        Map<Integer, Integer> lengths = new TreeMap<>();
        for (Node node : nodes) {
            lengths.compute(node.code.length(), (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        }
        int numberOfSymbols = M;
        for (Map.Entry<Integer, Integer> entry : lengths.entrySet()) {
            int numberOfLevelSymbols = entry.getValue();
            BigInteger temp = Utils.binomialCoefficient(numberOfSymbols, numberOfLevelSymbols);
            int bits = (int) Math.ceil(Utils.log(temp.doubleValue(), 2));
            total += bits;
            numberOfSymbols -= numberOfLevelSymbols;
        }
        return total;
    }

    /**
     * Обходит дерево и заполняет ярусы
     *
     * @param current узел
     * @param k номер яруса
     * @param levels ярусы
     */
    private void level(Node current, int k, Map<Integer, HuffmanLevel> levels) {
        if (current == null) {
            return;
        }
        HuffmanLevel level = levels.computeIfAbsent(k, HuffmanLevel::new);
        level.setNumberOfNodes(level.getNumberOfNodes() + 1);
        if (current.isLeaf()) {
            level.setNumberOfLeafs(level.getNumberOfLeafs() + 1);
        }
        level(current.left, k + 1, levels);
        level(current.right, k + 1, levels);
    }

    /**
     * Вычисляет и возвращает композицию
     *
     * @param x последовательность
     * @return композиция
     */
    private Map<Character, Integer> tau(char[] x) {
        Map<Character, Integer> tau = new HashMap<>();
        for (Character c : x) {
            tau.compute(c, (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        }
        return tau;
    }

    /**
     * Обходит дерево и вычисляет коды
     *
     * @param current текущий узел
     * @param path    текущий путь
     */
    private void dfs(Node current, String path) {
        if (current.isLeaf()) {
            current.code = path;
            return;
        }
        dfs(current.left, path + "0");
        dfs(current.right, path + "1");
    }

    @Data
    private static class Node {
        private Node left;
        private Node right;
        private String label = "";
        private int priority;
        private String code;

        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.priority = left.priority + right.priority;
        }

        public Node(String label, int priority) {
            this.label = label;
            this.priority = priority;
        }

        public boolean isLeaf() {
            return !Objects.equals(label, "");
        }
    }

}
