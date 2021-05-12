package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JMenuItem;

public final class EditMenu extends AbstractMenu {

    public EditMenu(final TextEditorArea textArea) {
        super(textArea,"Edit");
        this.add(this.cutText());
        this.add(this.copyText());
        this.add(this.pasteText());
        this.add(this.selectAll());
        this.add(this.deleteText());
        this.addSeparator();
        this.add(this.undo());
        this.add(this.redo());
    }

    private JMenuItem cutText() {
        final JMenuItem cutText = new JMenuItem("Cut (CTRL + X)");
        cutText.addActionListener(e -> super.getTextArea().cut());
        return cutText;
    }

    private JMenuItem copyText() {
        final JMenuItem copyText = new JMenuItem("Copy (CTRL + C)");
        copyText.addActionListener(e -> super.getTextArea().copy());
        return copyText;
    }

    private JMenuItem pasteText() {
        final JMenuItem pasteText = new JMenuItem("Paste (CTRL + V)");
        pasteText.addActionListener(e -> super.getTextArea().paste());
        return pasteText;
    }

    private JMenuItem deleteText() {
        final JMenuItem deleteText = new JMenuItem("Delete (DEL)");
        deleteText.addActionListener(e -> super.getTextArea().delete());
        return deleteText;
    }

    private JMenuItem undo() {
        final JMenuItem pasteText = new JMenuItem("Undo (CTRL + Z)");
        pasteText.addActionListener(e -> super.getTextArea().undo());
        return pasteText;
    }

    private JMenuItem redo() {
        final JMenuItem pasteText = new JMenuItem("Redo (CTRL + Y)");
        pasteText.addActionListener(e -> super.getTextArea().redo());
        return pasteText;
    }

    private JMenuItem selectAll() {
        final JMenuItem selectAll = new JMenuItem("Select All (CTRL + A)");
        selectAll.addActionListener(e -> super.getTextArea().selectAll());
        return selectAll;
    }
}