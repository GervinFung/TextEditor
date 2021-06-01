package editor.textarea;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class TextEditorSearch extends JFrame {

    private static final String SEARCH = "Enter Word To Search", REPLACE = "Enter Word To Replace";
    private static final Highlighter.HighlightPainter PAINTER_SEARCH_ALL = new DefaultHighlighter.DefaultHighlightPainter(Color.PINK);
    private static final Highlighter.HighlightPainter PAINTER_SEARCH_SPECIFIC = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);

    private final JTextField wordToSearch, replaceWord;
    
    public TextEditorSearch(final TextEditorArea textEditorArea) {
        super("Search or Replace");
        final JPanel textFieldPanel = new JPanel(new BorderLayout());


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                textEditorArea.setTextEditorSearchToNull();
                TextEditorSearch.this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        
        this.wordToSearch = new JTextField(SEARCH);
        this.replaceWord = new JTextField(REPLACE);
        
        this.wordToSearch.addFocusListener(new TextFieldFocusListener(SEARCH, this.wordToSearch));
        this.replaceWord.addFocusListener(new TextFieldFocusListener(REPLACE, this.replaceWord));

        textFieldPanel.add(this.wordToSearch, BorderLayout.NORTH);
        textFieldPanel.add(this.replaceWord, BorderLayout.SOUTH);

        this.add(textFieldPanel, BorderLayout.NORTH);
        this.add(this.buttonPanel(textEditorArea), BorderLayout.SOUTH);

        this.setMinimumSize(new Dimension(470, 200));
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
        this.setResizable(false);
    }

    private void searchWord(final TextEditorArea textEditorArea) {
        final String word = this.wordToSearch.getText();
        final String textAreaText = textEditorArea.getText();
        textEditorArea.getHighlighter().removeAllHighlights();
        int index = textAreaText.indexOf(word);
        if (index >= 0) {
            while(true){
                try {
                    final int len = word.length();
                    textEditorArea.getHighlighter().addHighlight(index,index + len, PAINTER_SEARCH_ALL);
                    index = textAreaText.indexOf(word, index + len);
                    if (index == -1) {
                        return;
                    }
                } catch (final BadLocationException e) { JOptionPane.showMessageDialog(this.getParent(), e.getMessage()); }
            }
        } else {
            JOptionPane.showMessageDialog(this.getParent(), "No Such Keyword. Please try again");
        }
    }

    private void replaceWord(final TextEditorArea textEditorArea) {
        final String searchText = this.wordToSearch.getText();
        final int length = searchText.length();
        final int index = textEditorArea.getText().indexOf(searchText, textEditorArea.getCaretPosition() - length);
        if (index >= 0) {
            textEditorArea.replaceRange(this.replaceWord.getText(), index, index + searchText.length());
        }
    }

    private void replaceAllWord(final TextEditorArea textEditorArea) {
        final String searchText = this.wordToSearch.getText();
        final String textAreaText = textEditorArea.getText();
        int index = textAreaText.indexOf(searchText);
        final int len = searchText.length();
        if (index >= 0) {
            while(true){
                if (index >= 0) {
                    textEditorArea.replaceRange(this.replaceWord.getText(), index, index + len);
                }
                else {
                    return;
                }
                index = textEditorArea.getText().indexOf(searchText, index + len);
            }
        }
    }
    
    private JPanel buttonPanel(final TextEditorArea textEditorArea) {
        final JPanel buttonPanel = new JPanel();
        
        final JButton searchButton = new JButton("Search All");
        final JButton replaceAllButton = new JButton("Replace All");

        final JButton nextButton = new JButton("Next");
        final JButton previousButton = new JButton("Previous");
        final JButton replaceButton = new JButton("Replace");

        searchButton.addActionListener(e -> this.searchWord(textEditorArea));
        replaceAllButton.addActionListener(e -> this.replaceAllWord(textEditorArea));

        nextButton.addActionListener(e -> {
            final int index = this.findNextWordIndex(textEditorArea);
            if (index >= 0) {
                this.findNextWord(textEditorArea, index);
            } else {
                final int caretPos = textEditorArea.getCaretPosition();
                try {
                    textEditorArea.setCaretPosition(textEditorArea.getLineStartOffset(0));
                    final int anotherIndex = this.findNextWordIndex(textEditorArea);
                    if (anotherIndex >= 0) {
                        this.findNextWord(textEditorArea, anotherIndex);
                    } else {
                        textEditorArea.setCaretPosition(caretPos);
                        JOptionPane.showMessageDialog(this.getParent(), "No Such Keyword. Please try again");
                    }
                } catch (final BadLocationException badLocationException) { JOptionPane.showMessageDialog(this.getParent(), badLocationException.getMessage()); }
            }
        });

        previousButton.addActionListener(e -> {
            final int index = this.findPreviousWordIndex(textEditorArea);
            if (index >= 0) {
                this.findPreviousWord(textEditorArea, index);
            } else {
                final int caretPos = textEditorArea.getCaretPosition();
                try {
                    textEditorArea.setCaretPosition(textEditorArea.getLineStartOffset(textEditorArea.getText().split("\n").length - 1));
                    final int anotherIndex = this.findPreviousWordIndex(textEditorArea);
                    if (anotherIndex >= 0) {
                        this.findPreviousWord(textEditorArea, anotherIndex);
                    } else {
                        textEditorArea.setCaretPosition(caretPos);
                        JOptionPane.showMessageDialog(this.getParent(), "No Such Keyword. Please try again");
                    }
                } catch (final BadLocationException badLocationException) { JOptionPane.showMessageDialog(this.getParent(), badLocationException.getMessage()); }
            }
        });

        replaceButton.addActionListener(e -> TextEditorSearch.this.replaceWord(textEditorArea));

        buttonPanel.add(searchButton);
        buttonPanel.add(replaceAllButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(replaceButton);
        
        return buttonPanel;
    }

    private int findNextWordIndex(final TextEditorArea textEditorArea) {
        final String searchText = this.wordToSearch.getText();
        final int length = searchText.length();
        return textEditorArea.getText().indexOf(searchText, textEditorArea.getCaretPosition() + length);
    }

    private int findPreviousWordIndex(final TextEditorArea textEditorArea) {
        final String searchText = this.wordToSearch.getText();
        final int length = searchText.length();
        return textEditorArea.getText().lastIndexOf(searchText, textEditorArea.getCaretPosition() - length);
    }

    private void findNextWord(final TextEditorArea textEditorArea, final int index) {
        if (index == -1) {
            throw new IllegalArgumentException("Index cannot be less than 0");
        }
        try {
            textEditorArea.setCaretPosition(index);
            textEditorArea.getHighlighter().removeAllHighlights();
            textEditorArea.getHighlighter().addHighlight(index, index + this.wordToSearch.getText().length(), PAINTER_SEARCH_SPECIFIC);
        } catch (final BadLocationException badLocationException) { JOptionPane.showMessageDialog(this.getParent(), badLocationException.getMessage()); }
    }

    private void findPreviousWord(final TextEditorArea textEditorArea, final int index) {
        if (index == -1) {
            throw new IllegalArgumentException("Index cannot be less than 0");
        }
        try {
            textEditorArea.setCaretPosition(index);
            textEditorArea.getHighlighter().removeAllHighlights();
            textEditorArea.getHighlighter().addHighlight(index, index + this.wordToSearch.getText().length(), PAINTER_SEARCH_SPECIFIC);
        } catch (final BadLocationException badLocationException) { JOptionPane.showMessageDialog(textEditorArea.getParent(), badLocationException.getMessage()); }
    }

    private final static class TextFieldFocusListener implements FocusListener {

        private final String defaultPlaceHolder;
        private final JTextField textField;

        private TextFieldFocusListener(final String defaultPlaceHolder, final JTextField textField) {
            this.defaultPlaceHolder = defaultPlaceHolder;
            this.textField = textField;
            this.textField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(final FocusEvent e) {
            if (defaultPlaceHolder.equals(this.textField.getText())) {
                this.textField.setForeground(Color.BLACK);
                this.textField.setText("");
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            if (this.textField.getText().isEmpty()) {
                this.textField.setText(defaultPlaceHolder);
                this.textField.setForeground(Color.GRAY);
            }
            else {
                this.textField.setForeground(Color.BLACK);
            }
        }
    }
}