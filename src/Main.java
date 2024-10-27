
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import ui.FirstWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Theme and Font setup
        FlatMacDarkLaf.setup();
        Font defaultFont = UIManager.getFont("defaultFont");
        UIManager.put("defaultFont", defaultFont.deriveFont(defaultFont.getSize2D() * 1.25f));


        FirstWindow.show();


    }
}
