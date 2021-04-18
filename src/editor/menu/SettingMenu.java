package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.Color;

public final class SettingMenu extends AbstractMenu {

    public SettingMenu(final TextEditorArea textArea, final JPanel panel) {
        super(textArea, "Setting");
        this.add(this.changeTextAreaColor(panel));
        this.add(this.changeTextFontColor());
        this.addSeparator();
        this.add(this.zoomOut());
        this.add(this.zoomIn());
        this.addSeparator();
        this.add(this.showDate());
        this.add(this.showSystemName());
        this.addSeparator();
        this.add(this.goToLine());
    }

    private JMenuItem changeTextAreaColor(final JPanel panel) {
        final JMenuItem changeTextAreaColor = new JMenuItem("Change Text Area Color");
        changeTextAreaColor.addActionListener(e -> {
            final Color color = JColorChooser.showDialog(this.getParent(),"Select a color", Color.WHITE);
            panel.setBackground(color);
            getTextArea().setBackground(color);
        });
        return changeTextAreaColor;
    }

    private JMenuItem changeTextFontColor() {
        final JMenuItem changeTextAreaColor = new JMenuItem("Change Text Font Color");
        changeTextAreaColor.addActionListener(e -> {
            final Color color = JColorChooser.showDialog(this.getParent(),"Select a color", Color.BLACK);
            getTextArea().setForeground(color);
        });
        return changeTextAreaColor;
    }

    private JMenuItem zoomOut() {
        final JMenuItem zoomOut = new JMenuItem("Zoom Out (F1)");
        zoomOut.addActionListener(e -> getTextArea().zoomOut());
        return zoomOut;
    }

    private JMenuItem zoomIn() {
        final JMenuItem zoomOut = new JMenuItem("Zoom In (F2)");
        zoomOut.addActionListener(e -> getTextArea().zoomIn());
        return zoomOut;
    }

    private JMenuItem showSystemName() {
        final JMenuItem showDate = new JMenuItem("Show Computer Name (F4)");
        showDate.addActionListener(e -> getTextArea().writeSystemName());
        return showDate;
    }

    private JMenuItem showDate() {
        final JMenuItem showDate = new JMenuItem("Show Date (F5)");
        showDate.addActionListener(e -> getTextArea().writeDate());
        return showDate;
    }

    private JMenuItem goToLine() {
        final JMenuItem showDate = new JMenuItem("Go To Line (CTRL + G)");
        showDate.addActionListener(e -> getTextArea().goToLine());
        return showDate;
    }
}