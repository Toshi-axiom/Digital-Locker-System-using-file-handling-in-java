import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class DigitalLockerSwing {
    private static final String LOCKER_DIR = "DigitalLockerFiles";

    private JFrame frame;
    private JTextArea fileContentArea;
    private JTextField filenameField;
    private DefaultListModel<String> fileListModel;
    private JList<String> fileList;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DigitalLockerSwing().createAndShowGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createAndShowGUI() throws IOException {
        // Ensure locker directory exists
        File dir = new File(LOCKER_DIR);
        if (!dir.exists()) dir.mkdir();

        frame = new JFrame("Digital Locker System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        // Layout
        frame.setLayout(new BorderLayout(10,10));
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // Top panel for filename input
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(230, 230, 250));

        JLabel filenameLabel = new JLabel("Filename:");
        filenameField = new JTextField(20);

        topPanel.add(filenameLabel);
        topPanel.add(filenameField);

        // Center split pane for file list and content
        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        refreshFileList();

        JScrollPane listScroll = new JScrollPane(fileList);
        listScroll.setPreferredSize(new Dimension(180, 300));
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileContentArea = new JTextArea();
        fileContentArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane contentScroll = new JScrollPane(fileContentArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, contentScroll);
        splitPane.setDividerLocation(200);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(230, 230, 250));

        JButton saveBtn = new JButton("Save File");
        JButton loadBtn = new JButton("Load File");
        JButton deleteBtn = new JButton("Delete File");
        JButton refreshBtn = new JButton("Refresh List");

        saveBtn.setBackground(new Color(70, 130, 180));
        saveBtn.setForeground(Color.WHITE);
        loadBtn.setBackground(new Color(46, 139, 87));
        loadBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(178, 34, 34));
        deleteBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(new Color(105, 105, 105));
        refreshBtn.setForeground(Color.WHITE);

        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(refreshBtn);

        // Add components to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        saveBtn.addActionListener(e -> saveFile());
        loadBtn.addActionListener(e -> loadFile());
        deleteBtn.addActionListener(e -> deleteFile());
        refreshBtn.addActionListener(e -> refreshFileList());

        // List selection listener to load selected file automatically
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    filenameField.setText(selectedFile);
                    loadFile();
                }
            }
        });

        frame.setVisible(true);
    }

    private void saveFile() {
        String filename = filenameField.getText().trim();
        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a filename.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File file = new File(LOCKER_DIR + File.separator + filename);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(fileContentArea.getText());
            JOptionPane.showMessageDialog(frame, "File saved successfully!");
            refreshFileList();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFile() {
        String filename = filenameField.getText().trim();
        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a filename.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File file = new File(LOCKER_DIR + File.separator + filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            fileContentArea.setText("");
            String line;
            while ((line = br.readLine()) != null) {
                fileContentArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFile() {
        String filename = filenameField.getText().trim();
        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a filename.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File file = new File(LOCKER_DIR + File.separator + filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete \"" + filename + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (file.delete()) {
                JOptionPane.showMessageDialog(frame, "File deleted successfully.");
                fileContentArea.setText("");
                filenameField.setText("");
                refreshFileList();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshFileList() {
        fileListModel.clear();
        File dir = new File(LOCKER_DIR);
        String[] files = dir.list();
        if (files != null) {
            for (String f : files) {
                fileListModel.addElement(f);
            }
        }
    }
}
