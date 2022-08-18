/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import com.google.gson.Gson;
import lombok.Getter;
import retrofit2.Call;
import romanow.abc.bridge.APICallSync;
import romanow.abc.convert.onewayticket.OWTDiscipline;
import romanow.abc.convert.onewayticket.OWTReader;
import romanow.abc.convert.onewayticket.OWTTheme;
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.baseentityes.JBoolean;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.baseentityes.JInt;
import romanow.abc.core.entity.baseentityes.JLong;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.mongo.*;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
 *
 * @author romanow
 */
public class PRSSemesterPanel extends BasePanel{
    private ChoiceConsts pointState;
    private ChoiceList<SAGroupRating> ratings;
    private ChoiceList<SAStudent> students;
    private ChoiceList<SAEduUnit> eduUnits;
    private ChoiceList<SATeam> teams;
    private ChoiceList<SAStudent> teamStudents;
    private EntityRefList<SAStudent> allStudents = new EntityRefList<>();
    private EntityRefList<SAGroupRating> allRatings = new EntityRefList<>();
    private SATeacher cTeacher=null;

    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        initComponents();
        pointState = new ChoiceConsts(PointState, Values.constMap().getGroupList("PointState"), new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                pointStateChanged();
                }
            });
        ratings = new ChoiceList<>(Ratings);
        students = new ChoiceList<>(Students);
        eduUnits = new ChoiceList<>(EduUnits);
        teams = new ChoiceList<>(Teams);
        teamStudents = new ChoiceList<>(TeamStudents);
        refreshAll(false);
        }

    public void clearPos(){
        eduUnits.clearPos();
        students.clearPos();
        ratings.clearPos();
        teams.clearPos();
        teamStudents.clearPos();
        }
    private void savePos(){
        eduUnits.savePos();
        students.savePos();
        ratings.savePos();
        teamStudents.savePos();
        teamStudents.savePos();
        }
    private void clear(){
        eduUnits.clear();
        students.clear();
        ratings.clear();
        teamStudents.clear();
        teamStudents.clear();
    }

    public void pointStateChanged(){

    }

    public void refreshAll(boolean withPos){
        if (withPos)
            savePos();
        clear();
        new APICall<ArrayList<DBRequest>>(null){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.getService().getEntityList(main.getDebugToken(),"SAGroupRating",Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                allRatings.clear();
                for(DBRequest dd : oo){
                    try {
                        SAGroupRating ss = (SAGroupRating) dd.get(new Gson());
                        allRatings.add(ss);
                        allRatings.add(ss);
                        } catch (UniException e) {
                            System.out.println(e);
                            }
                        }
                allRatings.createMap();
                loadRatings(withPos);
            }
        };


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        UserAccount = new javax.swing.JLabel();
        Ratings = new java.awt.Choice();
        aaa = new javax.swing.JLabel();
        Students = new java.awt.Choice();
        jLabel2 = new javax.swing.JLabel();
        Teams = new java.awt.Choice();
        jLabel3 = new javax.swing.JLabel();
        TeamStudents = new java.awt.Choice();
        StudentTableReport = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        EduUnits = new java.awt.Choice();
        TeamStudentAdd = new javax.swing.JButton();
        EduUnitTableReport = new javax.swing.JButton();
        StudentPdfReport = new javax.swing.JButton();
        RatingTableReport = new javax.swing.JButton();
        DocDownload = new javax.swing.JButton();
        RatingPdfReport2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        TeamAdd = new javax.swing.JButton();
        TeamStudentRemove = new javax.swing.JButton();
        bbb = new javax.swing.JLabel();
        EduUnitWeek = new javax.swing.JTextField();
        TeamStudentSelect2 = new javax.swing.JButton();
        TeamRemove1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        PointVariant = new javax.swing.JTextField();
        bbb1 = new javax.swing.JLabel();
        PointState = new java.awt.Choice();
        jSeparator1 = new javax.swing.JSeparator();
        bbb2 = new javax.swing.JLabel();
        PointDate = new javax.swing.JTextField();
        Point = new javax.swing.JTextField();
        ссс = new javax.swing.JLabel();
        StudentTeam1 = new javax.swing.JTextField();
        ссс1 = new javax.swing.JLabel();
        ссс2 = new javax.swing.JLabel();
        EduUnitPdfReport1 = new javax.swing.JButton();
        DocUpload = new javax.swing.JButton();
        SrcDownload = new javax.swing.JButton();
        SrcUpload = new javax.swing.JButton();
        bbb4 = new javax.swing.JLabel();
        bbb5 = new javax.swing.JLabel();
        EduUnitPoint = new javax.swing.JTextField();
        EduUnitPoint2 = new javax.swing.JTextField();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator3);
        jSeparator3.setBounds(400, 30, 10, 620);

        UserAccount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UserAccount.setText(".......");
        add(UserAccount);
        UserAccount.setBounds(20, 20, 360, 20);
        add(Ratings);
        Ratings.setBounds(20, 50, 370, 20);

        aaa.setText("Бригада");
        add(aaa);
        aaa.setBounds(450, 50, 60, 16);
        add(Students);
        Students.setBounds(110, 120, 280, 20);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Студент");
        add(jLabel2);
        jLabel2.setBounds(450, 80, 60, 16);
        add(Teams);
        Teams.setBounds(510, 50, 160, 20);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Выполнение");
        add(jLabel3);
        jLabel3.setBounds(20, 340, 90, 16);
        add(TeamStudents);
        TeamStudents.setBounds(510, 80, 160, 20);

        StudentTableReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/table.png"))); // NOI18N
        StudentTableReport.setBorderPainted(false);
        StudentTableReport.setContentAreaFilled(false);
        StudentTableReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentTableReportActionPerformed(evt);
            }
        });
        add(StudentTableReport);
        StudentTableReport.setBounds(20, 150, 35, 35);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Студент");
        add(jLabel4);
        jLabel4.setBounds(20, 120, 90, 16);
        add(EduUnits);
        EduUnits.setBounds(110, 250, 270, 20);

        TeamStudentAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/right.PNG"))); // NOI18N
        TeamStudentAdd.setBorderPainted(false);
        TeamStudentAdd.setContentAreaFilled(false);
        TeamStudentAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentAddActionPerformed(evt);
            }
        });
        add(TeamStudentAdd);
        TeamStudentAdd.setBounds(410, 110, 35, 35);

        EduUnitTableReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/table.png"))); // NOI18N
        EduUnitTableReport.setBorderPainted(false);
        EduUnitTableReport.setContentAreaFilled(false);
        EduUnitTableReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitTableReportActionPerformed(evt);
            }
        });
        add(EduUnitTableReport);
        EduUnitTableReport.setBounds(20, 280, 35, 35);

        StudentPdfReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pdf.png"))); // NOI18N
        StudentPdfReport.setBorderPainted(false);
        StudentPdfReport.setContentAreaFilled(false);
        StudentPdfReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentPdfReportActionPerformed(evt);
            }
        });
        add(StudentPdfReport);
        StudentPdfReport.setBounds(60, 150, 35, 35);

        RatingTableReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/table.png"))); // NOI18N
        RatingTableReport.setBorderPainted(false);
        RatingTableReport.setContentAreaFilled(false);
        RatingTableReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RatingTableReportActionPerformed(evt);
            }
        });
        add(RatingTableReport);
        RatingTableReport.setBounds(20, 80, 35, 35);

        DocDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        DocDownload.setBorderPainted(false);
        DocDownload.setContentAreaFilled(false);
        DocDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DocDownloadActionPerformed(evt);
            }
        });
        add(DocDownload);
        DocDownload.setBounds(120, 490, 35, 35);

        RatingPdfReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pdf.png"))); // NOI18N
        RatingPdfReport2.setBorderPainted(false);
        RatingPdfReport2.setContentAreaFilled(false);
        RatingPdfReport2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RatingPdfReport2ActionPerformed(evt);
            }
        });
        add(RatingPdfReport2);
        RatingPdfReport2.setBounds(60, 80, 35, 35);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Ед.контроля");
        add(jLabel5);
        jLabel5.setBounds(20, 250, 90, 16);

        TeamAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        TeamAdd.setBorderPainted(false);
        TeamAdd.setContentAreaFilled(false);
        TeamAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamAddActionPerformed(evt);
            }
        });
        add(TeamAdd);
        TeamAdd.setBounds(680, 40, 30, 30);

        TeamStudentRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TeamStudentRemove.setBorderPainted(false);
        TeamStudentRemove.setContentAreaFilled(false);
        TeamStudentRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentRemoveActionPerformed(evt);
            }
        });
        add(TeamStudentRemove);
        TeamStudentRemove.setBounds(680, 75, 30, 30);

        bbb.setText("Состояние");
        add(bbb);
        bbb.setBounds(20, 360, 80, 16);

        EduUnitWeek.setEnabled(false);
        add(EduUnitWeek);
        EduUnitWeek.setBounds(310, 420, 60, 25);

        TeamStudentSelect2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/left.PNG"))); // NOI18N
        TeamStudentSelect2.setBorderPainted(false);
        TeamStudentSelect2.setContentAreaFilled(false);
        TeamStudentSelect2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentSelect2ActionPerformed(evt);
            }
        });
        add(TeamStudentSelect2);
        TeamStudentSelect2.setBounds(410, 70, 35, 35);

        TeamRemove1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TeamRemove1.setBorderPainted(false);
        TeamRemove1.setContentAreaFilled(false);
        TeamRemove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamRemove1ActionPerformed(evt);
            }
        });
        add(TeamRemove1);
        TeamRemove1.setBounds(720, 40, 30, 30);

        jLabel6.setText("Бригада");
        add(jLabel6);
        jLabel6.setBounds(110, 150, 80, 16);

        PointVariant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PointVariantKeyPressed(evt);
            }
        });
        add(PointVariant);
        PointVariant.setBounds(110, 390, 60, 25);

        bbb1.setText("Балл");
        add(bbb1);
        bbb1.setBounds(20, 420, 80, 16);
        add(PointState);
        PointState.setBounds(110, 360, 260, 20);
        add(jSeparator1);
        jSeparator1.setBounds(110, 350, 290, 3);

        bbb2.setText("Вариант");
        add(bbb2);
        bbb2.setBounds(20, 390, 80, 16);

        PointDate.setEnabled(false);
        PointDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PointDateMouseClicked(evt);
            }
        });
        add(PointDate);
        PointDate.setBounds(80, 450, 90, 25);

        Point.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PointKeyPressed(evt);
            }
        });
        add(Point);
        Point.setBounds(110, 420, 60, 25);

        ссс.setText("Исходник (архив)");
        add(ссс);
        ссс.setBounds(180, 490, 110, 16);

        StudentTeam1.setEnabled(false);
        add(StudentTeam1);
        StudentTeam1.setBounds(170, 150, 60, 25);

        ссс1.setText("Дата");
        add(ссс1);
        ссс1.setBounds(20, 450, 50, 16);

        ссс2.setText("Отчет");
        add(ссс2);
        ссс2.setBounds(20, 490, 50, 16);

        EduUnitPdfReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pdf.png"))); // NOI18N
        EduUnitPdfReport1.setBorderPainted(false);
        EduUnitPdfReport1.setContentAreaFilled(false);
        EduUnitPdfReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitPdfReport1ActionPerformed(evt);
            }
        });
        add(EduUnitPdfReport1);
        EduUnitPdfReport1.setBounds(60, 280, 35, 35);

        DocUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        DocUpload.setBorderPainted(false);
        DocUpload.setContentAreaFilled(false);
        DocUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DocUploadActionPerformed(evt);
            }
        });
        add(DocUpload);
        DocUpload.setBounds(80, 490, 35, 35);

        SrcDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        SrcDownload.setBorderPainted(false);
        SrcDownload.setContentAreaFilled(false);
        SrcDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SrcDownloadActionPerformed(evt);
            }
        });
        add(SrcDownload);
        SrcDownload.setBounds(320, 490, 35, 35);

        SrcUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        SrcUpload.setBorderPainted(false);
        SrcUpload.setContentAreaFilled(false);
        SrcUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SrcUploadActionPerformed(evt);
            }
        });
        add(SrcUpload);
        SrcUpload.setBounds(280, 490, 35, 35);

        bbb4.setText("Нормат. балл");
        add(bbb4);
        bbb4.setBounds(190, 390, 90, 16);

        bbb5.setText("Срок сдачи (нед)");
        add(bbb5);
        bbb5.setBounds(190, 420, 110, 16);

        EduUnitPoint.setEnabled(false);
        add(EduUnitPoint);
        EduUnitPoint.setBounds(310, 390, 60, 25);

        EduUnitPoint2.setEnabled(false);
        add(EduUnitPoint2);
        EduUnitPoint2.setBounds(310, 390, 60, 25);
    }// </editor-fold>//GEN-END:initComponents

    private void StudentTableReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentTableReportActionPerformed

    }//GEN-LAST:event_StudentTableReportActionPerformed

    private void TeamStudentAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamStudentAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TeamStudentAddActionPerformed

    private void EduUnitTableReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitTableReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EduUnitTableReportActionPerformed

    private void StudentPdfReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentPdfReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentPdfReportActionPerformed

    private void RatingTableReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RatingTableReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RatingTableReportActionPerformed

    private void DocDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DocDownloadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DocDownloadActionPerformed

    private void RatingPdfReport2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RatingPdfReport2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RatingPdfReport2ActionPerformed

    private void TeamAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamAddActionPerformed
        new OKName(200,200,"Добавить дисциплину", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
            }
        });
    }//GEN-LAST:event_TeamAddActionPerformed

    private void TeamStudentRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamStudentRemoveActionPerformed

    }//GEN-LAST:event_TeamStudentRemoveActionPerformed

    private void TeamStudentSelect2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamStudentSelect2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TeamStudentSelect2ActionPerformed

    private void TeamRemove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamRemove1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TeamRemove1ActionPerformed

    private void PointVariantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointVariantKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PointVariantKeyPressed

    private void PointKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PointKeyPressed

    private void PointDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PointDateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PointDateMouseClicked

    private void EduUnitPdfReport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitPdfReport1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EduUnitPdfReport1ActionPerformed

    private void DocUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DocUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DocUploadActionPerformed

    private void SrcDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrcDownloadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SrcDownloadActionPerformed

    private void SrcUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrcUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SrcUploadActionPerformed

    public void loadRatings(boolean withPos){
        new APICall<ArrayList<DBRequest>>(null){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.getService().getEntityList(main.getDebugToken(),"SAGroupRating",Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                allRatings.clear();
                for(DBRequest dd : oo){
                    try {
                        SAGroupRating ss = (SAGroupRating) dd.get(new Gson());
                        allRatings.add(ss);
                        } catch (UniException e) {
                            System.out.println(e);
                            }
                        }
                loadTeacherRatings(withPos);
                }
            };
        }
    public void loadTeacherRatings(final boolean withPos) {
        UserAccount.setText(main.loginUser.getTitle()+": "+main.loginUser.getMail());
        if (main.loginUser.getTypeId() != Values.UserTeacher) {
            for (SAGroupRating gg : allRatings)
                ratings.add(gg);
            refreshSelectedRating(withPos);
            return;
            }
        final long userOid = main.loginUser.getOid();
        DBQueryList query =  new DBQueryList().
                add(new DBQueryLong(I_DBQuery.ModeEQ,"user",userOid)).
                add(new DBQueryBoolean("valid",true));
        final String xmlQuery = new DBXStream().toXML(query);
        new APICall<ArrayList<DBRequest>>(null){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.getService().getEntityListByQuery(main.getDebugToken(),"SATeacher",xmlQuery,1);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                if (oo.size()==0){
                    popup("Нет назначенных рейтингов");
                    return;
                    }
                try {
                    SATeacher current = (SATeacher)  oo.get(0).get(new Gson());
                    for(EntityLink<SAGroupRating> ss : current.getRatings()){
                        SAGroupRating gg = allRatings.getById(ss.getOid());
                        if (gg==null){
                            System.out.println("Не найден рейтинг id="+ss.getOid());
                            }
                        ratings.add(gg);
                        }
                    refreshSelectedRating(withPos);
                    } catch (UniException e) {
                        System.out.println(e);
                        }
                    }
                };
            }

        public void refreshSelectedRating(boolean withPos){

        }






    @Override
    public void refresh() {}

    @Override
    public void eventPanel(int code, int par1, long par2, String par3,Object oo) {
        if (code==EventRefreshSettings){
            refresh();
            main.sendEventPanel(EventRefreshSettingsDone,0,0,"",oo);
            }
    }

    @Override
    public void shutDown() {

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DocDownload;
    private javax.swing.JButton DocUpload;
    private javax.swing.JButton EduUnitPdfReport1;
    private javax.swing.JTextField EduUnitPoint;
    private javax.swing.JTextField EduUnitPoint2;
    private javax.swing.JButton EduUnitTableReport;
    private javax.swing.JTextField EduUnitWeek;
    private java.awt.Choice EduUnits;
    private javax.swing.JTextField Point;
    private javax.swing.JTextField PointDate;
    private java.awt.Choice PointState;
    private javax.swing.JTextField PointVariant;
    private javax.swing.JButton RatingPdfReport2;
    private javax.swing.JButton RatingTableReport;
    private java.awt.Choice Ratings;
    private javax.swing.JButton SrcDownload;
    private javax.swing.JButton SrcUpload;
    private javax.swing.JButton StudentPdfReport;
    private javax.swing.JButton StudentTableReport;
    private javax.swing.JTextField StudentTeam1;
    private java.awt.Choice Students;
    private javax.swing.JButton TeamAdd;
    private javax.swing.JButton TeamRemove1;
    private javax.swing.JButton TeamStudentAdd;
    private javax.swing.JButton TeamStudentRemove;
    private javax.swing.JButton TeamStudentSelect2;
    private java.awt.Choice TeamStudents;
    private java.awt.Choice Teams;
    private javax.swing.JLabel UserAccount;
    private javax.swing.JLabel aaa;
    private javax.swing.JLabel bbb;
    private javax.swing.JLabel bbb1;
    private javax.swing.JLabel bbb2;
    private javax.swing.JLabel bbb4;
    private javax.swing.JLabel bbb5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel ссс;
    private javax.swing.JLabel ссс1;
    private javax.swing.JLabel ссс2;
    // End of variables declaration//GEN-END:variables
}
