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
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.baseentityes.JBoolean;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.baseentityes.JLong;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.mongo.*;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;

import java.io.*;
import java.util.*;

/**
 *
 * @author romanow
 */
public class PRSRatingPanel extends BasePanel{
    @Getter private ArrayList<SAGroupRating> allRatings = new ArrayList<>();        // Список рейтингов
    @Getter private ArrayList<SAGroupRating> notAssigned = new ArrayList<>();       // Список рейтингов
    @Getter private SAGroup cGroup=null;
    @Getter private SATeacher cTeacher=null;
    private ChoiceList<SAGroup> groups;
    private ChoiceList<SAStudent> students;
    private ChoiceList<SAGroupRating> ratings;
    private ChoiceList<User> teachers;
    private ChoiceList<SADiscipline> ratingDisciplines;
    private ChoiceList<SAGroup> ratingGroups;
    private ChoiceList<SASemesterRule> ratingRules;
    //---------------------------------------------------------------------------------------
    private boolean refresh=false;                                      // Признак обновления для событий  CheckBox
    public PRSRatingPanel() {
        initComponents();
        groups = new ChoiceList<SAGroup>(Groups);
        students =   new ChoiceList<SAStudent>(Students);
        ratings = new ChoiceList(Ratings);
        teachers =  new ChoiceList(Teachers);
        ratingDisciplines = new ChoiceList(RatingDisciplineSelector);
        ratingGroups  = new ChoiceList(RatingGroupSelector);
        ratingRules  = new ChoiceList(RatingRuleSelector);
        }
    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        refreshAll();
        }
    public void clearPos(){
        groups.clearPos();
        students.clearPos();
        ratings.clearPos();
        teachers.clearPos();
        ratingDisciplines.clearPos();
        ratingGroups.clearPos();
        }
    private void savePos(){
        groups.savePos();
        students.savePos();
        ratings.savePos();
        teachers.savePos();
        ratingDisciplines.savePos();
        ratingGroups.savePos();
        }
    public void refreshAll(){
        refreshAll(false);
        }
    public void refreshAll(boolean withPos){
        if (withPos)
            savePos();
        else
            clearPos();
        refreshGroupsList(withPos);
        refreshDisciplineList(withPos);
        refreshTeachers(withPos);
        refreshRuleList(withPos);
        }

    public void refreshDisciplineList(boolean withPos){
        ratingDisciplines.clear();
        new APICall<ArrayList<DBRequest>>(main){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"SADiscipline", Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                try {
                for(DBRequest request : oo){
                    SADiscipline discipline = (SADiscipline) request.get(main.gson);
                    ratingDisciplines.add(discipline);
                    }
                ratingDisciplines.withPos(withPos);
                } catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Ошибка чтения списка дисциплин");
                    }
                }
            };
        }

    public void refreshRuleList(boolean withPos){
        ratingRules.clear();
        new APICall<ArrayList<DBRequest>>(main){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"SASemesterRule", Values.GetAllModeActual,0);
            }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                try {
                    for(DBRequest request : oo){
                        SASemesterRule rule = (SASemesterRule) request.get(main.gson);
                        ratingRules.add(rule);
                    }
                    ratingRules.withPos(withPos);
                } catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Ошибка чтения списка регламентов");
                }
            }
        };
    }

    public void refreshGroupsList(boolean withPos){
        groups.clear();
        students.clear();
        ratingGroups.clear();
        new APICall<ArrayList<DBRequest>>(main) {
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"SAGroup",Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                groups.clear();
                Groups.removeAll();
                try {
                    for (DBRequest dd : oo) {
                        SAGroup group = (SAGroup)dd.get(main.gson);
                        groups.add(group);
                        ratingGroups.add(group);
                        }
                    groups.withPos(withPos);
                    ratingGroups.withPos(withPos);
                    refreshStudentList(withPos);
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитан список групп");
                        return;
                        }
                    }
                };
        }
    public void refreshStudentList(boolean withPos){
        students.clear();
        if (groups.size()==0)
            return;
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"SAGroup",groups.get().getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try{
                    cGroup = (SAGroup)oo.get(main.gson);
                    for(SAStudent student : cGroup.getStudents())
                        students.add(student);
                    students.withPos(withPos);
                    }catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитаны данные группы "+cGroup.getName());
                        }
                }
            };
        }

    public void refreshSelectedRating(){
        GroupRatingName.setText("");
        SAGroupRating rating = ratings.get();
        if (rating==null)
            return;
        GroupRatingName.setText(rating.getName());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        Groups = new java.awt.Choice();
        Students = new java.awt.Choice();
        Refresh = new javax.swing.JButton();
        GroupAdd = new javax.swing.JButton();
        GroupRemove = new javax.swing.JButton();
        StudentAdd = new javax.swing.JButton();
        StudentRemove = new javax.swing.JButton();
        GroupEdit = new javax.swing.JButton();
        StudentEdit = new javax.swing.JButton();
        GroupsImport = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        Ratings = new java.awt.Choice();
        GroupRatingAdd = new javax.swing.JButton();
        GroupRatingRemove = new javax.swing.JButton();
        GroupRatingName = new javax.swing.JTextField();
        TaskTypeLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Teachers = new java.awt.Choice();
        RatingsAssigned = new java.awt.Choice();
        RatingAdd = new javax.swing.JButton();
        RatingRemove = new javax.swing.JButton();
        RatingsNotAssigned = new java.awt.Choice();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        StudentMail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        RatingDisciplineSelector = new java.awt.Choice();
        RatingGroupSelector = new java.awt.Choice();
        RatingRuleSelector = new java.awt.Choice();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Группа");
        add(jLabel4);
        jLabel4.setBounds(50, 10, 70, 16);

        Groups.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GroupsItemStateChanged(evt);
            }
        });
        add(Groups);
        Groups.setBounds(50, 30, 200, 20);

        Students.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StudentsItemStateChanged(evt);
            }
        });
        add(Students);
        Students.setBounds(10, 70, 240, 20);

        Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        Refresh.setBorderPainted(false);
        Refresh.setContentAreaFilled(false);
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });
        add(Refresh);
        Refresh.setBounds(10, 10, 30, 30);

        GroupAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        GroupAdd.setBorderPainted(false);
        GroupAdd.setContentAreaFilled(false);
        GroupAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupAddActionPerformed(evt);
            }
        });
        add(GroupAdd);
        GroupAdd.setBounds(260, 30, 30, 30);

        GroupRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        GroupRemove.setBorderPainted(false);
        GroupRemove.setContentAreaFilled(false);
        GroupRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupRemoveActionPerformed(evt);
            }
        });
        add(GroupRemove);
        GroupRemove.setBounds(300, 30, 30, 30);

        StudentAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        StudentAdd.setBorderPainted(false);
        StudentAdd.setContentAreaFilled(false);
        StudentAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentAddActionPerformed(evt);
            }
        });
        add(StudentAdd);
        StudentAdd.setBounds(260, 60, 30, 30);

        StudentRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        StudentRemove.setBorderPainted(false);
        StudentRemove.setContentAreaFilled(false);
        StudentRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentRemoveActionPerformed(evt);
            }
        });
        add(StudentRemove);
        StudentRemove.setBounds(300, 60, 30, 30);

        GroupEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        GroupEdit.setBorderPainted(false);
        GroupEdit.setContentAreaFilled(false);
        GroupEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupEditActionPerformed(evt);
            }
        });
        add(GroupEdit);
        GroupEdit.setBounds(340, 30, 30, 30);

        StudentEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        StudentEdit.setBorderPainted(false);
        StudentEdit.setContentAreaFilled(false);
        StudentEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentEditActionPerformed(evt);
            }
        });
        add(StudentEdit);
        StudentEdit.setBounds(340, 60, 30, 30);

        GroupsImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/archive.png"))); // NOI18N
        GroupsImport.setBorderPainted(false);
        GroupsImport.setContentAreaFilled(false);
        GroupsImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupsImportActionPerformed(evt);
            }
        });
        add(GroupsImport);
        GroupsImport.setBounds(380, 30, 40, 30);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Группа-дисциплина (рейтинг)");
        add(jLabel14);
        jLabel14.setBounds(10, 140, 250, 16);

        Ratings.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RatingsItemStateChanged(evt);
            }
        });
        add(Ratings);
        Ratings.setBounds(10, 160, 320, 20);

        GroupRatingAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        GroupRatingAdd.setBorderPainted(false);
        GroupRatingAdd.setContentAreaFilled(false);
        GroupRatingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupRatingAddActionPerformed(evt);
            }
        });
        add(GroupRatingAdd);
        GroupRatingAdd.setBounds(340, 150, 30, 30);

        GroupRatingRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        GroupRatingRemove.setBorderPainted(false);
        GroupRatingRemove.setContentAreaFilled(false);
        GroupRatingRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupRatingRemoveActionPerformed(evt);
            }
        });
        add(GroupRatingRemove);
        GroupRatingRemove.setBounds(380, 150, 30, 30);

        GroupRatingName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GroupRatingNameKeyPressed(evt);
            }
        });
        add(GroupRatingName);
        GroupRatingName.setBounds(10, 190, 320, 25);

        TaskTypeLabel.setText("Не назначенные");
        add(TaskTypeLabel);
        TaskTypeLabel.setBounds(10, 410, 130, 16);

        jLabel3.setText("Назначенные");
        add(jLabel3);
        jLabel3.setBounds(10, 370, 160, 16);

        Teachers.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TeachersItemStateChanged(evt);
            }
        });
        add(Teachers);
        Teachers.setBounds(10, 350, 320, 20);

        RatingsAssigned.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RatingsAssignedItemStateChanged(evt);
            }
        });
        add(RatingsAssigned);
        RatingsAssigned.setBounds(10, 390, 320, 20);

        RatingAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/up.PNG"))); // NOI18N
        RatingAdd.setBorderPainted(false);
        RatingAdd.setContentAreaFilled(false);
        RatingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RatingAddActionPerformed(evt);
            }
        });
        add(RatingAdd);
        RatingAdd.setBounds(340, 430, 30, 30);

        RatingRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RatingRemove.setBorderPainted(false);
        RatingRemove.setContentAreaFilled(false);
        RatingRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RatingRemoveActionPerformed(evt);
            }
        });
        add(RatingRemove);
        RatingRemove.setBounds(340, 390, 30, 30);

        RatingsNotAssigned.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RatingsNotAssignedItemStateChanged(evt);
            }
        });
        add(RatingsNotAssigned);
        RatingsNotAssigned.setBounds(10, 430, 320, 20);
        add(jSeparator1);
        jSeparator1.setBounds(10, 320, 400, 10);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Преподаватель");
        add(jLabel11);
        jLabel11.setBounds(10, 330, 120, 16);
        add(jSeparator3);
        jSeparator3.setBounds(20, 590, 400, 10);

        StudentMail.setEnabled(false);
        add(StudentMail);
        StudentMail.setBounds(10, 100, 320, 22);

        jLabel7.setText("Студент");
        add(jLabel7);
        jLabel7.setBounds(10, 50, 70, 16);
        add(jSeparator2);
        jSeparator2.setBounds(10, 130, 400, 10);

        RatingDisciplineSelector.setBackground(new java.awt.Color(204, 204, 204));
        add(RatingDisciplineSelector);
        RatingDisciplineSelector.setBounds(10, 230, 320, 20);

        RatingGroupSelector.setBackground(new java.awt.Color(204, 204, 204));
        add(RatingGroupSelector);
        RatingGroupSelector.setBounds(10, 260, 320, 20);

        RatingRuleSelector.setBackground(new java.awt.Color(204, 204, 204));
        add(RatingRuleSelector);
        RatingRuleSelector.setBounds(10, 290, 320, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void GroupsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GroupsItemStateChanged
        savePos();
        refreshGroupsList(true);
    }//GEN-LAST:event_GroupsItemStateChanged

    private void StudentsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StudentsItemStateChanged
        savePos();
    }//GEN-LAST:event_StudentsItemStateChanged

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        refreshAll(true);
    }//GEN-LAST:event_RefreshActionPerformed

    private void GroupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupAddActionPerformed
        new OKName(200,200,"Добавить группу", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                SAGroup bean = new SAGroup();
                bean.setName(value);
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken, new DBRequest(bean,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        savePos();
                        groups.toNewElement();
                        refreshGroupsList(true);
                        }
                    };
                }
            });
    }//GEN-LAST:event_GroupAddActionPerformed

    private void GroupRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupRemoveActionPerformed
        if (cGroup==null)
            return;
        new OK(200, 200, "Удалить группу: " + cGroup.getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"SAGroup",cGroup.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        savePos();
                        groups.toPrevElement();
                        refreshGroupsList(true);
                        }
                };
            }
        });

    }//GEN-LAST:event_GroupRemoveActionPerformed

    private void StudentAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentAddActionPerformed

    private void StudentRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentRemoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentRemoveActionPerformed

    private void GroupEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GroupEditActionPerformed

    private void StudentEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentEditActionPerformed


    private ExcelX2 excel;

    public void procNextSheet(final String sheets[], final int idx){
        new OKFull(200, 200, "Импорт группы " + sheets[idx], new I_ButtonFull() {
            @Override
            public void onPush(boolean yes) {
                if (!yes){
                    if ((idx+1)!=sheets.length) {
                        procNextSheet(sheets, idx + 1);
                        }
                    return;
                    }
                System.out.println("Импорт группы "+sheets[idx]);
                final SAGroup group = new SAGroup();
                group.setName(sheets[idx]);
                try {
                    final JLong group2 = new APICallSync<JLong>() {
                        @Override
                        public Call<JLong> apiFun() {
                            return main.service.addEntity(main.debugToken,new DBRequest(group,main.gson),0);
                            }
                        }.call();
                    excel.procSheet(sheets[idx], new I_ExcelBack() {
                        @Override
                        public void onRow(String[] values) {
                            System.out.println(values[0] + " " + values[1]);
                            Account account = new Account();
                            User user = new User();
                            account.setLogin(values[1]);
                            user.setPost(values[1]);
                            String ss[] = UtilsPRS.parseFIO(values[0]);
                            user.setLastName(ss[0]);
                            user.setFirstName(ss[1]);
                            user.setMiddleName(ss[2]);
                            account.setPassword("1234");
                            user.setTypeId(Values.UserStudent);
                            user.setAccount(account);
                            SAStudent student = new SAStudent();
                            student.setState(Values.StudentStateNormal);
                            student.getSAGroup().setOid(group2.getValue());
                            try {
                                JLong userOid = new APICallSync<JLong>() {
                                    @Override
                                    public Call<JLong> apiFun() {
                                        return main.service.addUser(main.debugToken,user);
                                        }
                                    }.call();
                                student.getUser().setOid(userOid.getValue());
                                JLong studOid = new APICallSync<JLong>() {
                                    @Override
                                    public Call<JLong> apiFun() {
                                        return main.service.addEntity(main.debugToken,new DBRequest(student,main.gson),0);
                                        }
                                    }.call();
                                } catch (IOException ee){
                                    System.out.println("Ошибка импорта студента "+values[0]);
                                    }
                            }

                        @Override
                        public void onFinishSheet(String errorMes) {
                            if (errorMes.length() != 0)
                                System.out.println(errorMes);
                            if ((idx + 1) != sheets.length)
                                procNextSheet(sheets, idx + 1);
                            }
                        });
                    } catch (Exception ee){
                        System.out.println("Ошибка импорта "+sheets[idx]);
                        if ((idx+1)!=sheets.length) {
                            procNextSheet(sheets, idx + 1);
                            }
                        }
                }
            });
        }

    private void GroupsImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupsImportActionPerformed
        FileNameExt fname = main.getInputFileName("Импорт групп", "xlsx", null);
        if (fname == null)
            return;
        excel = new ExcelX2();
        try{
            String sheets[] = excel.openTable(fname.fullName(), new String[]{"ФИО", "Эл. почта"});
            procNextSheet(sheets,0);
            } catch (Exception ee){
                System.out.println("Ошибка импорта:\n"+ee.toString());
                }
    }//GEN-LAST:event_GroupsImportActionPerformed

    private void GroupRatingAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupRatingAddActionPerformed
        if (ratingGroups.get()==null || ratingDisciplines.get()==null || ratingRules.get()==null)
            return;
        new OK(200, 200, "Добавить рейтинг " + ratingDisciplines.get().getName()+"-"+ratingGroups.get().getName(), new I_Button() {
            @Override
            public void onPush() {
                final SAGroupRating rating = new SAGroupRating();
                rating.getExamRule().setOid(ratingRules.get().getOid());
                rating.getSADiscipline().setOid(ratingDisciplines.get().getOid());
                rating.getGroup().setOid(ratingGroups.get().getOid());
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return ((PRSClient)main).service2.addGroupToDiscipline(main.debugToken,rating);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        ratings.toNewElement();
                        refreshAll(true);
                        }
                    };
                }
            });
    }//GEN-LAST:event_GroupRatingAddActionPerformed

    private void GroupRatingRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupRatingRemoveActionPerformed
        final SAGroupRating rating = ratings.get();
        if (rating==null)
            return;
        new OK(200, 200, "Удалить рейтинг " + rating.getTitle(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return ((PRSClient)main).service2.removeGroupFromExam(main.debugToken,rating.getOid());
                        }
                    @Override
                    public void onSucess(JEmpty oo) {
                        ratings.toPrevElement();
                        refreshAll(true);
                        popup("Рейтинг удален");
                    }
                };
            }
        });
    }//GEN-LAST:event_GroupRatingRemoveActionPerformed


    private void refreshTeachers(boolean withPos){
        DBQueryList query =  new DBQueryList().
                add(new DBQueryInt(I_DBQuery.ModeEQ,"typeId",Values.UserTeacher)).
                add(new DBQueryBoolean("valid",true));
        final String xmlQuery = new DBXStream().toXML(query);
        new APICall<ArrayList<DBRequest>>(null){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.getService().getEntityListByQuery(main.getDebugToken(),"User",xmlQuery,1);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                teachers.clear();
                for(DBRequest dd : oo){
                    try {
                        User ss = (User) dd.get(new Gson());
                        teachers.add(ss);
                        } catch (UniException e) {
                            System.out.println(e);
                            }
                    }
                teachers.withPos(withPos);
                loadAllRatings(withPos);
            }
        };
    }

    public void loadAllRatings(boolean withPos){
        ratings.clear();
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
                        ratings.add(ss);
                    } catch (UniException e) {
                        System.out.println(e);
                        }
                    }
                ratings.withPos(withPos);
                refreshSelectedRating();
                loadTeacherRatings(withPos);
                }
            };
        }
    public void loadTeacherRatings(boolean withPos){
        RatingsAssigned.removeAll();
        RatingsNotAssigned.removeAll();
        User user = teachers.get();
        if (user==null)
            return;
        final long userOid = user.getOid();
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
                    cTeacher = new SATeacher();
                    cTeacher.getUser().setOid(userOid);
                    addNewTeacher();
                    showRatingAssignment();
                    return;
                    }
                try {
                    cTeacher = (SATeacher)  oo.get(0).get(new Gson());
                    showRatingAssignment();
                } catch (UniException e) {
                    System.out.println(e);
                }
            }
        };
    }

    public void showRatingAssignment(){
        RatingsAssigned.removeAll();
        for(EntityLink<SAGroupRating> ss : cTeacher.getRatings())
            RatingsAssigned.add(ss.getTitle());
        RatingsNotAssigned.removeAll();
        notAssigned.clear();
        for(SAGroupRating ss : allRatings)
            if (cTeacher.getRatings().getById(ss.getOid())==null){
                notAssigned.add(ss);
                RatingsNotAssigned.add(ss.getTitle());
                }
    }
    public void addNewTeacher(){
        new APICall<JLong>(null) {
            @Override
            public Call<JLong> apiFun() {
                return main.getService().addEntity(main.debugToken,new DBRequest(cTeacher,main.gson),0);
                }
            @Override
            public void onSucess(JLong oo) {}
        };
    }

    public void ratingUpdate(){
        if (ratings.get() ==null) return;
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken, new DBRequest(ratings.get(),main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                savePos();
                refreshAll(true);
                }
            };       
        }

    private void GroupRatingNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GroupRatingNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (ratings.get() ==null) return;
        ratings.get().setName(GroupRatingName.getText());
        ratingUpdate();
        main.viewUpdate(evt,true);
    }//GEN-LAST:event_GroupRatingNameKeyPressed

    private void TeachersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TeachersItemStateChanged
        savePos();
        loadTeacherRatings(true);
    }//GEN-LAST:event_TeachersItemStateChanged

    private void RatingsAssignedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingsAssignedItemStateChanged

    }//GEN-LAST:event_RatingsAssignedItemStateChanged

    private void RatingAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RatingAddActionPerformed
        if (RatingsNotAssigned.getItemCount()==0 || teachers.size()==0)
        return;
        int idx = RatingsNotAssigned.getSelectedIndex();
        long oid = notAssigned.get(idx).getOid();
        cTeacher.getRatings().add(oid);
        new APICall<JEmpty>(null) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken,new DBRequest(cTeacher,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                loadTeacherRatings(true);
                }
        };
    }//GEN-LAST:event_RatingAddActionPerformed

    private void RatingRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RatingRemoveActionPerformed
        if (RatingsAssigned.getItemCount()==0 || teachers.size()==0)
        return;
        int idx = RatingsAssigned.getSelectedIndex();
        cTeacher.getRatings().remove(idx);
        new APICall<JEmpty>(null) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken,new DBRequest(cTeacher,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                loadTeacherRatings(true);
                }
        };
    }//GEN-LAST:event_RatingRemoveActionPerformed

    private void RatingsNotAssignedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingsNotAssignedItemStateChanged

    }//GEN-LAST:event_RatingsNotAssignedItemStateChanged

    private void RatingsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingsItemStateChanged
        refreshSelectedRating();
    }//GEN-LAST:event_RatingsItemStateChanged


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
    private javax.swing.JButton GroupAdd;
    private javax.swing.JButton GroupEdit;
    private javax.swing.JButton GroupRatingAdd;
    private javax.swing.JTextField GroupRatingName;
    private javax.swing.JButton GroupRatingRemove;
    private javax.swing.JButton GroupRemove;
    private java.awt.Choice Groups;
    private javax.swing.JButton GroupsImport;
    private javax.swing.JButton RatingAdd;
    private java.awt.Choice RatingDisciplineSelector;
    private java.awt.Choice RatingGroupSelector;
    private javax.swing.JButton RatingRemove;
    private java.awt.Choice RatingRuleSelector;
    private java.awt.Choice Ratings;
    private java.awt.Choice RatingsAssigned;
    private java.awt.Choice RatingsNotAssigned;
    private javax.swing.JButton Refresh;
    private javax.swing.JButton StudentAdd;
    private javax.swing.JButton StudentEdit;
    private javax.swing.JTextField StudentMail;
    private javax.swing.JButton StudentRemove;
    private java.awt.Choice Students;
    private javax.swing.JLabel TaskTypeLabel;
    private java.awt.Choice Teachers;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables


}
