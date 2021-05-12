package editor.textarea;

import editor.TextEditor;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static editor.TextEditor.createNewTextEditor;
import static editor.TextEditor.createNewTextEditorWithFilePath;
import static editor.textarea.KeyboardKey.KEYBOARD_KEY;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.NORMAL;

public final class TextEditorArea extends JTextArea {

    private static final File USER_HOME_DIRECTORY = new File(System.getProperty("user.home"));
    private static final JFileChooser FILE_CHOOSER = new JFileChooser(USER_HOME_DIRECTORY);
    private static final int DEFAULT_FONT_SIZE = 15;

    private static final UndoManager UNDO_MANAGER = new UndoManager();

    private String currentFilePath, currentContent;
    private TextEditorSearch textEditorSearch;

    private final TextEditor textEditor;

    public void zoomOut() { this.setFont(this.getFont().deriveFont((float)this.getFont().getSize() - 1)); }

    public void zoomIn() { this.setFont(this.getFont().deriveFont((float)this.getFont().getSize() + 1)); }

    public void setTextEditorSearchToNull() { this.textEditorSearch = null; }

    public void disposeTextEditorSearch() {
        if (this.textEditorSearch != null) {
            this.textEditorSearch.dispose();
        }
    }

    public TextEditorArea(final TextEditor textEditor, final String currentFilePath, final String currentContent) {
        this.currentContent = Objects.requireNonNullElse(currentContent, "");
        this.setText(this.currentContent);
        this.currentFilePath = currentFilePath;
        this.textEditorSearch = null;
        this.setFont(new Font("Consolas", Font.PLAIN, DEFAULT_FONT_SIZE));
        this.textEditor = textEditor;

        this.textEditor.updateCursorPosition(this.currentContent.split("\n").length,1);

        this.getDocument().addUndoableEditListener(e -> UNDO_MANAGER.addEdit(e.getEdit()));

        this.getActionMap().put(KEYBOARD_KEY.UNDO_KEY, new AbstractAction(KEYBOARD_KEY.UNDO_KEY) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.undo(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.REDO_KEY, new AbstractAction(KEYBOARD_KEY.REDO_KEY) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.redo(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.NEW_FILE, new AbstractAction(KEYBOARD_KEY.NEW_FILE) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.createNewFile(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.NEW_WINDOW, new AbstractAction(KEYBOARD_KEY.NEW_WINDOW) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.createNewWindow(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.SAVE_FILE, new AbstractAction(KEYBOARD_KEY.SAVE_FILE) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.saveFile(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.SAVE_AS, new AbstractAction(KEYBOARD_KEY.SAVE_AS) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.saveAs(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.OPEN_FILE, new AbstractAction(KEYBOARD_KEY.OPEN_FILE) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.saveFileBeforeOpening(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.OPEN_WINDOW, new AbstractAction(KEYBOARD_KEY.OPEN_WINDOW) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.openFileInNewWindow(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.QUIT_FILE, new AbstractAction(KEYBOARD_KEY.QUIT_FILE) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.quitApplication(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.PRINT_FILE, new AbstractAction(KEYBOARD_KEY.PRINT_FILE) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.printFile(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.WRITE_DATE, new AbstractAction(KEYBOARD_KEY.WRITE_DATE) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.writeDate(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.WRITE_COMPUTER_NAME, new AbstractAction(KEYBOARD_KEY.WRITE_COMPUTER_NAME) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.writeSystemName(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.ZOOM_IN, new AbstractAction(KEYBOARD_KEY.ZOOM_IN) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.zoomIn(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.ZOOM_OUT, new AbstractAction(KEYBOARD_KEY.ZOOM_OUT) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.zoomOut(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.FIND_WORD, new AbstractAction(KEYBOARD_KEY.FIND_WORD) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.findReplace(); }
        });

        this.getActionMap().put(KEYBOARD_KEY.GOTO, new AbstractAction(KEYBOARD_KEY.GOTO) {
            @Override
            public void actionPerformed(final ActionEvent evt) { TextEditorArea.this.goToLine(); }
        });

        this.getInputMap().put(KeyStroke.getKeyStroke("control Z"), KEYBOARD_KEY.UNDO_KEY);
        this.getInputMap().put(KeyStroke.getKeyStroke("control Y"), KEYBOARD_KEY.REDO_KEY);
        this.getInputMap().put(KeyStroke.getKeyStroke("control N"), KEYBOARD_KEY.NEW_FILE);
        this.getInputMap().put(KeyStroke.getKeyStroke("control shift N"), KEYBOARD_KEY.NEW_WINDOW);
        this.getInputMap().put(KeyStroke.getKeyStroke("control shift S"), KEYBOARD_KEY.SAVE_AS);
        this.getInputMap().put(KeyStroke.getKeyStroke("control S"), KEYBOARD_KEY.SAVE_FILE);
        this.getInputMap().put(KeyStroke.getKeyStroke("control O"), KEYBOARD_KEY.OPEN_FILE);
        this.getInputMap().put(KeyStroke.getKeyStroke("control shift O"), KEYBOARD_KEY.OPEN_WINDOW);
        this.getInputMap().put(KeyStroke.getKeyStroke("control Q"), KEYBOARD_KEY.QUIT_FILE);
        this.getInputMap().put(KeyStroke.getKeyStroke("control P"), KEYBOARD_KEY.PRINT_FILE);
        this.getInputMap().put(KeyStroke.getKeyStroke("F5"), KEYBOARD_KEY.WRITE_DATE);
        this.getInputMap().put(KeyStroke.getKeyStroke("F4"), KEYBOARD_KEY.WRITE_COMPUTER_NAME);
        this.getInputMap().put(KeyStroke.getKeyStroke("F2"), KEYBOARD_KEY.ZOOM_IN);
        this.getInputMap().put(KeyStroke.getKeyStroke("F1"), KEYBOARD_KEY.ZOOM_OUT);
        this.getInputMap().put(KeyStroke.getKeyStroke("control F"), KEYBOARD_KEY.FIND_WORD);
        this.getInputMap().put(KeyStroke.getKeyStroke("control shift R"), KEYBOARD_KEY.FIND_WORD);
        this.getInputMap().put(KeyStroke.getKeyStroke("F3"), KEYBOARD_KEY.FIND_WORD);
        this.getInputMap().put(KeyStroke.getKeyStroke("shift F3"), KEYBOARD_KEY.FIND_WORD);
        this.getInputMap().put(KeyStroke.getKeyStroke("control R"), KEYBOARD_KEY.FIND_WORD);
        this.getInputMap().put(KeyStroke.getKeyStroke("control G"), KEYBOARD_KEY.GOTO);

        this.addCaretListener(e -> {
            try {
                final int caretPosition = TextEditorArea.this.getCaretPosition();
                final int lineNumber = TextEditorArea.this.getLineOfOffset(caretPosition);
                final int columnNumber = caretPosition - TextEditorArea.this.getLineStartOffset(lineNumber);
                this.textEditor.updateCursorPosition(lineNumber + 1, columnNumber + 1);
            } catch (final BadLocationException ignored) { }
        });
    }

    public void delete() {
        if (this.getText().isEmpty()) { return; }
        this.replaceSelection("");
    }

    public void undo() {
        try { if (UNDO_MANAGER.canUndo()) { UNDO_MANAGER.undo(); } }
        catch (final CannotUndoException ignored) {}
    }

    public void redo() {
        try { if (UNDO_MANAGER.canRedo()) { UNDO_MANAGER.redo(); } }
        catch (final CannotRedoException ignored) {}
    }

    public void createNewFile() {
        if (this.getText().isEmpty()) { return; }
        final int option = JOptionPane.showConfirmDialog(this.getParent(),"Do you want to save before opening new file?", "New File", JOptionPane.YES_NO_CANCEL_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.saveAs();
            this.setText("");
            this.currentFilePath = null;
        } else if (option == JOptionPane.NO_OPTION) {
            this.setText("");
            this.currentFilePath = null;
        }
    }

    public void createNewWindow() { createNewTextEditor(); }

    public void save(final String absolutePath) {
        if (absolutePath == null) {
            this.saveAs();
            return;
        }
        try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath, false))) {

            bufferedWriter.write(this.getText());
            bufferedWriter.flush();

            this.currentFilePath = absolutePath;

            final String fileName = FILE_CHOOSER.getSelectedFile().getName();

            this.textEditor.setTitle(fileName);

            this.currentContent = this.getText();

            JOptionPane.showMessageDialog(this.getParent(), fileName + " successfully saved!");

        } catch (final IOException ioException) { ioException.printStackTrace(); }
    }

    public void saveFile() {
        this.save(this.currentFilePath);
    }

    public void saveAs() {
        final int option = FILE_CHOOSER.showSaveDialog(this.getParent());

        if (option == JFileChooser.APPROVE_OPTION) {

            if (FILE_CHOOSER.getSelectedFile().exists()) {
                final int overrideFile = JOptionPane.showConfirmDialog(
                        this.getParent(),
                        "Do you want to override " + FILE_CHOOSER.getSelectedFile().getName() + "?",
                        "Override File",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (overrideFile == JOptionPane.YES_OPTION) {
                    this.save(FILE_CHOOSER.getSelectedFile().getAbsolutePath());
                } else if (overrideFile == JOptionPane.NO_OPTION) {
                    this.saveFile();
                }
            } else {
                this.save(FILE_CHOOSER.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public void saveFileBeforeOpening() {
        if (this.getText().isEmpty() || this.currentContent.equals(this.getText())) {
            this.setText(this.readFile(false));
        } else {
            final int option = JOptionPane.showConfirmDialog(this.getParent(),"Do you want to save before opening saved file?", "Open File", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                this.saveFile();
                this.setText(this.readFile(false));
            } else if (option == JOptionPane.NO_OPTION) {
                this.setText(this.readFile(false));
            }
        }
    }

    public String readFile(final boolean openInNewWindow) {
        final int option = FILE_CHOOSER.showOpenDialog(this.getParent());

        if (option == JFileChooser.APPROVE_OPTION) {

            try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_CHOOSER.getSelectedFile().getAbsolutePath()))) {

                final StringBuilder stringBuilder = new StringBuilder();

                final List<String> allLines = bufferedReader.lines().collect(Collectors.toList());

                final int size = allLines.size() - 1;

                for (int i = 0; i <= size; i++) {
                    if (i == size) {
                        stringBuilder.append(allLines.get(i));
                    } else {
                        stringBuilder.append(allLines.get(i)).append("\n");
                    }
                }

                if (!openInNewWindow) {
                    this.currentFilePath = FILE_CHOOSER.getSelectedFile().getAbsolutePath();
                    this.textEditor.setTitle(FILE_CHOOSER.getSelectedFile().getName());
                    this.currentContent = stringBuilder.toString();
                }

                return stringBuilder.toString();

            } catch (final IOException exception) { JOptionPane.showMessageDialog(this.getParent(), exception.getMessage()); }
        }
        return null;
    }

    public void openFileInNewWindow() {
        try {
            final String fileContent = this.readFile(true);
            if (fileContent == null) { return; }
            createNewTextEditorWithFilePath(fileContent, FILE_CHOOSER.getSelectedFile().getAbsolutePath(), FILE_CHOOSER.getSelectedFile().getName());
        }
        catch (final NullPointerException ignored) { JOptionPane.showMessageDialog(this.getParent(), "No File Selected."); }
    }

    public boolean quitApplication() {
        if (this.getText().isEmpty() || this.currentContent.equals(this.getText())) {
            final int option = JOptionPane.showConfirmDialog(this.getParent(),"Do you want to quit application?", "Quit Application", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                this.textEditor.dispose();
                return true;
            }
            return false;
        }
        return saveFileBeforeQuit();
    }

    private boolean saveFileBeforeQuit() {
        final int option = JOptionPane.showConfirmDialog(this.getParent(),"Do you want to save before quitting application?", "Quit Application", JOptionPane.YES_NO_CANCEL_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (this.currentFilePath == null) { this.saveAs(); }
            else { this.saveFile(); }
            this.textEditor.dispose();
            return true;
        } else if (option == JOptionPane.NO_OPTION) {
            this.textEditor.dispose();
            return true;
        }
        return false;
    }

    public void printFile() {
        try { this.print(); }
        catch (final PrinterException printerException) { JOptionPane.showMessageDialog(this.getParent(), printerException.getMessage()); }
    }

    public void writeDate() { this.append(new Date().toString()); }

    public void writeSystemName() {
        try { this.append(InetAddress.getLocalHost().getHostName()); }
        catch (final UnknownHostException e) { JOptionPane.showMessageDialog(this.getParent(), e.getMessage()); }
    }

    public void findReplace() {
        SwingUtilities.invokeLater(() ->  {
            if (this.textEditorSearch == null) {
                this.textEditorSearch = new TextEditorSearch(this);
            } else if (this.textEditorSearch.getState() == ICONIFIED) {
                this.textEditorSearch.setState(NORMAL);
            } else {
                this.textEditorSearch.toFront();
            }
        });
    }

    public void goToLine() {

        try {

            final int goToLineNumber;

            final int maxLength = this.getText().split("\n").length;

            while(true) {
                final String input = JOptionPane.showInputDialog(this.getParent(), "Which Line Do You Want To Go To?");
                if (tryParseIntInRange(input, maxLength)) {
                    goToLineNumber = Integer.parseInt(input) - 1;
                    break;
                } else {
                    JOptionPane.showMessageDialog(this.getParent(), "Please enter number from 1 to " + maxLength + " only!", "Input Warning", JOptionPane.WARNING_MESSAGE);
                }
            }

            this.setCaretPosition(this.getLineStartOffset(goToLineNumber));

        } catch (final BadLocationException e) { JOptionPane.showMessageDialog(this.getParent(), e.getMessage()); }
    }

    private static boolean tryParseIntInRange(final String data, final int max) {
        try {
            final int temp = Integer.parseInt(data);
            if (temp >= 1 && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }
}