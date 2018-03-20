package spacebird;
import java.awt.Graphics;
import javax.swing.JPanel;
/**
 *
 * @author Dániel
 */
public class Renderer extends JPanel {
    private static final long serialVersion= 1L;
   
    protected void paintComponent(Graphics g)       
    {
        super.paintComponent(g);
        
        SpaceBird.spacebird.repaint(g);
    }
    
}