/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import java.awt.*;

/**
 *
 * @author romanow
 */
public class Message extends ABCBaseView {
    private I_Button ok;
    /**
     * Creates new form OK
     */
    public Message(int x,int y,String title,int delay0) {
        this(x,y,title,null);
        delayIt(delay0);
        }
    public Message(int x,int y,String title, int delay0,I_Button ok0) {
        this(x,y,title,ok0);
        delayIt(delay0);
        }
    public Message(int x,int y,String title) {
        this(x,y,title,null);
        }
    int cnt=0;
    public Message(int x,int y,String title, I_Button ok0) {
        super(100,100);
        initComponents();
        ok = ok0;
        Text.setFont(new Font("Arial Cyr", Font.PLAIN, 14));
        if (title.indexOf("$")!=-1)
            cnt = UtilsDesktop.setLabelText(Text,title);
        else
            cnt = UtilsDesktop.setLabelText(Text,title,40);
        setWH(450,cnt*25+120);
        Text.setBounds(20,10,410,cnt*25+30);
        OK.setBounds(180, cnt*25+40, 70, 30);
        positionOn(x+20,y+20);
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OK = new javax.swing.JButton();
        Text = new javax.swing.JLabel();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));
        setUndecorated(true);
        getContentPane().setLayout(null);

        OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKActionPerformed(evt);
            }
        });
        getContentPane().add(OK);
        OK.setBounds(180, 110, 70, 30);
        getContentPane().add(Text);
        Text.setBounds(20, 10, 410, 80);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKActionPerformed
        closeView();
        if (ok!=null)
            ok.onPush();
    }//GEN-LAST:event_OKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OK;
    private javax.swing.JLabel Text;
    // End of variables declaration//GEN-END:variables
}