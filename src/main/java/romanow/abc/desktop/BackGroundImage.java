package romanow.abc.desktop;

import javax.swing.*;
import java.awt.*;

public class BackGroundImage  extends JPanel {
    private String ifile;
    private ScreenMode screen;
    public BackGroundImage(String imageFile,ScreenMode screen0){
        ifile = imageFile;
        screen = screen0;
        }
    @Override
    public void paint(Graphics g){
        Image im = null;
        try {
            ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/"+ifile));
            im = icon.getImage();
            } catch (Exception e) {
                System.out.println(e.toString());
                }
        g.drawImage(im, 0, 0, screen.ScreenW, screen.ScreenH,null);
        }
    public static void main(String aa[]){
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 450);
        frame.setContentPane(new BackGroundImage("temp.jpg",new ScreenMode("",450,500)));
        frame.setVisible(true);
        JButton button = new JButton();
        button.setBounds(100,200,100,30);
        button.setText("12345");
        frame.getContentPane().add(button);
        }
}
