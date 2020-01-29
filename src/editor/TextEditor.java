package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextEditor extends JFrame {
    private JTextArea textAreaEditor;
    private JPanel northPanel;
    private JTextField textFieldFilepath;
    private JButton buttonSave;
    private JButton buttonLoad;
    private JScrollPane scrollPaneEditor;

    public TextEditor() {
        createView();
    }

    private void createView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setTitle("Text Editor");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createMainField();
        createNorthPanel();

        add(scrollPaneEditor, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void createMainField() {
        textAreaEditor = new JTextArea();
        textAreaEditor.setName("TextArea");

        scrollPaneEditor = new JScrollPane(textAreaEditor);
        scrollPaneEditor.setName("ScrollPane");
        scrollPaneEditor.setBorder(new EmptyBorder(3, 3, 3, 3));
    }

    private void createNorthPanel() {
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.setBorder(new EmptyBorder(3, 8, 0, 8));

        textFieldFilepath = new JTextField();
        textFieldFilepath.setName("FilenameField");

        buttonSave = new JButton("Save");
        buttonSave.setName("SaveButton");
        buttonSave.addActionListener(actionEvent -> {
            try {
                Path filepath = Path.of(textFieldFilepath.getText().trim());
                Files.writeString(filepath, textAreaEditor.getText());
                JOptionPane.showMessageDialog(getContentPane(), "Saved", "Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonLoad = new JButton("Load");
        buttonLoad.setName("LoadButton");
        buttonLoad.addActionListener(actionEvent -> {
            try {
                Path filepath = Path.of(textFieldFilepath.getText());
                textAreaEditor.setText(Files.readString(filepath));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        northPanel.add(textFieldFilepath);
        northPanel.add(Box.createHorizontalStrut(2));
        northPanel.add(buttonLoad);
        northPanel.add(Box.createHorizontalStrut(2));
        northPanel.add(buttonSave);
    }
}
