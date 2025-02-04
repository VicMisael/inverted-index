package reverse_index.gui;

import index.InvertedIndex;
import index.abstractions.IRowItem;
import index.csv.RowCollection;
import index.csv.RowItem;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CSVSearchFrame extends JFrame {

    private JButton importButton;
    private JTextField searchField;
    private JButton searchButton;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;

    // These will be created once a CSV is loaded.
    private RowCollection rowCollection;
    private InvertedIndex invertedIndex;

    public CSVSearchFrame() {
        super("CSV Search GUI with Inverted Index");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);  // Center the window

        // Setup list and its model.
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(resultList);

        // Create controls.
        importButton = new JButton("Import CSV");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Place controls in a panel.
        JPanel topPanel = new JPanel();
        topPanel.add(importButton);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Layout the frame.
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Register action listeners.
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importCSVFile();
            }
        });

        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        };
        searchButton.addActionListener(searchAction);
        searchField.addActionListener(searchAction);
    }

    /**
     * Opens a file chooser and loads the CSV using RowCollection’s CSV-loading constructor.
     */
    private void importCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File csvFile = fileChooser.getSelectedFile();
            loadCSVData(csvFile);
        }
    }

    /**
     * Loads CSV data from the given file using the RowCollection constructor
     * and builds the inverted index.
     */
    private void loadCSVData(File csvFile) {
        listModel.clear();
        // Use your existing CSV-loading method:
        rowCollection = new RowCollection(csvFile.getAbsolutePath());
        // Build the inverted index from the loaded rows.
        invertedIndex = new InvertedIndex(rowCollection);
        // Optionally, display all rows.
        for (RowItem item : rowCollection) {
            listModel.addElement(item.toString());
        }
    }

    /**
     * Performs a search using the inverted index and updates the result list.
     */
    private void performSearch() {
        if (invertedIndex == null) {
            JOptionPane.showMessageDialog(this, "No CSV file loaded.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String query = searchField.getText().trim();
        List<RowItem> results = invertedIndex.find(query);
        listModel.clear();
        for (RowItem item : results) {
            listModel.addElement(item.getAsChild().toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CSVSearchFrame frame = new CSVSearchFrame();
            frame.setVisible(true);
        });
    }
}