
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * Blast'em Up (C#) - DIV Games Studio (Horizontal background scroll algorithm)
 * 
 * Originally created and kindly explained 
 * by Kriptok Games (https://www.youtube.com/channel/UC6n3GaKWGKgU4_D7XKo7NjQ)
 * 
 * Reference:
 * https://www.youtube.com/watch?v=E5hFPQrN_o4
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class View extends JPanel {
    
    private Timer timer;
    private BufferedImage image;
    private BufferedImage offscreen;
    private float[][] array;
    private float dx;
    private float dy;

    public View() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("background.jpg"));
            offscreen = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    public void start() {
        dx = 0;
        // pre-compute array
        array = new float[image.getHeight()][image.getWidth()];
        float modifier = 1f / (image.getHeight() / 4f + 150f); // This is just a variable that you can ajust to fit your needs. This value works for me for a 640x480 resolution.
        for (int i = 0; i < image.getWidth(); i++) {
            float u = i - image.getWidth() / 2f;
            for (int j = 0; j < image.getHeight(); j++) {
                if (u != 0) {
                    array[j][i] = (float) (-(u / Math.abs(u)) * Math.pow(Math.abs(u), Math.abs(j - image.getHeight() / 2) * modifier));                        
                    // System.out.println("array[" + j + "][" + i + "] = " + array[j][i]);
                }
            }
        }        
        // main loop
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                repaint();
            }
        }, 100, 1000 / 30);
    }
        
    public void update() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int x = (int) ((i + array[j][i] + dx) % image.getWidth());
                int y = (int) ((j + dy) % image.getHeight());
                if (x >= 0 && x < image.getWidth()) {
                    offscreen.setRGB(i, j, image.getRGB(x, y));
                }
            }
        }
        dx += 2;
        dy += 2;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.drawImage(offscreen, 0, 0, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.setPreferredSize(new Dimension(640, 480));
            
            JFrame frame = new JFrame();
            frame.setTitle("Java Kriptok Scroll Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            view.start();
        });
    }
    
}
