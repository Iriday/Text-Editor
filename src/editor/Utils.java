package editor;

import javax.swing.*;
import java.awt.*;

public class Utils {
    public static JComponent setFixedSize(JComponent component, int width, int height) {
        Dimension dimension = new Dimension(width, height);
        component.setMaximumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMinimumSize(dimension);
        return component;
    }

    public static JTextArea selectText(JTextArea textArea, int startPos, int endPos) {
        textArea.setCaretPosition(endPos);
        textArea.select(startPos, endPos);
        textArea.grabFocus();
        return textArea;
    }
}
