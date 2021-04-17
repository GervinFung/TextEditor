package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import static editor.TextEditorColor.TEXT_EDITOR_COLOR;

public final class FileMenu extends JMenu {

    public FileMenu(final TextEditorArea textArea) {
        super("File");
        this.add(this.newFileMenuItem(textArea));
        this.add(this.newWindowMenuItem(textArea));
        this.addSeparator();
		this.add(this.saveFileMenuItem(textArea));
        this.add(this.saveAsMenuItem(textArea));
        this.addSeparator();
		this.add(this.openFileMenuItem(textArea));
		this.add(this.openWindowMenuItem(textArea));
        this.addSeparator();
		this.add(this.printFile(textArea));
		this.add(this.quitFileMenuItem(textArea));
		this.setForeground(TEXT_EDITOR_COLOR.BAR_FONT_COLOR);
    }


    private JMenuItem newFileMenuItem(final TextEditorArea textArea) {
        final JMenuItem newFile = new JMenuItem("New File (CTRL + N)");
        newFile.addActionListener(e -> textArea.createNewFile());
        return newFile;
    }

    private JMenuItem newWindowMenuItem(final TextEditorArea textArea) {
        final JMenuItem newWindow = new JMenuItem("New Window (CTRL + SHIFT + N)");
        newWindow.addActionListener(e -> textArea.createNewWindow());
        return newWindow;
    }

    private JMenuItem saveFileMenuItem(final TextEditorArea textArea) {
        final JMenuItem saveFile = new JMenuItem("Save File (CTRL + S)");
        saveFile.addActionListener(e -> textArea.saveFile());
        return saveFile;
    }

    private JMenuItem saveAsMenuItem(final TextEditorArea textArea) {
        final JMenuItem saveFile = new JMenuItem("Save As (CTRL + SHIFT + S)");
        saveFile.addActionListener(e -> textArea.saveAs());
        return saveFile;
    }

    private JMenuItem openFileMenuItem(final TextEditorArea textArea) {
        final JMenuItem openFile = new JMenuItem("Open File (CTRL + O)");
        openFile.addActionListener(e -> textArea.saveFileBeforeOpening());
        return openFile;
    }

    private JMenuItem openWindowMenuItem(final TextEditorArea textArea) {
        final JMenuItem openWindow = new JMenuItem("Open File In New Window (CTRL + SHIFT + O)");
        openWindow.addActionListener(e -> textArea.openFileInNewWindow());
        return openWindow;
    }

    private JMenuItem printFile(final TextEditorArea textArea) {
        final JMenuItem printFile = new JMenuItem("Print File Content (CTRL + P)");
        printFile.addActionListener(e -> textArea.printFile());
        return printFile;
    }

    private JMenuItem quitFileMenuItem(final TextEditorArea textArea) {
        final JMenuItem quitFile = new JMenuItem("Quit Application (CTRL + Q)");
        quitFile.addActionListener(e -> textArea.quitApplication());
        return quitFile;
    }
}