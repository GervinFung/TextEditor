package editor;

import editor.bottompanel.BottomPanel;
import editor.menu.EditMenu;
import editor.menu.FileMenu;
import editor.menu.FindReplace;
import editor.menu.SettingMenu;
import editor.textarea.TextEditorArea;

import javax.swing.JButton;
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
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) { }

        this.bottomPanel = new BottomPanel();

        final TextEditorArea textArea = new TextEditorArea(this, currentFilePath, content);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (textArea.quitApplication()) {
                    textArea.disposeTextEditorSearch();
                    TextEditor.this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } else {
                    TextEditor.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        final JPanel panel = this.createJPanel(textArea);

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
        menuBar.add(new FindReplace(textArea));
        menuBar.setBackground(TEXT_EDITOR_COLOR.DRACULA_MENU_COLOR);

        menuBar.setBorder(null);

        menuBar.setBorderPainted(false);

        return menuBar;
    }

    private JPanel createJPanel(final TextEditorArea textArea) {
        final JPanel panel = new JPanel(new BorderLayout());

        panel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        panel.setBackground(TEXT_EDITOR_COLOR.DRACULA_BACKGROUND_COLOR);
        textArea.setBackground(TEXT_EDITOR_COLOR.DRACULA_BACKGROUND_COLOR);
        textArea.setForeground(TEXT_EDITOR_COLOR.DRACULA_FONT_COLOR);
        panel.add(textArea, BorderLayout.WEST);

        panel.setBorder(null);

        return panel;
    }

    private final static class MyBasicScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors(){
            this.thumbColor = TEXT_EDITOR_COLOR.SCROLL_BAR_THUMB_COLOR;
        }
        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton button = super.createDecreaseButton(orientation);
            button.setBackground(TEXT_EDITOR_COLOR.BOTTOM_PANEL_COLOR);
            return button;
        }
        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton button = super.createIncreaseButton(orientation);
            button.setBackground(TEXT_EDITOR_COLOR.BOTTOM_PANEL_COLOR);
            return button;
        }
    }

    private JScrollPane createScrollPane(final JPanel panel) {
        final JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getHorizontalScrollBar().setBackground(TEXT_EDITOR_COLOR.BOTTOM_PANEL_COLOR);
        scrollPane.getVerticalScrollBar().setBackground(TEXT_EDITOR_COLOR.BOTTOM_PANEL_COLOR);

        scrollPane.getVerticalScrollBar().setUI(new MyBasicScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new MyBasicScrollBarUI());

        scrollPane.setBorder(null);

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