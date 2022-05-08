/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import retrofit2.Call;
import romanow.abc.bridge.APICallSync;
import romanow.abc.bridge.constants.UserRole;
import romanow.abc.convert.onewayticket.OWTDiscipline;
import romanow.abc.convert.onewayticket.OWTReader;
import romanow.abc.convert.onewayticket.OWTTheme;
import romanow.abc.core.UniException;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.exam.model.*;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author romanow
 */
public class ExamAdminPanel extends BasePanel{
    private List<DisciplineBean> disciplines = new ArrayList<>();       // Список дисциплин
    private FullDisciplineBean cDiscipline = null;                      // Текущая дисциплина
    private FullThemeBean cTheme = null;                                // Текущая тема
    private FullTaskBean cTask=null;                                    // Текущая задача/тест
    private HashMap<Long,FullThemeBean> disciplineThemesMap = new HashMap<>();
    private HashMap<Long,FullThemeBean> ruleThemesMap = new HashMap<>();
    private ArrayList<FullThemeBean> ruleThemes = new ArrayList<>();
    private ExamRuleBean cRule=null;
    private int cTaskNum=0;
    private OWTDiscipline owtImportData = null;
    private List<GroupBean> groups = new ArrayList<>();                 // Список групп
    private FullGroupBean cGroup = null;                                // Текущая группа
    private List<GroupBean> examGroupsList = new ArrayList<>();         // Экзамен для текущей дисциплины
    private List<ExamBean> allExams = new ArrayList<>();                // Полный список экзаменов
    private List<ExamRuleBean> allExamRules = new ArrayList<>();        // Полный список регламентов
    private List<ExamRuleBean> cExamRules = new ArrayList<>();          // Список регламентов для дисциплины
    private List<RatingSystemBean> ratings = new ArrayList<>();
    public ExamAdminPanel() {
        initComponents();
        }
    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        ArtifactView.setEnabled(false);
        ArtifactDownLoad.setEnabled(false);
        DisciplineSaveImport.setEnabled(false);
        ArtifactDownLoad.setEnabled(false);
        ArtifactView.setEnabled(false);
        refreshAll();
        }

    private void refreshAll(){
        refreshDisciplineList();
        refreshGroupsList();
        refreshRatingsList();
        }

    private void refreshRatingsList(){
        new APICall<List<RatingSystemBean>>(main) {
            @Override
            public Call<List<RatingSystemBean>> apiFun() {
                return main.client.getRatingSystemApi().getAll1();
                }
            @Override
            public void onSucess(List<RatingSystemBean> oo) {
                ratings = oo;
                }
            };
        }

    private void refreshAllRules(){
        new APICall<List<ExamRuleBean>>(main) {
            @Override
            public Call<List<ExamRuleBean>> apiFun() {
                return main.client.getExamRuleApi().getAll4();
                }
            @Override
            public void onSucess(List<ExamRuleBean> oo) {
                allExamRules = oo;
                refreshRules();
                }
            };
    }

    private void refreshDisciplineList(){
        Discipline.removeAll();
        Theme.removeAll();
        TaskText.setText("");
        new APICall<List<DisciplineBean>>(main) {
            @Override
            public Call<List<DisciplineBean>> apiFun() {
                return main.client.getDisciplineApi().getAll5();
                }
            @Override
            public void onSucess(List<DisciplineBean> oo) {
                disciplines = oo;
                for(DisciplineBean dd : disciplines)
                    Discipline.add(dd.getName());
                refreshDisciplineFull();
                refreshAllRules();
                }
            };
        }

    private void refreshRules(){
        cExamRules.clear();
        RulesList.removeAll();
        for(ExamRuleBean examRule : allExamRules)
            if (cDiscipline.getDiscipline().getId().longValue()==examRule.getDisciplineId().longValue()){
                RulesList.add(examRule.getName());
                cExamRules.add(examRule);
                }
        refreshSelectedRule();
        }

    private void refreshSelectedRule(){
        RuleName.setText("");
        RuleDuration.setText("");
        RuleExcerCount.setText("");
        RuleThemesList.removeAll();
        cRule=null;
        if (cExamRules.size()==0)
            return;
        cRule = cExamRules.get(RulesList.getSelectedIndex());
        RuleName.setText(cRule.getName());
        RuleDuration.setText(""+cRule.getDuration());
        RuleExcerCount.setText(""+cRule.getExerciseCount());
        RuleQurestionCount.setText(""+cRule.getQuestionCount());
        ruleThemes.clear();
        RuleThemesList.removeAll();
        ruleThemesMap.clear();
        for(Long themeId : cRule.getThemeIds()){
            FullThemeBean theme  = disciplineThemesMap.get(themeId);
            if (theme==null)
                System.out.println("Не найдена тема id="+themeId);
            else {
                ruleThemes.add(theme);
                RuleThemesList.add(theme.getTheme().getName());
                ruleThemesMap.put(theme.getTheme().getId(),theme);
                }
            }
        }

    private void refreshGroupsList(){
        Group.removeAll();
        Student.removeAll();
        new APICall<List<GroupBean>>(main) {
            @Override
            public Call<List<GroupBean>> apiFun() {
                return main.client.getGroupApi().getAll2();
            }
            @Override
            public void onSucess(List<GroupBean> oo) {
                groups = oo;
                for(GroupBean dd : groups)
                    Group.add(dd.getName());
                refreshGroupFull();
                }
            };
        new APICall<List<ExamBean>>(main){
            @Override
            public Call<List<ExamBean>> apiFun() {
                return main.client.getExamApi().getAll3();
                }
            @Override
            public void onSucess(List<ExamBean> oo) {
                allExams = oo;
                AllExamsList.removeAll();
                for(ExamBean exam : allExams){
                    AllExamsList.add(exam.toString());
                    }
                }
            };
        }
    private void refreshGroupFull(){
        Student.removeAll();
        cGroup=null;
        if (groups.size()==0)
            return;
        long oid = groups.get(Group.getSelectedIndex()).getId();
        new APICall<FullGroupBean>(main) {
            @Override
            public Call<FullGroupBean> apiFun() {
                return main.client.getGroupApi().getOne2(oid,1);
                }
            @Override
            public void onSucess(FullGroupBean oo) {
                cGroup = oo;
                Student.removeAll();
                for(StudentBean student : cGroup.getStudents())
                    Student.add(student.getAccount().getName());
                //refreshStudentFull();
            }
        };
    }
    private void refreshDisciplineFull(){
        Theme.removeAll();
        Task.removeAll();
        cDiscipline=null;
        if (disciplines.size()==0)
            return;
        final long oid = disciplines.get(Discipline.getSelectedIndex()).getId();
        new APICall<FullDisciplineBean>(main) {
            @Override
            public Call<FullDisciplineBean> apiFun() {
                return main.client.getDisciplineApi().getFull3(oid,2);
                }
            @Override
            public void onSucess(FullDisciplineBean oo) {
                cDiscipline = oo;
                Theme.removeAll();
                disciplineThemesMap.clear();
                for(FullThemeBean theme : cDiscipline.getThemes()){
                    Theme.add(theme.getTheme().getName());
                    disciplineThemesMap.put(theme.getTheme().getId(),theme);
                    }
                refreshThemeFull();
                refreshRules();
                }
            };
        new APICall<List<GroupBean>>(main){
            @Override
            public Call<List<GroupBean>> apiFun() {
                return  main.client.getDisciplineApi().findGroups(oid);
                }
            @Override
            public void onSucess(List<GroupBean> oo) {
                examGroupsList = oo;
                ExamGroupsList.removeAll();
                for(GroupBean group : oo){
                    ExamGroupsList.add(group.getName());
                    }
                }
            };
        }

    private void refreshThemeFull(){
        Task.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Theme.getSelectedIndex());
        int i=1;
        for(FullTaskBean task : cTheme.getTasks())
            Task.add("Вопрос "+i++);
        refreshTaskFull();
        }

    private void refreshTaskFull(){
        TaskText.setText("");
        if (cTheme.getTasks().size()==0)
            return;
        cTaskNum = Task.getSelectedIndex();
        cTask = cTheme.getTasks().get(cTaskNum);
        TaskText.setText(UtilsEM.formatSize(cTask.getTask().getText(),60));
        boolean bb = cTask.getArtefact()!=null;
        ArtifactView.setEnabled(bb);
        ArtifactDownLoad.setEnabled(bb);
        if (bb){
            ArtefactBean artefact = cTask.getArtefact();
            TaskText.append("\n"+artefact.getFileName());
            }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TaskText = new java.awt.TextArea();
        Discipline = new java.awt.Choice();
        Theme = new java.awt.Choice();
        RefreshDisciplines = new javax.swing.JButton();
        DisciplineImport = new javax.swing.JButton();
        RemoveTask = new javax.swing.JButton();
        AddTask = new javax.swing.JButton();
        AddDiscipline = new javax.swing.JButton();
        RemoveDiscipline = new javax.swing.JButton();
        AddTheme = new javax.swing.JButton();
        RemoveTheme = new javax.swing.JButton();
        Task = new java.awt.Choice();
        ArtifactView = new javax.swing.JButton();
        ArtifactUpload = new javax.swing.JButton();
        ArtifactDownLoad = new javax.swing.JButton();
        EditTask = new javax.swing.JButton();
        EditDiscipline = new javax.swing.JButton();
        EditTheme = new javax.swing.JButton();
        FullTrace = new javax.swing.JCheckBox();
        DisciplineSaveImport = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Group = new java.awt.Choice();
        Student = new java.awt.Choice();
        RefreshGroups = new javax.swing.JButton();
        AddGroup = new javax.swing.JButton();
        RemoveGroup = new javax.swing.JButton();
        AddStudent = new javax.swing.JButton();
        RemoveStudent = new javax.swing.JButton();
        EditGroup = new javax.swing.JButton();
        EditStudent = new javax.swing.JButton();
        GroupsImport = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        ExamGroupsList = new java.awt.Choice();
        AllExamsList = new java.awt.Choice();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        RulesList = new java.awt.Choice();
        jLabel9 = new javax.swing.JLabel();
        RuleExcerCount = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        RuleDuration = new javax.swing.JTextField();
        RuleName = new javax.swing.JTextField();
        RuleThemeRemove = new javax.swing.JButton();
        RuleAdd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        RuleThemesList = new java.awt.Choice();
        RuleThemeAdd = new javax.swing.JButton();
        RuleDelete = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        RuleQurestionCount = new javax.swing.JTextField();
        RuleThemeAddAll = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        jLabel1.setText("Вопрос");
        add(jLabel1);
        jLabel1.setBounds(20, 105, 70, 16);

        jLabel2.setText("Предмет");
        add(jLabel2);
        jLabel2.setBounds(20, 25, 70, 16);

        jLabel3.setText("Тема");
        add(jLabel3);
        jLabel3.setBounds(20, 65, 70, 16);
        add(TaskText);
        TaskText.setBounds(20, 150, 420, 220);

        Discipline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DisciplineItemStateChanged(evt);
            }
        });
        add(Discipline);
        Discipline.setBounds(20, 40, 300, 20);

        Theme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ThemeItemStateChanged(evt);
            }
        });
        add(Theme);
        Theme.setBounds(20, 80, 300, 20);

        RefreshDisciplines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshDisciplines.setBorderPainted(false);
        RefreshDisciplines.setContentAreaFilled(false);
        RefreshDisciplines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshDisciplinesActionPerformed(evt);
            }
        });
        add(RefreshDisciplines);
        RefreshDisciplines.setBounds(80, 5, 30, 30);

        DisciplineImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        DisciplineImport.setBorderPainted(false);
        DisciplineImport.setContentAreaFilled(false);
        DisciplineImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineImportActionPerformed(evt);
            }
        });
        add(DisciplineImport);
        DisciplineImport.setBounds(210, 5, 30, 30);

        RemoveTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveTask.setBorderPainted(false);
        RemoveTask.setContentAreaFilled(false);
        RemoveTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveTaskActionPerformed(evt);
            }
        });
        add(RemoveTask);
        RemoveTask.setBounds(370, 115, 30, 30);

        AddTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddTask.setBorderPainted(false);
        AddTask.setContentAreaFilled(false);
        AddTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddTaskActionPerformed(evt);
            }
        });
        add(AddTask);
        AddTask.setBounds(330, 115, 30, 30);

        AddDiscipline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddDiscipline.setBorderPainted(false);
        AddDiscipline.setContentAreaFilled(false);
        AddDiscipline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddDisciplineActionPerformed(evt);
            }
        });
        add(AddDiscipline);
        AddDiscipline.setBounds(330, 40, 30, 30);

        RemoveDiscipline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveDiscipline.setBorderPainted(false);
        RemoveDiscipline.setContentAreaFilled(false);
        RemoveDiscipline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveDisciplineActionPerformed(evt);
            }
        });
        add(RemoveDiscipline);
        RemoveDiscipline.setBounds(370, 40, 30, 30);

        AddTheme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddTheme.setBorderPainted(false);
        AddTheme.setContentAreaFilled(false);
        AddTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddThemeActionPerformed(evt);
            }
        });
        add(AddTheme);
        AddTheme.setBounds(330, 75, 30, 30);

        RemoveTheme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveTheme.setBorderPainted(false);
        RemoveTheme.setContentAreaFilled(false);
        RemoveTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveThemeActionPerformed(evt);
            }
        });
        add(RemoveTheme);
        RemoveTheme.setBounds(370, 75, 30, 30);

        Task.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TaskItemStateChanged(evt);
            }
        });
        add(Task);
        Task.setBounds(20, 120, 300, 20);

        ArtifactView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/camera.png"))); // NOI18N
        ArtifactView.setBorderPainted(false);
        ArtifactView.setContentAreaFilled(false);
        ArtifactView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArtifactViewActionPerformed(evt);
            }
        });
        add(ArtifactView);
        ArtifactView.setBounds(100, 380, 40, 30);

        ArtifactUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        ArtifactUpload.setBorderPainted(false);
        ArtifactUpload.setContentAreaFilled(false);
        ArtifactUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArtifactUploadActionPerformed(evt);
            }
        });
        add(ArtifactUpload);
        ArtifactUpload.setBounds(20, 380, 40, 30);

        ArtifactDownLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        ArtifactDownLoad.setBorderPainted(false);
        ArtifactDownLoad.setContentAreaFilled(false);
        ArtifactDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArtifactDownLoadActionPerformed(evt);
            }
        });
        add(ArtifactDownLoad);
        ArtifactDownLoad.setBounds(60, 380, 40, 30);

        EditTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditTask.setBorderPainted(false);
        EditTask.setContentAreaFilled(false);
        EditTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditTaskActionPerformed(evt);
            }
        });
        add(EditTask);
        EditTask.setBounds(410, 115, 30, 30);

        EditDiscipline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditDiscipline.setBorderPainted(false);
        EditDiscipline.setContentAreaFilled(false);
        EditDiscipline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditDisciplineActionPerformed(evt);
            }
        });
        add(EditDiscipline);
        EditDiscipline.setBounds(410, 40, 30, 30);

        EditTheme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditTheme.setBorderPainted(false);
        EditTheme.setContentAreaFilled(false);
        EditTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditThemeActionPerformed(evt);
            }
        });
        add(EditTheme);
        EditTheme.setBounds(410, 75, 30, 30);

        FullTrace.setText("трассировка импорта");
        add(FullTrace);
        FullTrace.setBounds(240, 10, 160, 20);

        DisciplineSaveImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/archive.png"))); // NOI18N
        DisciplineSaveImport.setBorderPainted(false);
        DisciplineSaveImport.setContentAreaFilled(false);
        DisciplineSaveImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineSaveImportActionPerformed(evt);
            }
        });
        add(DisciplineSaveImport);
        DisciplineSaveImport.setBounds(405, 5, 40, 30);

        jLabel4.setText("Группа");
        add(jLabel4);
        jLabel4.setBounds(490, 20, 70, 16);

        jLabel5.setText("Студент");
        add(jLabel5);
        jLabel5.setBounds(490, 60, 70, 16);

        Group.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GroupItemStateChanged(evt);
            }
        });
        add(Group);
        Group.setBounds(490, 40, 240, 20);

        Student.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StudentItemStateChanged(evt);
            }
        });
        add(Student);
        Student.setBounds(490, 80, 240, 20);

        RefreshGroups.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshGroups.setBorderPainted(false);
        RefreshGroups.setContentAreaFilled(false);
        RefreshGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshGroupsActionPerformed(evt);
            }
        });
        add(RefreshGroups);
        RefreshGroups.setBounds(540, 5, 30, 30);

        AddGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddGroup.setBorderPainted(false);
        AddGroup.setContentAreaFilled(false);
        AddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddGroupActionPerformed(evt);
            }
        });
        add(AddGroup);
        AddGroup.setBounds(740, 40, 30, 30);

        RemoveGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveGroup.setBorderPainted(false);
        RemoveGroup.setContentAreaFilled(false);
        RemoveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveGroupActionPerformed(evt);
            }
        });
        add(RemoveGroup);
        RemoveGroup.setBounds(780, 40, 30, 30);

        AddStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddStudent.setBorderPainted(false);
        AddStudent.setContentAreaFilled(false);
        AddStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddStudentActionPerformed(evt);
            }
        });
        add(AddStudent);
        AddStudent.setBounds(740, 70, 30, 30);

        RemoveStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveStudent.setBorderPainted(false);
        RemoveStudent.setContentAreaFilled(false);
        RemoveStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveStudentActionPerformed(evt);
            }
        });
        add(RemoveStudent);
        RemoveStudent.setBounds(780, 70, 30, 30);

        EditGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditGroup.setBorderPainted(false);
        EditGroup.setContentAreaFilled(false);
        EditGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditGroupActionPerformed(evt);
            }
        });
        add(EditGroup);
        EditGroup.setBounds(820, 40, 30, 30);

        EditStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditStudent.setBorderPainted(false);
        EditStudent.setContentAreaFilled(false);
        EditStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditStudentActionPerformed(evt);
            }
        });
        add(EditStudent);
        EditStudent.setBounds(820, 70, 30, 30);

        GroupsImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/archive.png"))); // NOI18N
        GroupsImport.setBorderPainted(false);
        GroupsImport.setContentAreaFilled(false);
        GroupsImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupsImportActionPerformed(evt);
            }
        });
        add(GroupsImport);
        GroupsImport.setBounds(860, 40, 40, 30);
        add(jSeparator1);
        jSeparator1.setBounds(490, 110, 400, 10);

        jLabel6.setText("Все экзамены");
        add(jLabel6);
        jLabel6.setBounds(730, 590, 120, 20);
        add(ExamGroupsList);
        ExamGroupsList.setBounds(310, 380, 130, 20);
        add(AllExamsList);
        AllExamsList.setBounds(20, 620, 790, 20);

        jLabel7.setText("Кол-во задач");
        add(jLabel7);
        jLabel7.setBounds(20, 530, 100, 20);

        jLabel8.setText("Экзамен для групп");
        add(jLabel8);
        jLabel8.setBounds(180, 380, 120, 20);

        RulesList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RulesListItemStateChanged(evt);
            }
        });
        add(RulesList);
        RulesList.setBounds(20, 440, 230, 20);

        jLabel9.setText("Регламенты");
        add(jLabel9);
        jLabel9.setBounds(20, 410, 80, 20);

        RuleExcerCount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExcerCountKeyPressed(evt);
            }
        });
        add(RuleExcerCount);
        RuleExcerCount.setBounds(110, 530, 40, 25);

        jLabel10.setText("Продолж. (мин)");
        add(jLabel10);
        jLabel10.setBounds(160, 500, 100, 20);

        RuleDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleDurationKeyPressed(evt);
            }
        });
        add(RuleDuration);
        RuleDuration.setBounds(270, 500, 50, 25);

        RuleName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleNameKeyPressed(evt);
            }
        });
        add(RuleName);
        RuleName.setBounds(20, 470, 230, 25);

        RuleThemeRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RuleThemeRemove.setBorderPainted(false);
        RuleThemeRemove.setContentAreaFilled(false);
        RuleThemeRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeRemoveActionPerformed(evt);
            }
        });
        add(RuleThemeRemove);
        RuleThemeRemove.setBounds(400, 550, 30, 30);

        RuleAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        RuleAdd.setBorderPainted(false);
        RuleAdd.setContentAreaFilled(false);
        RuleAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleAddActionPerformed(evt);
            }
        });
        add(RuleAdd);
        RuleAdd.setBounds(270, 440, 30, 30);

        jLabel11.setText("Темы");
        add(jLabel11);
        jLabel11.setBounds(20, 560, 70, 16);
        add(RuleThemesList);
        RuleThemesList.setBounds(110, 560, 280, 20);

        RuleThemeAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RuleThemeAdd.setBorderPainted(false);
        RuleThemeAdd.setContentAreaFilled(false);
        RuleThemeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeAddActionPerformed(evt);
            }
        });
        add(RuleThemeAdd);
        RuleThemeAdd.setBounds(450, 75, 30, 30);

        RuleDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RuleDelete.setBorderPainted(false);
        RuleDelete.setContentAreaFilled(false);
        RuleDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleDeleteActionPerformed(evt);
            }
        });
        add(RuleDelete);
        RuleDelete.setBounds(310, 440, 30, 30);

        jLabel13.setText("Кол-во тестов");
        add(jLabel13);
        jLabel13.setBounds(20, 500, 100, 20);

        RuleQurestionCount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQurestionCountKeyPressed(evt);
            }
        });
        add(RuleQurestionCount);
        RuleQurestionCount.setBounds(110, 500, 40, 25);

        RuleThemeAddAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RuleThemeAddAll.setBorderPainted(false);
        RuleThemeAddAll.setContentAreaFilled(false);
        RuleThemeAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeAddAllActionPerformed(evt);
            }
        });
        add(RuleThemeAddAll);
        RuleThemeAddAll.setBounds(350, 440, 30, 30);

        jLabel12.setText("Все темы");
        add(jLabel12);
        jLabel12.setBounds(350, 420, 80, 16);
    }// </editor-fold>//GEN-END:initComponents

    private void RefreshDisciplinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshDisciplinesActionPerformed
        refreshAll();
    }//GEN-LAST:event_RefreshDisciplinesActionPerformed

    private void DisciplineImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineImportActionPerformed
        try {
            FileNameExt fname = main.getInputFileName("Импорт тестов", "txt", null);
            if (fname == null)
                return;
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fname.fullName()), "Windows-1251"));
            owtImportData = new OWTDiscipline();
            owtImportData.clear();
            OWTReader reader = new OWTReader(in);
            owtImportData.read(reader);
            StringBuffer sb = new StringBuffer();
            owtImportData.toLog(sb,FullTrace.isSelected());
            System.out.println(sb.toString());
            DisciplineSaveImport.setEnabled(true);
            } catch (Exception ee){
                popup("Ошибка импорта тестов:\n"+ee.toString());
                Discipline.setEnabled(false);
                }


    }//GEN-LAST:event_DisciplineImportActionPerformed

    private void RemoveTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveTaskActionPerformed
        if (cTask==null)
            return;
        new OK(200, 200, "Удалить задание номер " + (cTaskNum+1), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getTaskApi().deleteTask(cTask.getTask().getId());
                        }
                    @Override
                    public void onSucess(Void oo) {
                        refreshThemeFull();
                    }
                };
            }
        });
    }//GEN-LAST:event_RemoveTaskActionPerformed

    private void AddTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddTaskActionPerformed

    private void AddDisciplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddDisciplineActionPerformed
        new OKName(200,200,"Добавить дисциплину", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                DisciplineBean bean = new DisciplineBean();
                bean.setName(value);
                new APICall<DisciplineBean>(main) {
                    @Override
                    public Call<DisciplineBean> apiFun() {
                        return main.client.getDisciplineApi().create4(bean);
                        }
                    @Override
                    public void onSucess(DisciplineBean oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_AddDisciplineActionPerformed

    private void RemoveDisciplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveDisciplineActionPerformed
        if (cDiscipline==null)
            return;
        new OK(200, 200, "Удалить дисциплину: " + cDiscipline.getDiscipline().getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getDisciplineApi().delete2(cDiscipline.getDiscipline().getId());
                        }
                    @Override
                    public void onSucess(Void oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_RemoveDisciplineActionPerformed

    private void AddThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddThemeActionPerformed
        if (cDiscipline==null)
            return;
        new OKName(200,200,"Добавить тему в "+cDiscipline.getDiscipline().getName(), new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                ThemeBean bean = new ThemeBean();
                bean.setName(value);
                bean.setDisciplineId(cDiscipline.getDiscipline().getId());
                new APICall<ThemeBean>(main) {
                    @Override
                    public Call<ThemeBean> apiFun() {
                        return main.client.getThemeApi().create(bean);
                    }
                    @Override
                    public void onSucess(ThemeBean oo) {
                        refreshDisciplineFull();
                    }
                };
            }
        });
    }//GEN-LAST:event_AddThemeActionPerformed

    private void RemoveThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveThemeActionPerformed
        if (cTheme==null)
            return;
        new OK(200, 200, "Удалить тему: " + cTheme.getTheme().getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {        // TODO - это удаление
                        return main.client.getThemeApi().delete(cTheme.getTheme().getId());
                        }
                    @Override
                    public void onSucess(Void oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_RemoveThemeActionPerformed

    private void DisciplineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DisciplineItemStateChanged
        refreshDisciplineFull();
    }//GEN-LAST:event_DisciplineItemStateChanged

    private void ThemeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ThemeItemStateChanged
        refreshThemeFull();
    }//GEN-LAST:event_ThemeItemStateChanged

    private void TaskItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TaskItemStateChanged
        refreshTaskFull();
    }//GEN-LAST:event_TaskItemStateChanged

    private void ArtifactViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArtifactViewActionPerformed

    }//GEN-LAST:event_ArtifactViewActionPerformed

    private void ArtifactUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArtifactUploadActionPerformed
        main.uploadFileAsync(new I_Value<ArtefactBean>() {
            @Override
            public void onEnter(ArtefactBean value) {
                cTask.getTask().setArtefactId(value.getId());
                new APICall<TaskBean>(main) {
                    @Override
                    public Call<TaskBean> apiFun() {
                        return main.client.getTaskApi().updateTask(cTask.getTask(),cTask.getTask().getId());
                        }
                    @Override
                    public void onSucess(TaskBean oo) {
                        refreshTaskFull();
                        }
                    };
                }
            });
        }//GEN-LAST:event_ArtifactUploadActionPerformed
    private void ArtifactDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArtifactDownLoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ArtifactDownLoadActionPerformed

    private void EditTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditTaskActionPerformed

    private void EditDisciplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditDisciplineActionPerformed
        new OKName(200,200,"Изменить название дисциплины", cDiscipline.getDiscipline().getName(),new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                cDiscipline.getDiscipline().setName(value);
                new APICall<DisciplineBean>(main) {
                    @Override
                    public Call<DisciplineBean> apiFun() {
                        return main.client.getDisciplineApi().update2(cDiscipline.getDiscipline(),cDiscipline.getDiscipline().getId());
                        //return main.client.getDisciplineApi().update1(cDiscipline,cDiscipline.getId());
                        }
                    @Override
                    public void onSucess(DisciplineBean oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_EditDisciplineActionPerformed

    private void EditThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditThemeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditThemeActionPerformed
    //-------------------------------------------------------------------------------------------------------------------
    private void DisciplineSaveImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineSaveImportActionPerformed
        if (owtImportData==null)
            return;
        new OKName(200, 200, "Дисциплина с тестами: " + owtImportData.getHead(), owtImportData.getHead(),new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                DisciplineBean discipline = new DisciplineBean();
                discipline.setName(value);
                new APICall<DisciplineBean>(main) {
                    @Override
                    public Call<DisciplineBean> apiFun() {
                        return main.client.getDisciplineApi().create4(discipline);
                        }
                    @Override
                    public void onSucess(final DisciplineBean oo) {
                        int idx1=0;
                        int idx2=0;
                        OWTTheme owtTheme=null;
                        try {
                            final ThemeBean theme = new ThemeBean();
                            final TaskBean task = new TaskBean();
                            for (idx1 = 0; idx1 < owtImportData.size(); idx1++) {
                                idx2=-1;
                                owtTheme = owtImportData.get(idx1);
                                theme.disciplineId(oo.getId());
                                theme.setName(owtTheme.getQuestion());
                                final ThemeBean theme2 = new APICallSync<ThemeBean>() {
                                    @Override
                                    public Call<ThemeBean> apiFun() {
                                        return main.client.getThemeApi().create(theme);
                                        }
                                    }.call();
                                for(idx2=0;idx2 < owtTheme.size();idx2++){
                                    task.setThemeId(theme2.getId());
                                    task.setTaskType(TaskBean.TaskTypeEnum.QUESTION);
                                    task.setText(owtTheme.get(idx2).toString());
                                    new APICallSync<TaskBean>() {
                                        @Override
                                        public Call<TaskBean> apiFun() {
                                            return main.client.getTaskApi().createTask(task);
                                            }
                                        }.call();
                                    }
                                }
                            }catch (IOException ee){
                                System.out.println("Ошибка добавления темы "+(idx1+1)+ (idx2==-1 ? "" : " теста "+(idx2+1))+"\n"+owtTheme.getQuestion()+"\n"+ee.toString());
                                }
                        DisciplineSaveImport.setEnabled(false);
                        owtImportData = null;
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_DisciplineSaveImportActionPerformed

    private void GroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GroupItemStateChanged
        refreshGroupFull();
    }//GEN-LAST:event_GroupItemStateChanged

    private void StudentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StudentItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentItemStateChanged

    private void RefreshGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshGroupsActionPerformed
        refreshGroupsList();
    }//GEN-LAST:event_RefreshGroupsActionPerformed

    private void AddGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddGroupActionPerformed
        new OKName(200,200,"Добавить группу", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                GroupBean bean = new GroupBean();
                bean.setName(value);
                new APICall<GroupBean>(main) {
                    @Override
                    public Call<GroupBean> apiFun() {
                        return main.client.getGroupApi().create2(bean);
                    }
                    @Override
                    public void onSucess(GroupBean oo) {
                        refreshGroupsList();
                    }
                };
            }
        });
    }//GEN-LAST:event_AddGroupActionPerformed

    private void RemoveGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveGroupActionPerformed
        if (cGroup==null)
            return;
        new OK(200, 200, "Удалить группу: " + cGroup.getGroup().getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getGroupApi().delete1(cGroup.getGroup().getId());
                        }
                    @Override
                    public void onSucess(Void oo) {
                        refreshGroupsList();
                        }
                };
            }
        });

    }//GEN-LAST:event_RemoveGroupActionPerformed

    private void AddStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddStudentActionPerformed

    private void RemoveStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RemoveStudentActionPerformed

    private void EditGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditGroupActionPerformed

    private void EditStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditStudentActionPerformed


    private ExcelX2 excel;

    private void procNextSheet(final String sheets[], final int idx){
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
                final  GroupBean group = new GroupBean();
                group.setName(sheets[idx]);
                try {
                    final GroupBean group2 = new APICallSync<GroupBean>() {
                        @Override
                        public Call<GroupBean> apiFun() {
                            return main.client.getGroupApi().create2(group);
                            }
                        }.call();
                    excel.procSheet(sheets[idx], new I_ExcelBack() {
                        @Override
                        public void onRow(String[] values) {
                            System.out.println(values[0] + " " + values[1]);
                            AccountBean account = new AccountBean();
                            account.setUsername(values[1]);
                            String ss[] = UtilsEM.parseFIO(values[0]);
                            account.setName(ss[0]);
                            account.setSurname(ss[1]);
                            account.setPassword("");
                            ArrayList roles = new ArrayList<UserRole>();
                            roles.add(UserRole.ROLE_STUDENT);
                            account.setRoles(roles);
                            StudentBean student = new StudentBean();
                            student.setGroupId(group2.getId());
                            student.setAccount(account);
                            try {
                                new APICallSync<StudentBean>() {
                                    @Override
                                    public Call<StudentBean> apiFun() {
                                        return main.client.getOnlyStudentApi().createStudent(student);
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

    private void ruleUpdate(KeyEvent evt){
        if (cRule==null)
            return;
        cRule.setRatingSystemId(ratings.get(0).getId());
        try {
            new APICall2<ExamRuleBean>() {
                @Override
                public Call<ExamRuleBean> apiFun() {
                    return main.client.getExamRuleApi().update1(cRule,cRule.getId());
                    }
                }.call(main);
            refreshSelectedRule();
            if (evt!=null)
                main.viewUpdate(evt,true);
            popup("Регламент обновлен");
            } catch (UniException ee){
                System.out.println(ee.toString());
                if (evt!=null)
                    main.viewUpdate(evt,false);
                }
        }

    private static String shortString(String ss, int size){
        return ss.length()<=size ? ss : ss.substring(0,size)+"...";
        }

    private void RuleThemeRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleThemeRemoveActionPerformed
        if (cRule==null)
            return;
        if (cRule.getThemeIds().size()==0)
            return;
        new OK(200, 200, "Удалить тему: " + shortString(cTheme.getTheme().getName(), 20), new I_Button() {
            @Override
            public void onPush() {
                int idx = RuleThemesList.getSelectedIndex();
                cRule.getThemeIds().remove(idx);
                ruleUpdate(null);
                }
            });
    }//GEN-LAST:event_RuleThemeRemoveActionPerformed

    private void RuleAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleAddActionPerformed
        if (cDiscipline==null)
            return;
        final ExamRuleBean ruleBean = new ExamRuleBean();
        new OKName(200, 200, "Добавить регламент", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                ExamRuleBean ruleBean = new ExamRuleBean();
                ruleBean.setName(value);
                ruleBean.setQuestionCount(10);
                ruleBean.setExerciseCount(1);
                ruleBean.setDuration(180);
                ruleBean.setDisciplineId(cDiscipline.getDiscipline().getId());
                ruleBean.setMinimalRating(0);
                ruleBean.setRatingSystemId(0L);
                ruleBean.setRatingSystemId(ratings.get(0).getId());
                ArrayList<Long> xx = new ArrayList<>();
                xx.add(cTheme.getTheme().getId());
                ruleBean.setThemeIds(xx);
                new APICall<ExamRuleBean>(main) {
                    @Override
                    public Call<ExamRuleBean> apiFun() {
                        return main.client.getExamRuleApi().create3(ruleBean);
                        }
                    @Override
                    public void onSucess(ExamRuleBean oo) {
                        refreshAllRules();
                        refreshRules();
                        }
                    };
            }
        });
    }//GEN-LAST:event_RuleAddActionPerformed

    private void RuleNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        cRule.setName(RuleName.getText());
        ruleUpdate(evt);
    }//GEN-LAST:event_RuleNameKeyPressed

    private void RuleDurationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleDurationKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setDuration(Integer.parseInt(RuleDuration.getText()));
            ruleUpdate(evt);
            } catch (Exception ee){
                popup("Ошибка формата целого");
                main.viewUpdate(evt,false);
                }
    }//GEN-LAST:event_RuleDurationKeyPressed

    private void RuleExcerCountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleExcerCountKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setExerciseCount(Integer.parseInt(RuleExcerCount.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_RuleExcerCountKeyPressed

    private void RuleThemeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleThemeAddActionPerformed
        if (cRule==null)
            return;
        if (cTheme==null)
            return;
        if (ruleThemesMap.get(cTheme.getTheme().getId())!=null){
            popup("Тема уже есть в регламенте");
            return;
            }
        new OK(200, 200, "Добавить тему: " + shortString(cTheme.getTheme().getName(), 30), new I_Button() {
            @Override
            public void onPush() {
                cRule.getThemeIds().add(cTheme.getTheme().getId());
                ruleUpdate(null);
                }
            });
    }//GEN-LAST:event_RuleThemeAddActionPerformed

    private void RuleDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleDeleteActionPerformed
        if (cRule==null)
            return;
        new OK(200, 200, "Удалить регламент: " + cRule.getName(), new I_Button() {
            @Override
            public void onPush() {
                popup("Удалить? Хрен Вам...");
                /*
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getExamRuleApi().;
                        }
                    @Override
                    public void onSucess(Void oo) {

                    }
                };
                 */
                }
            });

    }//GEN-LAST:event_RuleDeleteActionPerformed

    private void RuleQurestionCountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleQurestionCountKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setQuestionCount(Integer.parseInt(RuleQurestionCount.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_RuleQurestionCountKeyPressed

    private void RulesListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RulesListItemStateChanged
        refreshSelectedRule();
    }//GEN-LAST:event_RulesListItemStateChanged

    private void RuleThemeAddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleThemeAddAllActionPerformed
        if (cRule==null)
            return;
        new OK(200, 200, "Копировать все темы", new I_Button() {
            @Override
            public void onPush() {
                List<Long> idsList = cRule.getThemeIds();
                idsList.clear();
                for(FullThemeBean theme : cDiscipline.getThemes())
                    idsList.add(theme.getTheme().getId());
                ruleUpdate(null);
            }
        });

    }//GEN-LAST:event_RuleThemeAddAllActionPerformed

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
    private javax.swing.JButton AddDiscipline;
    private javax.swing.JButton AddGroup;
    private javax.swing.JButton AddStudent;
    private javax.swing.JButton AddTask;
    private javax.swing.JButton AddTheme;
    private java.awt.Choice AllExamsList;
    private javax.swing.JButton ArtifactDownLoad;
    private javax.swing.JButton ArtifactUpload;
    private javax.swing.JButton ArtifactView;
    private java.awt.Choice Discipline;
    private javax.swing.JButton DisciplineImport;
    private javax.swing.JButton DisciplineSaveImport;
    private javax.swing.JButton EditDiscipline;
    private javax.swing.JButton EditGroup;
    private javax.swing.JButton EditStudent;
    private javax.swing.JButton EditTask;
    private javax.swing.JButton EditTheme;
    private java.awt.Choice ExamGroupsList;
    private javax.swing.JCheckBox FullTrace;
    private java.awt.Choice Group;
    private javax.swing.JButton GroupsImport;
    private javax.swing.JButton RefreshDisciplines;
    private javax.swing.JButton RefreshGroups;
    private javax.swing.JButton RemoveDiscipline;
    private javax.swing.JButton RemoveGroup;
    private javax.swing.JButton RemoveStudent;
    private javax.swing.JButton RemoveTask;
    private javax.swing.JButton RemoveTheme;
    private javax.swing.JButton RuleAdd;
    private javax.swing.JButton RuleDelete;
    private javax.swing.JTextField RuleDuration;
    private javax.swing.JTextField RuleExcerCount;
    private javax.swing.JTextField RuleName;
    private javax.swing.JTextField RuleQurestionCount;
    private javax.swing.JButton RuleThemeAdd;
    private javax.swing.JButton RuleThemeAddAll;
    private javax.swing.JButton RuleThemeRemove;
    private java.awt.Choice RuleThemesList;
    private java.awt.Choice RulesList;
    private java.awt.Choice Student;
    private java.awt.Choice Task;
    private java.awt.TextArea TaskText;
    private java.awt.Choice Theme;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
