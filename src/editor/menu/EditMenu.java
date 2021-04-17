package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import static editor.TextEditorColor.TEXT_EDITOR_COLOR;

public final class EditMenu extends JMenu {

    public EditMenu(final TextEditorArea textArea) {
        super("Edit");
        this.add(this.cutText(textArea));
        this.add(this.copyText(textArea));
        this.add(this.pasteText(textArea));
        this.add(this.selectAll(textArea));
        this.addSeparator();
        this.add(this.undo(textArea));
        this.add(this.redo(textArea));
        this.addSeparator();
        this.add(this.showDate(textArea));
        this.add(this.showSystemName(textArea));
        this.setForeground(TEXT_EDITOR_COLOR.BAR_FONT_COLOR);
    }

    private JMenuItem cutText(final TextEditorArea textArea) {
        final JMenuItem cutText = new JMenuItem("Cut (CTRL + X)");
        cutText.addActionListener(e -> textArea.cut());
        return cutText;
    }

    private JMenuItem copyText(final TextEditorArea textArea) {
        final JMenuItem copyText = new JMenuItem("Copy (CTRL + C)");
        copyText.addActionListener(e -> textArea.copy());
        return copyText;
    }

    private JMenuItem pasteText(final TextEditorArea textArea) {
        final JMenuItem pasteText = new JMenuItem("Paste (CTRL + V)");
        pasteText.addActionListener(e -> textArea.paste());
        return pasteText;
    }

    private JMenuItem undo(final TextEditorArea textArea) {
        final JMenuItem pasteText = new JMenuItem("Undo (CTRL + Z)");
        pasteText.addActionListener(e -> textArea.undo());
        return pasteText;
    }

    private JMenuItem redo(final TextEditorArea textArea) {
        final JMenuItem pasteText = new JMenuItem("Redo (CTRL + Y)");
        pasteText.addActionListener(e -> textArea.redo());
        return pasteText;
    }

    private JMenuItem selectAll(final TextEditorArea textArea) {
        final JMenuItem selectAll = new JMenuItem("Select All (CTRL + A)");
        selectAll.addActionListener(e -> textArea.selectAll());
        return selectAll;
    }

    private JMenuItem showSystemName(final TextEditorArea textArea) {
        final JMenuItem showDate = new JMenuItem("Show Computer Name (F4)");
        showDate.addActionListener(e -> textArea.writeSystemName());
        return showDate;
    }

    private JMenuItem showDate(final TextEditorArea textArea) {
        final JMenuItem showDate = new JMenuItem("Show Date (F5)");
        showDate.addActionListener(e -> textArea.writeDate());
        return showDate;
    }
}