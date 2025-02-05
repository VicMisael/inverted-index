package reverse_index.gui;

import index.InvertedIndex;
import index.csv.RowCollection;
import index.csv.RowItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class CSVSearchFrame extends JFrame {

    private JButton importButton;
    private JTextField searchField;
    private JButton searchButton;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;

    private InvertedIndex invertedIndex;

    public CSVSearchFrame() {
        super("CSV Search GUI with Inverted Index");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(resultList);

        importButton = new JButton("Import CSV");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        JPanel topPanel = new JPanel();
        topPanel.add(importButton);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        importButton.addActionListener(e -> importCSVFile());
        ActionListener searchAction = e -> performSearch();
        searchButton.addActionListener(searchAction);
        searchField.addActionListener(searchAction);
    }

    private void importCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File csvFile = fileChooser.getSelectedFile();
            loadCSVData(csvFile);
        }
    }

    private void loadCSVData(File csvFile) {
        listModel.clear();
        RowCollection rowCollection = new RowCollection(csvFile.getAbsolutePath());
        invertedIndex = new InvertedIndex(rowCollection);
        rowCollection.forEach(x -> listModel.addElement(x.toString()));
    }

    private void performSearch() {
        if (invertedIndex == null) {
            JOptionPane.showMessageDialog(this, "No CSV file loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = searchField.getText().trim();
        List<RowItem> results = invertedIndex.find(query);
        listModel.clear();
        results.forEach(item -> listModel.addElement(wrapText(item.getAsChild().toString(), 150)));
    }

    private String wrapText(String text, int lineLength) {
        StringBuilder wrappedText = new StringBuilder();
        while (text.length() > lineLength) {
            int breakPoint = text.lastIndexOf(' ', lineLength);
            if (breakPoint == -1) breakPoint = lineLength;
            wrappedText.append(text.substring(0, breakPoint)).append("<br>");
            text = text.substring(breakPoint).trim();
        }
        wrappedText.append(text);
        return "<html>" + wrappedText.toString() + "</html>";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CSVSearchFrame frame = new CSVSearchFrame();
            frame.setVisible(true);
        });
    }
}
