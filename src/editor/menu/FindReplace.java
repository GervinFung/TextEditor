package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JMenuItem;

public final class FindReplace extends AbstractMenu {

    public FindReplace(final TextEditorArea textArea) {
        super(textArea, "Find or Replace");
        this.add(this.find());
        this.add(this.findNext());
        this.add(this.findPrevious());
        this.addSeparator();
        this.add(this.replaceAll());
        this.add(this.replaceWord());
    }

    private JMenuItem find() {
        final JMenuItem find = new JMenuItem("Find Word (CTRL + F)");
        find.addActionListener(e -> getTextArea().findReplace());
        return find;
    }

    private JMenuItem replaceAll() {
        final JMenuItem replace = new JMenuItem("Replace All (CTRL + SHIFT + R)");
        replace.addActionListener(e -> getTextArea().findReplace());
        return replace;
    }

    private JMenuItem findNext() {
        final JMenuItem showDate = new JMenuItem("Find Next (F3)");
        showDate.addActionListener(e -> getTextArea().findReplace());
        return showDate;
    }

    private JMenuItem findPrevious() {
        final JMenuItem showDate = new JMenuItem("Find or Replace Word (SHIFT + F3)");
        showDate.addActionListener(e -> getTextArea().findReplace());
        return showDate;
    }

    private JMenuItem replaceWord() {
        final JMenuItem showDate = new JMenuItem("Replace Word (CTRL + R)");
        showDate.addActionListener(e -> getTextArea().findReplace());
        return showDate;
    }
}