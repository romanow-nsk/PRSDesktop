/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;


import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.utils.OwnDateTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author romanow
 */
public class CalendarPanel extends javax.swing.JPanel {
    private OwnDateTime date = new OwnDateTime();
    private OwnDateTime cdate = new OwnDateTime();
    private I_CalendarTime back;
    private int dayInMonth=0;
    private int hh=0,mm=0,ss=0;
    private int yy=0,mn=0,dd=0;
    private boolean dateSelected=false;
    private JButton days[]=new JButton[0];
    /**
     * Creates new form CalendarPanel
     */
    public CalendarPanel(I_CalendarTime back0) {
        back = back0;
        setVisible(true);
        initComponents();
        setBounds(0,0,380,425);
        date.day(1);
        OK.setVisible(false);
        Prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                date.decMonth();
                createList();
            }});
        Next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                date.incMonth();
                createList();
            }});
        createList();
        }
    public void enableDay(int day, boolean enable){
        days[day-1].setEnabled(enable);
        }
    public void enableAllDays(boolean enable){
        for(int i=0;i<days.length;i++)
            days[i].setEnabled(enable);
        }
    public int dayInMonth(){ return dayInMonth; }
    public void setDayColor(int day, Color color){
        days[day-1].setBackground(color);
        }
    public void setAllDaysColor(Color color){
        for(int i=0;i<days.length;i++)
            days[i].setBackground(color);
            }
    public void createList(){
        int i,j,k;
        Prev.setText(date.month()==1 ? ValuesBase.mnt[11] : ValuesBase.mnt[date.month()-2]);
        Next.setText(date.month()==12 ? ValuesBase.mnt[0] : ValuesBase.mnt[date.month()]);
        Month.setText(""+ValuesBase.mnt[date.month()-1]+" "+(date.year()));
        Days.removeAll();
        Days.revalidate();
        for (j=0;j<7;j++){
            JButton ff = new JButton();
            ff.setEnabled(false);
            ff.setBounds(52*j,0,50,40);
            ff.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            ff.setLabel(""+ValuesBase.week[j]);
            Days.add(ff);
            }
        int k0=date.dayOfWeek()-1;
        int k1=31;
        k=0;
        final int month=date.month();
        if (month==2){
            k1=28; if (date.year()%4==0) k1=29;
            }
        if (month==4 || month==6 || month==9 || month==11) k1=30;
        days = new JButton[k1];
        dayInMonth = k1;
        for (i=0;i<6;i++){
            for (j=0;j<7;j++,k++){
                if (k>=k0 && k<k0+k1){
                    JButton ff = new JButton();
                    ff.setBounds(52*j,(i+1)*42,50,40);
                    ff.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                    final int nn=k-k0+1;
                    ff.setLabel(""+nn);
                    days[nn-1] = ff;
                    Days.add(ff);
                    if (month==cdate.month() && nn==cdate.day() && date.year()==cdate.year()) {
                        Rectangle rr = ff.getBounds();
                        rr.x-=2;
                        rr.y-=2;
                        rr.height+=2;
                        rr.width+=2;
                        ff.setBounds(rr);
                    	//ff.setBackground(Color.GREEN);
                        }
                    ff.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            OwnDateTime d1 = new OwnDateTime(nn,date.month(),date.year(),hh,mm,ss);
                            DateField.setText(d1.fullToString());
                            dateSelected=true;
                            OK.setVisible(true);
                            dd = nn;
                            mn = date.month();
                            yy = date.year();
                            }
                        @Override
                        public void mousePressed(MouseEvent e) {}
                        @Override
                        public void mouseReleased(MouseEvent e) {}
                        @Override
                        public void mouseEntered(MouseEvent e) {}
                        @Override
                        public void mouseExited(MouseEvent e) {}
                        });
                    }
                }
            }
        revalidate();
        repaint();
        }        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Prev = new javax.swing.JButton();
        Month = new javax.swing.JLabel();
        Next = new javax.swing.JButton();
        Days = new javax.swing.JPanel();
        Secound = new javax.swing.JButton();
        Hours = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Minute = new javax.swing.JButton();
        Canсel = new javax.swing.JButton();
        OK = new javax.swing.JButton();
        DateField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(null);

        Prev.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        add(Prev);
        Prev.setBounds(10, 10, 110, 30);

        Month.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Month.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Month.setText("Month");
        add(Month);
        Month.setBounds(120, 10, 150, 20);

        Next.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        add(Next);
        Next.setBounds(270, 10, 100, 30);

        Days.setLayout(null);
        add(Days);
        Days.setBounds(10, 50, 360, 290);

        Secound.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Secound.setText("00");
        Secound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SecoundActionPerformed(evt);
            }
        });
        add(Secound);
        Secound.setBounds(150, 380, 50, 40);

        Hours.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Hours.setText("00");
        Hours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HoursActionPerformed(evt);
            }
        });
        add(Hours);
        Hours.setBounds(10, 380, 50, 40);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText(":");
        add(jLabel1);
        jLabel1.setBounds(140, 390, 10, 14);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText(":");
        add(jLabel2);
        jLabel2.setBounds(70, 390, 10, 14);

        Minute.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Minute.setText("00");
        Minute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinuteActionPerformed(evt);
            }
        });
        add(Minute);
        Minute.setBounds(80, 380, 50, 40);

        Canсel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        Canсel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanсelActionPerformed(evt);
            }
        });
        add(Canсel);
        Canсel.setBounds(320, 370, 50, 50);

        OK.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        OK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKActionPerformed(evt);
            }
        });
        add(OK);
        OK.setBounds(230, 370, 50, 50);

        DateField.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        DateField.setEnabled(false);
        add(DateField);
        DateField.setBounds(10, 340, 190, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void HoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HoursActionPerformed
        new NumSelectorPanel(0, 23, new I_RealValue() {
            @Override
            public void onEvent(double value) {
                hh = (int)value;
                Hours.setText(String.format("%-2d",hh));
                if (dateSelected){
                    OwnDateTime d1 = new OwnDateTime(dd,mn,yy,hh,mm,ss);
                    DateField.setText(d1.fullToString());
                }
            }
        });
    }//GEN-LAST:event_HoursActionPerformed

    private void MinuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinuteActionPerformed
        new NumSelectorPanel(0, 59, new I_RealValue() {
            @Override
            public void onEvent(double value) {
                mm = (int)value;
                Minute.setText(String.format("%-2d",mm));
                if (dateSelected){
                    OwnDateTime d1 = new OwnDateTime(dd,mn,yy,hh,mm,ss);
                    DateField.setText(d1.fullToString());
                }
            }
        });

    }//GEN-LAST:event_MinuteActionPerformed

    private void SecoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SecoundActionPerformed
        new NumSelectorPanel(0, 59, new I_RealValue() {
            @Override
            public void onEvent(double value) {
                ss = (int)value;
                Secound.setText(String.format("%-2d",ss));
                if (dateSelected){
                    OwnDateTime d1 = new OwnDateTime(dd,mn,yy,hh,mm,ss);
                    DateField.setText(d1.fullToString());
                    }
            }
        });

    }//GEN-LAST:event_SecoundActionPerformed

    private void CanсelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanсelActionPerformed
        back.onSelect(null);
    }//GEN-LAST:event_CanсelActionPerformed

    private void OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKActionPerformed
        OwnDateTime d1 = new OwnDateTime(dd,mn,yy,hh,mm,ss);
        back.onSelect(d1);
        }//GEN-LAST:event_OKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Canсel;
    private javax.swing.JTextField DateField;
    private javax.swing.JPanel Days;
    private javax.swing.JButton Hours;
    private javax.swing.JButton Minute;
    private javax.swing.JLabel Month;
    private javax.swing.JButton Next;
    private javax.swing.JButton OK;
    private javax.swing.JButton Prev;
    private javax.swing.JButton Secound;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
    public static void main(String ss[]){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        JFrame ff = new JFrame();
        ff.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        ff.setBounds(100,100,400,400);
        CalendarPanel cc = new CalendarPanel(new I_CalendarTime() {
            @Override
            public void onSelect(OwnDateTime time) {
                System.out.print(time.dateTimeToString());
            }
        });
        cc.enableAllDays(false);
        cc.enableDay(2,true);
        cc.enableDay(12,true);
        cc.setDayColor(3,Color.red);
        ff.add(cc);
        ff.setVisible(true);
    }
}
