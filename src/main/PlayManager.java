package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;
import mino.Block;
import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class PlayManager {

    //Main Play Area

    // final int WIDTH = 360;
    // final int HEIGHT = 600;

//--------------------------------------
    final int WIDTH = 600;
    final int HEIGHT = 360;
    public static int  left_x;
    public static int right_x;
    public static int bottom_y;
    public static int top_y;


    //Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
    

    //Others
    public static int dropInterval = 60; //mino drops every 60 frames
    boolean gameOver;

    // Effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    //Score
    int level = 1;
    int lines;
    int score;

public PlayManager() {

    //Main Play Area Frame

    left_x = (GamePanel.WIDTH/2) - (WIDTH/2);
    right_x = left_x + WIDTH;
    top_y = 50;
    bottom_y = top_y + HEIGHT;

    MINO_START_X = left_x + Block.SIZE;
    MINO_START_Y = top_y + (HEIGHT / 2);

    NEXTMINO_X = right_x + 175;
    NEXTMINO_Y = top_y + 500;

    
    //Set the starting Mino

    currentMino = pickMino(); 
    currentMino.setXY(MINO_START_X, MINO_START_Y);
    nextMino = pickMino();
    nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

    }
    private Mino pickMino() {
        
        // Pick a Random Mino
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch(i) {
            case 0: mino = new Mino_L1();break;
            case 1: mino = new Mino_L2();break;
            case 2: mino = new Mino_Square();break;
            case 3: mino = new Mino_Bar();break;
            case 4: mino = new Mino_T(); break;
            case 5: mino = new Mino_Z1(); break;
            case 6: mino = new Mino_Z2();break;
        }
        return mino;
    }
    public void update() {

        //Check if the currentMino is Active
        if(currentMino.active == false) {
            // if the mino is not active, put it into the staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //check if the game is over
            if(currentMino.b[0].y == MINO_START_Y && currentMino.b[0].x == MINO_START_X) {
                // this means the currentMino immediately collided a block and couldn't move at all
                // so it's xy are the same with the nextMino's
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }

            currentMino.deactivating = false;

            // replace the currenMino with the nextMino

            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);

            // when a mino becomes inactive, check if line(s) can be deleted
            checkDelete();
        }
        else {
            currentMino.update();
        }
    }
    private void checkDelete() {

        int lineCount = 0;
        int x = right_x - Block.SIZE;

        while(x >= left_x) {
            int blockCount = 0;

            for(int i = 0; i < staticBlocks.size(); i++) {
                if(staticBlocks.get(i).x == x) {
                    blockCount++;
                }
            }

            if(blockCount == HEIGHT / Block.SIZE) {

                effectCounterOn = true;
                effectY.add(x);

                for(int i = staticBlocks.size()-1; i >= 0; i--) {
                    if(staticBlocks.get(i).x == x) {
                        staticBlocks.remove(i);
                    }
                }

                lineCount++;
                lines++;

                if(lines % 10 == 0 && dropInterval > 1) {
                    level++;
                    if(dropInterval > 10) {
                        dropInterval -= 10;
                    } else {
                        dropInterval -= 1;
                    }
                }

                for(int i = 0; i < staticBlocks.size(); i++) {
                    if(staticBlocks.get(i).x < x) {
                        staticBlocks.get(i).x += Block.SIZE;
                    }
                }
                // Re-check same x: blocks from the left shifted into this column

            } else {
                x -= Block.SIZE;
            }
        }

        if(lineCount > 0) {
            GamePanel.se.play(1, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }
    public void draw(Graphics2D g2) {

        // Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        //Draw Next Mino Frame
        int x = right_x + 90;
        int y = bottom_y + 40;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);


        // Draw Score Frame
        g2.drawRect(x, top_y, 200, 300);
        x += 40;
        y = top_y + 80;
        g2.drawString("LEVEL: " + level, x, y); y+= 70;
        g2.drawString("LINES: " + level, x, y); y+= 70;
        g2.drawString("SCORE: " + score, x, y);


        //Draw the currentMino
        if(currentMino != null) {
            currentMino.draw(g2);
        }
        // Draw the nextMino
        nextMino.draw(g2);

        // Draw Static Blocks
        for(int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        //Draw Effect
        if(effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.red);
            for(int i = 0; i < effectY.size(); i++) {
                g2.fillRect(effectY.get(i), top_y, Block.SIZE, HEIGHT);
            }

            if(effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // Draw Pause or Game Over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont(). deriveFont(50f));
        if(gameOver) {
            x = left_x + 130;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }
        else if(KeyHandler.pausePressed) {
            x = left_x + 200;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        // Draw the Game Title
        x = 450;
        y = top_y + 620;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 40));
        g2.drawString("# Hash Hori Tetris ", x + 20, y);

        //Draw the Instructions
        x = 100;
        y = top_y + 380;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g2.drawString("HOW TO PLAY: ", x + 50, y);

        x = 130;
        y = top_y + 430;
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("[W]", x + 40, y + 20);

        x = 91;
        y = top_y + 460;
        g2.drawString("[A][S][D]", x + 40, y + 20);

        x = 100;
        y = top_y + 510;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g2.drawString("OR ", x + 70, y + 20);

        x = 130;
        y = top_y + 570;
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("[^]", x + 40, y + 20);


        x = 91;
        y = top_y + 600;
        g2.drawString("[<][V][>]", x + 40, y + 20);
    }
}