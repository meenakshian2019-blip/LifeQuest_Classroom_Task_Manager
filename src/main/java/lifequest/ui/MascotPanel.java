package lifequest.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/** Lumi, the little fox-like quest companion, drawn with Java2D (no image files needed). */
public class MascotPanel extends JPanel {
    public MascotPanel() { setOpaque(false); setPreferredSize(new Dimension(190, 210)); }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int cx = w / 2;
        g.setPaint(new RadialGradientPaint(new Point2D.Float(cx, h * .55f), Math.max(60, w * .48f),
                new float[]{0f, 1f}, new Color[]{new Color(107, 64, 255, 100), new Color(10, 15, 38, 0)}));
        g.fillOval(5, 5, w - 10, h - 10);
        // Floating sparkles
        g.setColor(new Color(115, 190, 255));
        star(g, cx - 61, 45, 5); star(g, cx + 61, 66, 4); star(g, cx + 43, 25, 3);
        // Ears
        Path2D leftEar = new Path2D.Float(); leftEar.moveTo(cx-44, 91); leftEar.lineTo(cx-39, 35); leftEar.quadTo(cx-16, 48, cx-13, 84); leftEar.closePath();
        Path2D rightEar = new Path2D.Float(); rightEar.moveTo(cx+13, 84); rightEar.quadTo(cx+16, 48, cx+39, 35); rightEar.lineTo(cx+44, 91); rightEar.closePath();
        g.setColor(new Color(46, 47, 102)); g.fill(leftEar); g.fill(rightEar);
        g.setColor(new Color(189, 150, 255)); g.fill(new Ellipse2D.Float(cx-36, 51, 12, 28)); g.fill(new Ellipse2D.Float(cx+24, 51, 12, 28));
        // Hood
        g.setPaint(new GradientPaint(cx-48, 55, new Color(41, 35, 101), cx+45, 145, new Color(11, 20, 55)));
        g.fill(new Ellipse2D.Float(cx-55, 54, 110, 113));
        g.setColor(new Color(131, 86, 255)); g.setStroke(new BasicStroke(2f)); g.draw(new Ellipse2D.Float(cx-55, 54, 110, 113));
        // Face
        g.setColor(new Color(240, 226, 255)); g.fill(new Ellipse2D.Float(cx-36, 76, 72, 73));
        // Eyes
        g.setColor(new Color(41, 31, 90)); g.fill(new Ellipse2D.Float(cx-23, 101, 15, 21)); g.fill(new Ellipse2D.Float(cx+8, 101, 15, 21));
        g.setColor(Color.WHITE); g.fillOval(cx-20, 104, 5, 6); g.fillOval(cx+11, 104, 5, 6);
        // Nose and smile
        g.setColor(new Color(220, 112, 184)); g.fill(new Ellipse2D.Float(cx-4, 121, 8, 5));
        g.setColor(new Color(104, 59, 131)); g.setStroke(new BasicStroke(1.5f));
        g.draw(new Arc2D.Float(cx-10, 121, 10, 10, 220, 100, Arc2D.OPEN)); g.draw(new Arc2D.Float(cx, 121, 10, 10, 220, 100, Arc2D.OPEN));
        // Cloak/body
        Path2D cloak = new Path2D.Float(); cloak.moveTo(cx-34, 143); cloak.quadTo(cx-49, 161, cx-41, 181); cloak.quadTo(cx, 198, cx+41, 181); cloak.quadTo(cx+49, 161, cx+34, 143); cloak.closePath();
        g.setPaint(new GradientPaint(cx-30, 145, new Color(32, 39, 92), cx+30, 190, new Color(9, 16, 41)));
        g.fill(cloak); g.setColor(new Color(100, 89, 207)); g.draw(cloak);
        // Crystal pendant
        Path2D crystal = new Path2D.Float(); crystal.moveTo(cx, 146); crystal.lineTo(cx+9, 157); crystal.lineTo(cx, 170); crystal.lineTo(cx-9, 157); crystal.closePath();
        g.setPaint(new GradientPaint(cx-7, 150, new Color(95, 245, 255), cx+7, 168, new Color(139, 71, 255))); g.fill(crystal);
        g.setColor(new Color(192, 211, 255)); g.draw(crystal);
        // Base shadow
        g.setColor(new Color(3, 6, 19, 140)); g.fill(new Ellipse2D.Float(cx-48, 183, 96, 13));
        g.dispose();
    }

    private void star(Graphics2D g, int x, int y, int r) {
        g.setStroke(new BasicStroke(1.5f)); g.drawLine(x-r, y, x+r, y); g.drawLine(x, y-r, x, y+r);
    }
}
