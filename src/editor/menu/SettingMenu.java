package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.Color;

import static editor.TextEditorColor.TEXT_EDITOR_COLOR;

public final class SettingMenu extends JMenu {

    public SettingMenu(final TextEditorArea textArea, final JPanel panel) {
        super("Setting");
        this.setForeground(TEXT_EDITOR_COLOR.BAR_FONT_COLOR);
        this.add(this.changeTextAreaColor(textArea, panel));
        this.add(this.changeTextFontColor(textArea));
        this.addSeparator();
        this.add(this.zoomOut(textArea));
        this.add(this.zoomIn(textArea));
    }

    private JMenuItem changeTextAreaColor(final TextEditorArea textArea, final JPanel panel) {
        final JMenuItem changeTextAreaColor = new JMenuItem("Change Text Area Color");
        changeTextAreaColor.addActionListener(e -> {
            final Color color = JColorChooser.showDialog(this.getParent(),"Select a color", Color.WHITE);
            panel.setBackground(color);
            textArea.setBackground(color);
        });
        return changeTextAreaColor;
    }

    private JMenuItem changeTextFontColor(final TextEditorArea textArea) {
        final JMenuItem changeTextAreaColor = new JMenuItem("Change Text Font Color");
        changeTextAreaColor.addActionListener(e -> {
            final Color color = JColorChooser.showDialog(this.getParent(),"Select a color", Color.BLACK);
            textArea.setForeground(color);
        });
        return changeTextAreaColor;
    }

    private JMenuItem zoomOut(final TextEditorArea textArea) {
        final JMenuItem zoomOut = new JMenuItem("Zoom Out (F1)");
        zoomOut.addActionListener(e -> textArea.zoomOut());
        return zoomOut;
    }

    private JMenuItem zoomIn(final TextEditorArea textArea) {
        final JMenuItem zoomOut = new JMenuItem("Zoom In (F2)");
        zoomOut.addActionListener(e -> textArea.zoomIn());
        return zoomOut;
    }
}