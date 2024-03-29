/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import com.google.gson.Gson;
import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;
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
import romanow.abc.core.reports.GroupRatingReport;
import romanow.abc.core.reports.TableData;
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
    private ArrayList<SAPoint> points = new ArrayList<>();
    private StateMashineView<PRSSemesterPanel> pointStateMashine;
    @Getter private SAPoint cPoint = null;
    private SASemesterRating cStudRating = null;
    @Getter private boolean newPoint=false;
    private ArrayList<ConstValue> pointStates;
    private ArrayList<ConstValue> quantityes;
    private QualitySelector quality;

    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        initComponents();
        pointStates = Values.constMap().getGroupList("PointState");
        eduUnitType = new ChoiceConsts(EduUnitType, Values.constMap().getGroupList("EduUnit"), null);
        quantityes = Values.constMap().getGroupList("QualityType");
        quantityes.sort(new Comparator<ConstValue>() {
            @Override
            public int compare(ConstValue o1, ConstValue o2) {
                return o1.value()-o2.value();
                }
            });
        quality = new QualitySelector(quantityes, Quality, 200, 440, new I_Value<Integer>() {
            @Override
            public void onEnter(Integer value) {
                cPoint.setQuality(value);
                pointUpdate(null,false);
                }
            });
        ratings = new ChoiceList<>(Ratings);
        students = new ChoiceList<>(Students);
        eduUnits = new ChoiceList<>(EduUnits);
        teams = new ChoiceList<>(Teams);
        teamStudents = new ChoiceList<>(TeamStudents);
        pointStateMashine = new StateMashineView<PRSSemesterPanel>(this,20,440,Values.PointFactory);
        refreshAll();
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

    public void refreshAll(){
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
                refreshRatings();
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
        jSeparator1 = new javax.swing.JSeparator();
        bbb2 = new javax.swing.JLabel();
        PointDeliveryWeek = new javax.swing.JTextField();
        Point = new javax.swing.JTextField();
        SrcLabel = new javax.swing.JLabel();
        StudentTeam1 = new javax.swing.JTextField();
        ссс1 = new javax.swing.JLabel();
        DocLabel = new javax.swing.JLabel();
        EduUnitPdfReport = new javax.swing.JButton();
        DocUpload = new javax.swing.JButton();
        SrcDownload = new javax.swing.JButton();
        SrcUpload = new javax.swing.JButton();
        bbb4 = new javax.swing.JLabel();
        bbb5 = new javax.swing.JLabel();
        EduUnitPoint = new javax.swing.JTextField();
        EduUnitType = new java.awt.Choice();
        RefreshAll = new javax.swing.JButton();
        PointState = new javax.swing.JTextField();
        PointDate = new javax.swing.JTextField();
        ссс2 = new javax.swing.JLabel();
        EduUnitManual = new javax.swing.JCheckBox();
        Quality = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        StudentRating = new javax.swing.JTextField();

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
        jLabel3.setBounds(20, 290, 90, 16);
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
        EduUnits.setBounds(110, 200, 270, 20);

        TeamStudentAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        TeamStudentAdd.setBorderPainted(false);
        TeamStudentAdd.setContentAreaFilled(false);
        TeamStudentAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentAddActionPerformed(evt);
            }
        });
        add(TeamStudentAdd);
        TeamStudentAdd.setBounds(680, 110, 35, 35);

        EduUnitTableReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/table.png"))); // NOI18N
        EduUnitTableReport.setBorderPainted(false);
        EduUnitTableReport.setContentAreaFilled(false);
        EduUnitTableReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitTableReportActionPerformed(evt);
            }
        });
        add(EduUnitTableReport);
        EduUnitTableReport.setBounds(20, 230, 35, 35);

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
        DocDownload.setBounds(150, 400, 35, 35);

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
        jLabel5.setBounds(20, 200, 90, 16);

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
        TeamStudentRemove.setBounds(720, 110, 30, 30);

        bbb.setText("Состояние");
        add(bbb);
        bbb.setBounds(20, 310, 80, 16);

        EduUnitWeek.setEnabled(false);
        add(EduUnitWeek);
        EduUnitWeek.setBounds(170, 260, 40, 25);

        TeamStudentSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/question.png"))); // NOI18N
        TeamStudentSelect.setBorderPainted(false);
        TeamStudentSelect.setContentAreaFilled(false);
        TeamStudentSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeamStudentSelectActionPerformed(evt);
            }
        });
        add(TeamStudentSelect);
        TeamStudentSelect.setBounds(410, 110, 35, 35);

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

        jLabel6.setText("Рейтинг");
        add(jLabel6);
        jLabel6.setBounds(250, 150, 60, 16);

        PointVariant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PointVariantKeyPressed(evt);
            }
        });
        add(PointVariant);
        PointVariant.setBounds(110, 340, 60, 25);

        bbb1.setText("Балл");
        add(bbb1);
        bbb1.setBounds(280, 340, 50, 16);
        add(jSeparator1);
        jSeparator1.setBounds(110, 300, 290, 3);

        bbb2.setText("Вариант");
        add(bbb2);
        bbb2.setBounds(20, 340, 80, 16);

        PointDeliveryWeek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PointDeliveryWeekKeyPressed(evt);
            }
        });
        add(PointDeliveryWeek);
        PointDeliveryWeek.setBounds(340, 370, 40, 25);

        Point.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PointKeyPressed(evt);
            }
        });
        add(Point);
        Point.setBounds(340, 340, 40, 25);

        SrcLabel.setText("Исходник (архив)");
        add(SrcLabel);
        SrcLabel.setBounds(200, 400, 110, 16);

        StudentTeam1.setEnabled(false);
        add(StudentTeam1);
        StudentTeam1.setBounds(170, 150, 60, 25);

        ссс1.setText("Неделя");
        add(ссс1);
        ссс1.setBounds(280, 370, 60, 20);

        DocLabel.setText("Отчет");
        add(DocLabel);
        DocLabel.setBounds(20, 400, 50, 16);

        EduUnitPdfReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pdf.png"))); // NOI18N
        EduUnitPdfReport.setBorderPainted(false);
        EduUnitPdfReport.setContentAreaFilled(false);
        EduUnitPdfReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitPdfReportActionPerformed(evt);
            }
        });
        add(EduUnitPdfReport);
        EduUnitPdfReport.setBounds(60, 230, 35, 35);

        DocUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        DocUpload.setBorderPainted(false);
        DocUpload.setContentAreaFilled(false);
        DocUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DocUploadActionPerformed(evt);
            }
        });
        add(DocUpload);
        DocUpload.setBounds(110, 400, 35, 35);

        SrcDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        SrcDownload.setBorderPainted(false);
        SrcDownload.setContentAreaFilled(false);
        SrcDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SrcDownloadActionPerformed(evt);
            }
        });
        add(SrcDownload);
        SrcDownload.setBounds(350, 400, 35, 35);

        SrcUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        SrcUpload.setBorderPainted(false);
        SrcUpload.setContentAreaFilled(false);
        SrcUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SrcUploadActionPerformed(evt);
            }
        });
        add(SrcUpload);
        SrcUpload.setBounds(310, 400, 35, 35);

        bbb4.setText("Балл");
        add(bbb4);
        bbb4.setBounds(280, 260, 60, 16);

        bbb5.setText("Неделя ");
        add(bbb5);
        bbb5.setBounds(110, 260, 60, 16);

        EduUnitPoint.setEnabled(false);
        add(EduUnitPoint);
        EduUnitPoint.setBounds(340, 260, 40, 25);

        EduUnitType.setBackground(new java.awt.Color(204, 204, 204));
        EduUnitType.setEnabled(false);
        add(EduUnitType);
        EduUnitType.setBounds(280, 230, 100, 20);

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

        PointState.setEnabled(false);
        add(PointState);
        PointState.setBounds(110, 310, 180, 25);

        PointDate.setEnabled(false);
        PointDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PointDateMouseClicked(evt);
            }
        });
        add(PointDate);
        PointDate.setBounds(110, 370, 90, 25);

        ссс2.setText("Дата");
        add(ссс2);
        ссс2.setBounds(20, 370, 50, 16);

        EduUnitManual.setText("\"Ручная\" установка");
        EduUnitManual.setEnabled(false);
        add(EduUnitManual);
        EduUnitManual.setBounds(110, 230, 160, 20);

        Quality.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Quality.setLayout(null);
        add(Quality);
        Quality.setBounds(200, 450, 190, 170);

        jLabel7.setText("Бригада");
        add(jLabel7);
        jLabel7.setBounds(110, 150, 80, 16);

        StudentRating.setEnabled(false);
        add(StudentRating);
        StudentRating.setBounds(320, 150, 60, 25);
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
        new APICall<TableData>(main) {
            @Override
            public Call<TableData> apiFun() {
                return  ((PRSClient)main).service2.createGroupReportTable(main.debugToken,cRating.getOid());
              }
            @Override
            public void onSucess(TableData oo) {
                new TableWindow(oo, 1, new I_TableBack() {
                    @Override
                    public void rowSelected(int row) {
                        System.out.println("row="+row);
                        }
                    @Override
                    public void colSelected(int col) {
                        System.out.println("col="+col);
                        }
                    @Override
                    public void cellSelected(int row, int col) {
                        System.out.println("row="+row+" col="+col);
                        }
                    @Override
                    public void onClose() {
                        System.out.println("closed");
                        }
                    });
                }
            };
    }//GEN-LAST:event_RatingTableReportActionPerformed

    private void DocDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DocDownloadActionPerformed
        main.loadFile(cPoint.getReport().getRef());
    }//GEN-LAST:event_DocDownloadActionPerformed

    private void RatingPdfReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RatingPdfReportActionPerformed
        new APICall<Artifact>(main) {
            @Override
            public Call<Artifact> apiFun() {
                return  ((PRSClient)main).service2.createGroupReportArtifact(main.debugToken,cRating.getOid(),Values.ReportPDF);
                }
            @Override
            public void onSucess(Artifact oo) {
                main.loadFileAndDelete(oo);
                }
        };
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
        if(evt.getKeyCode()!=10) return;
        if (cRating==null || cPoint==null)
            return;
            cPoint.setVariant(PointVariant.getText());
            pointUpdate(evt,true);
    }//GEN-LAST:event_PointVariantKeyPressed

    private void PointKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRating==null || cPoint==null)
            return;
        try {
            cPoint.setPoint(Integer.parseInt(Point.getText()));
            pointUpdate(evt,true);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
        }//GEN-LAST:event_PointKeyPressed

    public void pointUpdate(KeyEvent evt,boolean noPopup){
        try {
            if (newPoint)
                pointAdd();
            else{
                cPoint.calcPointValue(cEduUnit,cRating.getSemRule().getRef());
                new APICall2<JEmpty>() {
                @Override
                public Call<JEmpty> apiFun() {
                    return main.service.updateEntity(main.debugToken,new DBRequest(cPoint,main.gson));
                    }
                }.call(main);
                }
            newPoint = false;
            if (evt!=null)
                main.viewUpdate(evt,true);
            if (!noPopup)
                popup("Данные о выполнении обновлены");
            studentRatingUpdate();
            savePos();
            refreshStudentPoints();
        } catch (UniException ee){
            System.out.println(ee.toString());
            if (evt!=null)
                main.viewUpdate(evt,false);
            }
        }

    public void studentRatingUpdate(){
        try {
            cStudRating.calcSemesterRating(cDiscipline.getUnits(),cRating.getSemRule().getRef());
            new APICall2<JEmpty>() {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.updateEntity(main.debugToken,new DBRequest(cStudRating,main.gson));
                    }
                }.call(main);
            } catch (UniException ee){
                System.out.println(ee.toString());
                }
    }



    public void pointAdd() throws UniException {
        long oid =  new APICall2<JLong>() {
            @Override
            public Call<JLong> apiFun() {
                return main.service.addEntity(main.debugToken,new DBRequest(cPoint,main.gson),0);
            }
        }.call(main).getValue();
        cPoint.setOid(oid);
    }

    private void EduUnitPdfReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitPdfReportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EduUnitPdfReportActionPerformed

    private void DocUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DocUploadActionPerformed
        new UploadPanel(200, 200, main, new I_OK() {
            @Override
            public void onOK(final Entity ent) {
                if (cPoint.getReport().getOid()==0){
                    cPoint.getReport().setOidRef((Artifact) ent);
                    pointUpdate(null,true);
                    return;
                    }
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.removeArtifact(main.debugToken,cPoint.getReport().getOid());
                        }

                    @Override
                    public void onSucess(JEmpty oo) {
                        cPoint.getReport().setOidRef((Artifact) ent);
                        pointUpdate(null,true);
                        }
                    };
                }
            });
    }//GEN-LAST:event_DocUploadActionPerformed

    private void SrcDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrcDownloadActionPerformed
        main.loadFile(cPoint.getSource().getRef());
    }//GEN-LAST:event_SrcDownloadActionPerformed

    private void SrcUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrcUploadActionPerformed
        new UploadPanel(200, 200, main, new I_OK() {
            @Override
            public void onOK(final Entity ent) {
                if (cPoint.getSource().getOid()==0){
                    cPoint.getSource().setOidRef((Artifact) ent);
                    pointUpdate(null,true);
                    return;
                }
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.removeArtifact(main.debugToken,cPoint.getSource().getOid());
                    }

                    @Override
                    public void onSucess(JEmpty oo) {
                        cPoint.getSource().setOidRef((Artifact) ent);
                        pointUpdate(null,true);
                    }
                };
            }
        });

    }//GEN-LAST:event_SrcUploadActionPerformed

    private void EduUnitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EduUnitsItemStateChanged
        eduUnits.savePos();
        refreshSelectedEduUnit();
        refreshPoint();
    }//GEN-LAST:event_EduUnitsItemStateChanged

    private void StudentsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StudentsItemStateChanged
        students.savePos();
        refreshSelectedStudent();
        refreshStudentPoints();
        refreshPoint();
    }//GEN-LAST:event_StudentsItemStateChanged

    private void TeamsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TeamsItemStateChanged
        teams.savePos();
        refreshSelectedTeam();
    }//GEN-LAST:event_TeamsItemStateChanged

    private void RatingsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingsItemStateChanged
        savePos();
        refreshSelectedRating();
    }//GEN-LAST:event_RatingsItemStateChanged

    private void RefreshAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshAllActionPerformed
        refreshAll();
    }//GEN-LAST:event_RefreshAllActionPerformed

    private void PointDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PointDateMouseClicked
        if (evt.getClickCount()<2)
            return;
        if (cRating==null || cPoint.getState()==Values.PSNotIssued)
            return;
        new CalendarView("Дата сдачи", new I_CalendarTime() {
            @Override
            public void onSelect(OwnDateTime time) {
                cPoint.setDate(time);
                String ss = cPoint.setDeliveryWeekByDate(cRating.getSemRule().getRef());
                if (ss.length()!=0)
                    popup(ss);
                else
                    pointUpdate(null,false);
                }
            });
    }//GEN-LAST:event_PointDateMouseClicked

    private void PointDeliveryWeekKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointDeliveryWeekKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRating==null || cPoint==null || cPoint.getState()==Values.PSNotIssued)
            return;
        try {
            cPoint.setDeliveryWeek(Integer.parseInt(PointDeliveryWeek.getText()));
            cPoint.setDateByWeek(cRating.getSemRule().getRef());
            pointUpdate(evt,false);
            } catch (Exception ee){
                popup("Ошибка формата целого");
                main.viewUpdate(evt,false);
                }
    }//GEN-LAST:event_PointDeliveryWeekKeyPressed

    public void refreshRatings(){
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
                refreshTeacherRatings();
                }
            };
        }
    public void refreshTeacherRatings() {
        UserAccount.setText(main.loginUser.getTitle()+": "+main.loginUser.getMail());
        if (main.loginUser.getTypeId() != Values.UserTeacher) {
            for (SAGroupRating gg : allRatings)
                ratings.add(gg);
            refreshSelectedRating();
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
                    refreshSelectedRating();
                    } catch (UniException e) {
                        System.out.println(e);
                        }
                    }
                };
            }

        public void refreshSelectedRating(){
            ratings.restorePos();
            cRating = ratings.get();
            if (cRating==null)
                return;
            new APICall<DBRequest>(main) {
                @Override
                public Call<DBRequest> apiFun() {
                    return main.service.getEntity(main.debugToken,"SAGroupRating",cRating.getOid(),2);
                    }
                @Override
                public void onSucess(DBRequest oo) {
                    try{
                        cRating = (SAGroupRating)oo.get(main.gson);
                        refreshStudents();
                        refreshEduUnits();
                        refreshStudentPoints();
                        }catch (Exception ee){
                            System.out.println(ee.toString());
                            }
                    }
                };
            }

    public void refreshStudentPoints(){
        if (cRating==null)
            return;
        cStudRating = null;
        StudentRating.setText("");
        for(SASemesterRating semesterRating : cRating.getSemRatings())
            if (semesterRating.getStudent().getOid()==cStudent.getOid()){
                cStudRating = semesterRating;
                break;
                }
        if (cStudRating==null){
            System.out.println("На найдены данные семестра для студента "+cStudent.getTitle());
            return;
            }
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"SASemesterRating",cStudRating.getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try{
                    cStudRating = (SASemesterRating) oo.get(main.gson);
                    StudentRating.setText(""+cStudRating.getSemesterRating());
                    refreshPoint();
                    }catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитаны данные семестра для студента "+cStudent.getTitle());
                        }
                    }
                };
        }

    public void refreshPoint(){
        DocDownload.setVisible(false);
        DocUpload.setVisible(false);
        SrcDownload.setVisible(false);
        SrcUpload.setVisible(false);
        DocLabel.setVisible(false);
        SrcLabel.setVisible(false);
        Point.setEnabled(false);
        if (cStudent==null || cEduUnit==null || cStudRating==null)
            return;
        Point.setEnabled(cEduUnit.isManualPointSet());
        cPoint=null;
        for (SAPoint point : cStudRating.getPoints()){
            if (point.getState()==Values.PSArchive)
                continue;
            if (point.getEduUnit().getOid()==cEduUnit.getOid()){
                cPoint = point;
                break;
                }
            }
        newPoint = false;
        if (cPoint==null){
            newPoint = true;
            cPoint = new SAPoint(cRating.getOid(),cStudent.getOid(),cEduUnit.getOid(),cStudRating.getOid());
            }
        boolean bb = cPoint.getState()!=Values.PSNotIssued;
        if (bb && !cEduUnit.isManualPointSet()){
            quality.setVisible(true);
            quality.select(cPoint.getQuality());
            }
        PointVariant.setText(cPoint.getVariant());
        Point.setText(""+Math.round(cPoint.calcPointValue(cEduUnit,cRating.getSemRule().getRef())));
        PointDate.setText(cPoint.getDate().dateToString());
        int week = cPoint.getDeliveryWeek();
        PointDeliveryWeek.setText(cPoint.weekToString());
        DocDownload.setVisible(cPoint.getReport().getOid()!=0);
        DocUpload.setVisible(bb);
        DocLabel.setVisible(bb);
        SrcDownload.setVisible(cPoint.getSource().getOid()!=0);
        SrcUpload.setVisible(bb);
        SrcLabel.setVisible(bb);
        pointStateMashine.refresh(cPoint);
        if (!cEduUnit.isManualPointSet())
            quality.select(cPoint.getQuality());
        PointState.setText("");
        for(ConstValue cc : pointStates)
            if(cc.value()==cPoint.getState()){
                PointState.setText(cc.title());
                break;
            }
        }
    public void refreshTeams(){
        teams.restorePos();
        teams.clear();
        for (SATeam team : cRating.getTeams())
            teams.add(team);
        refreshSelectedTeam();
        }
    public void refreshSelectedTeam(){
        teams.restorePos();
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

    public void refreshEduUnits(){
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
                refreshSelectedEduUnit();
                }
            };
        }

    public void refreshSelectedEduUnit(){
        eduUnits.restorePos();
        refresh = true;
        EduUnitWeek.setText("");
        EduUnitPoint.setText("");
        EduUnitManual.setSelected(false);
        cEduUnit = eduUnits.get();
        if (cEduUnit==null){
            refresh=false;
            return;
            }
        if (cEduUnit.getDeliveryWeek()!=0)
            EduUnitWeek.setText(""+cEduUnit.getDeliveryWeek());
        EduUnitPoint.setText(""+cEduUnit.getBasePoint());
        EduUnitManual.setSelected(cEduUnit.isManualPointSet());
        if (eduUnitType.selectByValue(eduUnits.get().getUnitType())==null)
            System.out.println("Недопустимый тип учебной единицы: "+eduUnits.get().getUnitType());
        refresh=false;
        }

    public void refreshSelectedStudent(){
        students.restorePos();
        cStudent = students.get();
        }

    public void refreshStudents(){
        students.restorePos();
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
                    refreshSelectedStudent();
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
    private javax.swing.JLabel DocLabel;
    private javax.swing.JButton DocUpload;
    private javax.swing.JCheckBox EduUnitManual;
    private javax.swing.JButton EduUnitPdfReport;
    private javax.swing.JTextField EduUnitPoint;
    private javax.swing.JButton EduUnitTableReport;
    private java.awt.Choice EduUnitType;
    private javax.swing.JTextField EduUnitWeek;
    private java.awt.Choice EduUnits;
    private javax.swing.JTextField Point;
    private javax.swing.JTextField PointDate;
    private javax.swing.JTextField PointDeliveryWeek;
    private javax.swing.JTextField PointState;
    private javax.swing.JTextField PointVariant;
    private javax.swing.JPanel Quality;
    private javax.swing.JButton RatingPdfReport;
    private javax.swing.JButton RatingTableReport;
    private java.awt.Choice Ratings;
    private javax.swing.JButton RefreshAll;
    private javax.swing.JButton SrcDownload;
    private javax.swing.JLabel SrcLabel;
    private javax.swing.JButton SrcUpload;
    private javax.swing.JButton StudentPdfReport;
    private javax.swing.JTextField StudentRating;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel ссс1;
    private javax.swing.JLabel ссс2;
    // End of variables declaration//GEN-END:variables
}
