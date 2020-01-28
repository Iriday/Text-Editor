package editor;

import javax.swing.*;

public class TextEditor extends JFrame {

    public TextEditor() {
        createView();
    }

    private void createView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("Text Editor");
        setLocationRelativeTo(null);
        setLayout(null);

        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");
        jTextArea.setSize(300, 300);
        jTextArea.setLocation(0, 0);

        add(jTextArea);

        setVisible(true);
    }
}
