package ui;

import javax.swing.*;
import java.awt.*;

public class SwingHelper {

    public static JFrame createWindow(String title)
    {
        JFrame frame = new JFrame(title);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        return frame;
    }


    public static void centerShowWindow(JFrame frame) {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getComponent(0).requestFocusInWindow();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void packScaleWindow(JFrame frame, double sx, double sy) {
        frame.pack();
        frame.setSize((int) (frame.getWidth() * sx), (int) (frame.getHeight() * sy));
    }

    public static void quickPreview(Component component) {
        JFrame frame = new JFrame("Quick Preview");
        frame.add(component);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void scaleFont(JComponent component, float factor) {
        Font font = component.getFont();
        Font scaledFont = font.deriveFont(font.getSize2D() * factor);
        component.setFont(scaledFont);
    }

    public static class GBC extends GridBagConstraints {
        public GBC() {
        }

        public GBC x(int x) {
            gridx = x;
            return this;
        }
        public GBC y(int y) {
            gridy = y;
            return this;
        }

        public GBC width(int w) {
            gridwidth = w;
            return this;
        }
        public GBC height(int h) {
            gridheight = h;
            return this;
        }

        public GBC fill(int f) {
            fill = f;
            return this;
        }
        public GBC anchor(int a) {
            anchor = a;
            return this;
        }

        public GBC pad(int p) {
            return padx(p).pady(p);
        }
        public GBC padx(int px) {
            ipadx = px;
            return this;
        }
        public GBC pady(int py) {
            ipady = py;
            return this;
        }

        public GBC insets(int p) {
            insets = new Insets(p, p, p, p);
            return this;
        }
        public GBC insets(int p, int q, int r, int s) {
            insets = new Insets(p, q, r, s);
            return this;
        }

        public GBC weightx(double wx) {
            weightx = wx;
            return this;
        }
        public GBC weighty(double wy) {
            weighty = wy;
            return this;
        }

    }
}
