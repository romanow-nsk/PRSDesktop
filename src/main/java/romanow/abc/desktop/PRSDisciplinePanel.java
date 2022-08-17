/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import com.sun.org.apache.regexp.internal.RE;
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
public class PRSDisciplinePanel extends BasePanel{
    private ArrayList<SADiscipline> disciplines = new ArrayList<>();    // Список дисциплин
    private ArrayList<SASemesterRule> semesterRules = new ArrayList<>();// Список регламентов семестра
    @Getter private SASemesterRule cSemesterRule = null;                // Текущий регламент
    @Getter private SADiscipline cDiscipline = null;                    // Текущая дисциплина
    @Getter private SATheme cTheme = null;                              // Текущая тема
    @Getter private SATask cTask=null;                                  // Текущая задача/тест
    @Getter private SAExamRule cRule=null;                              // Текущий регламент сдачи
    @Getter private SAEduUnit cEduUnit=null;                            // Текущая уч.единица
    private ArrayList<SATask> sortedTasks =  new ArrayList<>();         // Отсортированы - вопрос/задача
    //----------------------------------------------------------------------------------------
    private ArrayList<SATheme> ruleThemes = new ArrayList<>();          // Темы регламента сдачи
    private HashMap<Long, SATheme> ruleThemesMap = new HashMap<>();
    private ChoiceConsts eduUnitTypes = null;
    //---------------------------------------------------------------------------------------
    private int themeIdx = -1;
    private int taskIdx = -1;
    private int ruleIdx = -1;
    private int semesterRuleIdx = -1;
    private int eduUnutIdx = -1;
    //----------------------------------------------------------------------------------------
    private int cTaskNum=0;
    private OWTDiscipline owtImportData = null;
    private boolean refresh=false;                                      // Признак обновления для событий  CheckBox
    private boolean taskTextChanged=false;
    public PRSDisciplinePanel() {
        initComponents();
        }
    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        TaskArtifactView.setEnabled(false);
        TaskArtifactDownLoad.setEnabled(false);
        DisciplineSaveImport.setEnabled(false);
        eduUnitTypes = new ChoiceConsts(EduUnitType, Values.constMap().getGroupList("EduUnit"), new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                }
            });
        refreshAll();
        }

    private void savePos(){
        themeIdx = Themes.getSelectedIndex();
        taskIdx = Tasks.getSelectedIndex();
        ruleIdx = Rules.getSelectedIndex();
        semesterRuleIdx = SemesterRules.getSelectedIndex();
        eduUnutIdx = EduUnits.getSelectedIndex();
        }

    public void refreshAll(){
        refreshDisciplineList();
        refreshSemesterRuleList();
        }

    public void refreshSemesterRuleList(){
        SemesterRules.removeAll();
        new APICall<ArrayList<DBRequest>>(main){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"SASemesterRule", Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                semesterRules.clear();
                SemesterRules.removeAll();
                try {
                    for(DBRequest request : oo){
                        SASemesterRule rule = (SASemesterRule)  request.get(main.gson);
                        SemesterRules.add(rule.getName());
                        semesterRules.add(rule);
                    }
                } catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Ошибка чтения регламентов семестра");
                    }
                refreshSelectedSemesterRule(false);
            }
        };
    }


    public void refreshDisciplineList(){
        Disciplines.removeAll();
        Themes.removeAll();
        TaskText.setText("");
        new APICall<ArrayList<DBRequest>>(main){
            @Override
            public Call<ArrayList<DBRequest>> apiFun() {
                return main.service.getEntityList(main.debugToken,"SADiscipline", Values.GetAllModeActual,0);
                }
            @Override
            public void onSucess(ArrayList<DBRequest> oo) {
                disciplines.clear();
                Disciplines.removeAll();
                try {
                for(DBRequest request : oo){
                    SADiscipline discipline = (SADiscipline) request.get(main.gson);
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

    public void refreshEduUnits(boolean withPos){
        EduUnits.removeAll();
        cDiscipline.getUnits().sortByKeyNum();
        caclEduUnitsSum();
        for(SAEduUnit rule : cDiscipline.getUnits()){
            EduUnits.add(rule.getName());
            }
        if (withPos && eduUnutIdx!=-1)
            EduUnits.select(eduUnutIdx);
        refreshSelectedEduUnit();
        }


    public void refreshRules(boolean withPos){
        Rules.removeAll();
        for(SAExamRule rule : cDiscipline.getRules()){
            Rules.add(rule.getName());
            }
        if (withPos && ruleIdx!=-1)
            Rules.select(ruleIdx);
        refreshSelectedRule();
        }

    public void refreshSelectedEduUnit(){
        refresh = true;
        EduUnitName.setText("");
        ManualPointSet.setSelected(false);
        DeliveryWeek.setText("");
        BasePoint.setText("");
        cEduUnit=null;
        if (cDiscipline.getUnits().size()==0){
            refresh=false;
            return;
            }
        cEduUnit = cDiscipline.getUnits().get(EduUnits.getSelectedIndex());
        EduUnitName.setText(cEduUnit.getName());
        DeliveryWeek.setText(""+cEduUnit.getDeliveryWeek());
        BasePoint.setText(""+cEduUnit.getBasePoint());
        ManualPointSet.setSelected(cEduUnit.isManualPointSet());
        if (eduUnitTypes.selectByValue(cEduUnit.getUnitType())==null)
            System.out.println("Недопустимый тип учебной единицы: "+cEduUnit.getUnitType());
        refresh=false;
        }

    public void refreshSelectedRule(){
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
            SATheme theme  = cDiscipline.getThemes().getById(themeId.getOid());
            if (theme==null)
                System.out.println("Не найдена тема id="+themeId);
            else {
                ruleThemes.add(theme);
                RuleThemes.add(theme.getName());
                ruleThemesMap.put(theme.getOid(),theme);
                }
            }
        }


    public void refreshSelectedDiscipline(){
        refreshSelectedDiscipline(true);
        }

    public void refreshSelectedDiscipline(boolean withPos){
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
                return main.service.getEntity(main.debugToken,"SADiscipline",cDiscipline.getOid(),1);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try {
                    cDiscipline = (SADiscipline) oo.get(main.gson);
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Ошибка чтения дисциплины "+cDiscipline.getName());
                        return;
                        }
                cDiscipline.createMaps();
                Themes.removeAll();
                for(SATheme theme : cDiscipline.getThemes())
                    Themes.add(theme.getName());
                refreshSelectedTheme(withPos);
                refreshRules(withPos);
                refreshEduUnits(withPos);
                }
            };
        }



    public ConstValue selectChoiceByState(ArrayList<ConstValue> list, Choice choice, int state) {
        ConstValue out = null;
        choice.select(0);
        for (int i = 0; i < list.size(); i++) {
            ConstValue cc = list.get(i);
            if (cc.value() == state) {
                choice.select(i);
                return cc;
                }
            }
        return null;
        }

    public void choiceStateCreate(ArrayList<ConstValue> list, Choice choice){
        choice.removeAll();
        choice.add("...");
        for(ConstValue cc : list)
            choice.add(cc.title());
        choice.select(0);
        }


    public void refreshSelectedTheme(boolean withPos){
        if (withPos)
            Themes.select(themeIdx);
        Tasks.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Themes.getSelectedIndex());
        new APICall<DBRequest>(main) {
            @Override
            public Call<DBRequest> apiFun() {
                return main.service.getEntity(main.debugToken, "SATheme",cTheme.getOid(),2);
                }
            @Override
            public void onSucess(DBRequest oo) {
                try {
                    cTheme = (SATheme) oo.get(main.gson);
                    //cTheme.getTasks().sort(new Comparator<SATask>() {
                    //    @Override
                    //    public int compare(SATask o1, SATask o2) {              // Сортировать по id (в порядке поступления)
                    //        return o1.getOwnRating() - o2.getOid() > 0 ? 1 : -1;
                    //        }
                    //    });
                    sortedTasks.clear();
                    int iq = 1, it = 1;
                    for (SATask task : cTheme.getTasks())
                        if (task.getType() == Values.TaskQuestion){
                            Tasks.add("Вопрос " + iq++ + " " + task.getName());
                            sortedTasks.add(task);
                            }
                    for (SATask task : cTheme.getTasks())
                        if (task.getType() == Values.TaskExercise){
                            Tasks.add("Задача " + it++ + " " + task.getName());
                            sortedTasks.add(task);
                            }
                    if (withPos)
                        Tasks.select(taskIdx);
                    refreshSelectedTask();
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Не прочитана тема: "+shortString(cTheme.getName(),30));
                        return;
                        }
                    }
                };
            }

    public void refreshSelectedSemesterRule(boolean withPos){
        cSemesterRule = null;
        SemesterRule.setText("");
        SmstrDate.setText("");
        refresh = true;
        FineOverDate.setEnabled(false);
        FineOverSemester.setEnabled(false);
        FineOverIrregular.setEnabled(false);
        SemesterDuration.setText("");
        OverDatePercent.setText("");
        OverSemesterPercent.setText("");
        OverDateWeeks.setText("");
        QualProc.setText("");
        PointPerSkip.setText("");
        IrregulaFirstWeek.setText("");
        if (semesterRules.size()==0){
            refresh = false;
            return;
            }
        if (withPos)
            SemesterRules.select(semesterRuleIdx);
        cSemesterRule = semesterRules.get(SemesterRules.getSelectedIndex());
        SmstrDate.setText(cSemesterRule.getSmstrDate().dateToString());
        SemesterRule.setText(cSemesterRule.getName());
        boolean bb = cSemesterRule.isFineOverSemester();
        FineOverSemester.setSelected(bb);
        OverSemesterPercent.setEnabled(bb);
        if (bb) OverSemesterPercent.setText(""+cSemesterRule.getOverSemesterPercent());
        bb = cSemesterRule.isFineOverDate();
        FineOverDate.setSelected(bb);
        OverDatePercent.setEnabled(bb);
        OverDateWeeks.setEnabled(bb);
        if (bb) {
            OverDatePercent.setText(""+cSemesterRule.getOverDatePercent());
            OverDateWeeks.setText(""+cSemesterRule.getOverDateWeeks());
            }
        bb = cSemesterRule.isFineOverIrregular();
        FineOverIrregular.setSelected(bb);
        IrregulaFirstWeek.setEnabled(bb);
        if (bb) IrregulaFirstWeek.setText(""+cSemesterRule.getIrregulaFirstWeek());
        SemesterDuration.setText(""+cSemesterRule.getSemesterDuration());
        QualProc.setText(""+cSemesterRule.getQualProc());
        PointPerSkip.setText(""+cSemesterRule.getPointPerSkip());
        FineOverDate.setEnabled(true);
        FineOverSemester.setEnabled(true);
        FineOverIrregular.setEnabled(true);
        refresh = false;
        }

    public void refreshSelectedTaskForce(){
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
        TaskText.setText(UtilsPRS.formatSize(cTask.getTaskText(),65));
        boolean bb = cTask.getArtifact().getOid()!=0;
        TaskArtifactView.setEnabled(bb);
        TaskArtifactDownLoad.setEnabled(bb);
        refresh=false;
        }

    public void refreshSelectedTask(){
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
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
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
        TaskType = new javax.swing.JCheckBox();
        RuleQuestionForOne = new javax.swing.JTextField();
        RuleQuestionSum = new javax.swing.JTextField();
        RuleDuration = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        RuleMinRating = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        SemesterRules = new java.awt.Choice();
        jLabel1 = new javax.swing.JLabel();
        SemesterRuleAdd = new javax.swing.JButton();
        SemesterRuleRemove = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        SmstrDate = new javax.swing.JTextField();
        FineOverDate = new javax.swing.JCheckBox();
        SemesterDuration = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        PointPerSkip = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        OverDatePercent = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        SemesterRule = new javax.swing.JTextField();
        FineOverSemester = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        OverSemesterPercent = new javax.swing.JTextField();
        FineOverIrregular = new javax.swing.JCheckBox();
        IrregulaFirstWeek = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        OverDateWeeks = new javax.swing.JTextField();
        QualProc = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        EduUnits = new java.awt.Choice();
        EduUnitAdd = new javax.swing.JButton();
        EduUnitRemove = new javax.swing.JButton();
        EduUnitToFront = new javax.swing.JButton();
        EduUnitToEnd = new javax.swing.JButton();
        EduUnitName = new javax.swing.JTextField();
        ManualPointSet = new javax.swing.JCheckBox();
        PointSum = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        DeliveryWeek = new javax.swing.JTextField();
        EduUnitType = new java.awt.Choice();
        BasePoint = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        TaskTypeLabel.setText("Вопрос");
        add(TaskTypeLabel);
        TaskTypeLabel.setBounds(20, 120, 100, 16);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Предмет");
        add(jLabel2);
        jLabel2.setBounds(60, 10, 70, 16);

        jLabel3.setText("Тема");
        add(jLabel3);
        jLabel3.setBounds(20, 80, 70, 16);

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
        TaskText.setBounds(20, 200, 460, 220);

        Disciplines.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DisciplinesItemStateChanged(evt);
            }
        });
        add(Disciplines);
        Disciplines.setBounds(20, 45, 260, 20);

        Themes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ThemesItemStateChanged(evt);
            }
        });
        add(Themes);
        Themes.setBounds(20, 100, 300, 20);

        RefreshDisciplines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshDisciplines.setBorderPainted(false);
        RefreshDisciplines.setContentAreaFilled(false);
        RefreshDisciplines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshDisciplinesActionPerformed(evt);
            }
        });
        add(RefreshDisciplines);
        RefreshDisciplines.setBounds(20, 5, 30, 30);

        DisciplineImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        DisciplineImport.setBorderPainted(false);
        DisciplineImport.setContentAreaFilled(false);
        DisciplineImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineImportActionPerformed(evt);
            }
        });
        add(DisciplineImport);
        DisciplineImport.setBounds(410, 40, 30, 30);

        TaskRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TaskRemove.setBorderPainted(false);
        TaskRemove.setContentAreaFilled(false);
        TaskRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskRemoveActionPerformed(evt);
            }
        });
        add(TaskRemove);
        TaskRemove.setBounds(370, 130, 30, 30);

        TaskAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        TaskAdd.setBorderPainted(false);
        TaskAdd.setContentAreaFilled(false);
        TaskAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskAddActionPerformed(evt);
            }
        });
        add(TaskAdd);
        TaskAdd.setBounds(330, 130, 30, 30);

        DisciplineAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        DisciplineAdd.setBorderPainted(false);
        DisciplineAdd.setContentAreaFilled(false);
        DisciplineAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineAddActionPerformed(evt);
            }
        });
        add(DisciplineAdd);
        DisciplineAdd.setBounds(290, 40, 30, 30);

        DisciplineRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        DisciplineRemove.setBorderPainted(false);
        DisciplineRemove.setContentAreaFilled(false);
        DisciplineRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineRemoveActionPerformed(evt);
            }
        });
        add(DisciplineRemove);
        DisciplineRemove.setBounds(330, 40, 30, 30);

        ThemeAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        ThemeAdd.setBorderPainted(false);
        ThemeAdd.setContentAreaFilled(false);
        ThemeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeAddActionPerformed(evt);
            }
        });
        add(ThemeAdd);
        ThemeAdd.setBounds(330, 90, 30, 30);

        ThemeRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        ThemeRemove.setBorderPainted(false);
        ThemeRemove.setContentAreaFilled(false);
        ThemeRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeRemoveActionPerformed(evt);
            }
        });
        add(ThemeRemove);
        ThemeRemove.setBounds(370, 90, 30, 30);

        Tasks.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TasksItemStateChanged(evt);
            }
        });
        add(Tasks);
        Tasks.setBounds(20, 140, 300, 20);

        TaskArtifactView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/camera.png"))); // NOI18N
        TaskArtifactView.setBorderPainted(false);
        TaskArtifactView.setContentAreaFilled(false);
        TaskArtifactView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskArtifactViewActionPerformed(evt);
            }
        });
        add(TaskArtifactView);
        TaskArtifactView.setBounds(100, 430, 40, 30);

        TaskArtifactUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        TaskArtifactUpload.setBorderPainted(false);
        TaskArtifactUpload.setContentAreaFilled(false);
        TaskArtifactUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskArtifactUploadActionPerformed(evt);
            }
        });
        add(TaskArtifactUpload);
        TaskArtifactUpload.setBounds(20, 430, 40, 30);

        TaskArtifactDownLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        TaskArtifactDownLoad.setBorderPainted(false);
        TaskArtifactDownLoad.setContentAreaFilled(false);
        TaskArtifactDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaskArtifactDownLoadActionPerformed(evt);
            }
        });
        add(TaskArtifactDownLoad);
        TaskArtifactDownLoad.setBounds(60, 430, 40, 30);

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
        TaskSaveText.setBounds(410, 130, 30, 30);

        DisciplineEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        DisciplineEdit.setBorderPainted(false);
        DisciplineEdit.setContentAreaFilled(false);
        DisciplineEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineEditActionPerformed(evt);
            }
        });
        add(DisciplineEdit);
        DisciplineEdit.setBounds(370, 40, 30, 30);

        ThemeEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        ThemeEdit.setBorderPainted(false);
        ThemeEdit.setContentAreaFilled(false);
        ThemeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemeEditActionPerformed(evt);
            }
        });
        add(ThemeEdit);
        ThemeEdit.setBounds(410, 90, 30, 30);

        FullTrace.setText("трассировка импорта");
        add(FullTrace);
        FullTrace.setBounds(330, 10, 160, 20);

        DisciplineSaveImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/archive.png"))); // NOI18N
        DisciplineSaveImport.setBorderPainted(false);
        DisciplineSaveImport.setContentAreaFilled(false);
        DisciplineSaveImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineSaveImportActionPerformed(evt);
            }
        });
        add(DisciplineSaveImport);
        DisciplineSaveImport.setBounds(440, 40, 40, 30);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator1);
        jSeparator1.setBounds(487, 20, 10, 620);

        jLabel7.setText("Задача: за одну - сумма ");
        add(jLabel7);
        jLabel7.setBounds(20, 580, 140, 20);

        Rules.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RulesItemStateChanged(evt);
            }
        });
        add(Rules);
        Rules.setBounds(20, 490, 230, 20);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Регламенты экзамена");
        add(jLabel9);
        jLabel9.setBounds(20, 470, 160, 20);

        RuleExceciseForOne.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExceciseForOneKeyPressed(evt);
            }
        });
        add(RuleExceciseForOne);
        RuleExceciseForOne.setBounds(170, 580, 30, 25);

        jLabel10.setText("Рейтинг экзамена");
        add(jLabel10);
        jLabel10.setBounds(270, 580, 110, 20);

        RuleOwnRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleOwnRatingKeyPressed(evt);
            }
        });
        add(RuleOwnRating);
        RuleOwnRating.setBounds(390, 580, 50, 25);

        RuleName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleNameKeyPressed(evt);
            }
        });
        add(RuleName);
        RuleName.setBounds(20, 520, 230, 25);

        RuleThemeRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RuleThemeRemove.setBorderPainted(false);
        RuleThemeRemove.setContentAreaFilled(false);
        RuleThemeRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeRemoveActionPerformed(evt);
            }
        });
        add(RuleThemeRemove);
        RuleThemeRemove.setBounds(400, 610, 30, 30);

        RuleAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        RuleAdd.setBorderPainted(false);
        RuleAdd.setContentAreaFilled(false);
        RuleAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleAddActionPerformed(evt);
            }
        });
        add(RuleAdd);
        RuleAdd.setBounds(270, 490, 30, 30);

        jLabel11.setText("Темы");
        add(jLabel11);
        jLabel11.setBounds(20, 620, 70, 16);
        add(RuleThemes);
        RuleThemes.setBounds(110, 620, 280, 20);

        RuleThemeAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RuleThemeAdd.setBorderPainted(false);
        RuleThemeAdd.setContentAreaFilled(false);
        RuleThemeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeAddActionPerformed(evt);
            }
        });
        add(RuleThemeAdd);
        RuleThemeAdd.setBounds(450, 90, 30, 30);

        RuleDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RuleDelete.setBorderPainted(false);
        RuleDelete.setContentAreaFilled(false);
        RuleDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleDeleteActionPerformed(evt);
            }
        });
        add(RuleDelete);
        RuleDelete.setBounds(310, 490, 30, 30);

        jLabel13.setText("Тест: за один - сумма");
        add(jLabel13);
        jLabel13.setBounds(20, 550, 140, 20);

        RuleExcerciseSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExcerciseSumKeyPressed(evt);
            }
        });
        add(RuleExcerciseSum);
        RuleExcerciseSum.setBounds(210, 580, 40, 25);

        RuleThemeAddAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RuleThemeAddAll.setBorderPainted(false);
        RuleThemeAddAll.setContentAreaFilled(false);
        RuleThemeAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeAddAllActionPerformed(evt);
            }
        });
        add(RuleThemeAddAll);
        RuleThemeAddAll.setBounds(350, 490, 30, 30);

        jLabel12.setText("Все темы");
        add(jLabel12);
        jLabel12.setBounds(390, 490, 80, 16);

        TaskType.setText("Задача(1) / Вопрос(0)");
        add(TaskType);
        TaskType.setBounds(330, 170, 150, 20);

        RuleQuestionForOne.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQuestionForOneKeyPressed(evt);
            }
        });
        add(RuleQuestionForOne);
        RuleQuestionForOne.setBounds(170, 550, 30, 25);

        RuleQuestionSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQuestionSumKeyPressed(evt);
            }
        });
        add(RuleQuestionSum);
        RuleQuestionSum.setBounds(210, 550, 40, 25);

        RuleDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleDurationKeyPressed(evt);
            }
        });
        add(RuleDuration);
        RuleDuration.setBounds(390, 520, 50, 25);

        jLabel17.setText("Продолж. (мин)");
        add(jLabel17);
        jLabel17.setBounds(270, 520, 100, 20);

        RuleMinRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleMinRatingKeyPressed(evt);
            }
        });
        add(RuleMinRating);
        RuleMinRating.setBounds(390, 550, 50, 25);

        jLabel22.setText("Рейтинг допуска");
        add(jLabel22);
        jLabel22.setBounds(270, 550, 110, 20);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Уч.единица");
        add(jLabel5);
        jLabel5.setBounds(500, 240, 120, 16);

        SemesterRules.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SemesterRulesItemStateChanged(evt);
            }
        });
        add(SemesterRules);
        SemesterRules.setBounds(500, 30, 320, 20);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Регламент семестра");
        add(jLabel1);
        jLabel1.setBounds(500, 10, 160, 16);

        SemesterRuleAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        SemesterRuleAdd.setBorderPainted(false);
        SemesterRuleAdd.setContentAreaFilled(false);
        SemesterRuleAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SemesterRuleAddActionPerformed(evt);
            }
        });
        add(SemesterRuleAdd);
        SemesterRuleAdd.setBounds(830, 20, 30, 30);

        SemesterRuleRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        SemesterRuleRemove.setBorderPainted(false);
        SemesterRuleRemove.setContentAreaFilled(false);
        SemesterRuleRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SemesterRuleRemoveActionPerformed(evt);
            }
        });
        add(SemesterRuleRemove);
        SemesterRuleRemove.setBounds(870, 20, 30, 30);

        jLabel4.setText("Пн. недели 1");
        add(jLabel4);
        jLabel4.setBounds(500, 110, 120, 16);

        SmstrDate.setEnabled(false);
        SmstrDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SmstrDateMouseClicked(evt);
            }
        });
        add(SmstrDate);
        SmstrDate.setBounds(620, 100, 110, 25);

        FineOverDate.setText("Нарушение срока сдачи");
        FineOverDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FineOverDateItemStateChanged(evt);
            }
        });
        add(FineOverDate);
        FineOverDate.setBounds(500, 140, 170, 20);

        SemesterDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SemesterDurationKeyPressed(evt);
            }
        });
        add(SemesterDuration);
        SemesterDuration.setBounds(850, 100, 40, 25);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("%");
        add(jLabel8);
        jLabel8.setBounds(670, 140, 20, 25);

        PointPerSkip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PointPerSkipKeyPressed(evt);
            }
        });
        add(PointPerSkip);
        PointPerSkip.setBounds(850, 200, 40, 25);

        jLabel6.setText("Недель");
        add(jLabel6);
        jLabel6.setBounds(740, 110, 60, 16);

        OverDatePercent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OverDatePercentKeyPressed(evt);
            }
        });
        add(OverDatePercent);
        OverDatePercent.setBounds(690, 140, 40, 25);

        jLabel16.setText("Недель");
        add(jLabel16);
        jLabel16.setBounds(740, 140, 60, 25);

        SemesterRule.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SemesterRuleKeyPressed(evt);
            }
        });
        add(SemesterRule);
        SemesterRule.setBounds(500, 60, 320, 25);

        FineOverSemester.setText("Сдача после семестра");
        FineOverSemester.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FineOverSemesterItemStateChanged(evt);
            }
        });
        add(FineOverSemester);
        FineOverSemester.setBounds(500, 170, 170, 20);

        jLabel23.setText("Балл за пропуск");
        add(jLabel23);
        jLabel23.setBounds(740, 200, 90, 25);

        OverSemesterPercent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OverSemesterPercentKeyPressed(evt);
            }
        });
        add(OverSemesterPercent);
        OverSemesterPercent.setBounds(690, 170, 40, 25);

        FineOverIrregular.setText("Нерегулярность (начиная с...)");
        FineOverIrregular.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FineOverIrregularItemStateChanged(evt);
            }
        });
        add(FineOverIrregular);
        FineOverIrregular.setBounds(500, 200, 190, 20);

        IrregulaFirstWeek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IrregulaFirstWeekKeyPressed(evt);
            }
        });
        add(IrregulaFirstWeek);
        IrregulaFirstWeek.setBounds(690, 200, 40, 25);

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setText("%");
        add(jLabel30);
        jLabel30.setBounds(670, 170, 20, 25);

        jLabel31.setText("% за качество");
        add(jLabel31);
        jLabel31.setBounds(740, 170, 90, 25);

        OverDateWeeks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OverDateWeeksKeyPressed(evt);
            }
        });
        add(OverDateWeeks);
        OverDateWeeks.setBounds(850, 140, 40, 25);

        QualProc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QualProcKeyPressed(evt);
            }
        });
        add(QualProc);
        QualProc.setBounds(850, 170, 40, 25);
        add(jSeparator2);
        jSeparator2.setBounds(20, 75, 450, 10);

        EduUnits.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EduUnitsItemStateChanged(evt);
            }
        });
        add(EduUnits);
        EduUnits.setBounds(500, 260, 310, 20);

        EduUnitAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        EduUnitAdd.setBorderPainted(false);
        EduUnitAdd.setContentAreaFilled(false);
        EduUnitAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitAddActionPerformed(evt);
            }
        });
        add(EduUnitAdd);
        EduUnitAdd.setBounds(820, 250, 30, 30);

        EduUnitRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        EduUnitRemove.setBorderPainted(false);
        EduUnitRemove.setContentAreaFilled(false);
        EduUnitRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitRemoveActionPerformed(evt);
            }
        });
        add(EduUnitRemove);
        EduUnitRemove.setBounds(860, 250, 30, 30);

        EduUnitToFront.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/up.PNG"))); // NOI18N
        EduUnitToFront.setBorderPainted(false);
        EduUnitToFront.setContentAreaFilled(false);
        EduUnitToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitToFrontActionPerformed(evt);
            }
        });
        add(EduUnitToFront);
        EduUnitToFront.setBounds(860, 290, 30, 30);

        EduUnitToEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        EduUnitToEnd.setBorderPainted(false);
        EduUnitToEnd.setContentAreaFilled(false);
        EduUnitToEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EduUnitToEndActionPerformed(evt);
            }
        });
        add(EduUnitToEnd);
        EduUnitToEnd.setBounds(820, 290, 30, 30);

        EduUnitName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EduUnitNameKeyPressed(evt);
            }
        });
        add(EduUnitName);
        EduUnitName.setBounds(500, 290, 310, 25);

        ManualPointSet.setText("\"Ручная\" установка");
        ManualPointSet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ManualPointSetItemStateChanged(evt);
            }
        });
        add(ManualPointSet);
        ManualPointSet.setBounds(750, 330, 140, 20);

        PointSum.setEnabled(false);
        add(PointSum);
        PointSum.setBounds(700, 360, 40, 25);

        jLabel33.setText("Неделя сдачи");
        add(jLabel33);
        jLabel33.setBounds(500, 360, 90, 16);

        jLabel14.setText("Балл");
        add(jLabel14);
        jLabel14.setBounds(650, 330, 60, 16);

        DeliveryWeek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DeliveryWeekKeyPressed(evt);
            }
        });
        add(DeliveryWeek);
        DeliveryWeek.setBounds(590, 360, 40, 25);

        EduUnitType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EduUnitTypeItemStateChanged(evt);
            }
        });
        add(EduUnitType);
        EduUnitType.setBounds(500, 330, 130, 20);

        BasePoint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BasePointKeyPressed(evt);
            }
        });
        add(BasePoint);
        BasePoint.setBounds(700, 330, 40, 25);

        jLabel15.setText("Сумма");
        add(jLabel15);
        jLabel15.setBounds(650, 360, 60, 16);
        add(jSeparator3);
        jSeparator3.setBounds(500, 230, 400, 10);
        add(jSeparator6);
        jSeparator6.setBounds(20, 460, 420, 10);
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
        new OK(200, 200, "Удалить: " + Tasks.getSelectedItem(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"SATask",cTask.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshSelectedDiscipline(false);
                    }
                };
            }
        });
    }//GEN-LAST:event_TaskRemoveActionPerformed

    private void TaskAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskAddActionPerformed
        if (cTheme==null)
            return;
        final boolean taskType = TaskType.isSelected();
        new OK(200, 200, "Добавить "+(taskType ? "задачу" : "вопрос"), new I_Button() {
            @Override
            public void onPush() {
                final SATask task = new SATask();
                task.setTaskText("Новый вопрос/задача");
                task.setType(taskType ? Values.TaskExercise : Values.TaskQuestion);
                task.getSATheme().setOid(cTheme.getOid());
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
                SADiscipline discipline = new SADiscipline();
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
                        return main.service.deleteById(main.debugToken,"SADiscipline",cDiscipline.getOid());
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
                SATheme bean = new SATheme();
                bean.setName(value);
                bean.getSADiscipline().setOid(cDiscipline.getOid());
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
                        return main.service.deleteById(main.debugToken,"SATheme",cTheme.getOid());
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
            main.showImageArtifact(cTask.getArtifact().getRef());
    }//GEN-LAST:event_TaskArtifactViewActionPerformed

    private void TaskArtifactUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskArtifactUploadActionPerformed
        new UploadPanel(200, 200, main, new I_OK() {
            @Override
            public void onOK(final Entity ent) {
                if (cTask.getArtifact().getOid()==0){
                    cTask.getArtifact().setOidRef((Artifact) ent);
                    taskUpdate();
                    return;
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
        main.loadFile(cTask.getArtifact().getRef());
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
                SADiscipline discipline = new SADiscipline();
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
                            final SATheme theme = new SATheme();
                            final SATask task = new SATask();
                            for (idx1 = 0; idx1 < owtImportData.size(); idx1++) {
                                idx2=-1;
                                owtTheme = owtImportData.get(idx1);
                                theme.getSADiscipline().setOid(oo.getValue());
                                theme.setName(owtTheme.getQuestion());
                                final JLong theme1 = new APICallSync<JLong>() {
                                    @Override
                                    public Call<JLong> apiFun() {
                                        return main.service.addEntity(main.debugToken,new DBRequest(theme,main.gson),0);
                                        }
                                    }.call();
                                for(idx2=0;idx2 < owtTheme.size();idx2++){
                                    task.getSATheme().setOid(theme1.getValue());
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

    public void ruleUpdate(KeyEvent evt){
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

    public static String shortString(String ss, int size){
        return ss.length()<=size ? ss : ss.substring(0,size)+"...";
        }

    private void RuleThemeRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RuleThemeRemoveActionPerformed
        if (cRule==null)
            return;
        if (cRule.getThemes().size()==0)
            return;
        long oid = cRule.getThemes().get(RuleThemes.getSelectedIndex()).getOid();
        SATheme theme = cDiscipline.getThemes().getById(oid);
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
        final SAExamRule ruleBean = new SAExamRule();
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
                ruleBean.getSADiscipline().setOid(cDiscipline.getOid());
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
                        return main.service.deleteById(main.debugToken,"SAExamRule",cRule.getOid());
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
                for(SATheme theme : cDiscipline.getThemes())
                    cRule.getThemes().add(theme.getOid());
                ruleUpdate(null);
                }
            });
    }//GEN-LAST:event_RuleThemeAddAllActionPerformed

    public void disciplineUpdate(){
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

    private void TaskTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_TaskTextInputMethodTextChanged
        taskTextChanged=true;
        TaskSaveText.setEnabled(true);
    }//GEN-LAST:event_TaskTextInputMethodTextChanged

    private void TaskTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaskTextKeyPressed
        taskTextChanged=true;
        TaskSaveText.setEnabled(true);
    }//GEN-LAST:event_TaskTextKeyPressed

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

    private void SemesterRuleAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SemesterRuleAddActionPerformed
        new OKName(200,200,"Добавить регламент", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                SASemesterRule semesterRule = new SASemesterRule();
                semesterRule.setName(value);
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(semesterRule,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        refreshSemesterRuleList();
                        }
                    };
                }
            });
    }//GEN-LAST:event_SemesterRuleAddActionPerformed

    private void SemesterRuleRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SemesterRuleRemoveActionPerformed
        if (cSemesterRule==null)
        return;
        new OK(200, 200, "Удалить регламент: " + cSemesterRule.getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"SASemesterRule",cSemesterRule.getOid());
                    }
                    @Override
                    public void onSucess(JBoolean oo) {
                        refreshSemesterRuleList();
                    }
                };
            }
        });
    }//GEN-LAST:event_SemesterRuleRemoveActionPerformed

    private void SmstrDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SmstrDateMouseClicked
        if (evt.getClickCount()<2)
            return;
        if (cSemesterRule ==null)
            return;
        new CalendarView("Начало семестра", new I_CalendarTime() {
            @Override
            public void onSelect(OwnDateTime time) {
                cSemesterRule.setSmstrDate(time);
                semesterRuleUpdate(null);
            }
        });
    }//GEN-LAST:event_SmstrDateMouseClicked

    private void FineOverDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_FineOverDateItemStateChanged
        if (refresh)
            return;
        if (cSemesterRule==null) return;
        cSemesterRule.setFineOverDate(FineOverDate.isSelected());
        semesterRuleUpdate(null);

    }//GEN-LAST:event_FineOverDateItemStateChanged

    private void SemesterDurationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SemesterDurationKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setSemesterDuration(Integer.parseInt(SemesterDuration.getText()));
            semesterRuleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_SemesterDurationKeyPressed

    private void PointPerSkipKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PointPerSkipKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setPointPerSkip(Double.parseDouble(PointPerSkip.getText()));
            semesterRuleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата вещественного");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_PointPerSkipKeyPressed

    private void OverDatePercentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OverDatePercentKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setOverDatePercent(Integer.parseInt(OverDatePercent.getText()));
            semesterRuleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_OverDatePercentKeyPressed

    private void SemesterRuleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SemesterRuleKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        cSemesterRule.setName(RuleQuestionForOne.getText());
        semesterRuleUpdate(evt);
        }//GEN-LAST:event_SemesterRuleKeyPressed

    private void FineOverSemesterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_FineOverSemesterItemStateChanged
        if (refresh)
            return;
        if (cSemesterRule==null) return;
        cSemesterRule.setFineOverSemester(FineOverSemester.isSelected());
        semesterRuleUpdate(null);
    }//GEN-LAST:event_FineOverSemesterItemStateChanged

    private void OverSemesterPercentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OverSemesterPercentKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setOverSemesterPercent(Integer.parseInt(OverSemesterPercent.getText()));
            semesterRuleUpdate(evt);
            } catch (Exception ee){
                popup("Ошибка формата целого");
                main.viewUpdate(evt,false);
                }
    }//GEN-LAST:event_OverSemesterPercentKeyPressed

    private void FineOverIrregularItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_FineOverIrregularItemStateChanged
        if (refresh)
            return;
        if (cSemesterRule==null) return;
        cSemesterRule.setFineOverIrregular(FineOverIrregular.isSelected());
        semesterRuleUpdate(null);
    }//GEN-LAST:event_FineOverIrregularItemStateChanged

    private void IrregulaFirstWeekKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IrregulaFirstWeekKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setIrregulaFirstWeek(Integer.parseInt(IrregulaFirstWeek.getText()));
            semesterRuleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_IrregulaFirstWeekKeyPressed

    private void OverDateWeeksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OverDateWeeksKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setOverDateWeeks(Integer.parseInt(OverDateWeeks.getText()));
            semesterRuleUpdate(evt);
            } catch (Exception ee){
                popup("Ошибка формата целого");
                main.viewUpdate(evt,false);
                }
    }//GEN-LAST:event_OverDateWeeksKeyPressed

    private void QualProcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QualProcKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cSemesterRule==null) return;
        try {
            cSemesterRule.setQualProc(Integer.parseInt(QualProc.getText()));
            semesterRuleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_QualProcKeyPressed

    private void EduUnitAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitAddActionPerformed
        new OKName(200,200,"Добавить уч.единицу", new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                final SAEduUnit eduUnit  = new SAEduUnit();
                int size = cDiscipline.getUnits().size();
                int num = size==0 ? 1 : cDiscipline.getUnits().get(size-1).getOrderNum()+1;     // Последнего +1
                eduUnit.setOrderNum(num);
                eduUnit.setName(value);
                eduUnit.getSADiscipline().setOid(cDiscipline.getOid());
                new APICall<JLong>(main) {
                    @Override
                    public Call<JLong> apiFun() {
                        return main.service.addEntity(main.debugToken,new DBRequest(eduUnit,main.gson),0);
                        }
                    @Override
                    public void onSucess(JLong oo) {
                        savePos();
                        refreshSelectedDiscipline();
                    }
                };
            }
        });
    }//GEN-LAST:event_EduUnitAddActionPerformed

    public void caclEduUnitsSum(){
        PointSum.setText("");
        if (cDiscipline==null)
            return;
        int sum=0;
        for(SAEduUnit eduUnit : cDiscipline.getUnits())
            sum += eduUnit.getBasePoint();
        PointSum.setText(""+sum);
        }

    private void EduUnitRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitRemoveActionPerformed
        if (cEduUnit==null)
            return;
        new OK(200, 200, "Удалить уч.единицу: " + cEduUnit.getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<JBoolean>(main) {
                    @Override
                    public Call<JBoolean> apiFun() {
                        return main.service.deleteById(main.debugToken,"SAEduUnit",cEduUnit.getOid());
                        }
                    @Override
                    public void onSucess(JBoolean oo) {
                        cDiscipline.getUnits().removeById(cEduUnit.getOid());
                        disciplineUpdate();
                        }
                };
            }
        });
    }//GEN-LAST:event_EduUnitRemoveActionPerformed

    private void EduUnitToFrontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitToFrontActionPerformed
        if (cEduUnit==null)
            return;
        int idx = EduUnits.getSelectedIndex();
        if (idx==0)
            return;
        SAEduUnit prev = cDiscipline.getUnits().get(idx-1);
        int ord1 = cEduUnit.getOrderNum();
        int ord2 = prev.getOrderNum();
        cEduUnit.setOrderNum(ord2);
        prev.setOrderNum(ord1);
        try {
            new APICall2<JEmpty>() {
                @Override
                public Call<JEmpty> apiFun() {
                    return main.service.updateEntity(main.debugToken,new DBRequest(prev,main.gson));
                    }
                }.call(main);
            EduUnits.select(idx-1);
            eduUnitUpdate(null,true);
            } catch (UniException ee){
                System.out.println(ee.toString());
                }

    }//GEN-LAST:event_EduUnitToFrontActionPerformed

    private void EduUnitToEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EduUnitToEndActionPerformed
        if (cEduUnit==null)
            return;
        int idx = EduUnits.getSelectedIndex();
        if (idx==cDiscipline.getUnits().size()-1)
            return;
        SAEduUnit next = cDiscipline.getUnits().get(idx+1);
        int ord1 = cEduUnit.getOrderNum();
        int ord2 = next.getOrderNum();
        cEduUnit.setOrderNum(ord2);
        next.setOrderNum(ord1);
        try {
            new APICall2<JEmpty>() {
                @Override
                public Call<JEmpty> apiFun() {
                    return main.service.updateEntity(main.debugToken,new DBRequest(next,main.gson));
                }
            }.call(main);
            EduUnits.select(idx+1);
            eduUnitUpdate(null,true);
        } catch (UniException ee){
            System.out.println(ee.toString());
        }
    }//GEN-LAST:event_EduUnitToEndActionPerformed

    private void EduUnitNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EduUnitNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cEduUnit==null) return;
        cEduUnit.setName(EduUnitName.getText());
        eduUnitUpdate(evt);
    }//GEN-LAST:event_EduUnitNameKeyPressed

    private void SemesterRulesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SemesterRulesItemStateChanged
        refreshSelectedSemesterRule(false);
    }//GEN-LAST:event_SemesterRulesItemStateChanged

    private void EduUnitTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EduUnitTypeItemStateChanged
        if (refresh)
            return;
        if (cEduUnit==null)
            return;
        final ConstValue cc = eduUnitTypes.get();
        new OKName(200,200,"Тип уч.единицы: "+cc.title(), new I_Value<String>() {
            @Override
            public void onEnter(String value) {
                cEduUnit.setUnitType(cc.value());
                eduUnitUpdate(null);
                }
            });
    }//GEN-LAST:event_EduUnitTypeItemStateChanged

    private void EduUnitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EduUnitsItemStateChanged
        refreshSelectedEduUnit();
    }//GEN-LAST:event_EduUnitsItemStateChanged

    private void BasePointKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BasePointKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cEduUnit==null) return;
        try {
            cEduUnit.setBasePoint(Integer.parseInt(BasePoint.getText()));
            eduUnitUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
            }
    }//GEN-LAST:event_BasePointKeyPressed

    private void ManualPointSetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ManualPointSetItemStateChanged
        if (refresh)
            return;
        if (cEduUnit==null)
            return;
        cEduUnit.setManualPointSet(ManualPointSet.isSelected());
        eduUnitUpdate(null);
    }//GEN-LAST:event_ManualPointSetItemStateChanged

    private void DeliveryWeekKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DeliveryWeekKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cEduUnit==null) return;
        try {
            cEduUnit.setDeliveryWeek(Integer.parseInt(DeliveryWeek.getText()));
            eduUnitUpdate(evt);
            } catch (Exception ee){
                popup("Ошибка формата целого");
                main.viewUpdate(evt,false);
                }
    }//GEN-LAST:event_DeliveryWeekKeyPressed

    public void semesterRuleUpdate(KeyEvent evt){
        if (cSemesterRule==null)
            return;
        try {
            new APICall2<JEmpty>() {
                @Override
                public Call<JEmpty> apiFun() {
                    return main.service.updateEntity(main.debugToken,new DBRequest(cSemesterRule,main.gson));
                    }
                }.call(main);
            if (evt!=null)
                main.viewUpdate(evt,true);
            popup("Регламент семестра обновлен");
            savePos();
            refreshSelectedSemesterRule(true);
            } catch (UniException ee){
                System.out.println(ee.toString());
                if (evt!=null)
                    main.viewUpdate(evt,false);
                    }
    }

    public void eduUnitUpdate(KeyEvent evt){
        eduUnitUpdate(evt,false);
        }
    public void eduUnitUpdate(KeyEvent evt,boolean noPopup){
        if (cEduUnit==null)
            return;
        try {
            new APICall2<JEmpty>() {
                @Override
                public Call<JEmpty> apiFun() {
                    return main.service.updateEntity(main.debugToken,new DBRequest(cEduUnit,main.gson));
                }
            }.call(main);
            if (evt!=null)
                main.viewUpdate(evt,true);
            if (!noPopup)
                popup("Учебная единица обновлена");
            savePos();
            refreshEduUnits(true);
        } catch (UniException ee){
            System.out.println(ee.toString());
            if (evt!=null)
                main.viewUpdate(evt,false);
        }
    }



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
    private javax.swing.JTextField BasePoint;
    private javax.swing.JTextField DeliveryWeek;
    private javax.swing.JButton DisciplineAdd;
    private javax.swing.JButton DisciplineEdit;
    private javax.swing.JButton DisciplineImport;
    private javax.swing.JButton DisciplineRemove;
    private javax.swing.JButton DisciplineSaveImport;
    private java.awt.Choice Disciplines;
    private javax.swing.JButton EduUnitAdd;
    private javax.swing.JTextField EduUnitName;
    private javax.swing.JButton EduUnitRemove;
    private javax.swing.JButton EduUnitToEnd;
    private javax.swing.JButton EduUnitToFront;
    private java.awt.Choice EduUnitType;
    private java.awt.Choice EduUnits;
    private javax.swing.JCheckBox FineOverDate;
    private javax.swing.JCheckBox FineOverIrregular;
    private javax.swing.JCheckBox FineOverSemester;
    private javax.swing.JCheckBox FullTrace;
    private javax.swing.JTextField IrregulaFirstWeek;
    private javax.swing.JCheckBox ManualPointSet;
    private javax.swing.JTextField OverDatePercent;
    private javax.swing.JTextField OverDateWeeks;
    private javax.swing.JTextField OverSemesterPercent;
    private javax.swing.JTextField PointPerSkip;
    private javax.swing.JTextField PointSum;
    private javax.swing.JTextField QualProc;
    private javax.swing.JButton RefreshDisciplines;
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
    private javax.swing.JTextField SemesterDuration;
    private javax.swing.JTextField SemesterRule;
    private javax.swing.JButton SemesterRuleAdd;
    private javax.swing.JButton SemesterRuleRemove;
    private java.awt.Choice SemesterRules;
    private javax.swing.JTextField SmstrDate;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
