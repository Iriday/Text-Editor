package editor;

import javax.swing.*;
import java.awt.*;

public class TextEditor extends JFrame {

    public TextEditor() {
        createView();
    }

    private void createView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setTitle("Text Editor");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");

        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setName("ScrollPane");

        add(jScrollPane);

        setVisible(true);
    }
}
