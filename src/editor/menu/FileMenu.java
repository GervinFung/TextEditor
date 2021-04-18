package editor.menu;

import editor.textarea.TextEditorArea;

import javax.swing.JMenuItem;

public final class FileMenu extends AbstractMenu {

    public FileMenu(final TextEditorArea textArea) {
        super(textArea, "File");
        this.add(this.newFileMenuItem());
        this.add(this.newWindowMenuItem());
        this.addSeparator();
		this.add(this.saveFileMenuItem());
        this.add(this.saveAsMenuItem());
        this.addSeparator();
		this.add(this.openFileMenuItem());
		this.add(this.openWindowMenuItem());
        this.addSeparator();
		this.add(this.printFile());
		this.add(this.quitFileMenuItem());
    }

    private JMenuItem newFileMenuItem() {
        final JMenuItem newFile = new JMenuItem("New File (CTRL + N)");
        newFile.addActionListener(e -> super.getTextArea().createNewFile());
        return newFile;
    }

    private JMenuItem newWindowMenuItem() {
        final JMenuItem newWindow = new JMenuItem("New Window (CTRL + SHIFT + N)");
        newWindow.addActionListener(e -> this.getTextArea().createNewWindow());
        return newWindow;
    }

    private JMenuItem saveFileMenuItem() {
        final JMenuItem saveFile = new JMenuItem("Save File (CTRL + S)");
        saveFile.addActionListener(e -> this.getTextArea().saveFile());
        return saveFile;
    }

    private JMenuItem saveAsMenuItem() {
        final JMenuItem saveFile = new JMenuItem("Save As (CTRL + SHIFT + S)");
        saveFile.addActionListener(e -> this.getTextArea().saveAs());
        return saveFile;
    }

    private JMenuItem openFileMenuItem() {
        final JMenuItem openFile = new JMenuItem("Open File (CTRL + O)");
        openFile.addActionListener(e -> this.getTextArea().saveFileBeforeOpening());
        return openFile;
    }

    private JMenuItem openWindowMenuItem() {
        final JMenuItem openWindow = new JMenuItem("Open File In New Window (CTRL + SHIFT + O)");
        openWindow.addActionListener(e -> this.getTextArea().openFileInNewWindow());
        return openWindow;
    }

    private JMenuItem printFile() {
        final JMenuItem printFile = new JMenuItem("Print File Content (CTRL + P)");
        printFile.addActionListener(e -> this.getTextArea().printFile());
        return printFile;
    }

    private JMenuItem quitFileMenuItem() {
        final JMenuItem quitFile = new JMenuItem("Quit Application (CTRL + Q)");
        quitFile.addActionListener(e -> this.getTextArea().quitApplication());
        return quitFile;
    }
}