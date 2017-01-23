/**
 *
 *
 *
 * @author	Timur Khairulin
 * @version
 */

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{
    public Window() {
        Field pan = new Field();
        Container cont = getContentPane();
        cont.add(pan);
        setTitle("Game \"Sea Battle\"");
        setBounds(150, 150, 900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

}