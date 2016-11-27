package ru.ifmo.ctddev.khovanskiy.information.task3;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Huffman algorithm's implementation
 *
 * @author Victor Khovanskiy
 */
@Slf4j
public class HuffmanAlgorithm extends Algorithm {
    static int totalNodes = 0;

    @Override
    public List<Integer> encode(String input, boolean showDebugInfo) {
        generateHuffmanCode(input);
        return null;
    }

    private void generateCodes(Node node, String currentCode) {
        if (node.left == null) {
            node.code = currentCode;
        } else {
            generateCodes(node.left, currentCode + "0");
            if (node.right != null) {
                generateCodes(node.right, currentCode + "1");
            }
        }
    }

    private void generateHuffmanCode(String s) {
        Map<Character, Integer> count = new HashMap<>();
        for (Character c : s.toCharArray()) {
            Integer currentCount = count.get(c);
            if (currentCount == null) {
                currentCount = 0;
            }
            currentCount++;
            count.put(c, currentCount);
        }
        TreeSet<Node> allNodes = new TreeSet<>();
        List<Node> nodesForCharacter = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            Node node = new Node(entry.getKey(), entry.getValue());
            nodesForCharacter.add(node);
            allNodes.add(node);
        }
        while (allNodes.size() > 1) {
            Node left = allNodes.pollFirst();
            Node right = allNodes.pollFirst();
            allNodes.add(new Node(left, right));
        }
        Node root = allNodes.pollFirst();
        if (root.left == null) {
            root = new Node(root, null);
        }
        generateCodes(root, "");
        out.println("строка = " + s);
        out.println("| Буква | Число появлений | Длинна кодового слова | Кодовое слово |");
        out.println("|:-|:-|:-|:-|");
        Collections.sort(nodesForCharacter);
        Collections.reverse(nodesForCharacter);
        for (Node node : nodesForCharacter) {
            char c = node.c;
            if (c == ' ') {
                c = '_';
            }
            out.println("|" + c + "|" + node.count + "|" + node.code.length() + "|" + node.code + "|");
        }
        out.println("Количество бит для кодирования дерева = " + (nodesForCharacter.size() * 2 - 1) + " + 8 * " +
                nodesForCharacter.size() + " = " + (nodesForCharacter.size() * 10 - 1));
        int messageCost = 0;
        Map<Character, String> codes = new HashMap<>();
        for (Node node : nodesForCharacter) {
            codes.put(node.c, node.code);
            messageCost += node.count * node.code.length();
        }
        int treeCost = nodesForCharacter.size() * 10 - 1;
        out.println("стоимость кодирования сообщения = " + messageCost);
        out.println("общая стоимость = " + (treeCost + messageCost));
        out.println("стоимость кодирования регулярным кодом (без сжатия) = 8 * " + s.length() + " = " + (8 * s.length()));
        out.println();
    }

    private static class Node implements Comparable<Node> {
        private Node left;
        private Node right;
        private char c;
        private int count;
        private int nodeId;
        private String code;

        Node(Node left, Node right) {
            Objects.requireNonNull(left);
            Objects.requireNonNull(right);
            this.left = left;
            this.right = right;
            this.count = left.count + right.count;
            this.nodeId = totalNodes++;
        }

        Node(char c, int count) {
            this.c = c;
            this.count = count;
            this.nodeId = totalNodes++;
        }

        @Override
        public int compareTo(Node o) {
            if (count == o.count) {
                return Integer.compare(nodeId, o.nodeId);
            }
            return Integer.compare(count, o.count);
        }
    }

}
