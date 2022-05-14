/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

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
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;

import java.awt.*;
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
    private EMExamTaking cTaking =null;                                 // Текущий прием
    private ArrayList<EMGroup> groups = new ArrayList<>();              // Список групп полный
    private ArrayList<EMExamTaking> cTakings = new ArrayList<>();       // Список приема для экзамена
    private ArrayList<ConstValue> takingStateList = new ArrayList<>();
    private ConstValue cTicketState = null;
    private ArrayList<ConstValue> ticketStateList = new ArrayList<>();
    private ConstValue cAnswerState = null;
    private ArrayList<ConstValue> answerStateList = new ArrayList<>();
    private ArrayList<EMTask> sortedTasks =  new ArrayList<>();         // Отсортированы - вопрос/задача
    //----------------------------------------------------------------------------------------
    private ArrayList<EMTheme> ruleThemes = new ArrayList<>();          // Темы регламента
    private HashMap<Long,EMTheme> ruleThemesMap = new HashMap<>();
    private HashMap<Long,EMGroup> groupsMap = new HashMap<>();          // Мар всех групп
    private ConstValue cTakingState = null;
    private EMExam ticketsForExam = null;
    private EMExamTaking ticketsForTaking = null;
    private EntityRefList<EMTicket> tickets = new EntityRefList<>();
    private EMTicket cTicket = null;
    //---------------------------------------------------------------------------------------
    private int themeIdx = -1;
    private int taskIdx = -1;
    private int examIdx = -1;
    private int ruleIdx = -1;
    private int takingIdx = -1;
    private int groupIdx = -1;
    private int studentIdx = -1;
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
        TaskArtifactView.setEnabled(false);
        TaskArtifactDownLoad.setEnabled(false);
        DisciplineSaveImport.setEnabled(false);
        TaskArtifactDownLoad.setEnabled(false);
        TaskArtifactView.setEnabled(false);
        TakingState.removeAll();
        TakingState.add("...");
        takingStateList = Values.constMap().getGroupList("Taking");
        choiceStateCreate(takingStateList,TakingState);
        ticketStateList = Values.constMap().getGroupList("Ticket");
        choiceStateCreate(ticketStateList,TicketState);
        answerStateList = Values.constMap().getGroupList("Answer");
        choiceStateCreate(answerStateList,AnswerState);
        refreshAll();
        }

    private void savePos(){
        themeIdx = Themes.getSelectedIndex();
        taskIdx = Tasks.getSelectedIndex();
        examIdx = Exams.getSelectedIndex();
        ruleIdx = Rules.getSelectedIndex();
        takingIdx = Takings.getSelectedIndex();
        groupIdx = Groups.getSelectedIndex();
        studentIdx = Students.getSelectedIndex();
        }

    private void refreshAll(){
        refreshGroupsList();
        refreshDisciplineList();
        }

    private void refreshDisciplineList(){
        Disciplines.removeAll();
        Themes.removeAll();
        TaskText.setText("");
        new APICall<ArrayList<DBRequest>>(main){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"EMDiscipline", Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                disciplines.clear();
                Disciplines.removeAll();
                try {
                for(DBRequest request : oo){
                    EMDiscipline discipline = (EMDiscipline) request.get(main.gson);
                    Disciplines.add(discipline.getName());
                    disciplines.add(discipline);
                    }
                } catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Ошибка чтения списка дисциплин");
                    }
                refreshSelectedDiscipline(false);
                }
            };
        }

    private void refreshRules(boolean withPos){
        Rules.removeAll();
        for(EMExamRule rule : cDiscipline.getRules()){
            Rules.add(rule.getName());
            }
        if (ruleIdx!=-1)
            Rules.select(ruleIdx);
        if (withPos)
            Rules.select(ruleIdx);
        refreshSelectedRule();
        }

    private void refreshSelectedRule(){
        RuleName.setText("");
        RuleOwnRating.setText("");
        RuleExceciseForOne.setText("");
        RuleThemes.removeAll();
        cRule=null;
        if (cDiscipline.getRules().size()==0)
            return;
        cRule = cDiscipline.getRules().get(Rules.getSelectedIndex());
        RuleName.setText(cRule.getName());
        RuleDuration.setText(""+cRule.getExamDuration());
        RuleOwnRating.setText(""+cRule.getExamDuration());
        RuleExceciseForOne.setText(""+cRule.getOneExcerciceDefBall());
        RuleExcerciseSum.setText(""+cRule.getExcerciceRating());
        RuleQuestionForOne.setText(""+cRule.getOneQuestionDefBall());
        RuleQuestionSum.setText(""+cRule.getQuestionRating());
        RuleMinRating.setText(""+cRule.getMinimalRating());
        RuleOwnRating.setText(""+cRule.getExamOwnRating());
        ruleThemes.clear();
        ruleThemesMap.clear();
        RuleThemes.removeAll();
        for(EntityLink themeId : cRule.getThemes()){
            EMTheme theme  = cDiscipline.getThemes().getById(themeId.getOid());
            if (theme==null)
                System.out.println("Не найдена тема id="+themeId);
            else {
                ruleThemes.add(theme);
                RuleThemes.add(theme.getName());
                ruleThemesMap.put(theme.getOid(),theme);
                }
            }
        }

    private void refreshGroupsList(){
        Groups.removeAll();
        Students.removeAll();
        new APICall<ArrayList<DBRequest>>(main) {
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"EMGroup",Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                groupsMap.clear();
                groups.clear();
                Groups.removeAll();
                try {
                    for (DBRequest dd : oo) {
                        EMGroup group = (EMGroup)dd.get(main.gson);
                        Groups.add(group.getName());
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
        Students.removeAll();
        cGroup=null;
        if (groups.size()==0)
            return;
        cGroup = groups.get(Groups.getSelectedIndex());
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken,"EMGroup",cGroup.getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try{
                    cGroup = (EMGroup)oo.get(main.gson);
                    Students.removeAll();
                    for(EMStudent student : cGroup.getStudents())
                        Students.add(student.getUser().getTitle());
                    //refreshStudentFull();
                    }catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитаны данные группы "+cGroup.getName());
                        }
                }
            };
        }

    private void refreshSelectedDiscipline(){
        refreshSelectedDiscipline(true);
        }

    private void refreshSelectedDiscipline(boolean withPos){
        if (withPos)
            savePos();
        Themes.removeAll();
        Tasks.removeAll();
        cDiscipline=null;
        if (disciplines.size()==0)
            return;
        cDiscipline = disciplines.get(Disciplines.getSelectedIndex());
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
                Themes.removeAll();
                for(EMTheme theme : cDiscipline.getThemes())
                    Themes.add(theme.getName());
                refreshSelectedTheme(withPos);
                refreshRules(withPos);
                refreshExams(withPos);
                refreshTikets();
                ExamsForGroupList.removeAll();
                for(EntityLink<EMGroup> group : cDiscipline.getGroups()){
                    ExamsForGroupList.add(group.getRef().getName());
                    }
                ExamsForGroupList.removeAll();
                for(EntityLink<EMGroup> group :  cDiscipline.getGroups())
                    ExamsForGroupList.add(group.getRef().getName());
                }
            };
        }

    private void refreshExams(boolean withPos){
        Exams.removeAll();
        if (cDiscipline==null)
            return;
        for(EMExam exam : cDiscipline.getExamens()){
            EMExamRule rule = cDiscipline.getRules().getById(exam.getRule().getOid());
            Exams.add((exam.getName().length()==0 ?  "..." : exam.getName())+" "+(rule==null ? "???" : rule.getName()));
            }
        if (withPos)
            Exams.select(examIdx);
        refreshSelectedExam(withPos);
        }

    private void refreshSelectedExam(boolean withPos){
        cExam=null;
        ExamGroups.removeAll();
        if (cDiscipline.getExamens().size()==0)
            return;
        cExam = cDiscipline.getExamens().get(Exams.getSelectedIndex());
        ExamName.setText(""+cExam.getName());
        ExamGroups.removeAll();
        for(EntityLink id : cExam.getGroups()){
            EMGroup group = groupsMap.get(id.getOid());
            if (group==null){
                System.out.println("Не найдена группа id="+id);
                continue;
                }
            ExamGroups.add(group.getName());
            }
        refreshTakings(withPos);
        }

    private void refreshTakings(boolean withPos){
        Takings.removeAll();
        cTakings.clear();
        for(EMExamTaking taking : cDiscipline.getTakings()){
            if (taking.getExam().getOid()!=cExam.getOid())
                continue;
            cTakings.add(taking);
            Takings.add(taking.getTitle());
            }
        if (withPos)
            Takings.select(takingIdx);
        refreshSelectedTaking();
        }

    private void  refreshSelectedTaking(){
        TakingState.select(0);
        cTaking =null;
        if (cTakings.size()==0)
            return;
        cTaking = cTakings.get(Takings.getSelectedIndex());
        TakingName.setText(cTaking.getName());
        if (cTaking.getStartTime().timeInMS()==0){
            TakingData.setText("---");
            TakingEndTime.setText("---");
            }
        else{
            OwnDateTime date1 = cTaking.getStartTime();
            OwnDateTime date2 = new OwnDateTime(cTaking.getStartTime().timeInMS()+cTaking.getDuration()*60*1000);
            TakingData.setText(date1.dateToString());
            TakingStartTime.setText(date1.timeToString());
            TakingEndTime.setText(date2.timeToString());
            }
        TakingDuration.setText(""+cTaking.getDuration());
        int state = cTaking.getState();
        TakingForGroup.setSelected(cTaking.isOneGroup());
        TakingGroup.setText(!cTaking.isOneGroup() ? "" : groupsMap.get(cTaking.getGroup().getOid()).getName());
        cTakingState = choiceStateSet(takingStateList,TakingState,state);
        }

    private ConstValue choiceStateSet(ArrayList<ConstValue> list, Choice choice, int state) {
        ConstValue out = null;
        choice.select(0);
        for (int i = 0; i < list.size(); i++) {
            ConstValue cc = list.get(i);
            if (cc.value() == state) {
                choice.select(i + 1);
                return cc;
                }
            }
        return null;
        }

    private void choiceStateCreate(ArrayList<ConstValue> list, Choice choice){
        choice.removeAll();
        choice.add("...");
        for(ConstValue cc : list)
            choice.add(cc.title());
        choice.select(0);
        }


    private void refreshSelectedTheme(boolean withPos){
        if (withPos)
            Themes.select(themeIdx);
        Tasks.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Themes.getSelectedIndex());
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken, "EMTheme",cTheme.getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try {
                    cTheme = (EMTheme) oo.get(main.gson);
                    //cTheme.getTasks().sort(new Comparator<EMTask>() {
                    //    @Override
                    //    public int compare(EMTask o1, EMTask o2) {              // Сортировать по id (в порядке поступления)
                    //        return o1.getOwnRating() - o2.getOid() > 0 ? 1 : -1;
                    //        }
                    //    });
                    sortedTasks.clear();
                    int iq = 1, it = 1;
                    for (EMTask task : cTheme.getTasks())
                        if (task.getType() == Values.TaskQuestion){
                            Tasks.add("Вопрос " + iq++ + " " + task.getName());
                            sortedTasks.add(task);
                            }
                    for (EMTask task : cTheme.getTasks())
                        if (task.getType() == Values.TaskExercise){
                            Tasks.add("Задача " + it++ + " " + task.getName());
                            sortedTasks.add(task);
                            }
                    if (withPos)
                        Tasks.select(taskIdx);
                    refreshSelectedTask();
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитана тем: "+cTheme.getName());
                        return;
                        }
                    }
                };
            }

    private void refreshSelectedTaskForce(){
        TaskText.setText("");
        if (cTheme.getTasks().size()==0)
            return;
        refresh=true;
        TaskSaveText.setEnabled(false);
        cTaskNum = Tasks.getSelectedIndex();
        cTask = sortedTasks.get(cTaskNum);
        boolean isTask = cTask.getType()== Values.TaskExercise;
        TaskType.setSelected(isTask);
        TaskTypeLabel.setText(isTask ? "Задача" : "Вопрос (тест)");
        TaskText.setText(UtilsEM.formatSize(cTask.getTaskText(),60));
        boolean bb = cTask.getArtifact().getOid()!=0;
        TaskArtifactView.setEnabled(bb);
        TaskArtifactDownLoad.setEnabled(bb);
        if (bb){
            Artifact artifact = cTask.getArtifact().getRef();
            TaskText.append("\n"+artifact.getOriginalName());
            }
        refresh=false;
        }

    private void refreshSelectedTask(){
        if (!taskTextChanged){
            refreshSelectedTaskForce();
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
                    refreshSelectedTaskForce();
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
        Disciplines = new java.awt.Choice();
        Themes = new java.awt.Choice();
        RefreshDisciplines = new javax.swing.JButton();
        DisciplineImport = new javax.swing.JButton();
        TaskRemove = new javax.swing.JButton();
        TaskAdd = new javax.swing.JButton();
        DisciplineAdd = new javax.swing.JButton();
        DisciplineRemove = new javax.swing.JButton();
        ThemeAdd = new javax.swing.JButton();
        ThemeRemove = new javax.swing.JButton();
        Tasks = new java.awt.Choice();
        TaskArtifactView = new javax.swing.JButton();
        TaskArtifactUpload = new javax.swing.JButton();
        TaskArtifactDownLoad = new javax.swing.JButton();
        TaskSaveText = new javax.swing.JButton();
        DisciplineEdit = new javax.swing.JButton();
        ThemeEdit = new javax.swing.JButton();
        FullTrace = new javax.swing.JCheckBox();
        DisciplineSaveImport = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Groups = new java.awt.Choice();
        Students = new java.awt.Choice();
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
        Rules = new java.awt.Choice();
        jLabel9 = new javax.swing.JLabel();
        RuleExceciseForOne = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        RuleOwnRating = new javax.swing.JTextField();
        RuleName = new javax.swing.JTextField();
        RuleThemeRemove = new javax.swing.JButton();
        RuleAdd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        RuleThemes = new java.awt.Choice();
        RuleThemeAdd = new javax.swing.JButton();
        RuleDelete = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        RuleExcerciseSum = new javax.swing.JTextField();
        RuleThemeAddAll = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        Exams = new java.awt.Choice();
        ExamGroups = new java.awt.Choice();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        TakingForGroup = new javax.swing.JCheckBox();
        Takings = new java.awt.Choice();
        TakingData = new javax.swing.JTextField();
        TakingEndTime = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        ExamGroupAdd = new javax.swing.JButton();
        ExamGroupRemove = new javax.swing.JButton();
        ExamAdd = new javax.swing.JButton();
        ExamRemove = new javax.swing.JButton();
        TakingAdd = new javax.swing.JButton();
        TakingRemove = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        TakingDuration = new javax.swing.JTextField();
        TaskType = new javax.swing.JCheckBox();
        TakingState = new java.awt.Choice();
        RuleQuestionForOne = new javax.swing.JTextField();
        RuleQuestionSum = new javax.swing.JTextField();
        RuleDuration = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        RuleMinRating = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        TakingName = new javax.swing.JTextField();
        ExamName = new javax.swing.JTextField();
        TakingStateNext = new javax.swing.JButton();
        TakingStatePrev = new javax.swing.JButton();
        TakingGroup = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        TakingStartTime = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        TicketStudentList = new java.awt.Choice();
        Состояние = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        Вопросы1 = new javax.swing.JLabel();
        TicketQuestionRating = new javax.swing.JTextField();
        Задачи1 = new javax.swing.JLabel();
        TicketExcerciseRating = new javax.swing.JTextField();
        Answers = new java.awt.Choice();
        AnswerMessages = new java.awt.Choice();
        jLabel28 = new javax.swing.JLabel();
        TicketAnswerText = new java.awt.TextArea();
        AnswerArtifactView = new javax.swing.JButton();
        AnserArtifactUpload = new javax.swing.JButton();
        AnswerArtifactDownLoad = new javax.swing.JButton();
        TicketState = new java.awt.Choice();
        TicketStateNext = new javax.swing.JButton();
        TicketStatePrev = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        Состояние1 = new javax.swing.JLabel();
        AnswerState = new java.awt.Choice();
        AnswerStateNext = new javax.swing.JButton();
        AnswerStatePrev = new javax.swing.JButton();
        TicketSemesterRating = new javax.swing.JTextField();
        AnswerBall = new java.awt.Choice();
        jLabel29 = new javax.swing.JLabel();
        TicketExamOrTakingMode = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();

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

        Disciplines.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DisciplinesItemStateChanged(evt);
            }
        });
        add(Disciplines);
        Disciplines.setBounds(20, 40, 300, 20);

        Themes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ThemesItemStateChanged(evt);
            }
        });
        add(Themes);
        Themes.setBounds(20, 80, 300, 20);

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

        Tasks.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TasksItemStateChanged(evt);
            }
        });
        add(Tasks);
        Tasks.setBounds(20, 120, 300, 20);

        TaskArtifactView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/camera.png"))); // NOI18N
        TaskArtifactView.setBorderPainted(false);
        TaskArtifactView.setContentAreaFilled(false);
        TaskArtifactView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskArtifactViewActionPerformed(evt);
            }
        });
        add(TaskArtifactView);
        TaskArtifactView.setBounds(100, 380, 40, 30);

        TaskArtifactUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        TaskArtifactUpload.setBorderPainted(false);
        TaskArtifactUpload.setContentAreaFilled(false);
        TaskArtifactUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskArtifactUploadActionPerformed(evt);
            }
        });
        add(TaskArtifactUpload);
        TaskArtifactUpload.setBounds(20, 380, 40, 30);

        TaskArtifactDownLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        TaskArtifactDownLoad.setBorderPainted(false);
        TaskArtifactDownLoad.setContentAreaFilled(false);
        TaskArtifactDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskArtifactDownLoadActionPerformed(evt);
            }
        });
        add(TaskArtifactDownLoad);
        TaskArtifactDownLoad.setBounds(60, 380, 40, 30);

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

        jLabel5.setText("Допуск/Билет/Ответы");
        add(jLabel5);
        jLabel5.setBounds(460, 355, 150, 20);

        Groups.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GroupsItemStateChanged(evt);
            }
        });
        add(Groups);
        Groups.setBounds(490, 40, 240, 20);

        Students.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StudentsItemStateChanged(evt);
            }
        });
        add(Students);
        Students.setBounds(490, 80, 240, 20);

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

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator1);
        jSeparator1.setBounds(447, 120, 10, 550);
        add(ExamsForGroupList);
        ExamsForGroupList.setBounds(310, 380, 130, 20);

        jLabel7.setText("Задача: за одну - сумма ");
        add(jLabel7);
        jLabel7.setBounds(20, 530, 140, 20);

        jLabel8.setText("Экзамен для групп");
        add(jLabel8);
        jLabel8.setBounds(180, 380, 120, 20);

        Rules.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RulesItemStateChanged(evt);
            }
        });
        add(Rules);
        Rules.setBounds(20, 440, 230, 20);

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
        add(RuleThemes);
        RuleThemes.setBounds(110, 580, 280, 20);

        RuleThemeAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RuleThemeAdd.setBorderPainted(false);
        RuleThemeAdd.setContentAreaFilled(false);
        RuleThemeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeAddActionPerformed(evt);
            }
        });
        add(RuleThemeAdd);
        RuleThemeAdd.setBounds(445, 75, 30, 30);

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

        Exams.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ExamsItemStateChanged(evt);
            }
        });
        add(Exams);
        Exams.setBounds(460, 150, 190, 20);
        add(ExamGroups);
        ExamGroups.setBounds(740, 150, 100, 20);

        jLabel15.setText("Продолж. (мин)");
        add(jLabel15);
        jLabel15.setBounds(710, 280, 100, 16);

        jLabel16.setText("Группы");
        add(jLabel16);
        jLabel16.setBounds(740, 130, 80, 16);

        TakingForGroup.setText("Одна группа");
        TakingForGroup.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TakingForGroupItemStateChanged(evt);
            }
        });
        TakingForGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingForGroupActionPerformed(evt);
            }
        });
        add(TakingForGroup);
        TakingForGroup.setBounds(740, 210, 110, 20);

        Takings.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TakingsItemStateChanged(evt);
            }
        });
        add(Takings);
        Takings.setBounds(460, 220, 240, 20);

        TakingData.setEnabled(false);
        TakingData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TakingDataMouseClicked(evt);
            }
        });
        add(TakingData);
        TakingData.setBounds(710, 250, 100, 25);

        TakingEndTime.setEnabled(false);
        add(TakingEndTime);
        TakingEndTime.setBounds(880, 250, 50, 25);

        jLabel18.setText("Прием экзамена");
        add(jLabel18);
        jLabel18.setBounds(460, 200, 140, 16);

        jLabel19.setText("Состояние приема");
        add(jLabel19);
        jLabel19.setBounds(460, 280, 130, 16);

        jLabel20.setText("Дата");
        add(jLabel20);
        jLabel20.setBounds(710, 230, 70, 16);

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

        TakingAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        TakingAdd.setBorderPainted(false);
        TakingAdd.setContentAreaFilled(false);
        TakingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingAddActionPerformed(evt);
            }
        });
        add(TakingAdd);
        TakingAdd.setBounds(660, 180, 30, 30);

        TakingRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TakingRemove.setBorderPainted(false);
        TakingRemove.setContentAreaFilled(false);
        TakingRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingRemoveActionPerformed(evt);
            }
        });
        add(TakingRemove);
        TakingRemove.setBounds(700, 180, 30, 30);

        jLabel21.setText("Оконч.");
        add(jLabel21);
        jLabel21.setBounds(880, 230, 60, 16);

        TakingDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TakingDurationKeyPressed(evt);
            }
        });
        add(TakingDuration);
        TakingDuration.setBounds(820, 280, 50, 25);

        TaskType.setText("Задача(1) / Вопрос(0)");
        TaskType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TaskTypeItemStateChanged(evt);
            }
        });
        add(TaskType);
        TaskType.setBounds(180, 100, 160, 20);

        TakingState.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TakingStateItemStateChanged(evt);
            }
        });
        add(TakingState);
        TakingState.setBounds(460, 300, 160, 20);

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

        RuleDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleDurationKeyPressed(evt);
            }
        });
        add(RuleDuration);
        RuleDuration.setBounds(390, 470, 50, 25);

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

        TakingName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TakingNameKeyPressed(evt);
            }
        });
        add(TakingName);
        TakingName.setBounds(460, 250, 240, 25);

        ExamName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ExamNameKeyPressed(evt);
            }
        });
        add(ExamName);
        ExamName.setBounds(460, 175, 190, 25);

        TakingStateNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/right.PNG"))); // NOI18N
        TakingStateNext.setBorderPainted(false);
        TakingStateNext.setContentAreaFilled(false);
        TakingStateNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingStateNextActionPerformed(evt);
            }
        });
        add(TakingStateNext);
        TakingStateNext.setBounds(670, 290, 30, 30);

        TakingStatePrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/left.PNG"))); // NOI18N
        TakingStatePrev.setBorderPainted(false);
        TakingStatePrev.setContentAreaFilled(false);
        TakingStatePrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingStatePrevActionPerformed(evt);
            }
        });
        add(TakingStatePrev);
        TakingStatePrev.setBounds(630, 290, 30, 30);

        TakingGroup.setEnabled(false);
        add(TakingGroup);
        TakingGroup.setBounds(850, 200, 70, 25);

        jLabel24.setText("Начало");
        add(jLabel24);
        jLabel24.setBounds(820, 230, 70, 16);

        TakingStartTime.setEnabled(false);
        add(TakingStartTime);
        TakingStartTime.setBounds(820, 250, 50, 25);
        add(jSeparator2);
        jSeparator2.setBounds(450, 120, 470, 10);

        jLabel6.setText("Студент");
        add(jLabel6);
        jLabel6.setBounds(490, 60, 70, 16);
        add(TicketStudentList);
        TicketStudentList.setBounds(520, 375, 220, 20);

        Состояние.setText("Состояние сдачи");
        add(Состояние);
        Состояние.setBounds(630, 400, 160, 16);

        jLabel25.setText("Сообщения");
        add(jLabel25);
        jLabel25.setBounds(450, 520, 70, 16);

        jLabel26.setText("Семестр");
        add(jLabel26);
        jLabel26.setBounds(460, 400, 70, 16);

        Вопросы1.setText("Вопросы");
        add(Вопросы1);
        Вопросы1.setBounds(520, 400, 70, 16);

        TicketQuestionRating.setEnabled(false);
        add(TicketQuestionRating);
        TicketQuestionRating.setBounds(520, 420, 40, 25);

        Задачи1.setText("Задачи");
        add(Задачи1);
        Задачи1.setBounds(580, 400, 70, 16);

        TicketExcerciseRating.setEnabled(false);
        add(TicketExcerciseRating);
        TicketExcerciseRating.setBounds(580, 420, 40, 25);
        add(Answers);
        Answers.setBounds(520, 450, 350, 20);
        add(AnswerMessages);
        AnswerMessages.setBounds(520, 520, 350, 20);

        jLabel28.setText("Балл");
        add(jLabel28);
        jLabel28.setBounds(460, 490, 60, 16);
        add(TicketAnswerText);
        TicketAnswerText.setBounds(520, 550, 350, 110);

        AnswerArtifactView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/camera.png"))); // NOI18N
        AnswerArtifactView.setBorderPainted(false);
        AnswerArtifactView.setContentAreaFilled(false);
        AnswerArtifactView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerArtifactViewActionPerformed(evt);
            }
        });
        add(AnswerArtifactView);
        AnswerArtifactView.setBounds(880, 620, 40, 30);

        AnserArtifactUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        AnserArtifactUpload.setBorderPainted(false);
        AnserArtifactUpload.setContentAreaFilled(false);
        AnserArtifactUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnserArtifactUploadActionPerformed(evt);
            }
        });
        add(AnserArtifactUpload);
        AnserArtifactUpload.setBounds(880, 540, 40, 30);

        AnswerArtifactDownLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        AnswerArtifactDownLoad.setBorderPainted(false);
        AnswerArtifactDownLoad.setContentAreaFilled(false);
        AnswerArtifactDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerArtifactDownLoadActionPerformed(evt);
            }
        });
        add(AnswerArtifactDownLoad);
        AnswerArtifactDownLoad.setBounds(880, 580, 40, 30);
        add(TicketState);
        TicketState.setBounds(630, 420, 170, 20);

        TicketStateNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/right.PNG"))); // NOI18N
        TicketStateNext.setBorderPainted(false);
        TicketStateNext.setContentAreaFilled(false);
        TicketStateNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TicketStateNextActionPerformed(evt);
            }
        });
        add(TicketStateNext);
        TicketStateNext.setBounds(850, 410, 30, 30);

        TicketStatePrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/left.PNG"))); // NOI18N
        TicketStatePrev.setBorderPainted(false);
        TicketStatePrev.setContentAreaFilled(false);
        TicketStatePrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TicketStatePrevActionPerformed(evt);
            }
        });
        add(TicketStatePrev);
        TicketStatePrev.setBounds(810, 410, 30, 30);
        add(jSeparator3);
        jSeparator3.setBounds(580, 365, 350, 10);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator4);
        jSeparator4.setBounds(480, 10, 3, 110);

        Состояние1.setText("Состояние ответа");
        add(Состояние1);
        Состояние1.setBounds(660, 470, 140, 16);
        add(AnswerState);
        AnswerState.setBounds(660, 490, 170, 20);

        AnswerStateNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/right.PNG"))); // NOI18N
        AnswerStateNext.setBorderPainted(false);
        AnswerStateNext.setContentAreaFilled(false);
        AnswerStateNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerStateNextActionPerformed(evt);
            }
        });
        add(AnswerStateNext);
        AnswerStateNext.setBounds(880, 480, 30, 30);

        AnswerStatePrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/left.PNG"))); // NOI18N
        AnswerStatePrev.setBorderPainted(false);
        AnswerStatePrev.setContentAreaFilled(false);
        AnswerStatePrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerStatePrevActionPerformed(evt);
            }
        });
        add(AnswerStatePrev);
        AnswerStatePrev.setBounds(840, 480, 30, 30);

        TicketSemesterRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TicketSemesterRatingKeyPressed(evt);
            }
        });
        add(TicketSemesterRating);
        TicketSemesterRating.setBounds(460, 420, 50, 25);

        AnswerBall.setEnabled(false);
        add(AnswerBall);
        AnswerBall.setBounds(520, 490, 80, 20);

        jLabel29.setText("Ответы");
        add(jLabel29);
        jLabel29.setBounds(460, 450, 60, 16);

        TicketExamOrTakingMode.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TicketExamOrTakingMode.setText("Прием(1) / Экзамен(0)");
        TicketExamOrTakingMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TicketExamOrTakingModeItemStateChanged(evt);
            }
        });
        add(TicketExamOrTakingMode);
        TicketExamOrTakingMode.setBounds(750, 375, 170, 20);

        jLabel27.setText("Студент");
        add(jLabel27);
        jLabel27.setBounds(460, 375, 60, 16);
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
                Disciplines.setEnabled(false);
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
                        refreshSelectedDiscipline();
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
                        refreshSelectedDiscipline(Tasks.getItemCount()!=0);
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

    private void DisciplinesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DisciplinesItemStateChanged
        refreshSelectedDiscipline();
    }//GEN-LAST:event_DisciplinesItemStateChanged

    private void ThemesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ThemesItemStateChanged
        refreshSelectedTheme(false);
    }//GEN-LAST:event_ThemesItemStateChanged

    private void TasksItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TasksItemStateChanged
        refreshSelectedTask();
    }//GEN-LAST:event_TasksItemStateChanged

    private void TaskArtifactViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskArtifactViewActionPerformed

    }//GEN-LAST:event_TaskArtifactViewActionPerformed

    private void TaskArtifactUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskArtifactUploadActionPerformed
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
        }//GEN-LAST:event_TaskArtifactUploadActionPerformed
    private void TaskArtifactDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskArtifactDownLoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TaskArtifactDownLoadActionPerformed

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

    private void GroupsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GroupsItemStateChanged
        refreshGroupFull();
    }//GEN-LAST:event_GroupsItemStateChanged

    private void StudentsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StudentsItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentsItemStateChanged

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
            if (evt!=null)
                main.viewUpdate(evt,true);
            popup("Регламент обновлен");
            savePos();
            refreshSelectedDiscipline();
            } catch (UniException ee){
                System.out.println(ee.toString());
                if (evt!=null)
                    main.viewUpdate(evt,false);
                }
        }

    private void takingStartTimeUpdate(long timeInMS){
        cTaking.setStartTime(new OwnDateTime(timeInMS));
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken, new DBRequest(cTaking,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                popup("Время сдачи изменено");
                refreshSelectedTaking();
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
        long oid = cRule.getThemes().get(RuleThemes.getSelectedIndex()).getOid();
        EMTheme theme = cDiscipline.getThemes().getById(oid);
        new OK(200, 200, "Удалить тему: " + shortString(theme.getName(), 20), new I_Button() {
            @Override
            public void onPush() {
                int idx = RuleThemes.getSelectedIndex();
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
                        refreshSelectedDiscipline(Rules.getItemCount()!=0);
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
                        refreshSelectedDiscipline();
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

    private void RulesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RulesItemStateChanged
        refreshSelectedRule();
    }//GEN-LAST:event_RulesItemStateChanged

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

    private boolean isGroupInExam(){
        for(EntityLink<EMGroup> group : cDiscipline.getGroups())
            if (group.getOid()==cGroup.getOid())
                return true;
        return false;
        }

    private void ExamGroupAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamGroupAddActionPerformed
        if (cGroup==null || cExam==null)
            return;
        if (isGroupInExam()){
            popup("Группа "+cGroup.getName()+" уже в экзамене");
            return;
            }
        new OK(200, 200, "Добавить в экзамен группу " + cGroup.getName(), new I_Button() {
            @Override
            public void onPush() {
                cExam.getGroups().add(cGroup.getOid());
                new APICall<JInt>(main) {
                    @Override
                    public Call<JInt> apiFun() {
                        return ((EMClient)main).service2.addGroupToExam(main.debugToken, cExam.getOid(),cGroup.getOid());
                        }
                    @Override
                    public void onSucess(JInt oo) {
                        popup("Добавлена группа "+cGroup.getName()+": "+oo.getValue()+" студентов");
                        refreshSelectedDiscipline();
                        }
                    };
                /* - старая версия без EMTicket
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.updateEntity(main.debugToken, new DBRequest(cExam,main.gson));
                        }
                    @Override
                    public void onSucess(JEmpty oo) {
                        cDiscipline.getGroups().add(cGroup.getOid());
                        disciplineUpdate();
                        popup("Группа "+cGroup.getName()+" добавлена");
                        }
                    };
                 */
                }
            });
    }//GEN-LAST:event_ExamGroupAddActionPerformed

    private void ExamGroupRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamGroupRemoveActionPerformed
        if (cGroup==null || cExam==null)
            return;
        long groupId = cExam.getGroups().get(ExamGroups.getSelectedIndex()).getOid();
        final EMGroup group = groupsMap.get(groupId);
        new OK(200, 200, "Убрать из экзамена группу " + group.getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JInt>(main) {
                    @Override
                    public Call<JInt> apiFun() {
                        return ((EMClient)main).service2.removeGroupFromExam(main.debugToken, cExam.getOid(),group.getOid());
                        }
                    @Override
                    public void onSucess(JInt oo) {
                        popup("Удалена группа "+group.getName()+": "+oo.getValue()+" студентов");
                        refreshSelectedDiscipline();
                        }
                    };
                /* - старая версия без EMTicket
                cExam.getGroups().remove(ExamGroups.getSelectedIndex());
                new APICall<JEmpty>(main) {
                    @Override
                    public Call<JEmpty> apiFun() {
                        return main.service.updateEntity(main.debugToken,new DBRequest(cExam,main.gson));
                        }
                    @Override
                    public void onSucess(JEmpty oo) {
                        cDiscipline.getGroups().removeById(group.getOid());
                        disciplineUpdate();
                        popup("Группа "+group.getName()+" удалена");
                        }
                    };
                    */
                }
            });

    }//GEN-LAST:event_ExamGroupRemoveActionPerformed

    private void disciplineUpdate(){
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken,new DBRequest(cDiscipline,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                refreshSelectedDiscipline();
                }
            };
        }

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
                        refreshSelectedDiscipline(Exams.getItemCount()!=0);
                        }
                    };
                }
            });
    }//GEN-LAST:event_ExamAddActionPerformed

    private void ExamRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExamRemoveActionPerformed
        if (cExam==null)
            return;
        new OK(200, 200, "Удалить экзамен: " + Exams.getItem(Exams.getSelectedIndex()), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"EMExam",cExam.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        for(EntityLink<EMGroup> group : cExam.getGroups()){
                            cDiscipline.getGroups().removeById(group.getOid());
                            }
                        cDiscipline.getExamens().removeById(cExam.getOid());
                        disciplineUpdate();
                        popup("Экзамен удален");
                    }
                };
            }
        });
    }//GEN-LAST:event_ExamRemoveActionPerformed

    private void TakingAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingAddActionPerformed
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
                        refreshSelectedDiscipline(Takings.getItemCount()!=0);
                        }
                    };
                }
            });
    }//GEN-LAST:event_TakingAddActionPerformed

    private void TakingRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingRemoveActionPerformed
        if (cTaking ==null)
            return;
        new OK(200, 200, "Удалить прием: " + cTaking.getTitle(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"EMTacking", cTaking.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        cDiscipline.getTakings().removeById(cTaking.getOid());
                        disciplineUpdate();
                        popup("Прием удален");
                    }
                };
            }
        });
    }//GEN-LAST:event_TakingRemoveActionPerformed

    private void TakingDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TakingDataMouseClicked
        if (evt.getClickCount()<2)
            return;
        if (cTaking ==null)
            return;
        new CalendarView("Начало экзамена", new I_CalendarTime() {
            @Override
            public void onSelect(OwnDateTime time) {
                cTaking.setStartTime(time);
                takingUpdate();
                }
            });
    }//GEN-LAST:event_TakingDataMouseClicked

    private void ExamsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ExamsItemStateChanged
        refreshSelectedExam(false);
        if (!TicketExamOrTakingMode.isSelected())
            refreshTikets();
    }//GEN-LAST:event_ExamsItemStateChanged

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

    private void takingUpdate(){
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken, new DBRequest(cTaking,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                savePos();
                popup("Прием экзамена обновлен");
                refreshSelectedDiscipline();
                }
            };
        }

    private void TakingStateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TakingStateItemStateChanged
        int idx = TakingState.getSelectedIndex();
        if (idx==0)
            return;
        if (cTaking ==null)
            return;
        new OK(200, 200, "Смена состояния: " + (cTakingState == null ? "" : cTakingState.title()) + "->" + takingStateList.get(idx - 1).title(), new I_Button() {
            @Override
            public void onPush() {
                cTaking.setState(takingStateList.get(idx-1).value());
                takingUpdate();
            }
        });
    }//GEN-LAST:event_TakingStateItemStateChanged

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

    private void RuleDurationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleDurationKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setExamDuration(Integer.parseInt(RuleDuration.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleDurationKeyPressed

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

    private void TakingsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TakingsItemStateChanged
        refreshSelectedTaking();
        if (TicketExamOrTakingMode.isSelected())
            refreshTikets();
    }//GEN-LAST:event_TakingsItemStateChanged

    private void refreshTikets(){
        if (TicketExamOrTakingMode.isSelected()){
            if (cTaking==null)
                return;
            new APICall<EMExamTaking>(main) {
                @Override
                public Call<EMExamTaking> apiFun() {
                    return ((EMClient)main).service2.getTicketsForTaking(main.debugToken, cTaking.getOid());
                    }
                @Override
                public void onSucess(EMExamTaking oo) {
                    ticketsForTaking = oo;
                    createTicketStudentList(ticketsForTaking.getTickets());
                    refreshSelectedTicket();
                    }
                };
            }
        else{
            if (cExam==null)
                return;
            new APICall<EMExam>(main) {
                @Override
                public Call<EMExam> apiFun() {
                    return ((EMClient)main).service2.getTicketsForExam(main.debugToken, cExam.getOid());
                }
                @Override
                public void onSucess(EMExam oo) {
                    ticketsForExam = oo;
                    createTicketStudentList(ticketsForExam.getTickets());
                    refreshSelectedTicket();
                    }
                };
            }

        }

    private void createTicketStudentList(EntityRefList<EMTicket> tickets){
        TicketStudentList.removeAll();
        for(EMTicket ticket : tickets)
            TicketStudentList.add(ticket.getStudent().getRef().getUser().getTitle());
        refreshSelectedTicket();
        }


    public void refreshSelectedTicket(){
        if (tickets.size()==0){
            TicketExcerciseRating.setText("");
            TicketSemesterRating.setText("");
            TicketQuestionRating.setText("");
            TicketState.select(0);
            TicketAnswerText.setText("");
            Answers.removeAll();
            AnswerMessages.removeAll();
            return;
            }
        cTicket = tickets.get(TicketStudentList.getSelectedIndex());
        TicketExcerciseRating.setText(""+cTicket.getExcerciceRating());
        TicketSemesterRating.setText(""+cTicket.getSemesterRating());
        TicketQuestionRating.setText(""+cTicket.getQuestionRating());
        choiceStateSet(ticketStateList,TicketState,cTicket.getState());
        }

    private void examUpdate(){
        if (cExam==null) return;
        new APICall<JEmpty>(main) {
            @Override
            public Call<JEmpty> apiFun() {
                return main.service.updateEntity(main.debugToken, new DBRequest(cExam,main.gson));
                }
            @Override
            public void onSucess(JEmpty oo) {
                savePos();
                refreshSelectedDiscipline();
                }
            };       
        }

    private void ExamNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ExamNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cExam==null) return;
        cExam.setName(ExamName.getText());
        examUpdate();
        main.viewUpdate(evt,true);
    }//GEN-LAST:event_ExamNameKeyPressed

    private void TakingNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TakingNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cTaking ==null) return;
        cTaking.setName(TakingName.getText());
        takingUpdate();
        main.viewUpdate(evt,true);
    }//GEN-LAST:event_TakingNameKeyPressed

    private void TakingStateNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingStateNextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TakingStateNextActionPerformed

    private void TakingStatePrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingStatePrevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TakingStatePrevActionPerformed

    private void TakingDurationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TakingDurationKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cTaking==null) return;
        try {
            cTaking.setDuration(Integer.parseInt(TakingDuration.getText()));
            takingUpdate();
            main.viewUpdate(evt,true);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_TakingDurationKeyPressed

    private void TakingForGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TakingForGroupItemStateChanged
        if (cTaking==null)
            return;
        if (TakingForGroup.isSelected() && ExamGroups.getItemCount()==0){
            popup("Нет групп в списке к экзамену");
            TakingForGroup.setSelected(false);
            return;
            }
        cTaking.setOneGroup(TakingForGroup.isSelected());
        if (cTaking.isOneGroup()){
            long oid = cExam.getGroups().get(ExamGroups.getSelectedIndex()).getOid();
            cTaking.getGroup().setOid(oid);
            }
        takingUpdate();
    }//GEN-LAST:event_TakingForGroupItemStateChanged

    private void TakingForGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingForGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TakingForGroupActionPerformed

    private void AnswerArtifactViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerArtifactViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnswerArtifactViewActionPerformed

    private void AnserArtifactUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnserArtifactUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnserArtifactUploadActionPerformed

    private void AnswerArtifactDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerArtifactDownLoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnswerArtifactDownLoadActionPerformed

    private void TicketStateNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TicketStateNextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TicketStateNextActionPerformed

    private void TicketStatePrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TicketStatePrevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TicketStatePrevActionPerformed

    private void AnswerStateNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerStateNextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnswerStateNextActionPerformed

    private void AnswerStatePrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerStatePrevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnswerStatePrevActionPerformed

    private void TicketSemesterRatingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TicketSemesterRatingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TicketSemesterRatingKeyPressed

    private void TicketExamOrTakingModeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TicketExamOrTakingModeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_TicketExamOrTakingModeItemStateChanged

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
                savePos();
                refreshSelectedDiscipline();
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
    private javax.swing.JButton AnserArtifactUpload;
    private javax.swing.JButton AnswerArtifactDownLoad;
    private javax.swing.JButton AnswerArtifactView;
    private java.awt.Choice AnswerBall;
    private java.awt.Choice AnswerMessages;
    private java.awt.Choice AnswerState;
    private javax.swing.JButton AnswerStateNext;
    private javax.swing.JButton AnswerStatePrev;
    private java.awt.Choice Answers;
    private javax.swing.JButton DisciplineAdd;
    private javax.swing.JButton DisciplineEdit;
    private javax.swing.JButton DisciplineImport;
    private javax.swing.JButton DisciplineRemove;
    private javax.swing.JButton DisciplineSaveImport;
    private java.awt.Choice Disciplines;
    private javax.swing.JButton ExamAdd;
    private javax.swing.JButton ExamGroupAdd;
    private javax.swing.JButton ExamGroupRemove;
    private java.awt.Choice ExamGroups;
    private javax.swing.JTextField ExamName;
    private javax.swing.JButton ExamRemove;
    private java.awt.Choice Exams;
    private java.awt.Choice ExamsForGroupList;
    private javax.swing.JCheckBox FullTrace;
    private javax.swing.JButton GroupAdd;
    private javax.swing.JButton GroupEdit;
    private javax.swing.JButton GroupRemove;
    private java.awt.Choice Groups;
    private javax.swing.JButton GroupsImport;
    private javax.swing.JButton RefreshDisciplines;
    private javax.swing.JButton RefreshGroups;
    private javax.swing.JButton RuleAdd;
    private javax.swing.JButton RuleDelete;
    private javax.swing.JTextField RuleDuration;
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
    private java.awt.Choice RuleThemes;
    private java.awt.Choice Rules;
    private javax.swing.JButton StudentAdd;
    private javax.swing.JButton StudentEdit;
    private javax.swing.JButton StudentRemove;
    private java.awt.Choice Students;
    private javax.swing.JButton TakingAdd;
    private javax.swing.JTextField TakingData;
    private javax.swing.JTextField TakingDuration;
    private javax.swing.JTextField TakingEndTime;
    private javax.swing.JCheckBox TakingForGroup;
    private javax.swing.JTextField TakingGroup;
    private javax.swing.JTextField TakingName;
    private javax.swing.JButton TakingRemove;
    private javax.swing.JTextField TakingStartTime;
    private java.awt.Choice TakingState;
    private javax.swing.JButton TakingStateNext;
    private javax.swing.JButton TakingStatePrev;
    private java.awt.Choice Takings;
    private javax.swing.JButton TaskAdd;
    private javax.swing.JButton TaskArtifactDownLoad;
    private javax.swing.JButton TaskArtifactUpload;
    private javax.swing.JButton TaskArtifactView;
    private javax.swing.JButton TaskRemove;
    private javax.swing.JButton TaskSaveText;
    private java.awt.TextArea TaskText;
    private javax.swing.JCheckBox TaskType;
    private javax.swing.JLabel TaskTypeLabel;
    private java.awt.Choice Tasks;
    private javax.swing.JButton ThemeAdd;
    private javax.swing.JButton ThemeEdit;
    private javax.swing.JButton ThemeRemove;
    private java.awt.Choice Themes;
    private java.awt.TextArea TicketAnswerText;
    private javax.swing.JCheckBox TicketExamOrTakingMode;
    private javax.swing.JTextField TicketExcerciseRating;
    private javax.swing.JTextField TicketQuestionRating;
    private javax.swing.JTextField TicketSemesterRating;
    private java.awt.Choice TicketState;
    private javax.swing.JButton TicketStateNext;
    private javax.swing.JButton TicketStatePrev;
    private java.awt.Choice TicketStudentList;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel Вопросы1;
    private javax.swing.JLabel Задачи1;
    private javax.swing.JLabel Состояние;
    private javax.swing.JLabel Состояние1;
    // End of variables declaration//GEN-END:variables
}
