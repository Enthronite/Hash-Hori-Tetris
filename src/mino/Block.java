package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle
{

    public int x, y;
    public static final int SIZE = 30; //30x30 block
    public Color c;
    
    public Block(Color c) 
    {
        this.c = c;
    }

     public void draw(Graphics2D g2) 
     {
         int margin = 2;
         int width = Block.SIZE - (margin * 2);
         int height = Block.SIZE - (margin * 2);
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int strWidth = fm.stringWidth("#");
        int strHeight = fm.getAscent();
        int cx = x + margin + (width - strWidth) / 2;
        int cy = y + margin + (height + strHeight) / 2 - fm.getDescent();
        g2.setColor(c);
        g2.drawString("#", cx, cy);
     }

    // public void draw(Graphics g)
    // {
    //     super.draw(g);
    //     g.setFont(new Font("Monospaced", Font.BOLD, 18));
    //     for (Block block : blocks) 
    //         {
    //         g.setColor(block.c);
    //         // Draw '#' at the block's position
    //         g.drawString("#", block.x * cellSize, (block.y + 1) * cellSize);
    //         }
    // }

}