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
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.desktop.exam.ExamPeriodStateFactory;
import romanow.abc.exam.model.*;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
 *
 * @author romanow
 */
public class EMVKRExamAdminPanel extends BasePanel{
    private List<DisciplineBean> disciplines = new ArrayList<>();       // Список дисциплин
    private FullDisciplineBean cDiscipline = null;                      // Текущая дисциплина
    private FullThemeBean cTheme = null;                                // Текущая тема
    private FullTaskBean cTask=null;                                    // Текущая задача/тест
    private HashMap<Long,FullThemeBean> disciplineThemesMap = new HashMap<>();
    private HashMap<Long,FullThemeBean> ruleThemesMap = new HashMap<>();
    private ArrayList<FullThemeBean> ruleThemes = new ArrayList<>();
    private ExamRuleBean cRule=null;
    private ExamBean cExam=null;
    private int cTaskNum=0;
    private OWTDiscipline owtImportData = null;
    private List<GroupBean> groups = new ArrayList<>();                 // Список групп
    private HashMap<Long,GroupBean> groupsMap = new HashMap<>();        // Мар всех групп
    private HashMap<Long,Long> examGroupsMap = new HashMap<>();         // Мар групп для экзамена по дисциплине
    private HashMap<Long,ExamRuleBean> examRulesMap = new HashMap<>();  // Мар имеющихся регламентов в экзаменах по дисциплине
    private FullGroupBean cGroup = null;                                // Текущая группа
    private List<GroupBean> examGroupsList = new ArrayList<>();         // Экзамен для текущей дисциплины
    private List<ExamBean> allExams = new ArrayList<>();                // Полный список экзаменов
    private List<ExamBean> disciplineExams=new ArrayList<>();           // Экзамены для дисциплины
    private List<ExamRuleBean> allExamRules = new ArrayList<>();        // Полный список регламентов
    private List<ExamRuleBean> cExamRules = new ArrayList<>();          // Список регламентов для дисциплины
    private HashMap<Long,ExamRuleBean> cExamRulesMap = new HashMap<>(); // Мар регламентов для дисциплины
    private boolean refresh=false;                                      // Признак обновления для событий  CheckBox
    private List<ExamBean> periods=new ArrayList<>();             // Список СДАЧ для экзамена
    private ExamBean cPeriod=null;                                // Текущая сдача
    private boolean taskTextChanged=false;
    private EMVKRMainBaseFrame main;
    private ExamPeriodStateFactory stateFactory = new ExamPeriodStateFactory();
    public EMVKRExamAdminPanel() {
        initComponents();
        }
    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        main = (EMVKRMainBaseFrame) main0;
        ArtifactView.setEnabled(false);
        ArtifactDownLoad.setEnabled(false);
        DisciplineSaveImport.setEnabled(false);
        ArtifactDownLoad.setEnabled(false);
        ArtifactView.setEnabled(false);
        PeriodState.removeAll();
        PeriodState.add("...");
        for(ExamPeriodStateFactory.EnumPeriodStatePair pair : stateFactory.getList())
            PeriodState.add(pair.name);
        PeriodState.select(0);
        refreshAll();
        }

    private void refreshAll(){
        refreshDisciplineList();
        refreshGroupsList();
        refreshRatingsList();
        }

    private void refreshRatingsList(){
        //new APICall<List<RatingSystemBean>>(main) {
        //    @Override
        //    public Call<List<RatingSystemBean>> apiFun() {
        //        return main.client.getRatingSystemApi().getAll1();
        //        }
        //    @Override
        //    public void onSucess(List<RatingSystemBean> oo) {
        //        ratings = oo;
        //        }
        //    };
        }

    private void refreshAllRules(){
        new APICall<List<ExamRuleBean>>(main) {
            @Override
            public Call<List<ExamRuleBean>> apiFun() {
                return main.client.getExamRuleApi().getAll3();
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
                return main.client.getDisciplineApi().getAll4();
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
        cExamRulesMap.clear();
        RulesList.removeAll();
        new APICall<List<ExamRuleBean>>(main) {
            @Override
            public Call<List<ExamRuleBean>> apiFun() {
                return main.client.getDisciplineApi().findExamRules(cDiscipline.getDiscipline().getId());
                }
            @Override
            public void onSucess(List<ExamRuleBean> oo) {
                for(ExamRuleBean examRule : oo){
                    RulesList.add(examRule.getName());
                    cExamRules.add(examRule);
                    cExamRulesMap.put(examRule.getId(),examRule);
                    }
                refreshSelectedRule();
                }
            };
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
        //RuleExcerCount.setText(""+cRule.getExerciseCount());
        //RuleQurestionCount.setText(""+cRule.getQuestionCount());
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
                return main.client.getGroupApi().getAll1();
            }
            @Override
            public void onSucess(List<GroupBean> oo) {
                groups = oo;
                Group.removeAll();
                groupsMap.clear();
                for(GroupBean dd : groups){
                    Group.add(dd.getName());
                    groupsMap.put(dd.getId(),dd);
                    }
                refreshGroupFull();
                }
            };
        new APICall<List<ExamBean>>(main){
            @Override
            public Call<List<ExamBean>> apiFun() {
                return main.client.getExamApi().getAll2();
                }
            @Override
            public void onSucess(List<ExamBean> oo) {
                allExams = oo;
                ExamsTotalList.removeAll();
                for(ExamBean exam : allExams){
                    ExamsTotalList.add(exam.toString());
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
                return main.client.getGroupApi().getFull2(oid,1);
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
                return main.client.getDisciplineApi().getFull5(oid,2);
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
                new APICall<List<GroupBean>>(main){
                    @Override
                    public Call<List<GroupBean>> apiFun() {
                        return  null; //main.client.getDisciplineApi().findGroups(oid);
                    }
                    @Override
                    public void onSucess(List<GroupBean> oo) {
                        examGroupsList = oo;
                        ExamsForGroupList.removeAll();
                        for(GroupBean group : oo){
                            ExamsForGroupList.add(group.getName());
                            }
                        refreshDisciplineExams();
                        }
                    };
                }
            };
        }


    private void refreshDisciplineExams(){
        ExamList.removeAll();
        disciplineExams.clear();
        examGroupsMap.clear();
        examRulesMap.clear();
        if (cDiscipline==null)
            return;
        /*
        for(ExamBean exam : allExams){
            if(cDiscipline.getDiscipline().getId().longValue()==exam.getDisciplineId().longValue()){
                disciplineExams.add(exam);
                ExamRuleBean examRule = cExamRulesMap.get(exam.getExamRuleId());
                if (examRule==null){
                    System.out.println("Не найден регламент для экзамена id="+exam.getExamRuleId());
                    continue;
                    }
                for(Long groupId : exam.getGroupIds())
                    examGroupsMap.put(groupId,0L);
                ExamList.add(examRule.getName());
                examRulesMap.put(exam.getExamRuleId(),examRule);
                }
            }
         */
        refreshSelectedExam();
        }

    private void refreshSelectedExam(){
        cExam=null;
        ExamGroupsList.removeAll();
        if (disciplineExams.size()==0)
            return;
        cExam = disciplineExams.get(ExamList.getSelectedIndex());
        ExamGroupsList.removeAll();
        /*
        for(Long id : cExam.getGroupIds()){
            GroupBean group = groupsMap.get(id);
            if (group==null){
                System.out.println("Не найдена группа id="+id);
                continue;
                }
            ExamGroupsList.add(group.getName());
            }
         */
        refreshExamPeriods();
        }

    private void  refreshExamPeriods(){
        new APICall<List<ExamBean>>(main) {
            @Override
            public Call<List<ExamBean>> apiFun() {
                return null; //main.client.getExamApi().getPeriods(cExam.getId());
                }
            @Override
            public void onSucess(List<ExamBean> oo) {
                periods = oo;
                PeriodList.removeAll();
                for(ExamBean examPeriod : periods){
                    PeriodList.add(new OwnDateTime(examPeriod.getStart()).dateTimeToString());
                    }
                refreshSelectedExamPeriod();
                }
            };
        }

    private void  refreshSelectedExamPeriod(){
        PeriodState.select(0);
        cPeriod=null;
        if (periods.size()==0)
            return;
        cPeriod = periods.get(PeriodList.getSelectedIndex());
        new APICall<ExamBean>(main) {
            @Override
            public Call<ExamBean> apiFun() {
                return main.client.getExamApi().getOne2(cPeriod.getId());
                }
            @Override
            public void onSucess(ExamBean oo) {
                cPeriod = oo;
                periods.set(PeriodList.getSelectedIndex(),cPeriod);
                if (cPeriod.getStart().longValue()==0){
                    PeriodData.setText("---");
                    PeriodStartTime.setText("---");
                    PeriodEndTime.setText("---");
                }
                else{
                    OwnDateTime date1 = new OwnDateTime(cPeriod.getStart());
                    OwnDateTime date2 = new OwnDateTime(cPeriod.getEnd());
                    PeriodData.setText(date1.dateToString());
                    PeriodStartTime.setText(date1.timeToString());
                    PeriodEndTime.setText(cPeriod.getEnd().longValue()==0 ? "---" : date2.timeToString());
                }
                int idx = stateFactory.getValueIdx(cPeriod.getState());
                PeriodState.select(idx+1);
                }
            };
        }


    private void refreshThemeFull(){
        Task.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Theme.getSelectedIndex());
        cTheme.getTasks().sort(new Comparator<FullTaskBean>() {
            @Override
            public int compare(FullTaskBean o1, FullTaskBean o2) {              // Сортировать по id (в порядке поступления)
                return o1.getTask().getId()-o2.getTask().getId() >0 ? 1 : -1;
                }
            });
        int iq=1,it=1;
        for(FullTaskBean task : cTheme.getTasks())
            if (task.getTask().getTaskType()== TaskBean.TaskTypeEnum.QUESTION)
                Task.add("Вопрос "+iq++);
        for(FullTaskBean task : cTheme.getTasks())
            if (task.getTask().getTaskType()== TaskBean.TaskTypeEnum.EXERCISE)
                Task.add("Задача "+it++);
        refreshTaskFull();
        }

    private void refreshTaskFullForce(){
        TaskText.setText("");
        if (cTheme.getTasks().size()==0)
            return;
        refresh=true;
        TaskSaveText.setEnabled(false);
        cTaskNum = Task.getSelectedIndex();
        cTask = cTheme.getTasks().get(cTaskNum);
        boolean isTask = cTask.getTask().getTaskType()== TaskBean.TaskTypeEnum.EXERCISE;
        TaskType.setSelected(isTask);
        TaskTypeLabel.setText(isTask ? "Задача" : "Вопрос (тест)");
        TaskText.setText(UtilsEM.formatSize(cTask.getTask().getText(),60));
        boolean bb = cTask.getArtefact()!=null;
        ArtifactView.setEnabled(bb);
        ArtifactDownLoad.setEnabled(bb);
        if (bb){
            ArtefactBean artefact = cTask.getArtefact();
            TaskText.append("\n"+artefact.getFileName());
            }
        refresh=false;
        }

    private void refreshTaskFull(){
        if (!taskTextChanged){
            refreshTaskFullForce();
            return;
            }
        taskTextChanged=false;
        TaskSaveText.setEnabled(false);
        new OKFull(200, 200, "Сохранить изменения текста", new I_ButtonFull() {
            @Override
            public void onPush(boolean yes) {
                if (yes)
                    taskUpdate();
                else
                    refreshTaskFullForce();
                }
            });
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TaskTypeLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TaskText = new java.awt.TextArea();
        Discipline = new java.awt.Choice();
        Theme = new java.awt.Choice();
        RefreshDisciplines = new javax.swing.JButton();
        DisciplineImport = new javax.swing.JButton();
        TaskRemove = new javax.swing.JButton();
        TaskAdd = new javax.swing.JButton();
        DisciplineAdd = new javax.swing.JButton();
        DisciplineRemove = new javax.swing.JButton();
        ThemeAdd = new javax.swing.JButton();
        ThemeRemove = new javax.swing.JButton();
        Task = new java.awt.Choice();
        ArtifactView = new javax.swing.JButton();
        ArtifactUpload = new javax.swing.JButton();
        ArtifactDownLoad = new javax.swing.JButton();
        TaskSaveText = new javax.swing.JButton();
        DisciplineEdit = new javax.swing.JButton();
        ThemeEdit = new javax.swing.JButton();
        FullTrace = new javax.swing.JCheckBox();
        DisciplineSaveImport = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Group = new java.awt.Choice();
        Student = new java.awt.Choice();
        RefreshGroups = new javax.swing.JButton();
        GroupAdd = new javax.swing.JButton();
        GroupRemove = new javax.swing.JButton();
        StudentAdd = new javax.swing.JButton();
        StudentRemove = new javax.swing.JButton();
        GroupEdit = new javax.swing.JButton();
        StudentEdit = new javax.swing.JButton();
        GroupsImport = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        ExamsForGroupList = new java.awt.Choice();
        ExamsTotalList = new java.awt.Choice();
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
        jLabel14 = new javax.swing.JLabel();
        ExamList = new java.awt.Choice();
        ExamGroupsList = new java.awt.Choice();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        PeriodForAll = new javax.swing.JCheckBox();
        PeriodList = new java.awt.Choice();
        PeriodData = new javax.swing.JTextField();
        PeriodStartTime = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        PeriodOneGroup = new java.awt.Choice();
        ExamGroupAdd = new javax.swing.JButton();
        ExamGroupRemove = new javax.swing.JButton();
        ExamAdd = new javax.swing.JButton();
        ExamRemove = new javax.swing.JButton();
        PeriodAdd = new javax.swing.JButton();
        PeriodRemove = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        PeriodEndTime = new javax.swing.JTextField();
        TaskType = new javax.swing.JCheckBox();
        PeriodState = new java.awt.Choice();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        TaskTypeLabel.setText("Вопрос");
        add(TaskTypeLabel);
        TaskTypeLabel.setBounds(20, 105, 100, 16);

        jLabel2.setText("Предмет");
        add(jLabel2);
        jLabel2.setBounds(20, 25, 70, 16);

        jLabel3.setText("Тема");
        add(jLabel3);
        jLabel3.setBounds(20, 65, 70, 16);

        TaskText.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                TaskTextInputMethodTextChanged(evt);
            }
        });
        TaskText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TaskTextKeyPressed(evt);
            }
        });
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

        TaskRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TaskRemove.setBorderPainted(false);
        TaskRemove.setContentAreaFilled(false);
        TaskRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskRemoveActionPerformed(evt);
            }
        });
        add(TaskRemove);
        TaskRemove.setBounds(370, 115, 30, 30);

        TaskAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        TaskAdd.setBorderPainted(false);
        TaskAdd.setContentAreaFilled(false);
        TaskAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskAddActionPerformed(evt);
            }
        });
        add(TaskAdd);
        TaskAdd.setBounds(330, 115, 30, 30);

        DisciplineAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        DisciplineAdd.setBorderPainted(false);
        DisciplineAdd.setContentAreaFilled(false);
        DisciplineAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineAddActionPerformed(evt);
            }
        });
        add(DisciplineAdd);
        DisciplineAdd.setBounds(330, 40, 30, 30);

        DisciplineRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        DisciplineRemove.setBorderPainted(false);
        DisciplineRemove.setContentAreaFilled(false);
        DisciplineRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineRemoveActionPerformed(evt);
            }
        });
        add(DisciplineRemove);
        DisciplineRemove.setBounds(370, 40, 30, 30);

        ThemeAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        ThemeAdd.setBorderPainted(false);
        ThemeAdd.setContentAreaFilled(false);
        ThemeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeAddActionPerformed(evt);
            }
        });
        add(ThemeAdd);
        ThemeAdd.setBounds(330, 75, 30, 30);

        ThemeRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ThemeRemove.setBorderPainted(false);
        ThemeRemove.setContentAreaFilled(false);
        ThemeRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeRemoveActionPerformed(evt);
            }
        });
        add(ThemeRemove);
        ThemeRemove.setBounds(370, 75, 30, 30);

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

        TaskSaveText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/save.png"))); // NOI18N
        TaskSaveText.setBorderPainted(false);
        TaskSaveText.setContentAreaFilled(false);
        TaskSaveText.setEnabled(false);
        TaskSaveText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskSaveTextActionPerformed(evt);
            }
        });
        add(TaskSaveText);
        TaskSaveText.setBounds(410, 115, 30, 30);

        DisciplineEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        DisciplineEdit.setBorderPainted(false);
        DisciplineEdit.setContentAreaFilled(false);
        DisciplineEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineEditActionPerformed(evt);
            }
        });
        add(DisciplineEdit);
        DisciplineEdit.setBounds(410, 40, 30, 30);

        ThemeEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        ThemeEdit.setBorderPainted(false);
        ThemeEdit.setContentAreaFilled(false);
        ThemeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeEditActionPerformed(evt);
            }
        });
        add(ThemeEdit);
        ThemeEdit.setBounds(410, 75, 30, 30);

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

        GroupAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        GroupAdd.setBorderPainted(false);
        GroupAdd.setContentAreaFilled(false);
        GroupAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupAddActionPerformed(evt);
            }
        });
        add(GroupAdd);
        GroupAdd.setBounds(740, 40, 30, 30);

        GroupRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        GroupRemove.setBorderPainted(false);
        GroupRemove.setContentAreaFilled(false);
        GroupRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupRemoveActionPerformed(evt);
            }
        });
        add(GroupRemove);
        GroupRemove.setBounds(780, 40, 30, 30);

        StudentAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        StudentAdd.setBorderPainted(false);
        StudentAdd.setContentAreaFilled(false);
        StudentAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentAddActionPerformed(evt);
            }
        });
        add(StudentAdd);
        StudentAdd.setBounds(740, 70, 30, 30);

        StudentRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        StudentRemove.setBorderPainted(false);
        StudentRemove.setContentAreaFilled(false);
        StudentRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentRemoveActionPerformed(evt);
            }
        });
        add(StudentRemove);
        StudentRemove.setBounds(780, 70, 30, 30);

        GroupEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        GroupEdit.setBorderPainted(false);
        GroupEdit.setContentAreaFilled(false);
        GroupEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupEditActionPerformed(evt);
            }
        });
        add(GroupEdit);
        GroupEdit.setBounds(820, 40, 30, 30);

        StudentEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        StudentEdit.setBorderPainted(false);
        StudentEdit.setContentAreaFilled(false);
        StudentEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentEditActionPerformed(evt);
            }
        });
        add(StudentEdit);
        StudentEdit.setBounds(820, 70, 30, 30);

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
        jSeparator1.setBounds(460, 120, 480, 10);

        jLabel6.setText("Все экзамены");
        add(jLabel6);
        jLabel6.setBounds(730, 590, 120, 20);
        add(ExamsForGroupList);
        ExamsForGroupList.setBounds(310, 380, 130, 20);
        add(ExamsTotalList);
        ExamsTotalList.setBounds(20, 620, 790, 20);

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

        jLabel14.setText("Экзамены (по регламентам)");
        add(jLabel14);
        jLabel14.setBounds(460, 130, 250, 16);

        ExamList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExamListItemStateChanged(evt);
            }
        });
        add(ExamList);
        ExamList.setBounds(460, 150, 190, 20);
        add(ExamGroupsList);
        ExamGroupsList.setBounds(740, 150, 100, 20);

        jLabel15.setText("Окончание");
        add(jLabel15);
        jLabel15.setBounds(650, 225, 80, 16);

        jLabel16.setText("Группы");
        add(jLabel16);
        jLabel16.setBounds(740, 130, 42, 16);

        PeriodForAll.setText("Группа/ведомость");
        add(PeriodForAll);
        PeriodForAll.setBounds(460, 300, 130, 20);
        add(PeriodList);
        PeriodList.setBounds(460, 200, 240, 20);

        PeriodData.setEnabled(false);
        PeriodData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodDataMouseClicked(evt);
            }
        });
        add(PeriodData);
        PeriodData.setBounds(460, 240, 110, 25);

        PeriodStartTime.setEnabled(false);
        PeriodStartTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodStartTimeMouseClicked(evt);
            }
        });
        add(PeriodStartTime);
        PeriodStartTime.setBounds(590, 240, 50, 25);

        jLabel18.setText("Сдача экзамена");
        add(jLabel18);
        jLabel18.setBounds(460, 180, 140, 16);

        jLabel19.setText("Статус");
        add(jLabel19);
        jLabel19.setBounds(460, 275, 60, 16);

        jLabel20.setText("Дата");
        add(jLabel20);
        jLabel20.setBounds(460, 225, 70, 16);

        PeriodOneGroup.setEnabled(false);
        add(PeriodOneGroup);
        PeriodOneGroup.setBounds(600, 300, 100, 20);

        ExamGroupAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        ExamGroupAdd.setBorderPainted(false);
        ExamGroupAdd.setContentAreaFilled(false);
        ExamGroupAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExamGroupAddActionPerformed(evt);
            }
        });
        add(ExamGroupAdd);
        ExamGroupAdd.setBounds(850, 140, 30, 30);

        ExamGroupRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ExamGroupRemove.setBorderPainted(false);
        ExamGroupRemove.setContentAreaFilled(false);
        ExamGroupRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExamGroupRemoveActionPerformed(evt);
            }
        });
        add(ExamGroupRemove);
        ExamGroupRemove.setBounds(890, 140, 30, 30);

        ExamAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        ExamAdd.setBorderPainted(false);
        ExamAdd.setContentAreaFilled(false);
        ExamAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExamAddActionPerformed(evt);
            }
        });
        add(ExamAdd);
        ExamAdd.setBounds(660, 140, 30, 30);

        ExamRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ExamRemove.setBorderPainted(false);
        ExamRemove.setContentAreaFilled(false);
        ExamRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExamRemoveActionPerformed(evt);
            }
        });
        add(ExamRemove);
        ExamRemove.setBounds(700, 140, 30, 30);

        PeriodAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        PeriodAdd.setBorderPainted(false);
        PeriodAdd.setContentAreaFilled(false);
        PeriodAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PeriodAddActionPerformed(evt);
            }
        });
        add(PeriodAdd);
        PeriodAdd.setBounds(710, 190, 30, 30);

        PeriodRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        PeriodRemove.setBorderPainted(false);
        PeriodRemove.setContentAreaFilled(false);
        PeriodRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PeriodRemoveActionPerformed(evt);
            }
        });
        add(PeriodRemove);
        PeriodRemove.setBounds(750, 190, 30, 30);

        jLabel21.setText("Начало");
        add(jLabel21);
        jLabel21.setBounds(590, 225, 70, 16);

        PeriodEndTime.setEnabled(false);
        PeriodEndTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PeriodEndTimeMouseClicked(evt);
            }
        });
        add(PeriodEndTime);
        PeriodEndTime.setBounds(650, 240, 50, 25);

        TaskType.setText("Задача(1)/Вопрос(0)");
        TaskType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TaskTypeItemStateChanged(evt);
            }
        });
        add(TaskType);
        TaskType.setBounds(180, 100, 160, 20);

        PeriodState.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PeriodStateItemStateChanged(evt);
            }
        });
        add(PeriodState);
        PeriodState.setBounds(520, 275, 180, 20);
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

    private void TaskRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskRemoveActionPerformed
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
    }//GEN-LAST:event_TaskRemoveActionPerformed

    private void TaskAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskAddActionPerformed
        if (cTheme==null)
            return;
        new OK(200, 200, "Добавить вопрос/задачу", new I_Button() {
            @Override
            public void onPush() {
                final TaskBean task = new TaskBean();
                task.setArtefactId(null);
                task.setText("Новый вопрос/задача");
                task.setTaskType(TaskBean.TaskTypeEnum.QUESTION);
                task.setThemeId(cTheme.getTheme().getId());
                new APICall<TaskBean>(main) {
                    @Override
                    public Call<TaskBean> apiFun() {
                        return main.client.getTaskApi().createTask(task);
                        }
                    @Override
                    public void onSucess(TaskBean oo) {
                        System.out.println("taskId="+oo.getId());
                        refreshThemeFull();
                    }
                };
            }
        });
    }//GEN-LAST:event_TaskAddActionPerformed

    private void DisciplineAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineAddActionPerformed
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
    }//GEN-LAST:event_DisciplineAddActionPerformed

    private void DisciplineRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineRemoveActionPerformed
        if (cDiscipline==null)
            return;
        new OK(200, 200, "Удалить дисциплину: " + cDiscipline.getDiscipline().getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getDisciplineApi().delete3(cDiscipline.getDiscipline().getId());
                        }
                    @Override
                    public void onSucess(Void oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_DisciplineRemoveActionPerformed

    private void ThemeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemeAddActionPerformed
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
    }//GEN-LAST:event_ThemeAddActionPerformed

    private void ThemeRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemeRemoveActionPerformed
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
    }//GEN-LAST:event_ThemeRemoveActionPerformed

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
                taskUpdate();
                }
            });
        }//GEN-LAST:event_ArtifactUploadActionPerformed
    private void ArtifactDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArtifactDownLoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ArtifactDownLoadActionPerformed

    private void TaskSaveTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskSaveTextActionPerformed
        TaskSaveText.setEnabled(false);
        taskTextChanged = false;
        if (cTask==null)
            return;
        cTask.getTask().setText(TaskText.getText());
        taskUpdate();
    }//GEN-LAST:event_TaskSaveTextActionPerformed

    private void DisciplineEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineEditActionPerformed
        new OKName(200,200,"Изменить название дисциплины", cDiscipline.getDiscipline().getName(),new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                cDiscipline.getDiscipline().setName(value);
                new APICall<DisciplineBean>(main) {
                    @Override
                    public Call<DisciplineBean> apiFun() {
                        return main.client.getDisciplineApi().update4(cDiscipline.getDiscipline());
                        //return main.client.getDisciplineApi().update1(cDiscipline,cDiscipline.getId());
                        }
                    @Override
                    public void onSucess(DisciplineBean oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_DisciplineEditActionPerformed

    private void ThemeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemeEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ThemeEditActionPerformed
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

    private void GroupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupAddActionPerformed
        new OKName(200,200,"Добавить группу", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                GroupBean bean = new GroupBean();
                bean.setName(value);
                new APICall<GroupBean>(main) {
                    @Override
                    public Call<GroupBean> apiFun() {
                        return main.client.getGroupApi().create1(bean);
                    }
                    @Override
                    public void onSucess(GroupBean oo) {
                        refreshGroupsList();
                    }
                };
            }
        });
    }//GEN-LAST:event_GroupAddActionPerformed

    private void GroupRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupRemoveActionPerformed
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
                            return main.client.getGroupApi().create1(group);
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
                            account.setPassword("1234");                    // TODO - пустой-не пустой
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
        //cRule.setRatingSystemId(ratings.get(0).getId());        // TODO ---------------- какие
        try {
            new APICall2<ExamRuleBean>() {
                @Override
                public Call<ExamRuleBean> apiFun() {
                    return main.client.getExamRuleApi().update3(cRule);
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

    private void periodStartTimeUpdate(long timeInMS){
        cPeriod.setStart(timeInMS);
        new APICall<ExamBean>(main) {
            @Override
            public Call<ExamBean> apiFun() {
                return main.client.getExamApi().updateExam(cPeriod);
                }
            @Override
            public void onSucess(ExamBean oo) {
                popup("Время сдачи изменено");
                refreshSelectedExamPeriod();
                }
            };
        }
    private void periodStateUpdate(ExamBean.StateEnum state){
        cPeriod.state(ExamBean.StateEnum.fromValue(state.getValue()));
        new APICall<ExamBean>(main) {
            @Override
            public Call<ExamBean> apiFun() {
                return main.client.getExamApi().updateExam(cPeriod);
                }
            @Override
            public void onSucess(ExamBean oo) {
                popup("Состояние сдачи изменено");
                refreshSelectedExamPeriod();
                }
            };
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
                ruleBean.setDuration(180);
                ruleBean.setDisciplineId(cDiscipline.getDiscipline().getId());
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
            //cRule.setExerciseCount(Integer.parseInt(RuleExcerCount.getText()));
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
            //cRule.setQuestionCount(Integer.parseInt(RuleQurestionCount.getText()));
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

    private void ExamGroupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamGroupAddActionPerformed
        if (cGroup==null || cExam==null)
            return;
        if (examGroupsMap.get(cGroup.getGroup().getId())!=null){
            popup("Группа "+cGroup.getGroup().getName()+" уже в экзамене");
            return;
            }
        new OK(200, 200, "Добавить группу " + cGroup.getGroup().getName(), new I_Button() {
            @Override
            public void onPush() {
                //cExam.getGroupIds().add(cGroup.getGroup().getId());
                new APICall<ExamBean>(main) {
                    @Override
                    public Call<ExamBean> apiFun() {
                        return main.client.getExamApi().updateExam(cExam);
                    }
                    @Override
                    public void onSucess(ExamBean oo) {
                        popup("Группа "+cGroup.getGroup().getName()+" добавлена");
                        refreshSelectedExam();
                    }
                };
            }
        });
    }//GEN-LAST:event_ExamGroupAddActionPerformed

    private void ExamGroupRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamGroupRemoveActionPerformed
        if (cGroup==null || cExam==null)
            return;
        /*
        long groupId = cExam.getGroupIds().get(ExamGroupsList.getSelectedIndex()).longValue();
        final GroupBean group = groupsMap.get(groupId);
        new OK(200, 200, "Удалить группу " + group.getName(), new I_Button() {
            @Override
            public void onPush() {
                cExam.getGroupIds().remove(ExamGroupsList.getSelectedIndex());
                new APICall<ExamBean>(main) {
                    @Override
                    public Call<ExamBean> apiFun() {
                        return main.client.getExamApi().updateExam(cExam);
                    }
                    @Override
                    public void onSucess(ExamBean oo) {
                        popup("Группа "+group.getName()+" удалена");
                        refreshSelectedExam();
                    }
                };
            }
        });
         */

    }//GEN-LAST:event_ExamGroupRemoveActionPerformed

    private void ExamAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamAddActionPerformed
        if (cRule==null)
            return;
        if (examRulesMap.get(cRule.getId())!=null){
            popup("Экзамен для регламента: "+cRule.getName()+" уже создан");
            return;
            }
        new OK(200, 200, "Экзамен для регламента: " + cRule.getName(), new I_Button() {
            @Override
            public void onPush() {
                final ExamBean exam = new ExamBean();
                //exam.setExamRuleId(cRule.getId());
                exam.setDisciplineId(cDiscipline.getDiscipline().getId());
                exam.setStart(new OwnDateTime().timeInMS());
                ArrayList<Long> xx = new ArrayList<>();
                xx.add(groups.get(0).getId());
                new APICall<ExamBean>(main) {
                    @Override
                    public Call<ExamBean> apiFun() {
                        return main.client.getExamApi().createExam(exam);
                        }
                    @Override
                    public void onSucess(ExamBean oo) {
                        refreshDisciplineFull();
                        }
                    };
                }
            });
    }//GEN-LAST:event_ExamAddActionPerformed

    private void ExamRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamRemoveActionPerformed
        if (cExam==null)
            return;
        new OK(200, 200, "Удалить экзамен для регламента: " + ExamList.getItem(ExamList.getSelectedIndex()), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getExamApi().deleteExam(cExam.getId());
                        }
                    @Override
                    public void onSucess(Void oo) {
                        refreshDisciplineFull();
                    }
                };
            }
        });
    }//GEN-LAST:event_ExamRemoveActionPerformed

    private void PeriodAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PeriodAddActionPerformed
        if (cExam==null)
            return;
        new OK(200, 200, "Добавить сдачу экзамена по: " + cDiscipline.getDiscipline().getName(), new I_Button() {
            @Override
            public void onPush() {
                }
            });
    }//GEN-LAST:event_PeriodAddActionPerformed

    private void PeriodRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PeriodRemoveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PeriodRemoveActionPerformed

    private void PeriodStartTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PeriodStartTimeMouseClicked
    }//GEN-LAST:event_PeriodStartTimeMouseClicked

    private void PeriodDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PeriodDataMouseClicked
        if (evt.getClickCount()<2)
            return;
        if (cPeriod==null)
            return;
        new CalendarView("Начало экзамена", new I_CalendarTime() {
            @Override
            public void onSelect(OwnDateTime time) {
                periodStartTimeUpdate(time.timeInMS());
                }
            });
    }//GEN-LAST:event_PeriodDataMouseClicked

    private void PeriodEndTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PeriodEndTimeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PeriodEndTimeMouseClicked

    private void ExamListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExamListItemStateChanged
        refreshSelectedExam();
    }//GEN-LAST:event_ExamListItemStateChanged

    private void TaskTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TaskTypeItemStateChanged
        if (refresh)
            return;
        if (cTask==null)
            return;
        cTask.getTask().setTaskType(TaskType.isSelected() ? TaskBean.TaskTypeEnum.EXERCISE : TaskBean.TaskTypeEnum.QUESTION);
        taskUpdate();
    }//GEN-LAST:event_TaskTypeItemStateChanged

    private void TaskTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_TaskTextInputMethodTextChanged
        taskTextChanged=true;
        TaskSaveText.setEnabled(true);
    }//GEN-LAST:event_TaskTextInputMethodTextChanged

    private void TaskTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaskTextKeyPressed
        taskTextChanged=true;
        TaskSaveText.setEnabled(true);
    }//GEN-LAST:event_TaskTextKeyPressed

    private void PeriodStateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PeriodStateItemStateChanged
        int idx = PeriodState.getSelectedIndex();
        if (idx==0)
            return;
        periodStateUpdate(stateFactory.getList().get(idx-1).value);
    }//GEN-LAST:event_PeriodStateItemStateChanged

    public void taskUpdate(){
        if (cTask==null)
            return;
        new APICall<TaskBean>(main) {
            @Override
            public Call<TaskBean> apiFun() {
                return main.client.getTaskApi().updateTask(cTask.getTask());
                }
            @Override
            public void onSucess(TaskBean oo) {
                popup("Вопрос/задача изменены");
                refreshThemeFull();
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
    private javax.swing.JButton ArtifactDownLoad;
    private javax.swing.JButton ArtifactUpload;
    private javax.swing.JButton ArtifactView;
    private java.awt.Choice Discipline;
    private javax.swing.JButton DisciplineAdd;
    private javax.swing.JButton DisciplineEdit;
    private javax.swing.JButton DisciplineImport;
    private javax.swing.JButton DisciplineRemove;
    private javax.swing.JButton DisciplineSaveImport;
    private javax.swing.JButton ExamAdd;
    private javax.swing.JButton ExamGroupAdd;
    private javax.swing.JButton ExamGroupRemove;
    private java.awt.Choice ExamGroupsList;
    private java.awt.Choice ExamList;
    private javax.swing.JButton ExamRemove;
    private java.awt.Choice ExamsForGroupList;
    private java.awt.Choice ExamsTotalList;
    private javax.swing.JCheckBox FullTrace;
    private java.awt.Choice Group;
    private javax.swing.JButton GroupAdd;
    private javax.swing.JButton GroupEdit;
    private javax.swing.JButton GroupRemove;
    private javax.swing.JButton GroupsImport;
    private javax.swing.JButton PeriodAdd;
    private javax.swing.JTextField PeriodData;
    private javax.swing.JTextField PeriodEndTime;
    private javax.swing.JCheckBox PeriodForAll;
    private java.awt.Choice PeriodList;
    private java.awt.Choice PeriodOneGroup;
    private javax.swing.JButton PeriodRemove;
    private javax.swing.JTextField PeriodStartTime;
    private java.awt.Choice PeriodState;
    private javax.swing.JButton RefreshDisciplines;
    private javax.swing.JButton RefreshGroups;
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
    private javax.swing.JButton StudentAdd;
    private javax.swing.JButton StudentEdit;
    private javax.swing.JButton StudentRemove;
    private java.awt.Choice Task;
    private javax.swing.JButton TaskAdd;
    private javax.swing.JButton TaskRemove;
    private javax.swing.JButton TaskSaveText;
    private java.awt.TextArea TaskText;
    private javax.swing.JCheckBox TaskType;
    private javax.swing.JLabel TaskTypeLabel;
    private java.awt.Choice Theme;
    private javax.swing.JButton ThemeAdd;
    private javax.swing.JButton ThemeEdit;
    private javax.swing.JButton ThemeRemove;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
