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
    private JTextField textFieldFilepathAndSearch;
    private JButton buttonSave;
    private JButton buttonOpen;
    private JButton buttonNextMatch;
    private JButton buttonPreviousMatch;
    private JButton buttonStartSearch;
    private JScrollPane scrollPaneEditor;
    private JMenuBar menuBar;

    private int searchStartPos = -1;
    private int searchEndPos = -1;
    private String searchText = "";

    private final ActionListener actionListenerSave = actionEvent -> {
        try {
            Path filepath = Path.of(textFieldFilepathAndSearch.getText().trim());
            Files.writeString(filepath, textAreaEditor.getText());
            JOptionPane.showMessageDialog(getContentPane(), "Saved", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    private final ActionListener actionListenerLoad = actionEvent -> {
        try {
            Path filepath = Path.of(textFieldFilepathAndSearch.getText());
            textAreaEditor.setText(Files.readString(filepath));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    private final ActionListener actionListenerStartSearch = actionEvent -> {
        String text = textAreaEditor.getText();
        searchText = textFieldFilepathAndSearch.getText();
        searchStartPos = text.indexOf(searchText);

        if (searchStartPos != -1) {
            searchEndPos = searchStartPos + searchText.length();
            textAreaEditor.setCaretPosition(searchEndPos);
            textAreaEditor.select(searchStartPos, searchEndPos);
            textAreaEditor.grabFocus();
        }
    };

    private final ActionListener actionListenerPreviousMatch = actionEvent -> {
        if (searchText.isEmpty()) {
            return;
        }
        String text = textAreaEditor.getText().substring(0, searchEndPos - 1);
        searchStartPos = text.lastIndexOf(searchText);

        if (searchStartPos == -1) { //if the beginning start from the end
            searchStartPos = textAreaEditor.getText().lastIndexOf(searchText);
        }
        if (searchStartPos != -1) {
            searchEndPos = searchStartPos + searchText.length();

            textAreaEditor.setCaretPosition(searchEndPos);
            textAreaEditor.select(searchStartPos, searchEndPos);
            textAreaEditor.grabFocus();
        }
    };

    private final ActionListener actionListenerNextMatch = actionEvent -> {
        if (searchText.isEmpty()) {
            return;
        }
        String text = textAreaEditor.getText();
        searchStartPos = text.indexOf(searchText, searchStartPos + 1/*end*/);

        if (searchStartPos == -1) { //if the end start from the beginning
            searchStartPos = text.indexOf(searchText);
        }
        if (searchStartPos != -1) {
            searchEndPos = searchStartPos + searchText.length();

            textAreaEditor.setCaretPosition(searchEndPos);
            textAreaEditor.select(searchStartPos, searchEndPos);
            textAreaEditor.grabFocus();
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
        int buttonWidth = 28;
        int buttonHeight = 24;
        int widthBetweenComponents = 2;

        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.setBorder(new EmptyBorder(3, 8, 0, 8));


        buttonOpen = new JButton(new ImageIcon("\\icons\\file.png"));
        buttonOpen.setName("OpenButton");
        buttonOpen.addActionListener(actionListenerLoad);
        setFixedSize(buttonOpen, buttonWidth, buttonHeight);

        buttonSave = new JButton(new ImageIcon("\\icons\\save.png"));
        buttonSave.setName("SaveButton");
        buttonSave.addActionListener(actionListenerSave);
        setFixedSize(buttonSave, buttonWidth, buttonHeight);

        textFieldFilepathAndSearch = new JTextField();
        textFieldFilepathAndSearch.setName("SearchField");

        buttonStartSearch = new JButton(new ImageIcon("\\icons\\search.png"));
        buttonStartSearch.setName("StartSearchButton");
        setFixedSize(buttonStartSearch, buttonWidth, buttonHeight);
        buttonStartSearch.addActionListener(actionListenerStartSearch);

        buttonPreviousMatch = new JButton(new ImageIcon("\\icons\\arrowLeft.png"));
        buttonPreviousMatch.setName("PreviousMatchButton");
        setFixedSize(buttonPreviousMatch, buttonWidth, buttonHeight);
        buttonPreviousMatch.addActionListener(actionListenerPreviousMatch);

        buttonNextMatch = new JButton(new ImageIcon("\\icons\\arrowRight.png"));
        buttonNextMatch.setName("NextMatchButton");
        setFixedSize(buttonNextMatch, buttonWidth, buttonHeight);
        buttonNextMatch.addActionListener(actionListenerNextMatch);


        northPanel.add(buttonOpen);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonSave);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(textFieldFilepathAndSearch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonStartSearch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonPreviousMatch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonNextMatch);
    }

    public static JComponent setFixedSize(JComponent component, int width, int height) {
        Dimension dimension = new Dimension(width, height);
        component.setMaximumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMinimumSize(dimension);
        return component;
    }
}
