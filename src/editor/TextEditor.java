package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
    private JMenuBar menuBar;

    private final ActionListener actionListenerSave = actionEvent -> {
        try {
            Path filepath = Path.of(textFieldFilepath.getText().trim());
            Files.writeString(filepath, textAreaEditor.getText());
            JOptionPane.showMessageDialog(getContentPane(), "Saved", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    private final ActionListener actionListenerLoad = actionEvent -> {
        try {
            Path filepath = Path.of(textFieldFilepath.getText());
            textAreaEditor.setText(Files.readString(filepath));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    private final ActionListener actionListenerExit = actionEvent -> dispose();

    public TextEditor() {
        createView();
    }

    private void createView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setTitle("Text Editor");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createMenuBar();
        createMainField();
        createNorthPanel();

        setJMenuBar(menuBar);
        add(scrollPaneEditor, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setName("MenuBar");

        JMenu menuFile = new JMenu("File");
        menuFile.setName("MenuFile");
        menuFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItemSave = new JMenuItem("Save");
        menuItemSave.addActionListener(actionListenerSave);
        menuItemSave.setName("MenuSave");
        JMenuItem menuItemLoad = new JMenuItem("Load");
        menuItemLoad.addActionListener(actionListenerLoad);
        menuItemLoad.setName("MenuLoad");
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(actionListenerExit);
        menuItemExit.setName("MenuExit");

        menuFile.add(menuItemLoad);
        menuFile.add(menuItemSave);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);

        menuBar.add(menuFile);
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
        buttonSave.addActionListener(actionListenerSave);

        buttonLoad = new JButton("Load");
        buttonLoad.setName("LoadButton");
        buttonLoad.addActionListener(actionListenerLoad);

        northPanel.add(textFieldFilepath);
        northPanel.add(Box.createHorizontalStrut(2));
        northPanel.add(buttonLoad);
        northPanel.add(Box.createHorizontalStrut(2));
        northPanel.add(buttonSave);
    }
}
