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
    private ChoiceConsts eduUnitType;
    private ChoiceList<SAGroupRating> ratings;
    private ChoiceList<SAStudent> students;
    private ChoiceList<SAEduUnit> eduUnits;
    private ChoiceList<SATeam> teams;
    private ChoiceList<SAStudent> teamStudents;
    private EntityRefList<SAStudent> allStudents = new EntityRefList<>();
    private EntityRefList<SAGroupRating> allRatings = new EntityRefList<>();
    private SATeacher cTeacher=null;
    private SAGroupRating cRating=null;
    private SADiscipline cDiscipline=null;
    private SAGroup cGroup = null;
    private SAStudent cStudent=null;
    private SAEduUnit cEduUnit=null;
    private SATeam cTeam=null;
    private boolean refresh=false;

    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        initComponents();
        pointState = new ChoiceConsts(PointState, Values.constMap().getGroupList("PointState"), new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                pointStateChanged();
                }
            });
        eduUnitType = new ChoiceConsts(EduUnitType, Values.constMap().getGroupList("EduUnit"), null);
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
    private void savePos(boolean withPos){
        eduUnits.savePos(withPos);
        students.savePos(withPos);
        ratings.savePos(withPos);
        teamStudents.savePos(withPos);
        teamStudents.savePos(withPos);
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
        savePos(withPos);
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
                refreshRatings(withPos);
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
        RatingPdfReport = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        TeamAdd = new javax.swing.JButton();
        TeamStudentRemove = new javax.swing.JButton();
        bbb = new javax.swing.JLabel();
        EduUnitWeek = new javax.swing.JTextField();
        TeamStudentSelect = new javax.swing.JButton();
        TeamRemove = new javax.swing.JButton();
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
        EduUnitPdfReport = new javax.swing.JButton();
        DocUpload = new javax.swing.JButton();
        SrcDownload = new javax.swing.JButton();
        SrcUpload = new javax.swing.JButton();
        bbb4 = new javax.swing.JLabel();
        bbb5 = new javax.swing.JLabel();
        EduUnitPoint = new javax.swing.JTextField();
        EduUnitPoint2 = new javax.swing.JTextField();
        EduUnitType = new java.awt.Choice();
        RefreshAll = new javax.swing.JButton();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator3);
        jSeparator3.setBounds(400, 30, 10, 620);

        UserAccount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UserAccount.setText(".......");
        add(UserAccount);
        UserAccount.setBounds(80, 20, 310, 20);

        Ratings.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RatingsItemStateChanged(evt);
            }
        });
        add(Ratings);
        Ratings.setBounds(20, 50, 370, 20);

        aaa.setText("Бригада");
        add(aaa);
        aaa.setBounds(450, 80, 60, 16);

        Students.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StudentsItemStateChanged(evt);
            }
        });
        add(Students);
        Students.setBounds(110, 120, 280, 20);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Студент");
        add(jLabel2);
        jLabel2.setBounds(450, 120, 60, 16);

        Teams.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TeamsItemStateChanged(evt);
            }
        });
        add(Teams);
        Teams.setBounds(510, 80, 160, 20);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Выполнение");
        add(jLabel3);
        jLabel3.setBounds(20, 340, 90, 16);
        add(TeamStudents);
        TeamStudents.setBounds(510, 120, 160, 20);

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

        EduUnits.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EduUnitsItemStateChanged(evt);
            }
        });
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

        RatingPdfReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pdf.png"))); // NOI18N
        RatingPdfReport.setBorderPainted(false);
        RatingPdfReport.setContentAreaFilled(false);
        RatingPdfReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RatingPdfReportActionPerformed(evt);
            }
        });
        add(RatingPdfReport);
        RatingPdfReport.setBounds(60, 80, 35, 35);

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
        TeamAdd.setBounds(680, 70, 30, 30);

        TeamStudentRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TeamStudentRemove.setBorderPainted(false);
        TeamStudentRemove.setContentAreaFilled(false);
        TeamStudentRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentRemoveActionPerformed(evt);
            }
        });
        add(TeamStudentRemove);
        TeamStudentRemove.setBounds(680, 110, 30, 30);

        bbb.setText("Состояние");
        add(bbb);
        bbb.setBounds(20, 360, 80, 16);

        EduUnitWeek.setEnabled(false);
        add(EduUnitWeek);
        EduUnitWeek.setBounds(310, 420, 60, 25);

        TeamStudentSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/question.png"))); // NOI18N
        TeamStudentSelect.setBorderPainted(false);
        TeamStudentSelect.setContentAreaFilled(false);
        TeamStudentSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentSelectActionPerformed(evt);
            }
        });
        add(TeamStudentSelect);
        TeamStudentSelect.setBounds(720, 110, 35, 35);

        TeamRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TeamRemove.setBorderPainted(false);
        TeamRemove.setContentAreaFilled(false);
        TeamRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamRemoveActionPerformed(evt);
            }
        });
        add(TeamRemove);
        TeamRemove.setBounds(720, 70, 30, 30);

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

        EduUnitPdfReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pdf.png"))); // NOI18N
        EduUnitPdfReport.setBorderPainted(false);
        EduUnitPdfReport.setContentAreaFilled(false);
        EduUnitPdfReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitPdfReportActionPerformed(evt);
            }
        });
        add(EduUnitPdfReport);
        EduUnitPdfReport.setBounds(60, 280, 35, 35);

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

        EduUnitType.setBackground(new java.awt.Color(204, 204, 204));
        EduUnitType.setEnabled(false);
        add(EduUnitType);
        EduUnitType.setBounds(288, 280, 90, 20);

        RefreshAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshAll.setBorderPainted(false);
        RefreshAll.setContentAreaFilled(false);
        RefreshAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshAllActionPerformed(evt);
            }
        });
        add(RefreshAll);
        RefreshAll.setBounds(20, 10, 30, 30);
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

    private void RatingPdfReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RatingPdfReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RatingPdfReportActionPerformed

    private void TeamAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamAddActionPerformed
        new OKName(200,200,"Добавить дисциплину", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
            }
        });
    }//GEN-LAST:event_TeamAddActionPerformed

    private void TeamStudentRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamStudentRemoveActionPerformed

    }//GEN-LAST:event_TeamStudentRemoveActionPerformed

    private void TeamStudentSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamStudentSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TeamStudentSelectActionPerformed

    private void TeamRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeamRemoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TeamRemoveActionPerformed

    private void PointVariantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointVariantKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PointVariantKeyPressed

    private void PointKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PointKeyPressed

    private void PointDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PointDateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PointDateMouseClicked

    private void EduUnitPdfReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitPdfReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EduUnitPdfReportActionPerformed

    private void DocUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DocUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DocUploadActionPerformed

    private void SrcDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrcDownloadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SrcDownloadActionPerformed

    private void SrcUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrcUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SrcUploadActionPerformed

    private void EduUnitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EduUnitsItemStateChanged
        eduUnits.savePos();
        refreshSelectedEduUnit(true);
        refreshPoint();
    }//GEN-LAST:event_EduUnitsItemStateChanged

    private void StudentsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StudentsItemStateChanged
        students.savePos();
        refreshSelectedStudent(true);
        refreshPoint();
    }//GEN-LAST:event_StudentsItemStateChanged

    private void TeamsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TeamsItemStateChanged
        teams.savePos();
        refreshSelectedTeam(true);
    }//GEN-LAST:event_TeamsItemStateChanged

    private void RatingsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingsItemStateChanged
        savePos(true);
        refreshSelectedRating(true);
    }//GEN-LAST:event_RatingsItemStateChanged

    private void RefreshAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshAllActionPerformed
        refreshAll(false);
    }//GEN-LAST:event_RefreshAllActionPerformed

    public void refreshRatings(boolean withPos){
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
                refreshTeacherRatings(withPos);
                }
            };
        }
    public void refreshTeacherRatings(final boolean withPos) {
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
            ratings.withPos(withPos);
            cRating = ratings.get();
            if (cRating==null)
                return;
            refreshStudents(false);
            refreshEduUnits(false);
            refreshAllPoints();
            refreshPoint();
            }

    public void refreshAllPoints(){

        }

    public void refreshPoint(){

        }
    public void refreshTeams(boolean withPos){
        teams.withPos(withPos);
        teams.clear();
        for (SATeam team : cRating.getTeams())
            teams.add(team);
        refreshSelectedTeam(withPos);
        }
    public void refreshSelectedTeam(boolean withPos){
        teams.withPos(withPos);
        teamStudents.clear();
        cTeam = teams.get();
        if (cTeam==null)
            return;
        for(EntityLink<SAStudent> ss : cTeam.getStudents()){
            SAStudent cc = allStudents.getById(ss.getOid());
            if (cc==null){
                System.out.println("Не найден студент в бригаде id="+cc.getOid());
                }
            else
                teamStudents.add(cc);
                }
        }

    public void refreshEduUnits(boolean withPos){
        eduUnits.savePos();
        eduUnits.clear();
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"SADiscipline",cRating.getSADiscipline().getOid(),1);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try {
                    cDiscipline = (SADiscipline) oo.get(main.gson);
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Ошибка чтения дисциплины "+cRating.getName());
                        return;
                        }
                for(SAEduUnit eduUnit : cDiscipline.getUnits())
                    eduUnits.add(eduUnit);
                refreshSelectedEduUnit(withPos);
                }
            };
        }

    public void refreshSelectedEduUnit(boolean withPos){
        eduUnits.withPos(withPos);
        refresh = true;
        EduUnitWeek.setText("");
        EduUnitPoint.setText("");
        cEduUnit = eduUnits.get();
        if (cEduUnit==null){
            refresh=false;
            return;
            }
        if (cEduUnit.getDeliveryWeek()!=0)
            EduUnitWeek.setText(""+cEduUnit.getDeliveryWeek());
        EduUnitPoint.setText(""+cEduUnit.getBasePoint());
        if (eduUnitType.selectByValue(eduUnits.get().getUnitType())==null)
            System.out.println("Недопустимый тип учебной единицы: "+eduUnits.get().getUnitType());
        refresh=false;
        }

    public void refreshSelectedStudent(boolean withPos){
        students.withPos(withPos);
        }

    public void refreshStudents(boolean withPos){
        students.withPos(withPos);
        students.clear();
        allStudents.clear();
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"SAGroup",cRating.getGroup().getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try{
                    cGroup = (SAGroup)oo.get(main.gson);
                    for(SAStudent student : cGroup.getStudents()){
                        students.add(student);
                        allStudents.add(student);
                        }
                    allStudents.createMap();
                    refreshSelectedStudent(withPos);
                }catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Не прочитаны данные группы "+cGroup.getName());
                }
            }
        };
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
    private javax.swing.JButton EduUnitPdfReport;
    private javax.swing.JTextField EduUnitPoint;
    private javax.swing.JTextField EduUnitPoint2;
    private javax.swing.JButton EduUnitTableReport;
    private java.awt.Choice EduUnitType;
    private javax.swing.JTextField EduUnitWeek;
    private java.awt.Choice EduUnits;
    private javax.swing.JTextField Point;
    private javax.swing.JTextField PointDate;
    private java.awt.Choice PointState;
    private javax.swing.JTextField PointVariant;
    private javax.swing.JButton RatingPdfReport;
    private javax.swing.JButton RatingTableReport;
    private java.awt.Choice Ratings;
    private javax.swing.JButton RefreshAll;
    private javax.swing.JButton SrcDownload;
    private javax.swing.JButton SrcUpload;
    private javax.swing.JButton StudentPdfReport;
    private javax.swing.JButton StudentTableReport;
    private javax.swing.JTextField StudentTeam1;
    private java.awt.Choice Students;
    private javax.swing.JButton TeamAdd;
    private javax.swing.JButton TeamRemove;
    private javax.swing.JButton TeamStudentAdd;
    private javax.swing.JButton TeamStudentRemove;
    private javax.swing.JButton TeamStudentSelect;
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