package life;

import java.awt.*;

public class DeadPanel extends AbstractPanel {
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        // cast Graphics to Graphics2D
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.DARK_GRAY); // sets Graphics2D color
        g2.drawRect(0,0,30,30); // drawRect(x-position, y-position, width, height)
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,30,30); // fill new rectangle with color black
    }
}
