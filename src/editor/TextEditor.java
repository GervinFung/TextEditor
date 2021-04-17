package editor;

import editor.bottompanel.BottomPanel;
import editor.menu.EditMenu;
import editor.menu.FileMenu;
import editor.menu.SettingMenu;
import editor.textarea.TextEditorArea;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static editor.TextEditorColor.TEXT_EDITOR_COLOR;

public final class TextEditor extends JFrame {

    private static final String TEXT_EDITOR = "Java Text Editor";

    private TextEditor() { this(null, null, TEXT_EDITOR); }

    private final BottomPanel bottomPanel;

    private TextEditor(final String content, final String currentFilePath, final String fileName) {
        super(fileName);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) { }

        final TextEditorArea textArea = new TextEditorArea(this, currentFilePath, content);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (textArea.quitApplication()) {
                    TextEditor.this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else {
                    TextEditor.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        final JPanel panel = this.createJPanel(textArea);

        this.bottomPanel = new BottomPanel();

        this.add(this.createScrollPane(panel));
        this.add(this.bottomPanel, BorderLayout.SOUTH);

        this.setJMenuBar(this.createJMenuBar(textArea, panel));
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void updateCursorPosition(final int lineNumber, int columnNumber) {
        this.bottomPanel.updateCursorPosition(lineNumber, columnNumber);
    }

    private JMenuBar createJMenuBar(final TextEditorArea textArea, final JPanel panel) {
        final JMenuBar menuBar = new JMenuBar();

        menuBar.add(new FileMenu(textArea));
        menuBar.add(new EditMenu(textArea));
        menuBar.add(new SettingMenu(textArea, panel));
        menuBar.setBackground(TEXT_EDITOR_COLOR.BAR_COLOR);

        return menuBar;
    }

    private JPanel createJPanel(final TextEditorArea textArea) {
        final JPanel panel = new JPanel(new BorderLayout());

        panel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        panel.setBackground(TEXT_EDITOR_COLOR.TEXT_AREA_COLOR);
        panel.add(textArea, BorderLayout.WEST);

        return panel;
    }

    private JScrollPane createScrollPane(final JPanel panel) {
        final JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final BasicScrollBarUI basicScrollBarUI = new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors(){
                this.thumbColor = TEXT_EDITOR_COLOR.SCROLL_BAR_THUMB_COLOR;
            }
        };

        scrollPane.getVerticalScrollBar().setUI(basicScrollBarUI);
        scrollPane.getHorizontalScrollBar().setUI(basicScrollBarUI);

        return scrollPane;
    }

    public static void createNewTextEditor() {
        SwingUtilities.invokeLater(TextEditor::new);
    }

    public static void createNewTextEditorWithFilePath(final String content, final String currentFilePath, final String fileName) {
        SwingUtilities.invokeLater(() -> new TextEditor(content, currentFilePath, fileName));
    }

    public static void main(final String[] args) {
        createNewTextEditor();
    }
}