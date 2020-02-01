package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextArea textAreaEditor;
    private JPanel northPanel;
    private JTextField textFieldSearch;
    private JButton buttonSave;
    private JButton buttonOpen;
    private JButton buttonNextMatch;
    private JButton buttonPreviousMatch;
    private JButton buttonStartSearch;
    private JScrollPane scrollPaneEditor;
    private JMenuBar menuBar;
    private JFileChooser fileChooser;
    private JCheckBox checkBoxUseRegEx;

    private int searchStartPos = -1;
    private int searchEndPos = -1;
    private boolean useRegEx = false;
    private String searchRegEx = "";
    private String searchText = "";

    private final ActionListener actionListenerSave = actionEvent -> {
        int choice = fileChooser.showSaveDialog(null);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Path filepath = fileChooser.getSelectedFile().toPath();
            Files.writeString(filepath, textAreaEditor.getText());
            JOptionPane.showMessageDialog(getContentPane(), "Saved", "Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    };

    private final ActionListener actionListenerOpen = actionEvent -> {
        int choice = fileChooser.showOpenDialog(null);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Path filepath = fileChooser.getSelectedFile().toPath();
            textAreaEditor.setText(Files.readString(filepath));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getContentPane(), "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    };

    Pattern pattern;
    Matcher matcher;
    private final ActionListener actionListenerStartSearch = actionEvent -> {
        useRegEx = checkBoxUseRegEx.isSelected();
        String text = textAreaEditor.getText();

        if (!useRegEx) {
            searchText = textFieldSearch.getText();
            searchStartPos = text.indexOf(searchText);
        } else {
            searchRegEx = textFieldSearch.getText();

            pattern = Pattern.compile(searchRegEx);
            matcher = pattern.matcher(text);
            if (matcher.find()) {
                searchText = matcher.group();
                searchStartPos = matcher.start();
            } else {
                searchStartPos = -1;
            }
        }

        if (searchStartPos != -1) {  //method select text if match
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
        String text = textAreaEditor.getText().substring(0, searchEndPos -1);

        if (!useRegEx) {
            searchStartPos = text.lastIndexOf(searchText);
            if (searchStartPos == -1) { //if the beginning start from the end //search
                searchStartPos = textAreaEditor.getText().lastIndexOf(searchText);
            }
        } else {
            matcher = pattern.matcher(text); //update in case user edited text
            String foundLastMatch = "";
            int startPos = -1;

            if (matcher.find(0)) {
                foundLastMatch = matcher.group();
                startPos = matcher.start();
            } else { //if the beginning start from the end //regEx search
                text = textAreaEditor.getText();
                matcher = pattern.matcher(text);
            }

            while (matcher.find()) {
                foundLastMatch = matcher.group();
                startPos = matcher.start();
            }
            if (startPos != -1) {
                searchText = foundLastMatch;
                searchStartPos = startPos;
            } else {

                searchStartPos = -1;
            }
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
        if (!useRegEx) {
            searchStartPos = text.indexOf(searchText, searchStartPos + 1/*end*/);
            if (searchStartPos == -1) { //if the end start from the beginning //search
                searchStartPos = text.indexOf(searchText);
            }
        } else {
            matcher = pattern.matcher(text); //update in case user edited text
            if (matcher.find(searchStartPos + 1)) {
                searchStartPos = matcher.start();
                searchText = matcher.group();
            } else { //if the end start from the beginning //regEx search
                if (matcher.find(0)) {
                    searchStartPos = matcher.start();
                    searchText = matcher.group();
                } else {
                    searchStartPos = -1;
                }
            }
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
        fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        add(fileChooser);

        createMenuBar();
        createMainField();
        createNorthPanel();

        setJMenuBar(menuBar);
        add(scrollPaneEditor, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void createMenuBar() {
        //menu file
        JMenu menuFile = new JMenu("File");
        menuFile.setName("MenuFile");
        menuFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItemSave = new JMenuItem("Save");
        menuItemSave.addActionListener(actionListenerSave);
        menuItemSave.setName("MenuSave");
        JMenuItem menuItemOpen = new JMenuItem("Open");
        menuItemOpen.addActionListener(actionListenerOpen);
        menuItemOpen.setName("MenuOpen");
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(actionListenerExit);
        menuItemExit.setName("MenuExit");

        menuFile.add(menuItemOpen);
        menuFile.add(menuItemSave);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);

        //menu search
        JMenu menuSearch = new JMenu("Search");
        menuSearch.setName("MenuSearch");
        menuSearch.setMnemonic(KeyEvent.VK_S);

        JMenuItem menuItemStartSearch = new JMenuItem("Start search");
        menuItemStartSearch.addActionListener(actionListenerStartSearch);
        menuItemStartSearch.setName("MenuStartSearch");
        JMenuItem menuItemPreviousMatch = new JMenuItem("Previous match");
        menuItemPreviousMatch.addActionListener(actionListenerPreviousMatch);
        menuItemPreviousMatch.setName("MenuPreviousMatch");
        JMenuItem menuItemNextMatch = new JMenuItem("Next mach");
        menuItemNextMatch.addActionListener(actionListenerNextMatch);
        menuItemNextMatch.setName("MenuNextMatch");
        JMenuItem menuItemUseRegEx = new JMenuItem("Use regex");
        menuItemUseRegEx.addActionListener(actionEvent -> {
            checkBoxUseRegEx.setSelected(!checkBoxUseRegEx.isSelected());
        });
        menuItemUseRegEx.setName("MenuUseRegExp");


        menuSearch.add(menuItemStartSearch);
        menuSearch.add(menuItemPreviousMatch);
        menuSearch.add(menuItemNextMatch);
        menuSearch.add(menuItemUseRegEx);

        //menu bar
        menuBar = new JMenuBar();
        menuBar.setName("MenuBar");
        menuBar.add(menuFile);
        menuBar.add(menuSearch);
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
        buttonOpen.addActionListener(actionListenerOpen);
        setFixedSize(buttonOpen, buttonWidth, buttonHeight);

        buttonSave = new JButton(new ImageIcon("\\icons\\save.png"));
        buttonSave.setName("SaveButton");
        buttonSave.addActionListener(actionListenerSave);
        setFixedSize(buttonSave, buttonWidth, buttonHeight);

        textFieldSearch = new JTextField();
        textFieldSearch.setName("SearchField");

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


        checkBoxUseRegEx = new JCheckBox("Use regex");
        checkBoxUseRegEx.setName("UseRegExCheckbox");

        northPanel.add(buttonOpen);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonSave);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(textFieldSearch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonStartSearch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonPreviousMatch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(buttonNextMatch);
        northPanel.add(Box.createHorizontalStrut(widthBetweenComponents));
        northPanel.add(checkBoxUseRegEx);
    }

    public static JComponent setFixedSize(JComponent component, int width, int height) {
        Dimension dimension = new Dimension(width, height);
        component.setMaximumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMinimumSize(dimension);
        return component;
    }
}
