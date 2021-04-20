package editor.bottompanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;

import static editor.TextEditorColor.TEXT_EDITOR_COLOR;

public final class BottomPanel extends JPanel {

    private final JLabel label;

    public BottomPanel() {
        this.label = this.cursorPosition();
        this.add(this.label);
        this.setBackground(TEXT_EDITOR_COLOR.BOTTOM_PANEL_COLOR);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private JLabel cursorPosition() {
        final JLabel label = new JLabel();
        label.setVisible(true);
        label.setForeground(TEXT_EDITOR_COLOR.BAR_FONT_COLOR);
        return label;
    }

    public void updateCursorPosition(final int lineNumber, int columnNumber) {
        this.label.setText("Line: " + lineNumber + " Column: " + columnNumber);
    }
}