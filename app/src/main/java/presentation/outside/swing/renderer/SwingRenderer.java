package presentation.outside.swing.renderer;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class SwingRenderer implements ContentRenderer {
    private Graphics2D g2;

    public void setGraphics2D(Graphics2D g2) {
        this.g2 = g2;
    }

    @Override
    public int drawText(String text, int x, int y, float w, int vgap, int hgap, int padding) {
        if (text == null) return y;

        Graphics2D g = (Graphics2D) g2.create();

        int startX = x + hgap + padding;
        int startY = y + vgap;
        float maxWidth = w - (hgap * 2) - (padding * 2);

        int finalY = drawWrappedText(g, text, startX, startY, maxWidth);

        g.dispose();

        return finalY;
    }

    public int measureTextHeight(Graphics2D g, String text, int maxWidth) {
        if (text == null || text.isEmpty()) return 0;
        
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split("\\s+");
        String currentLine = "";
        int lineCount = 1; 

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                lineCount++; 
                currentLine = word;
            } else {
                currentLine = testLine;
            }
        }
        
        return lineCount * fm.getHeight();
    }

    private int drawWrappedText(Graphics2D g, String text, int x, int y, float maxWidth) {
        if (text == null || text.isEmpty()) return y;
        
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split("\\s+");
        String currentLine = "";
        int currentY = y + fm.getAscent(); 

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g.drawString(currentLine, x, currentY);
                currentLine = word;
                currentY += fm.getHeight();
            } else {
                currentLine = testLine;
            }
        }
        
        g.drawString(currentLine, x, currentY);
        return currentY + fm.getDescent(); 
    }
}
