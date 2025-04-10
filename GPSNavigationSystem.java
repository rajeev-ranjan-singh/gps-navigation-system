import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GPSNavigationSystem extends JFrame {

    private final String[] places = {"A", "B", "C", "D", "E"};
    private final int[][] graph = {
        {0, 10, 0, 30, 100},
        {10, 0, 50, 0, 0},
        {0, 50, 0, 20, 10},
        {30, 0, 20, 0, 60},
        {100, 0, 10, 60, 0}
    };

    private JComboBox<String> sourceBox;
    private JComboBox<String> destinationBox;
    private JTextArea resultArea;

    public GPSNavigationSystem() {
        setTitle("GPS Navigation System - Shortest Path Finder");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Source:"));
        sourceBox = new JComboBox<>(places);
        topPanel.add(sourceBox);

        topPanel.add(new JLabel("Destination:"));
        destinationBox = new JComboBox<>(places);
        topPanel.add(destinationBox);

        JButton findButton = new JButton("Find Shortest Path");
        topPanel.add(findButton);

        add(topPanel, BorderLayout.NORTH);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Button Action
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findShortestPath();
            }
        });

        setVisible(true);
    }

    private void findShortestPath() {
        int source = sourceBox.getSelectedIndex();
        int destination = destinationBox.getSelectedIndex();

        int[] distance = new int[places.length];
        boolean[] visited = new boolean[places.length];
        int[] prev = new int[places.length];
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);

        distance[source] = 0;

        for (int i = 0; i < places.length; i++) {
            int u = getMinDistanceNode(distance, visited);
            visited[u] = true;

            for (int v = 0; v < places.length; v++) {
                if (!visited[v] && graph[u][v] != 0 &&
                        distance[u] + graph[u][v] < distance[v]) {
                    distance[v] = distance[u] + graph[u][v];
                    prev[v] = u;
                }
            }
        }

        // Build path
        List<Integer> path = new ArrayList<>();
        for (int at = destination; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);

        // Display results
        StringBuilder sb = new StringBuilder();
        sb.append("Shortest Path from ").append(places[source])
          .append(" to ").append(places[destination]).append(":\n");

        for (int i = 0; i < path.size(); i++) {
            sb.append(places[path.get(i)]);
            if (i < path.size() - 1) sb.append(" -> ");
        }

        sb.append("\nTotal Distance: ").append(distance[destination]).append(" km");
        resultArea.setText(sb.toString());
    }

    private int getMinDistanceNode(int[] distance, boolean[] visited) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int i = 0; i < distance.length; i++) {
            if (!visited[i] && distance[i] < min) {
                min = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPSNavigationSystem());
    }
}
