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
import romanow.abc.core.API.RestAPIEM;
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.baseentityes.JBoolean;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.baseentityes.JLong;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.User;
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
public class EMExamAdminPanel extends BasePanel{
    private ArrayList<EMDiscipline> disciplines = new ArrayList<>();    // Список дисциплин
    private EMDiscipline cDiscipline = null;                            // Текущая дисциплина
    private EMTheme cTheme = null;                                      // Текущая тема
    private EMTask cTask=null;                                          // Текущая задача/тест
    private EMExamRule cRule=null;
    private EMExam cExam=null;
    private EMGroup cGroup = null;                                      // Текущая группа
    private EMExamTaking  cPeriod=null;                                 // Текущий прием
    private ArrayList<EMGroup> groups = new ArrayList<>();              // Список групп полный
    private ArrayList<EMExamTaking> cTakings = new ArrayList<>();       // Список приема для экзамена
    private ArrayList<ConstValue> takingStateList = new ArrayList<>();
    //----------------------------------------------------------------------------------------
    private ArrayList<EMTheme> ruleThemes = new ArrayList<>();          // Темы регламента
    private HashMap<Long,EMTheme> ruleThemesMap = new HashMap<>();
    private HashMap<Long,EMGroup> groupsMap = new HashMap<>();          // Мар всех групп
    private HashMap<Long,Long> groupsExamMap = new HashMap<>();         // Мар групп с экзаменом
    //----------------------------------------------------------------------------------------
    private int cTaskNum=0;
    private OWTDiscipline owtImportData = null;
    private boolean refresh=false;                                      // Признак обновления для событий  CheckBox
    private boolean taskTextChanged=false;
    public EMExamAdminPanel() {
        initComponents();
        }
    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        ArtifactView.setEnabled(false);
        ArtifactDownLoad.setEnabled(false);
        DisciplineSaveImport.setEnabled(false);
        ArtifactDownLoad.setEnabled(false);
        ArtifactView.setEnabled(false);
        PeriodState.removeAll();
        PeriodState.add("...");
        takingStateList = Values.constMap().getGroupList("Taking");
        for(ConstValue cc : takingStateList)
            PeriodState.add(cc.title());
        PeriodState.select(0);
        refreshAll();
        }

    private void refreshAll(){
        refreshGroupsList();
        refreshDisciplineList();
        }

    private void refreshDisciplineList(){
        Discipline.removeAll();
        Theme.removeAll();
        TaskText.setText("");
        new APICall<ArrayList<DBRequest>>(main){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"EMDiscipline", Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                disciplines.clear();
                Discipline.removeAll();
                try {
                for(DBRequest request : oo){
                    EMDiscipline discipline = (EMDiscipline) request.get(main.gson);
                    Discipline.add(discipline.getName());
                    disciplines.add(discipline);
                    }
                } catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Ошибка чтения списка дисциплин");
                    }
                refreshSelectedDiscipline();
                }
            };
        }

    private void refreshRules(){
        RulesList.removeAll();
        for(EMExamRule rule : cDiscipline.getRules()){
            RulesList.add(rule.getName());
            }
        refreshSelectedRule();
        }

    private void refreshSelectedRule(){
        RuleName.setText("");
        RuleOwnRating.setText("");
        RuleExceciseForOne.setText("");
        RuleThemesList.removeAll();
        cRule=null;
        if (cDiscipline.getRules().size()==0)
            return;
        cRule = cDiscipline.getRules().get(RulesList.getSelectedIndex());
        RuleName.setText(cRule.getName());
        RuleDuration1.setText(""+cRule.getExamDuration());
        RuleOwnRating.setText(""+cRule.getExamDuration());
        RuleExceciseForOne.setText(""+cRule.getOneExcerciceDefBall());
        RuleExcerciseSum.setText(""+cRule.getExcerciceRating());
        RuleQuestionForOne.setText(""+cRule.getOneQuestionDefBall());
        RuleQuestionSum.setText(""+cRule.getQuestionRating());
        RuleMinRating.setText(""+cRule.getMinimalRating());
        RuleOwnRating.setText(""+cRule.getExamOwnRating());
        ruleThemes.clear();
        RuleThemesList.removeAll();
        for(EntityLink themeId : cRule.getThemes()){
            EMTheme theme  = cDiscipline.getThemes().getById(themeId.getOid());
            if (theme==null)
                System.out.println("Не найдена тема id="+themeId);
            else {
                ruleThemes.add(theme);
                RuleThemesList.add(theme.getName());
                ruleThemesMap.put(theme.getOid(),theme);
                }
            }
        }

    private void refreshGroupsList(){
        Group.removeAll();
        Student.removeAll();
        new APICall<ArrayList<DBRequest>>(main) {
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"EMGroup",Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                groupsMap.clear();
                groups.clear();
                Group.removeAll();
                try {
                    for (DBRequest dd : oo) {
                        EMGroup group = (EMGroup)dd.get(main.gson);
                        Group.add(group.getName());
                        groupsMap.put(group.getOid(), group);
                        groups.add(group);
                        }
                    refreshGroupFull();
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитан список групп");
                        return;
                        }
                    }
                };
        }
    private void refreshGroupFull(){
        Student.removeAll();
        cGroup=null;
        if (groups.size()==0)
            return;
        cGroup = groups.get(Group.getSelectedIndex());
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"EMGroup",cGroup.getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try{
                    cGroup = (EMGroup)oo.get(main.gson);
                    Student.removeAll();
                    for(EMStudent student : cGroup.getStudents())
                        Student.add(student.getUser().getTitle());
                    //refreshStudentFull();
                    }catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитаны данные группы "+cGroup.getName());
                        }
                }
            };
        }
    private void refreshSelectedDiscipline(){
        Theme.removeAll();
        Task.removeAll();
        cDiscipline=null;
        if (disciplines.size()==0)
            return;
        cDiscipline = disciplines.get(Discipline.getSelectedIndex());
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"EMDiscipline",cDiscipline.getOid(),1);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try {
                    cDiscipline = (EMDiscipline) oo.get(main.gson);
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Ошибка чтения дисциплины "+cDiscipline.getName());
                        return;
                        }
                cDiscipline.createMaps();
                cDiscipline.getTakings().clear();
                Theme.removeAll();
                for(EMTheme theme : cDiscipline.getThemes())
                    Theme.add(theme.getName());
                refreshThemeFull();
                refreshRules();
                refreshDisciplineExams();
                ExamsForGroupList.removeAll();
                for(EntityLink<EMGroup> group : cDiscipline.getGroups()){
                    ExamsForGroupList.add(group.getRef().getName());
                    }
                groupsExamMap.clear();
                ExamsForGroupList.removeAll();
                for(EMExam exam :  cDiscipline.getExamens())
                    for(EntityLink gg : exam.getGroups()){
                        groupsExamMap.put(gg.getOid(),gg.getOid());
                        EMGroup group = groupsMap.get(gg.getOid());
                        if (group==null){
                            popup("Не найдена группа id="+gg.getOid());
                            }
                        else
                            ExamsForGroupList.add(group.getName());
                        }
                }
            };
        }

    private void refreshDisciplineExams(){
        ExamList.removeAll();
        if (cDiscipline==null)
            return;
        for(EMExam exam : cDiscipline.getExamens()){
            EMExamRule rule = cDiscipline.getRules().getById(exam.getRule().getOid());
            ExamList.add((exam.getName().length()==0 ?  "..." : exam.getName())+" "+(rule==null ? "???" : rule.getName()));
            }
        refreshSelectedExam();
        }

    private void refreshSelectedExam(){
        cExam=null;
        ExamGroupsList.removeAll();
        if (cDiscipline.getExamens().size()==0)
            return;
        cExam = cDiscipline.getExamens().get(ExamList.getSelectedIndex());
        ExamGroupsList.removeAll();
        for(EntityLink id : cExam.getGroups()){
            EMGroup group = groupsMap.get(id);
            if (group==null){
                System.out.println("Не найдена группа id="+id);
                continue;
                }
            ExamGroupsList.add(group.getName());
            }
        refreshExamPeriods();
        }

    private void  refreshExamPeriods(){
        PeriodList.removeAll();
        cTakings.clear();
        for(EMExamTaking taking : cDiscipline.getTakings()){
            if (taking.getExam().getOid()!=cExam.getOid())
                continue;
            cTakings.add(taking);
            PeriodList.add(taking.getName().length()==0 ? "..." : taking.getName()+" "+taking.getStartTime().dateTimeToString());
            }
        refreshSelectedExamPeriod();
        }

    private void  refreshSelectedExamPeriod(){
        PeriodState.select(0);
        cPeriod=null;
        if (cTakings.size()==0)
            return;
        cPeriod = cTakings.get(PeriodList.getSelectedIndex());
        if (cPeriod.getStartTime().timeInMS()==0){
            PeriodData.setText("---");
            PeriodStartTime.setText("---");
            PeriodEndTime.setText("---");
            }
        else{
            OwnDateTime date1 = new OwnDateTime(cPeriod.getStartTime().timeInMS());
            OwnDateTime date2 = new OwnDateTime(cPeriod.getEndTime().timeInMS());
            PeriodData.setText(date1.dateToString());
            PeriodStartTime.setText(date1.timeToString());
            PeriodEndTime.setText(cPeriod.getEndTime().timeInMS()==0 ? "---" : date2.timeToString());
            }
        int state = cPeriod.getState();
        PeriodState.select(0);
        for(int i=0; i<takingStateList.size();i++){
            ConstValue cc = takingStateList.get(i);
            if (cc.value()==state){}
                PeriodState.select(i+1);
                break;
                }
            }


    private void refreshThemeFull(){
        Task.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Theme.getSelectedIndex());
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken, "EMTheme",cTheme.getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try {
                    cTheme = (EMTheme) oo.get(main.gson);
                    cTheme.getTasks().sort(new Comparator<EMTask>() {
                        @Override
                        public int compare(EMTask o1, EMTask o2) {              // Сортировать по id (в порядке поступления)
                            return o1.getOwnRating() - o2.getOid() > 0 ? 1 : -1;
                            }
                        });
                    int iq = 1, it = 1;
                    for (EMTask task : cTheme.getTasks())
                        if (task.getType() == Values.TaskQuestion)
                            Task.add("Вопрос " + iq++ + " " + task.getName());
                    for (EMTask task : cTheme.getTasks())
                        if (task.getType() == Values.TaskExercise)
                            Task.add("Задача " + it++ + " " + task.getName());
                    refreshTaskFull();
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитана тем: "+cTheme.getName());
                        return;
                        }
                    }
                };
            }

    private void refreshTaskFullForce(){
        TaskText.setText("");
        if (cTheme.getTasks().size()==0)
            return;
        refresh=true;
        TaskSaveText.setEnabled(false);
        cTaskNum = Task.getSelectedIndex();
        cTask = cTheme.getTasks().get(cTaskNum);
        boolean isTask = cTask.getType()== Values.TaskExercise;
        TaskType.setSelected(isTask);
        TaskTypeLabel.setText(isTask ? "Задача" : "Вопрос (тест)");
        TaskText.setText(UtilsEM.formatSize(cTask.getTaskText(),60));
        boolean bb = cTask.getArtifact().getOid()!=0;
        ArtifactView.setEnabled(bb);
        ArtifactDownLoad.setEnabled(bb);
        if (bb){
            Artifact artifact = cTask.getArtifact().getRef();
            TaskText.append("\n"+artifact.getOriginalName());
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
        ExamsForGroupList = new java.awt.Choice();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        RulesList = new java.awt.Choice();
        jLabel9 = new javax.swing.JLabel();
        RuleExceciseForOne = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        RuleOwnRating = new javax.swing.JTextField();
        RuleName = new javax.swing.JTextField();
        RuleThemeRemove = new javax.swing.JButton();
        RuleAdd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        RuleThemesList = new java.awt.Choice();
        RuleThemeAdd = new javax.swing.JButton();
        RuleDelete = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        RuleExcerciseSum = new javax.swing.JTextField();
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
        RuleQuestionForOne = new javax.swing.JTextField();
        RuleQuestionSum = new javax.swing.JTextField();
        RuleDuration1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        RuleMinRating = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();

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
        add(ExamsForGroupList);
        ExamsForGroupList.setBounds(310, 380, 130, 20);

        jLabel7.setText("Задача: за одну - сумма ");
        add(jLabel7);
        jLabel7.setBounds(20, 530, 140, 20);

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

        RuleExceciseForOne.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExceciseForOneKeyPressed(evt);
            }
        });
        add(RuleExceciseForOne);
        RuleExceciseForOne.setBounds(170, 530, 30, 25);

        jLabel10.setText("Рейтинг экзамена");
        add(jLabel10);
        jLabel10.setBounds(270, 530, 110, 20);

        RuleOwnRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleOwnRatingKeyPressed(evt);
            }
        });
        add(RuleOwnRating);
        RuleOwnRating.setBounds(390, 530, 50, 25);

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
        RuleThemeRemove.setBounds(400, 570, 30, 30);

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
        jLabel11.setBounds(20, 580, 70, 16);
        add(RuleThemesList);
        RuleThemesList.setBounds(110, 580, 280, 20);

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

        jLabel13.setText("Тест: за один - сумма");
        add(jLabel13);
        jLabel13.setBounds(20, 500, 140, 20);

        RuleExcerciseSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExcerciseSumKeyPressed(evt);
            }
        });
        add(RuleExcerciseSum);
        RuleExcerciseSum.setBounds(210, 530, 40, 25);

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

        PeriodList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PeriodListItemStateChanged(evt);
            }
        });
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

        RuleQuestionForOne.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQuestionForOneKeyPressed(evt);
            }
        });
        add(RuleQuestionForOne);
        RuleQuestionForOne.setBounds(170, 500, 30, 25);

        RuleQuestionSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQuestionSumKeyPressed(evt);
            }
        });
        add(RuleQuestionSum);
        RuleQuestionSum.setBounds(210, 500, 40, 25);

        RuleDuration1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleDuration1KeyPressed(evt);
            }
        });
        add(RuleDuration1);
        RuleDuration1.setBounds(390, 470, 50, 25);

        jLabel17.setText("Продолж. (мин)");
        add(jLabel17);
        jLabel17.setBounds(270, 475, 100, 20);

        RuleMinRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleMinRatingKeyPressed(evt);
            }
        });
        add(RuleMinRating);
        RuleMinRating.setBounds(390, 500, 50, 25);

        jLabel22.setText("Рейтинг допуска");
        add(jLabel22);
        jLabel22.setBounds(270, 500, 110, 20);
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
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"EMTask",cTask.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
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
                final EMTask task = new EMTask();
                task.setTaskText("Новый вопрос/задача");
                task.setType(Values.TaskQuestion);
                task.getEMTheme().setOid(cTheme.getOid());
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(task,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        System.out.println("taskId="+oo.getValue());
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
                EMDiscipline discipline = new EMDiscipline();
                discipline.setName(value);
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(discipline,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_DisciplineAddActionPerformed

    private void DisciplineRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineRemoveActionPerformed
        if (cDiscipline==null)
            return;
        new OK(200, 200, "Удалить дисциплину: " + cDiscipline.getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"EMDiscipline",cDiscipline.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_DisciplineRemoveActionPerformed

    private void ThemeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemeAddActionPerformed
        if (cDiscipline==null)
            return;
        new OKName(200,200,"Добавить тему в "+cDiscipline.getName(), new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                EMTheme bean = new EMTheme();
                bean.setName(value);
                bean.getEMDiscipline().setOid(cDiscipline.getOid());
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(bean,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        refreshSelectedDiscipline();
                    }
                };
            }
        });
    }//GEN-LAST:event_ThemeAddActionPerformed

    private void ThemeRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemeRemoveActionPerformed
        if (cTheme==null)
            return;
        new OK(200, 200, "Удалить тему: " + cTheme.getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {        // TODO - это удаление
                        return main.service.deleteById(main.debugToken,"EMTheme",cTheme.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshDisciplineList();
                    }
                };
            }
        });
    }//GEN-LAST:event_ThemeRemoveActionPerformed

    private void DisciplineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DisciplineItemStateChanged
        refreshSelectedDiscipline();
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
        new UploadPanel(200, 200, main, new I_OK() {
            @Override
            public void onOK(final Entity ent) {
                if (cTask.getArtifact().getOid()==0){
                    cTask.getArtifact().setOidRef((Artifact) ent);
                    taskUpdate();
                    }
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.removeArtifact(main.debugToken,cTask.getArtifact().getOid());
                        }

                    @Override
                    public void onSucess(JEmpty oo) {
                        cTask.getArtifact().setOidRef((Artifact) ent);
                        taskUpdate();
                        }
                    };
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
        cTask.setTaskText(TaskText.getText());
        taskUpdate();
    }//GEN-LAST:event_TaskSaveTextActionPerformed

    private void DisciplineEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisciplineEditActionPerformed
        new OKName(200,200,"Изменить название дисциплины", cDiscipline.getName(),new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                cDiscipline.setName(value);
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.updateEntity(main.debugToken, new DBRequest(cDiscipline,main.gson));
                        }
                    @Override
                    public void onSucess(JEmpty oo) {
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
                EMDiscipline discipline = new EMDiscipline();
                discipline.setName(value);
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(discipline,main.gson),0);
                        }
                    @Override
                    public void onSucess(final JLong oo) {
                        int idx1=0;
                        int idx2=0;
                        OWTTheme owtTheme=null;
                        try {
                            final EMTheme theme = new EMTheme();
                            final EMTask task = new EMTask();
                            for (idx1 = 0; idx1 < owtImportData.size(); idx1++) {
                                idx2=-1;
                                owtTheme = owtImportData.get(idx1);
                                theme.getEMDiscipline().setOid(oo.getValue());
                                theme.setName(owtTheme.getQuestion());
                                final JLong theme1 = new APICallSync<JLong>() {
                                    @Override
                                    public Call<JLong> apiFun() {
                                        return main.service.addEntity(main.debugToken,new DBRequest(theme,main.gson),0);
                                        }
                                    }.call();
                                for(idx2=0;idx2 < owtTheme.size();idx2++){
                                    task.getEMTheme().setOid(theme1.getValue());
                                    task.setType(Values.TaskQuestion);
                                    task.setTaskText(owtTheme.get(idx2).toString());
                                    new APICallSync<JLong>() {
                                        @Override
                                        public Call<JLong> apiFun() {
                                            return main.service.addEntity(main.debugToken,new DBRequest(task,main.gson),0);
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
                EMGroup bean = new EMGroup();
                bean.setName(value);
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken, new DBRequest(bean,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        refreshGroupsList();
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
                        return main.service.deleteById(main.debugToken,"EMGroup",cGroup.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
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
                final  EMGroup group = new EMGroup();
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
                            String ss[] = UtilsEM.parseFIO(values[0]);
                            user.setLastName(ss[0]);
                            user.setFirstName(ss[1]);
                            user.setMiddleName(ss[2]);
                            account.setPassword("1234");
                            user.setTypeId(Values.UserEMStudent);
                            user.setAccount(account);
                            EMStudent student = new EMStudent();
                            student.setState(Values.StudentStateNormal);
                            student.getEMGroup().setOid(group2.getValue());
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

    private void ruleUpdate(KeyEvent evt){
        if (cRule==null)
            return;
        try {
            new APICall2<JEmpty>() {
                @Override
                public Call<JEmpty> apiFun() {
                    return main.service.updateEntity(main.debugToken,new DBRequest(cRule,main.gson));
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
        cPeriod.setStartTime(new OwnDateTime(timeInMS));
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken, new DBRequest(cPeriod,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                popup("Время сдачи изменено");
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
        if (cRule.getThemes().size()==0)
            return;
        long oid = cRule.getThemes().get(RuleThemesList.getSelectedIndex()).getOid();
        EMTheme theme = cDiscipline.getThemes().getById(oid);
        new OK(200, 200, "Удалить тему: " + shortString(theme.getName(), 20), new I_Button() {
            @Override
            public void onPush() {
                int idx = RuleThemesList.getSelectedIndex();
                cRule.getThemes().remove(idx);
                ruleUpdate(null);
                }
            });
    }//GEN-LAST:event_RuleThemeRemoveActionPerformed


    private void RuleAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleAddActionPerformed
        if (cDiscipline==null)
            return;
        final EMExamRule ruleBean = new EMExamRule();
        new OKName(200, 200, "Добавить регламент", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                ruleBean.setName(value);
                ruleBean.setExamDuration(180);
                ruleBean.setMinimalRating(30);
                ruleBean.setExamOwnRating(40);
                ruleBean.setExcerciceRating(20);
                ruleBean.setQuestionRating(20);
                ruleBean.setOneExcerciceDefBall(10);
                ruleBean.setOneQuestionDefBall(2);
                ruleBean.getEMDiscipline().setOid(cDiscipline.getOid());
                ruleBean.setMinimalRating(0);
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(ruleBean,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
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

    private void RuleOwnRatingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleOwnRatingKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setExamOwnRating(Integer.parseInt(RuleOwnRating.getText()));
            ruleUpdate(evt);
            } catch (Exception ee){
                popup("Ошибка формата целого");
                main.viewUpdate(evt,false);
                }
    }//GEN-LAST:event_RuleOwnRatingKeyPressed

    private void RuleExceciseForOneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleExceciseForOneKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setOneExcerciceDefBall(Integer.parseInt(RuleExceciseForOne.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_RuleExceciseForOneKeyPressed

    private void RuleThemeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleThemeAddActionPerformed
        if (cRule==null)
            return;
        if (cTheme==null)
            return;
        if (ruleThemesMap.get(cTheme.getOid())!=null){
            popup("Тема уже есть в регламенте");
            return;
            }
        new OK(200, 200, "Добавить тему: " + shortString(cTheme.getName(), 30), new I_Button() {
            @Override
            public void onPush() {
                cRule.getThemes().add(cTheme.getOid());
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
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"EMExamRule",cRule.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshRules();
                        }
                    };
                }
            });

    }//GEN-LAST:event_RuleDeleteActionPerformed

    private void RuleExcerciseSumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleExcerciseSumKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setExcerciceRating(Integer.parseInt(RuleExcerciseSum.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_RuleExcerciseSumKeyPressed

    private void RulesListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RulesListItemStateChanged
        refreshSelectedRule();
    }//GEN-LAST:event_RulesListItemStateChanged

    private void RuleThemeAddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleThemeAddAllActionPerformed
        if (cRule==null)
            return;
        new OK(200, 200, "Копировать все темы", new I_Button() {
            @Override
            public void onPush() {
                cRule.getThemes().clear();
                for(EMTheme theme : cDiscipline.getThemes())
                    cRule.getThemes().add(theme.getOid());
                ruleUpdate(null);
                }
            });
    }//GEN-LAST:event_RuleThemeAddAllActionPerformed

    private void ExamGroupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamGroupAddActionPerformed
        if (cGroup==null || cExam==null)
            return;
        if (groupsExamMap.get(cGroup.getOid())!=null){
            popup("Группа "+cGroup.getName()+" уже в экзамене");
            return;
            }
        new OK(200, 200, "Добавить группу " + cGroup.getName(), new I_Button() {
            @Override
            public void onPush() {
                cExam.getGroups().add(cGroup.getOid());
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.updateEntity(main.debugToken, new DBRequest(cExam,main.gson));
                        }
                    @Override
                    public void onSucess(JEmpty oo) {
                        popup("Группа "+cGroup.getName()+" добавлена");
                        refreshSelectedExam();
                        }
                    };
                }
            });
    }//GEN-LAST:event_ExamGroupAddActionPerformed

    private void ExamGroupRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamGroupRemoveActionPerformed
        if (cGroup==null || cExam==null)
            return;
        long groupId = cExam.getGroups().get(ExamGroupsList.getSelectedIndex()).getOid();
        final EMGroup group = groupsMap.get(groupId);
        new OK(200, 200, "Удалить группу " + group.getName(), new I_Button() {
            @Override
            public void onPush() {
                cExam.getGroups().remove(ExamGroupsList.getSelectedIndex());
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.updateEntity(main.debugToken,new DBRequest(cExam,main.gson));
                        }
                    @Override
                    public void onSucess(JEmpty oo) {
                        popup("Группа "+group.getName()+" удалена");
                        refreshSelectedExam();
                    }
                };
            }
        });

    }//GEN-LAST:event_ExamGroupRemoveActionPerformed

    private void ExamAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamAddActionPerformed
        if (cRule==null)
            return;
        new OK(200, 200, "Экзамен для регламента: " + cRule.getName(), new I_Button() {
            @Override
            public void onPush() {
                final EMExam exam = new EMExam();
                exam.getRule().setOid(cRule.getOid());
                exam.getEMDiscipline().setOid(cDiscipline.getOid());
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(exam,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        refreshSelectedDiscipline();
                        }
                    };
                }
            });
    }//GEN-LAST:event_ExamAddActionPerformed

    private void ExamRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamRemoveActionPerformed
        if (cExam==null)
            return;
        new OK(200, 200, "Удалить экзамен: " + ExamList.getItem(ExamList.getSelectedIndex()), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"EMExam",cExam.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshSelectedDiscipline();
                    }
                };
            }
        });
    }//GEN-LAST:event_ExamRemoveActionPerformed

    private void PeriodAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PeriodAddActionPerformed
        if (cExam==null)
            return;
        new OK(200, 200, "Добавить сдачу экзамена по: " + cDiscipline.getName(), new I_Button() {
            @Override
            public void onPush() {
                EMExamTaking taking = new EMExamTaking();
                taking.setState(Values.TakingEdited);
                taking.setName("Новый прием экзамена");
                taking.getEMDiscipline().setOid(cDiscipline.getOid());
                taking.getExam().setOid(cExam.getOid());
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(taking,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        refreshSelectedDiscipline();
                        }
                    };
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
                cPeriod.setStartTime(time);
                periodUpdate();
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
        cTask.setType(TaskType.isSelected() ? Values.TaskExercise : Values.TaskQuestion);
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

    private void periodUpdate(){
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken, new DBRequest(cPeriod,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                refreshSelectedExamPeriod();
                }
            };
        }

    private void PeriodStateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PeriodStateItemStateChanged
        int idx = PeriodState.getSelectedIndex();
        if (idx==0)
            return;
        if (cPeriod==null)
            return;
        cPeriod.setState(takingStateList.get(idx-1).value());
        periodUpdate();
    }//GEN-LAST:event_PeriodStateItemStateChanged

    private void RuleQuestionForOneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleQuestionForOneKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setOneQuestionDefBall(Integer.parseInt(RuleQuestionForOne.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleQuestionForOneKeyPressed

    private void RuleQuestionSumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleQuestionSumKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setQuestionRating(Integer.parseInt(RuleQuestionSum.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleQuestionSumKeyPressed

    private void RuleDuration1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleDuration1KeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setExamDuration(Integer.parseInt(RuleDuration1.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleDuration1KeyPressed

    private void RuleMinRatingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleMinRatingKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setMinimalRating(Integer.parseInt(RuleMinRating.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleMinRatingKeyPressed

    private void PeriodListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PeriodListItemStateChanged
        refreshSelectedExamPeriod();
    }//GEN-LAST:event_PeriodListItemStateChanged

    public void taskUpdate(){
        if (cTask==null)
            return;
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken,new DBRequest(cTask,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
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
    private javax.swing.JTextField RuleDuration1;
    private javax.swing.JTextField RuleExceciseForOne;
    private javax.swing.JTextField RuleExcerciseSum;
    private javax.swing.JTextField RuleMinRating;
    private javax.swing.JTextField RuleName;
    private javax.swing.JTextField RuleOwnRating;
    private javax.swing.JTextField RuleQuestionForOne;
    private javax.swing.JTextField RuleQuestionSum;
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
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
