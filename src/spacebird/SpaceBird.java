package spacebird;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import spacebird.RW;


/**
 *
 * @author Dániel
 */
public class SpaceBird implements ActionListener, KeyListener {
    
    
    //fő változók
    
    public static SpaceBird spacebird;                                                      
    public final int WIDTH = 1000;
    public final int HEIGHT = 600;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    public Random rand;
    public RW best = new RW();
    public int bestscore = best.readfile();
    public int level = 1;
    public int speed;
    
    
    public  SpaceBird(){
   
        Timer timer = new Timer(20, this);
        JFrame jframe = new JFrame();
  
        renderer = new Renderer();
        rand = new Random();
        
        jframe.add(renderer);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.setTitle("Space Bird");
        
        jframe.addKeyListener(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();
        
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        
        
        timer.start();
        
    }

    public void addColumn(boolean start){
        
        int space = 150;
        int width = 100;
        int height =100 + rand.nextInt(300);
        
        if(start){
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }else{
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }  
    }
    
    public void paintColumn(Graphics g, Rectangle column){
        if(level == 1){
            g.setColor(Color.white.darker());
        }else if(level == 2){
            g.setColor(Color.gray.darker());   
        }else if(level == 3){
            g.setColor(Color.red.darker());
        }
        g.fillRect(column.x, column.y, column.width, column.height);
    }
    
    public void jump(){
        if(gameOver){
            gameOver = false;
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;
            level = 1;
        
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
        }
        
        if(!started){
            started=true;
  
        }else if(!gameOver){
            if(!gameOver){
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }
  
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;
        if(started){
            if(gameOver){
                speed = 0;
            }else if(level == 2){
                speed = 10;
            }else if(level == 3){
                speed = 13;
            }else
                speed = 7;
        
            for(int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);
                column.x -= speed;
            }
        
            if(ticks % 2 == 0 && yMotion < 15){
                yMotion += 2;
            }
        
            for(int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);
                if(column.x + column.width <0){
                    columns.remove(column);
                    if(column.y == 0){
                        addColumn(false);
                    }
                }
            }
        
            bird.y += yMotion;
        
            for (Rectangle column : columns){
                
                if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width /2 -4 && bird.x + bird.width / 2 < column.x + column.width /2 +4){
                    score++;
                }
                
                if(score>=7){
                    level=2;
                }
                
                if(score>=20){
                    level=3;
                }
                
                if(column.intersects(bird)){
                    gameOver=true;
                    
                    if(bird.x <= column.x){
                        bird.x = column.x - bird.width;
                    }else{
                        if(column.y != 0){
                            bird.y=column.y - bird.height;
                        }else if (bird.y < column.height){
                            bird.y = column.height;
                        }
                    }
                }
            }
        
            if (bird.y > HEIGHT || bird.y < 0){
                gameOver = true;
            }
            
        }
        renderer.repaint();
    }
    
    
    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        g.setColor(Color.white);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        
        for (Rectangle column : columns){
            paintColumn(g, column);
        }
        
        g.setColor(Color.white);
        g.setFont(new Font("Tahoma", 1, 20));
        
        if(gameOver)
        {
            g.drawString("Vége a játéknak!", WIDTH / 2 - 89, HEIGHT / 2 - 50 );
        }
        
        if(!started)
        {
            g.drawString("Nyomd a Space-t!", WIDTH / 2 - 89, HEIGHT / 2 - 50);
        }
        
        if(started)
        {
            g.drawString("Szint: "+String.valueOf(level),299 , 25);
        }
        
        g.drawString("Pont: "+ String.valueOf(score), 15, 25);
       
        if(score > bestscore && gameOver){
            bestscore = score;
            best.writefile(bestscore);
            
        }
        g.drawString("Legjobb: "+ String.valueOf(bestscore), 135, 25);
        
    }


    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if( e.getKeyCode()== KeyEvent.VK_SPACE){
            jump();
        }
    
    }
 
}
