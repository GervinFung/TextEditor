package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JMenu;

import static editor.TextEditorColor.TEXT_EDITOR_COLOR;

public abstract class AbstractMenu extends JMenu {

    private final TextEditorArea textArea;

    protected AbstractMenu(final TextEditorArea textArea, final String title) {
        super(title);
        this.textArea = textArea;
        this.setForeground(TEXT_EDITOR_COLOR.BAR_FONT_COLOR);
        this.setBorder(null);
    }

    protected final TextEditorArea getTextArea() { return this.textArea; }
}