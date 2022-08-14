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
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.desktop.exam.AnswerStateFactory;
import romanow.abc.desktop.exam.ExamTakingStateFactory;
import romanow.abc.desktop.exam.StudentRatingStateFactory;
import romanow.abc.desktop.exam.StudentStateFactory;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;
import romanow.abc.vkr.exam.model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

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
    private int cTaskNum=0;
    private OWTDiscipline owtImportData = null;
    private List<GroupBean> groups = new ArrayList<>();                 // Список групп
    private HashMap<Long,GroupBean> groupsMap = new HashMap<>();        // Мар всех групп
    private FullGroupBean cGroup = null;                                // Текущая группа
    private List<GroupRatingBean> disciplineGroupsList = new ArrayList<>(); // Рейтинги групп (экзамен) для текущей дисциплины
    private List<ExamRuleBean> cExamRules = new ArrayList<>();          // Список регламентов для дисциплины
    private HashMap<Long,ExamRuleBean> cExamRulesMap = new HashMap<>(); // Мар регламентов для дисциплины
    private boolean refresh=false;                                      // Признак обновления для событий  CheckBox
    private boolean taskTextChanged=false;
    private EMVKRMainBaseFrame main;
    private FullGroupRatingBean cRating =null;
    private FullMessageBean cAnswerMessage =null;                       //
    private FullStudentRatingBean cStudRating = null;
    private ArrayList<FullTaskBean> sortedTasks =  new ArrayList<>();   // Отсортированы - вопрос/задача
    private List<FullStudentRatingBean> studRatings = new ArrayList();
    private ArrayList<FullAnswerBean> answers = new ArrayList<>();
    private FullExamBean cTaking =null;                                 // Текущий прием
    private FullAnswerBean cAnswer =null;                               // Текущий ответ
    private FullGroupRatingBean cGroupRating=null;                      // Рейтинг группы для выбранного студента
    private ArrayList<ExamBean> cTakings = new ArrayList<>();           // Список приема для экзамена (по дисциплине)
    private AnswerStateFactory answerStateFactory = new AnswerStateFactory();
    private ExamTakingStateFactory takingStateFactory = new ExamTakingStateFactory();
    private StudentRatingStateFactory ratingStateFactory = new StudentRatingStateFactory();
    private StudentStateFactory studentStateFactory = new StudentStateFactory();
    private HashMap<Long,ExamRuleBean> rulesMap = new HashMap<>();
    //------------------------------------------------------------------------------------------------------------------
    //private ConstValue cTakingState = null;
    //----------------------------------------------------------------------------------------
    //private StateMashineView takingStateMashine;
    //private StateMashineView answerStateMashine;
    //private StateMashineView studRatingStateMashine;
    //---------------------------------------------------------------------------------------
    private int themeIdx = -1;
    private int taskIdx = -1;
    private int examIdx = -1;
    private int ruleIdx = -1;
    private int takingIdx = -1;
    private int groupIdx = -1;
    private int studentIdx = -1;
    private int answerIdx = -1;
    private int ratStudIdx = -1;
    private boolean isRefresh=false;            // Для событий checkBox
    //------------------------------------------------------------------------------------------------------------------
    public EMVKRExamAdminPanel() {
        initComponents();
    }
    public void initPanel(MainBaseFrame main0){
        super.initPanel(main0);
        main = (EMVKRMainBaseFrame) main0;
        DisciplineSaveImport.setEnabled(false);
        TaskArtifactDownLoad.setEnabled(false);
        TaskArtifactView.setEnabled(false);
        //takingStateMashine = new StateMashineView(this,610,275,Values.TakingFactory);
        //studRatingStateMashine = new StateMashineView(this,780,365,Values.StudRatingFactory);
        //answerStateMashine = new StateMashineView(this,780,440,Values.AnswerFactory);
        refreshAll();
        }

    private void savePos(){
        themeIdx = Themes.getSelectedIndex();
        taskIdx = Tasks.getSelectedIndex();
        examIdx = GroupRatings.getSelectedIndex();
        ruleIdx = Rules.getSelectedIndex();
        takingIdx = Takings.getSelectedIndex();
        groupIdx = Groups.getSelectedIndex();
        studentIdx = Students.getSelectedIndex();
        answerIdx = Answers.getSelectedIndex();
        ratStudIdx = RatingStudentList.getSelectedIndex();
        }

    public void refreshAll(){
        refreshGroupsList();
        refreshDisciplineList();
    }

    public void refreshRules(boolean withPos){
        Rules.removeAll();
        new APICall<List<ExamRuleBean>>(main) {
            @Override
            public Call<List<ExamRuleBean>> apiFun() {
                return main.client.getDisciplineApi().findExamRules(cDiscipline.getDiscipline().getId());
                }
            @Override
            public void onSucess(List<ExamRuleBean> oo) {
                cExamRules = oo;
                rulesMap.clear();
                for(ExamRuleBean rule : cExamRules){
                    Rules.add(rule.getName());
                    rulesMap.put(rule.getId(),rule);
                    }
                if (ruleIdx!=-1)
                    Rules.select(ruleIdx);
                if (withPos && ruleIdx!=-1)
                    Rules.select(ruleIdx);
                refreshSelectedRule();

                }
            };
        }

    public void refreshSelectedRule(){
        RuleName.setText("");
        RuleOwnRating.setText("");
        RuleExceciseForOne.setText("");
        RuleThemes.removeAll();
        cRule=null;
        if (cExamRules.size()==0)
            return;
        cRule = cExamRules.get(Rules.getSelectedIndex());
        RuleName.setText(cRule.getName());
        RuleDuration.setText(""+cRule.getDuration());
        RuleOwnRating.setText(""+cRule.getDuration());
        RuleExceciseForOne.setText(""+cRule.getSingleExerciseDefaultRating());
        RuleExcerciseSum.setText(""+cRule.getExercisesRatingSum());
        RuleQuestionForOne.setText(""+cRule.getSingleQuestionDefaultRating());
        RuleQuestionSum.setText(""+cRule.getQuestionsRatingSum());
        RuleMinRating.setText(""+cRule.getMinimalSemesterRating());
        RuleOwnRating.setText(""+cRule.getMinimalExamRating());
        ruleThemes.clear();
        RuleThemes.removeAll();
        ruleThemesMap.clear();
        for(Long themeId : cRule.getThemeIds()){
            FullThemeBean theme  = disciplineThemesMap.get(themeId);
            if (theme==null)
                System.out.println("Не найдена тема id="+themeId);
            else {
                ruleThemes.add(theme);
                RuleThemes.add(theme.getTheme().getName());
                ruleThemesMap.put(theme.getTheme().getId(),theme);
               }
            }
        }

    public void refreshSelectedDiscipline(){
        refreshSelectedDiscipline(true);
        }

    public void refreshSelectedDiscipline(boolean withPos){
        if (withPos)
            savePos();
        answerClear();
        Themes.removeAll();
        Tasks.removeAll();
        cDiscipline=null;
        if (disciplines.size()==0)
            return;
        long oid = disciplines.get(Disciplines.getSelectedIndex()).getId();
        new APICall<FullDisciplineBean>(main) {
            @Override
            public Call<FullDisciplineBean> apiFun() {
                return main.client.getDisciplineApi().getFull5(oid,1);
            }
            @Override
            public void onSucess(FullDisciplineBean oo) {
                try {
                    cDiscipline = oo;
                    } catch (Exception ee){
                        System.out.println(ee.toString());
                        popup("Ошибка чтения дисциплины "+disciplines.get(Disciplines.getSelectedIndex()).getName());
                        return;
                        }
                Themes.removeAll();
                disciplineThemesMap.clear();
                for(FullThemeBean theme : cDiscipline.getThemes()){
                    Themes.add(theme.getTheme().getName());
                    disciplineThemesMap.put(theme.getTheme().getId(),theme);
                    }
                refreshSelectedTheme(withPos);
                refreshRules(withPos);
                refreshRatings(withPos);
                refreshStudRatings();
                //takingStateMashine.refresh(cTaking);
                //studRatingStateMashine.refresh(cStudRating);
                //answerStateMashine.refresh(cAnswer);
            }
        };
    }


    public void refreshSelectedTheme(boolean withPos){
        if (withPos && themeIdx!=-1)
            Themes.select(themeIdx);
        Tasks.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Themes.getSelectedIndex());
        new APICall<FullThemeBean>(main) {
            @Override
            public Call<FullThemeBean> apiFun() {
                return main.client.getThemeApi().getFull(cTheme.getTheme().getId(),2);
                }
            @Override
            public void onSucess(FullThemeBean oo) {
                try {
                    cTheme = oo;
                    sortedTasks.clear();
                    int iq = 1, it = 1;
                    for (FullTaskBean task : cTheme.getTasks())
                        if (task.getTask().getTaskType() == TaskBean.TaskTypeEnum.QUESTION){
                            Tasks.add("Вопрос " + iq++);
                            sortedTasks.add(task);
                        }
                    for (FullTaskBean task : cTheme.getTasks())
                        if (task.getTask().getTaskType() == TaskBean.TaskTypeEnum.EXERCISE){
                            Tasks.add("Задача " + it++);
                            sortedTasks.add(task);
                            }
                    if (withPos && taskIdx!=-1)
                        Tasks.select(taskIdx);
                    refreshSelectedTask();
                } catch (Exception ee){
                    System.out.println(ee.toString());
                    popup("Не прочитана тема: "+cTheme.getTheme().getName());
                    return;
                }
            }
        };
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

    public void refreshSelectedTaskForce(){
        TaskText.setText("");
        if (cTheme.getTasks().size()==0)
            return;
        refresh=true;
        TaskSaveText.setEnabled(false);
        cTaskNum = Tasks.getSelectedIndex();
        cTask = sortedTasks.get(cTaskNum);
        boolean isTask = cTask.getTask().getTaskType()==TaskBean.TaskTypeEnum.EXERCISE;
        TaskType.setSelected(isTask);
        TaskTypeLabel.setText(isTask ? "Задача" : "Вопрос (тест)");
        TaskText.setText(UtilsEM.formatSize(cTask.getTask().getText(),65));
        boolean bb = cTask.getArtefact()!=null && cTask.getArtefact().getId()!=0;
        TaskArtifactView.setEnabled(bb);
        TaskArtifactDownLoad.setEnabled(bb);
        refresh=false;
    }

    public void refreshRatings(boolean withPos){
        GroupRatings.removeAll();
        if (cDiscipline==null)
            return;
        new APICall<List<GroupRatingBean>>(main){
            @Override
            public Call<List<GroupRatingBean>> apiFun() {
                return  main.client.getGroupRatingApi().findAll2();
            }
            @Override
            public void onSucess(List<GroupRatingBean> oo) {
                disciplineGroupsList.clear();
                GroupRatings.removeAll();
                for(GroupRatingBean group : oo){
                    if (group.getDisciplineId().longValue()==cDiscipline.getDiscipline().getId().longValue()){
                        GroupBean groupBean = groupsMap.get(group.getGroupId());
                        if (groupBean==null)
                            System.out.println("Не найдена группа id="+group.getGroupId());
                        else{
                            GroupRatings.add(groupBean.getName());
                            disciplineGroupsList.add(group);
                            }
                        }
                    }
                if (withPos && examIdx!=-1)
                    GroupRatings.select(examIdx);
                refreshSelectedRating(withPos);
                }
            };
    }

    public void refreshSelectedRating(boolean withPos){
        cRating =null;
        if (disciplineGroupsList.size()==0)
            return;
        new APICall<FullGroupRatingBean>(main) {
            @Override
            public Call<FullGroupRatingBean> apiFun() {
                return main.client.getGroupRatingApi().findFull1(disciplineGroupsList.get(GroupRatings.getSelectedIndex()).getId(),2);
                }
            @Override
            public void onSucess(FullGroupRatingBean oo) {
                cRating = oo;
                GroupRatingName.setText(""+ cRating.getGroupRating().getName());
                refreshTakings(withPos);
                }
            };
    }

    public void refreshTakings(boolean withPos){
        //takingStateMashine.clear();
        Takings.removeAll();
        cTakings.clear();
        cTaking=null;
        new APICall<List<ExamBean>>(main) {
            @Override
            public Call<List<ExamBean>> apiFun() {
                return main.client.getExamApi().getAll2();
                }
            @Override
            public void onSucess(List<ExamBean> oo) {
                for(ExamBean taking : oo){
                    if (taking.getDisciplineId().longValue() !=cDiscipline.getDiscipline().getId().longValue())
                        continue;
                    cTakings.add(taking);
                    Takings.add(taking.getName());
                    }
                if (withPos && takingIdx!=-1)
                    Takings.select(takingIdx);
                refreshSelectedTaking();
                }
            };
    }

    public void  refreshSelectedTaking(){
        TakingAddAll.setVisible(false);
        cTaking = null;
        if (cTakings.size()==0)
            return;
        new APICall<FullExamBean>(main) {
            @Override
            public Call<FullExamBean> apiFun() {
                return main.client.getExamApi().getFull3(cTakings.get(Takings.getSelectedIndex()).getId(),2);
                }
            @Override
            public void onSucess(FullExamBean oo) {
                cTaking = oo;
                ExamBean exam = cTaking.getExam();
                TakingName.setText(exam.getName());
                if (exam.getStart()==0){
                    TakingData.setText("---");
                    TakingEndTime.setText("---");
                }
                else{
                    OwnDateTime date1 = new OwnDateTime(exam.getStart());
                    OwnDateTime date2 = new OwnDateTime(exam.getEnd());
                    TakingData.setText(date1.dateToString());
                    TakingStartTime.setText(date1.timeToString());
                    TakingEndTime.setText(date2.timeToString());
                }
                TakingDuration.setText(""+(exam.getEnd()-exam.getStart())/1000/60);
                ExamBean.StateEnum state = cTaking.getExam().getState();
                isRefresh=true;
                TakingForGroup.setSelected(exam.isOneGroup());
                isRefresh=false;
                TakingGroup.setText(!exam.isOneGroup() ? "" : groupsMap.get(exam.getGroupId()).getName());
                TakingState.setText(takingStateFactory.getByValue(state));
                //takingStateMashine.refresh(cTaking);
                ExamBean.StateEnum ss = cTaking.getExam().getState();
                TakingAddAll.setVisible(ss==ExamBean.StateEnum.TIME_SET || ss==ExamBean.StateEnum.READY);
                }
            };
    }

    public ConstValue choiceStateSet(ArrayList<ConstValue> list, Choice choice, int state) {
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

    public void refreshStudRatingFull(boolean withPos){
        if (cStudRating==null)
            return;
        if (withPos)
            savePos();
        new APICall<FullStudentRatingBean>(main) {
            @Override
            public Call<FullStudentRatingBean> apiFun() {
                return main.client.getStudentRatingApi().getFull1(cStudRating.getStudentRating().getId(), 3);
                }
            @Override
            public void onSucess(FullStudentRatingBean oo) {
                    cStudRating = oo;
                    answers.clear();
                    Answers.removeAll();
                    int qIdx=1;
                    int eIdx=1;
                    for(FullAnswerBean answer : cStudRating.getAnswers()){
                        if (answer.getTask().getTask().getTaskType()==TaskBean.TaskTypeEnum.QUESTION){
                            Answers.add("Вопрос "+qIdx++);
                            answers.add(answer);
                            }
                        }
                    for(FullAnswerBean answer : cStudRating.getAnswers()){
                        if (answer.getTask().getTask().getTaskType()==TaskBean.TaskTypeEnum.EXERCISE){
                            Answers.add("Задача "+eIdx++);
                            answers.add(answer);
                        }
                    }
                    if (withPos && answerIdx!=-1)
                        Answers.select(answerIdx);
                    refreshSelectedAnswer();
                    new APICall<FullGroupRatingBean>(main) {
                        @Override
                        public Call<FullGroupRatingBean> apiFun() {
                            return main.client.getGroupRatingApi().findFull1(cStudRating.getStudentRating().getGroupRatingId(),1);
                            }
                        @Override
                        public void onSucess(FullGroupRatingBean oo) {
                            cGroupRating = oo;
                        }
                    };
                }
            };
        }

    public void refreshSelectedAnswer(){
        cAnswer=null;
        AnswerThemeTask.setText("");
        AnswerMessageText.setText("");
        AnswerBallSelector.setEnabled(false);
        if (answers.size()==0)
            return;
        cAnswer = answers.get(Answers.getSelectedIndex());
        AnswerState.setText(answerStateFactory.getByValue(cAnswer.getAnswer().getState()));
        long themeId = cAnswer.getTask().getTask().getThemeId();
        FullThemeBean theme = disciplineThemesMap.get(themeId);
        String themeText = theme.getTheme().getName();
        AnswerThemeTask.append(UtilsEM.formatSize(themeText,70)+"\n-----------------------------------------\n");
        AnswerThemeTask.append(UtilsEM.formatSize(cAnswer.getTask().getTask().getText(),70));
        AnswerMessages.removeAll();
        int n=1;
        for(FullMessageBean message : cAnswer.getMessages()){
            AnswerMessages.add("Сообщение "+n+" "+message.getMessage().getText());
            n++;
            }
        //answerStateMashine.refresh(cAnswer);
        AnswerBall.setText(""+cAnswer.getAnswer().getRating());
        AnswerMessageAdd.setEnabled(cAnswer.getAnswer().getState()==AnswerBean.StateEnum.IN_PROGRESS);
        AnswerBallSelector.setEnabled(cAnswer.getAnswer().getState()==AnswerBean.StateEnum.CHECKING);
        AnswerBallSelector.removeAll();
        if(cAnswer.getAnswer().getState()==AnswerBean.StateEnum.CHECKING){
            ExamRuleBean rule = rulesMap.get(cRating.getGroupRating().getExamRuleId());
            boolean question = cAnswer.getTask().getTask().getTaskType()==TaskBean.TaskTypeEnum.QUESTION;
            int maxBall = question ? rule.getSingleQuestionDefaultRating() : rule.getSingleExerciseDefaultRating();
            for(int i=maxBall;i>=0;i--)
                AnswerBallSelector.add(""+i);
            if (question)
                AnswerBallSelector.add(""+(-rule.getSingleQuestionDefaultRating()));
            }
        refreshSelectedMessage();
        }

    public void refreshSelectedMessage(){
        cAnswerMessage=null;
        AnswerArtifactView.setEnabled(false);
        AnswerArtifactDownLoad.setEnabled(false);
        AnswerArtifactUpload.setEnabled(false);
        if (cAnswer==null || cAnswer.getMessages().size()==0)
            return;
        cAnswerMessage = cAnswer.getMessages().get(AnswerMessages.getSelectedIndex());
        AnswerMessageText.setText(UtilsEM.formatSize(cAnswerMessage.getMessage().getText(),70));
        AnswerArtifactUpload.setEnabled(true);
        boolean bb = cAnswerMessage.getArtefact()!=null && cAnswerMessage.getArtefact().getId()!=0;
        AnswerArtifactDownLoad.setEnabled(bb);
        AnswerArtifactView.setEnabled(bb);
        }

    private void refreshDisciplineList(){
        Disciplines.removeAll();
        Themes.removeAll();
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
                    Disciplines.add(dd.getName());
                refreshSelectedDiscipline();
                }
            };
        }

    private void refreshRules(){
        cExamRules.clear();
        cExamRulesMap.clear();
        Rules.removeAll();
        new APICall<List<ExamRuleBean>>(main) {
            @Override
            public Call<List<ExamRuleBean>> apiFun() {
                return main.client.getDisciplineApi().findExamRules(cDiscipline.getDiscipline().getId());
                }
            @Override
            public void onSucess(List<ExamRuleBean> oo) {
                cExamRules = oo;
                for(ExamRuleBean examRule : oo){
                    Rules.add(examRule.getName());
                    cExamRulesMap.put(examRule.getId(),examRule);
                    }
                refreshSelectedRule();
                }
            };
        }

    private void refreshGroupsList(){
        Groups.removeAll();
        Students.removeAll();
        new APICall<List<GroupBean>>(main) {
            @Override
            public Call<List<GroupBean>> apiFun() {
                return main.client.getGroupApi().getAll1();
            }
            @Override
            public void onSucess(List<GroupBean> oo) {
                groups = oo;
                Groups.removeAll();
                groupsMap.clear();
                for(GroupBean dd : groups){
                    Groups.add(dd.getName());
                    groupsMap.put(dd.getId(),dd);
                    }
                refreshGroupFull();
                }
            };
        }
    private void refreshGroupFull(){
        Students.removeAll();
        cGroup=null;
        if (groups.size()==0)
            return;
        long oid = groups.get(Groups.getSelectedIndex()).getId();
        new APICall<FullGroupBean>(main) {
            @Override
            public Call<FullGroupBean> apiFun() {
                return main.client.getGroupApi().getFull2(oid,1);
                }
            @Override
            public void onSucess(FullGroupBean oo) {
                cGroup = oo;
                Students.removeAll();
                for(StudentBean student : cGroup.getStudents())
                    Students.add(student.getAccount().getName());
                //refreshStudentFull();
            }
        };
    }

    private void refreshThemeFull(){
        Tasks.removeAll();
        if (cDiscipline.getThemes().size()==0)
            return;
        cTheme = cDiscipline.getThemes().get(Themes.getSelectedIndex());
        cTheme.getTasks().sort(new Comparator<FullTaskBean>() {
            @Override
            public int compare(FullTaskBean o1, FullTaskBean o2) {              // Сортировать по id (в порядке поступления)
                return o1.getTask().getId()-o2.getTask().getId() >0 ? 1 : -1;
                }
            });
        int iq=1,it=1;
        for(FullTaskBean task : cTheme.getTasks())
            if (task.getTask().getTaskType()== TaskBean.TaskTypeEnum.QUESTION)
                Tasks.add("Вопрос "+iq++);
        for(FullTaskBean task : cTheme.getTasks())
            if (task.getTask().getTaskType()== TaskBean.TaskTypeEnum.EXERCISE)
                Tasks.add("Задача "+it++);
        refreshTaskFull();
        }

    private void refreshTaskFullForce(){
        TaskText.setText("");
        if (cTheme.getTasks().size()==0)
            return;
        refresh=true;
        TaskSaveText.setEnabled(false);
        cTaskNum = Tasks.getSelectedIndex();
        cTask = cTheme.getTasks().get(cTaskNum);
        boolean isTask = cTask.getTask().getTaskType()== TaskBean.TaskTypeEnum.EXERCISE;
        TaskType.setSelected(isTask);
        TaskTypeLabel.setText(isTask ? "Задача" : "Вопрос (тест)");
        TaskText.setText(UtilsEM.formatSize(cTask.getTask().getText(),60));
        boolean bb = cTask.getArtefact()!=null;
        TaskArtifactView.setEnabled(bb);
        TaskArtifactDownLoad.setEnabled(bb);
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
        Rules = new java.awt.Choice();
        jLabel9 = new javax.swing.JLabel();
        RuleName = new javax.swing.JTextField();
        RuleThemeRemove = new javax.swing.JButton();
        RuleAdd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        RuleThemes = new java.awt.Choice();
        RuleThemeAdd = new javax.swing.JButton();
        RuleDelete = new javax.swing.JButton();
        RuleThemeAddAll = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        TaskType = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        RuleOwnRating = new javax.swing.JTextField();
        RuleDuration = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        RuleMinRating = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        RuleExceciseForOne = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        RuleExcerciseSum = new javax.swing.JTextField();
        RuleQuestionForOne = new javax.swing.JTextField();
        RuleQuestionSum = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        AnswerThemeTask = new java.awt.TextArea();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        GroupRatings = new java.awt.Choice();
        jLabel15 = new javax.swing.JLabel();
        TakingForGroup = new javax.swing.JCheckBox();
        Takings = new java.awt.Choice();
        TakingData = new javax.swing.JTextField();
        TakingEndTime = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        GroupRatingAdd = new javax.swing.JButton();
        GroupRatingRemove = new javax.swing.JButton();
        TakingAdd = new javax.swing.JButton();
        TakingRemove = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        TakingDuration = new javax.swing.JTextField();
        TakingName = new javax.swing.JTextField();
        GroupRatingName = new javax.swing.JTextField();
        TakingGroup = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        TakingStartTime = new javax.swing.JTextField();
        RatingStudentList = new java.awt.Choice();
        Состояние = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        RatingQuestionSum = new javax.swing.JTextField();
        RatingSum = new javax.swing.JTextField();
        Answers = new java.awt.Choice();
        jLabel28 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        Состояние1 = new javax.swing.JLabel();
        RatingSemesterSum = new javax.swing.JTextField();
        AnswerBallSelector = new java.awt.Choice();
        jLabel29 = new javax.swing.JLabel();
        RatingOrTakingMode = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        AnswerState = new javax.swing.JTextField();
        TakingState = new javax.swing.JTextField();
        RatingStudentState = new javax.swing.JTextField();
        TakingAddAll = new javax.swing.JButton();
        AnswerBall = new javax.swing.JTextField();
        RatingExcerciseSum1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        AnswerMessages = new java.awt.Choice();
        AnswerMessageText = new java.awt.TextArea();
        AnswerArtifactView = new javax.swing.JButton();
        AnswerArtifactUpload = new javax.swing.JButton();
        AnswerArtifactDownLoad = new javax.swing.JButton();
        AnswerMessageAdd = new javax.swing.JButton();

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

        jLabel5.setText("Студент");
        add(jLabel5);
        jLabel5.setBounds(490, 60, 70, 16);

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
        add(jSeparator1);
        jSeparator1.setBounds(450, 120, 490, 10);

        Rules.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RulesItemStateChanged(evt);
            }
        });
        add(Rules);
        Rules.setBounds(20, 420, 230, 20);

        jLabel9.setText("Регламенты");
        add(jLabel9);
        jLabel9.setBounds(150, 390, 80, 20);

        RuleName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleNameKeyPressed(evt);
            }
        });
        add(RuleName);
        RuleName.setBounds(20, 450, 230, 25);

        RuleThemeRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RuleThemeRemove.setBorderPainted(false);
        RuleThemeRemove.setContentAreaFilled(false);
        RuleThemeRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeRemoveActionPerformed(evt);
            }
        });
        add(RuleThemeRemove);
        RuleThemeRemove.setBounds(390, 540, 30, 30);

        RuleAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        RuleAdd.setBorderPainted(false);
        RuleAdd.setContentAreaFilled(false);
        RuleAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleAddActionPerformed(evt);
            }
        });
        add(RuleAdd);
        RuleAdd.setBounds(270, 410, 30, 30);

        jLabel11.setText("Темы");
        add(jLabel11);
        jLabel11.setBounds(20, 550, 50, 16);
        add(RuleThemes);
        RuleThemes.setBounds(70, 550, 310, 20);

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
        RuleDelete.setBounds(310, 410, 30, 30);

        RuleThemeAddAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        RuleThemeAddAll.setBorderPainted(false);
        RuleThemeAddAll.setContentAreaFilled(false);
        RuleThemeAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuleThemeAddAllActionPerformed(evt);
            }
        });
        add(RuleThemeAddAll);
        RuleThemeAddAll.setBounds(350, 410, 30, 30);

        jLabel12.setText("Все темы");
        add(jLabel12);
        jLabel12.setBounds(350, 390, 80, 16);

        TaskType.setText("Задача(1)/Вопрос(0)");
        TaskType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TaskTypeItemStateChanged(evt);
            }
        });
        add(TaskType);
        TaskType.setBounds(180, 100, 160, 20);

        jLabel17.setText("Рейтинг экзамена");
        add(jLabel17);
        jLabel17.setBounds(260, 510, 110, 20);

        RuleOwnRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleOwnRatingKeyPressed(evt);
            }
        });
        add(RuleOwnRating);
        RuleOwnRating.setBounds(380, 510, 50, 25);

        RuleDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleDurationKeyPressed(evt);
            }
        });
        add(RuleDuration);
        RuleDuration.setBounds(380, 450, 50, 25);

        jLabel22.setText("Продолж. (мин)");
        add(jLabel22);
        jLabel22.setBounds(260, 450, 100, 20);

        RuleMinRating.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleMinRatingKeyPressed(evt);
            }
        });
        add(RuleMinRating);
        RuleMinRating.setBounds(380, 480, 50, 25);

        jLabel23.setText("Рейтинг допуска");
        add(jLabel23);
        jLabel23.setBounds(260, 480, 110, 20);

        jLabel7.setText("Задача: за одну - сумма ");
        add(jLabel7);
        jLabel7.setBounds(20, 510, 140, 20);

        RuleExceciseForOne.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExceciseForOneKeyPressed(evt);
            }
        });
        add(RuleExceciseForOne);
        RuleExceciseForOne.setBounds(170, 510, 30, 25);

        jLabel13.setText("Тест: за один - сумма");
        add(jLabel13);
        jLabel13.setBounds(20, 480, 140, 20);

        RuleExcerciseSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleExcerciseSumKeyPressed(evt);
            }
        });
        add(RuleExcerciseSum);
        RuleExcerciseSum.setBounds(210, 510, 40, 25);

        RuleQuestionForOne.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQuestionForOneKeyPressed(evt);
            }
        });
        add(RuleQuestionForOne);
        RuleQuestionForOne.setBounds(170, 480, 30, 25);

        RuleQuestionSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RuleQuestionSumKeyPressed(evt);
            }
        });
        add(RuleQuestionSum);
        RuleQuestionSum.setBounds(210, 480, 40, 25);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator4);
        jSeparator4.setBounds(485, 10, 3, 110);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator2);
        jSeparator2.setBounds(447, 120, 10, 450);

        AnswerThemeTask.setEditable(false);
        add(AnswerThemeTask);
        AnswerThemeTask.setBounds(10, 580, 490, 80);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        add(jSeparator3);
        jSeparator3.setBounds(447, 120, 10, 450);

        jLabel14.setText("Группы по дисциплине (рейтинги)");
        add(jLabel14);
        jLabel14.setBounds(460, 130, 250, 16);

        GroupRatings.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GroupRatingsItemStateChanged(evt);
            }
        });
        add(GroupRatings);
        GroupRatings.setBounds(460, 150, 230, 20);

        jLabel15.setText("Продолж. (мин)");
        add(jLabel15);
        jLabel15.setBounds(780, 280, 100, 16);

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
        TakingForGroup.setBounds(820, 180, 110, 20);

        Takings.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TakingsItemStateChanged(evt);
            }
        });
        add(Takings);
        Takings.setBounds(460, 220, 230, 20);

        TakingData.setEnabled(false);
        TakingData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TakingDataMouseClicked(evt);
            }
        });
        add(TakingData);
        TakingData.setBounds(700, 250, 110, 25);

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
        jLabel20.setBounds(780, 230, 40, 16);

        GroupRatingAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        GroupRatingAdd.setBorderPainted(false);
        GroupRatingAdd.setContentAreaFilled(false);
        GroupRatingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupRatingAddActionPerformed(evt);
            }
        });
        add(GroupRatingAdd);
        GroupRatingAdd.setBounds(700, 140, 30, 30);

        GroupRatingRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        GroupRatingRemove.setBorderPainted(false);
        GroupRatingRemove.setContentAreaFilled(false);
        GroupRatingRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupRatingRemoveActionPerformed(evt);
            }
        });
        add(GroupRatingRemove);
        GroupRatingRemove.setBounds(740, 140, 30, 30);

        TakingAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        TakingAdd.setBorderPainted(false);
        TakingAdd.setContentAreaFilled(false);
        TakingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingAddActionPerformed(evt);
            }
        });
        add(TakingAdd);
        TakingAdd.setBounds(700, 210, 30, 30);

        TakingRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        TakingRemove.setBorderPainted(false);
        TakingRemove.setContentAreaFilled(false);
        TakingRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingRemoveActionPerformed(evt);
            }
        });
        add(TakingRemove);
        TakingRemove.setBounds(740, 210, 30, 30);

        jLabel21.setText("Оконч.");
        add(jLabel21);
        jLabel21.setBounds(880, 230, 60, 16);

        TakingDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TakingDurationKeyPressed(evt);
            }
        });
        add(TakingDuration);
        TakingDuration.setBounds(880, 280, 50, 25);

        TakingName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TakingNameKeyPressed(evt);
            }
        });
        add(TakingName);
        TakingName.setBounds(460, 250, 230, 25);

        GroupRatingName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GroupRatingNameKeyPressed(evt);
            }
        });
        add(GroupRatingName);
        GroupRatingName.setBounds(460, 175, 230, 25);

        TakingGroup.setEnabled(false);
        add(TakingGroup);
        TakingGroup.setBounds(820, 205, 70, 25);

        jLabel24.setText("Начало");
        add(jLabel24);
        jLabel24.setBounds(820, 230, 70, 16);

        TakingStartTime.setEnabled(false);
        add(TakingStartTime);
        TakingStartTime.setBounds(820, 250, 50, 25);

        RatingStudentList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RatingStudentListItemStateChanged(evt);
            }
        });
        add(RatingStudentList);
        RatingStudentList.setBounds(520, 375, 250, 20);

        Состояние.setText("Состояние обучения");
        add(Состояние);
        Состояние.setBounds(650, 400, 130, 16);

        jLabel26.setText("Семестр  Вопросы/Задачи/Итог");
        add(jLabel26);
        jLabel26.setBounds(460, 400, 180, 16);

        RatingQuestionSum.setEnabled(false);
        add(RatingQuestionSum);
        RatingQuestionSum.setBounds(520, 420, 35, 25);

        RatingSum.setEnabled(false);
        add(RatingSum);
        RatingSum.setBounds(600, 420, 35, 25);

        Answers.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                AnswersItemStateChanged(evt);
            }
        });
        add(Answers);
        Answers.setBounds(520, 450, 250, 20);

        jLabel28.setText("Балл");
        add(jLabel28);
        jLabel28.setBounds(660, 470, 60, 16);
        add(jSeparator6);
        jSeparator6.setBounds(460, 350, 470, 10);

        Состояние1.setText("Состояние ответа");
        add(Состояние1);
        Состояние1.setBounds(520, 470, 140, 16);

        RatingSemesterSum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RatingSemesterSumKeyPressed(evt);
            }
        });
        add(RatingSemesterSum);
        RatingSemesterSum.setBounds(460, 420, 40, 25);

        AnswerBallSelector.setEnabled(false);
        add(AnswerBallSelector);
        AnswerBallSelector.setBounds(720, 490, 50, 20);

        jLabel29.setText("Ответы");
        add(jLabel29);
        jLabel29.setBounds(460, 450, 60, 16);

        RatingOrTakingMode.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        RatingOrTakingMode.setText("По приему экзамена (1) / По группе (0)");
        RatingOrTakingMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RatingOrTakingModeItemStateChanged(evt);
            }
        });
        add(RatingOrTakingMode);
        RatingOrTakingMode.setBounds(460, 355, 280, 20);

        jLabel27.setText("Студент");
        add(jLabel27);
        jLabel27.setBounds(460, 380, 60, 16);

        AnswerState.setEnabled(false);
        AnswerState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AnswerStateMouseClicked(evt);
            }
        });
        add(AnswerState);
        AnswerState.setBounds(520, 490, 140, 25);

        TakingState.setEnabled(false);
        TakingState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TakingStateMouseClicked(evt);
            }
        });
        add(TakingState);
        TakingState.setBounds(460, 300, 140, 25);

        RatingStudentState.setEnabled(false);
        RatingStudentState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RatingStudentStateMouseClicked(evt);
            }
        });
        add(RatingStudentState);
        RatingStudentState.setBounds(640, 420, 130, 25);

        TakingAddAll.setText("Назначить всех");
        TakingAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakingAddAllActionPerformed(evt);
            }
        });
        add(TakingAddAll);
        TakingAddAll.setBounds(810, 310, 120, 22);

        AnswerBall.setEnabled(false);
        add(AnswerBall);
        AnswerBall.setBounds(670, 490, 40, 25);

        RatingExcerciseSum1.setEnabled(false);
        add(RatingExcerciseSum1);
        RatingExcerciseSum1.setBounds(560, 420, 35, 25);

        jLabel25.setText("Сообщения");
        add(jLabel25);
        jLabel25.setBounds(450, 520, 70, 16);

        AnswerMessages.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                AnswerMessagesItemStateChanged(evt);
            }
        });
        add(AnswerMessages);
        AnswerMessages.setBounds(520, 520, 310, 20);
        add(AnswerMessageText);
        AnswerMessageText.setBounds(520, 550, 350, 110);

        AnswerArtifactView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/camera.png"))); // NOI18N
        AnswerArtifactView.setBorderPainted(false);
        AnswerArtifactView.setContentAreaFilled(false);
        AnswerArtifactView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerArtifactViewActionPerformed(evt);
            }
        });
        add(AnswerArtifactView);
        AnswerArtifactView.setBounds(880, 630, 40, 30);

        AnswerArtifactUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        AnswerArtifactUpload.setBorderPainted(false);
        AnswerArtifactUpload.setContentAreaFilled(false);
        AnswerArtifactUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerArtifactUploadActionPerformed(evt);
            }
        });
        add(AnswerArtifactUpload);
        AnswerArtifactUpload.setBounds(880, 560, 40, 30);

        AnswerArtifactDownLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/download.png"))); // NOI18N
        AnswerArtifactDownLoad.setBorderPainted(false);
        AnswerArtifactDownLoad.setContentAreaFilled(false);
        AnswerArtifactDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerArtifactDownLoadActionPerformed(evt);
            }
        });
        add(AnswerArtifactDownLoad);
        AnswerArtifactDownLoad.setBounds(880, 600, 40, 30);

        AnswerMessageAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AnswerMessageAdd.setBorderPainted(false);
        AnswerMessageAdd.setContentAreaFilled(false);
        AnswerMessageAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerMessageAddActionPerformed(evt);
            }
        });
        add(AnswerMessageAdd);
        AnswerMessageAdd.setBounds(880, 520, 30, 30);
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
                        refreshSelectedDiscipline();
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

    private void DisciplinesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DisciplinesItemStateChanged
        refreshSelectedDiscipline();
    }//GEN-LAST:event_DisciplinesItemStateChanged

    private void ThemesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ThemesItemStateChanged
        refreshThemeFull();
    }//GEN-LAST:event_ThemesItemStateChanged

    private void TasksItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TasksItemStateChanged
        refreshTaskFull();
    }//GEN-LAST:event_TasksItemStateChanged

    private void TaskArtifactViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskArtifactViewActionPerformed

    }//GEN-LAST:event_TaskArtifactViewActionPerformed

    private void TaskArtifactUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaskArtifactUploadActionPerformed
        main.uploadFileAsync(new I_Value<ArtefactBean>() {
            @Override
            public void onEnter(ArtefactBean value) {
                cTask.getTask().setArtefactId(value.getId());
                taskUpdate();
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
                int idx = RuleThemes.getSelectedIndex();
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

    private void RulesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RulesItemStateChanged
        refreshSelectedRule();
    }//GEN-LAST:event_RulesItemStateChanged

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

    private void RuleOwnRatingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleOwnRatingKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setMinimalExamRating(Integer.parseInt(RuleOwnRating.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleOwnRatingKeyPressed

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

    private void RuleMinRatingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleMinRatingKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setMinimalSemesterRating(Integer.parseInt(RuleMinRating.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleMinRatingKeyPressed

    private void RuleExceciseForOneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleExceciseForOneKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setSingleExerciseDefaultRating(Integer.parseInt(RuleExceciseForOne.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleExceciseForOneKeyPressed

    private void RuleExcerciseSumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleExcerciseSumKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setExercisesRatingSum(Integer.parseInt(RuleExcerciseSum.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleExcerciseSumKeyPressed

    private void RuleQuestionForOneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RuleQuestionForOneKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRule==null) return;
        try {
            cRule.setSingleQuestionDefaultRating(Integer.parseInt(RuleQuestionForOne.getText()));
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
            cRule.setQuestionsRatingSum(Integer.parseInt(RuleQuestionSum.getText()));
            ruleUpdate(evt);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_RuleQuestionSumKeyPressed

    private void GroupRatingsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GroupRatingsItemStateChanged
        refreshSelectedRating(false);
        if (!RatingOrTakingMode.isSelected())
        refreshStudRatings();
    }//GEN-LAST:event_GroupRatingsItemStateChanged

    private void TakingForGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TakingForGroupItemStateChanged
        if (isRefresh)
        return;
        if (cTaking==null)
        return;
        if (TakingForGroup.isSelected() && GroupRatings.getItemCount()==0){
            popup("Нет групп в списке к экзамену");
            TakingForGroup.setSelected(false);
            return;
            }
        cTaking.getExam().setOneGroup(TakingForGroup.isSelected());
        if (cTaking.getExam().isOneGroup()){
            long oid = cRating.getGroup().getGroup().getId();
            cTaking.getExam().setGroupId(oid);
            }
        takingUpdate();
    }//GEN-LAST:event_TakingForGroupItemStateChanged

    private void TakingForGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingForGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TakingForGroupActionPerformed

    private void TakingsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TakingsItemStateChanged
        refreshSelectedTaking();
        if (RatingOrTakingMode.isSelected())
        refreshStudRatings();
    }//GEN-LAST:event_TakingsItemStateChanged

    private void TakingDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TakingDataMouseClicked
        if (evt.getClickCount()<2)
        return;
        if (cTaking ==null)
        return;
        new CalendarView("Начало экзамена", new I_CalendarTime() {
            @Override
            public void onSelect(OwnDateTime time) {
                cTaking.getExam().setStart(time.timeInMS());
                cTaking.getExam().setEnd(time.timeInMS()+Integer.parseInt(TakingDuration.getText()));
                takingUpdate();
            }
        });
    }//GEN-LAST:event_TakingDataMouseClicked

    private void GroupRatingAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupRatingAddActionPerformed
        if (cGroup==null || cDiscipline==null || cRule==null)
        return;
        new OK(200, 200, "Добавить рейтинг " + cDiscipline.getDiscipline().getName()+"-"+cGroup.getGroup().getName(), new I_Button() {
            @Override
            public void onPush() {
                final GroupRatingBean exam = new GroupRatingBean();
                exam.setExamRuleId(cRule.getId());
                exam.setDisciplineId(cDiscipline.getDiscipline().getId());
                exam.setGroupId(cGroup.getGroup().getId());
                exam.setName(cDiscipline.getDiscipline().getName()+"-"+cGroup.getGroup().getName());
                new APICall<GroupRatingBean>(main) {
                    @Override
                    public Call<GroupRatingBean> apiFun() {
                        return main.client.getGroupRatingApi().create2(exam);
                    }
                    @Override
                    public void onSucess(GroupRatingBean oo) {
                        refreshSelectedDiscipline(GroupRatings.getItemCount()!=0);
                    }
                };
            }
        });
    }//GEN-LAST:event_GroupRatingAddActionPerformed

    private void GroupRatingRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupRatingRemoveActionPerformed
        if (GroupRatings.getItemCount()==0)
        return;
        new OK(200, 200, "Удалить рейтинг " + GroupRatings.getItem(GroupRatings.getSelectedIndex()), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getGroupRatingApi().delete2(cRating.getGroupRating().getId());
                    }
                    @Override
                    public void onSucess(Void oo) {
                        refreshSelectedDiscipline(GroupRatings.getItemCount()!=1);
                        popup("Рейтинг удален");
                    }
                };
            }
        });
    }//GEN-LAST:event_GroupRatingRemoveActionPerformed

    private void TakingAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingAddActionPerformed
        if (cRating ==null)
        return;
        new OK(200, 200, "Добавить сдачу экзамена по: " + cDiscipline.getDiscipline().getName(), new I_Button() {
            @Override
            public void onPush() {
                ExamBean taking = new ExamBean();
                taking.setState(ExamBean.StateEnum.REDACTION);
                taking.setName("Новый прием экзамена");
                taking.setDisciplineId(cDiscipline.getDiscipline().getId());
                new APICall<ExamBean>(main) {
                    @Override
                    public Call<ExamBean> apiFun() {
                        return main.client.getExamApi().createExam(taking);
                        }
                    @Override
                    public void onSucess(ExamBean oo) {
                        refreshSelectedDiscipline(Takings.getItemCount()!=0);
                    }
                };
            }
        });
    }//GEN-LAST:event_TakingAddActionPerformed

    private void TakingRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingRemoveActionPerformed
        if (cTaking ==null)
        return;
        new OK(200, 200, "Удалить прием: " + cTaking.getExam().getName(), new I_Button() {
            @Override
            public void onPush() {
                new APICall<Void>(main) {
                    @Override
                    public Call<Void> apiFun() {
                        return main.client.getExamApi().deleteExam(cTaking.getExam().getId());
                    }
                    @Override
                    public void onSucess(Void oo) {
                        refreshSelectedDiscipline();
                        popup("Прием удален");
                    }
                };
            }
        });
    }//GEN-LAST:event_TakingRemoveActionPerformed

    public void takingUpdate(){
        new APICall<ExamBean>(main) {
            @Override
            public Call<ExamBean> apiFun() {
                return main.client.getExamApi().updateExam(cTaking.getExam());
            }
            @Override
            public void onSucess(ExamBean oo) {
                savePos();
                popup("Прием экзамена обновлен");
                refreshSelectedDiscipline();
                }
            };
        }

    private void TakingDurationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TakingDurationKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cTaking==null) return;
        try {
            cTaking.getExam().setEnd(cTaking.getExam().getStart()+Integer.parseInt(TakingDuration.getText()));
            takingUpdate();
            main.viewUpdate(evt,true);
        } catch (Exception ee){
            popup("Ошибка формата целого");
            main.viewUpdate(evt,false);
        }
    }//GEN-LAST:event_TakingDurationKeyPressed

    private void TakingNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TakingNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cTaking ==null) return;
        cTaking.getExam().setName(TakingName.getText());
        takingUpdate();
        main.viewUpdate(evt,true);
    }//GEN-LAST:event_TakingNameKeyPressed

    private void GroupRatingNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GroupRatingNameKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cRating ==null) return;
        cRating.getGroupRating().setName(GroupRatingName.getText());
        ratingUpdate();
        main.viewUpdate(evt,true);
    }//GEN-LAST:event_GroupRatingNameKeyPressed

    private void RatingStudentListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingStudentListItemStateChanged
        cStudRating = studRatings.get(RatingStudentList.getSelectedIndex());
        refreshSelectedStudRating();
    }//GEN-LAST:event_RatingStudentListItemStateChanged

    private void AnswersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_AnswersItemStateChanged
        refreshSelectedAnswer();
    }//GEN-LAST:event_AnswersItemStateChanged

    private void RatingSemesterSumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RatingSemesterSumKeyPressed
        if(evt.getKeyCode()!=10) return;
        if (cStudRating==null)
        return;
        try{
            cStudRating.getStudentRating().setSemesterRating(Integer.parseInt(RatingSemesterSum.getText()));
            //studRatingStateMashine.forceStateChange(cStudRating,Values.StudRatingAllowed);
        } catch(Exception ee){
            popup("Ошибка формата целого");
        }
    }//GEN-LAST:event_RatingSemesterSumKeyPressed

    public void ratingUpdate(){
        if (cRating ==null) return;
        new APICall<GroupRatingBean>(main) {
            @Override
            public Call<GroupRatingBean> apiFun() {
                return main.client.getGroupRatingApi().update2(cRating.getGroupRating());
            }
            @Override
            public void onSucess(GroupRatingBean oo) {
                savePos();
                refreshSelectedDiscipline();
                }
            };
        }
    private void loadStudRating(final  List<StudentRatingBean> oo, final int idx){
        if (idx==oo.size()){
            createRatingStudentList();
            refreshSelectedStudRating();
            return;
            }
        new APICall<FullStudentRatingBean>(main) {
            @Override
            public Call<FullStudentRatingBean> apiFun() {
                return main.client.getStudentRatingApi().getFull1(oo.get(idx).getId(),2);
                }
            @Override
            public void onSucess(FullStudentRatingBean vv) {
                studRatings.add(vv);
                loadStudRating(oo,idx+1);
                }
            };
        }
    public void refreshStudRatings(){
        //studRatingStateMashine.clear();
        studRatings.clear();
        if (RatingOrTakingMode.isSelected()){
            if (cTaking==null)
                return;
            new APICall<List<StudentRatingBean>>(main) {
                @Override
                public Call<List<StudentRatingBean>> apiFun() {
                    return main.client.getExamApi().getStudentRatings(cTaking.getExam().getId());
                    }
                @Override
                public void onSucess(List<StudentRatingBean> oo) {
                    loadStudRating(oo,0);
                    }
                };
            }
        else{
            if (cRating ==null)
                return;
            new APICall<FullGroupRatingBean>(main) {
                @Override
                public Call<FullGroupRatingBean> apiFun() {
                    return main.client.getGroupRatingApi().findFull1(cRating.getGroupRating().getId(),2);
                }
                @Override
                public void onSucess(FullGroupRatingBean oo) {
                    cGroupRating = oo;
                    studRatings = oo.getStudentRatings();
                    createRatingStudentList();
                    refreshSelectedStudRating();
                }
            };
        }

    }

    public void createRatingStudentList(){
        RatingStudentList.removeAll();
        for(FullStudentRatingBean rating : studRatings)
            RatingStudentList.add(rating.getStudent().getStudent().getAccount().getName());
        refreshSelectedStudRating();
        }

    public void refreshSelectedStudRating(){
        answerClear();
        cStudRating=null;
        if (studRatings.size()==0)
            return;
        cStudRating = studRatings.get(RatingStudentList.getSelectedIndex());
        refreshStudRatingFull(true);
        StudentRatingBean studentRating = cStudRating.getStudentRating();
        RatingSemesterSum.setEnabled(studentRating.getStudentRatingState()== StudentRatingBean.StudentRatingStateEnum.NOT_ALLOWED);
        RatingSum.setText(""+ studentRating.getExerciseRating());
        int val1= studentRating.getSemesterRating();
        int val2= studentRating.getQuestionRating();
        int val3 = studentRating.getExerciseRating();
        RatingSemesterSum.setText(""+ val1);
        RatingQuestionSum.setText(""+ val2);
        RatingExcerciseSum1.setText(""+val3);
        RatingSum.setText(""+(val1+val2+val3));
        RatingStudentState.setText(ratingStateFactory.getByValue(cStudRating.getStudentRating().getStudentRatingState()));
        //studRatingStateMashine.refresh(cStudRating);
        }

    public void answerClear(){
        AnswerBallSelector.removeAll();
        AnswerMessageText.setText("");
        AnswerThemeTask.setText("");
        AnswerState.setText("");
        AnswerArtifactView.setEnabled(false);
        AnswerMessageAdd.setEnabled(false);
        AnswerArtifactDownLoad.setEnabled(false);
        AnswerArtifactUpload.setEnabled(false);
        AnswerMessages.removeAll();
        //answerStateMashine.clear();
        }

    private void RatingOrTakingModeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RatingOrTakingModeItemStateChanged
        refreshStudRatings();
    }//GEN-LAST:event_RatingOrTakingModeItemStateChanged

    private void AnswerStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AnswerStateMouseClicked
        if (evt.getClickCount()<2)
        return;
        if (cAnswer ==null)
        return;
        new ListSelector(200, 200, "Состояние приема", answerStateFactory.getNames(), new I_ListSelected() {
            @Override
            public void onSelect(final int idx) {
                new OK(200, 200, "Сменить состояние на " + answerStateFactory.getList().get(idx).name, new I_Button() {
                    @Override
                    public void onPush() {
                        cAnswer.getAnswer().setState(answerStateFactory.getList().get(idx).value);
                        answerUpdateState();
                    }
                });
            }
        });
    }//GEN-LAST:event_AnswerStateMouseClicked


    private void answerUpdateState(){
        new APICall<AnswerBean>(main) {
            @Override
            public Call<AnswerBean> apiFun() {
                return main.client.getAnswerApi().updateState2(cAnswer.getAnswer());
            }
            @Override
            public void onSucess(AnswerBean oo) {
                refreshSelectedDiscipline();
                popup("Состояние ответа изменено");
            }
        };
    }


    private void takingUpdateState(){
        new APICall<ExamBean>(main) {
            @Override
            public Call<ExamBean> apiFun() {
                return main.client.getExamApi().updateState1(cTaking.getExam());
                }
            @Override
            public void onSucess(ExamBean oo) {
                refreshSelectedDiscipline();
                popup("Состояние приема экзамена изменено");
            }
        };
    }


    private void ratingUpdateState(){
        new APICall<StudentRatingBean>(main) {
            @Override
            public Call<StudentRatingBean> apiFun() {
                return main.client.getStudentRatingApi().updateState(cStudRating.getStudentRating());
            }
            @Override
            public void onSucess(StudentRatingBean oo) {
                refreshSelectedDiscipline();
                popup("Состояние сдачи студентом изменено");
            }
        };
    }


    private void TakingStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TakingStateMouseClicked
        if (evt.getClickCount()<2)
        return;
        if (cTaking ==null)
        return;
        new ListSelector(200, 200, "Состояние приема", takingStateFactory.getNames(), new I_ListSelected() {
            @Override
            public void onSelect(final int idx) {
                new OK(200, 200, "Сменить состояние на " + takingStateFactory.getList().get(idx).name, new I_Button() {
                    @Override
                    public void onPush() {
                        cTaking.getExam().setState(takingStateFactory.getList().get(idx).value);
                        takingUpdateState();
                    }
                });
            }
        });
    }//GEN-LAST:event_TakingStateMouseClicked

    private void RatingStudentStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RatingStudentStateMouseClicked
        if (evt.getClickCount()<2)
        return;
        if (cStudRating ==null)
        return;
        new ListSelector(200, 200, "Состояние приема", ratingStateFactory.getNames(), new I_ListSelected() {
            @Override
            public void onSelect(final int idx) {
                new OK(200, 200, "Сменить состояние на " + ratingStateFactory.getList().get(idx).name, new I_Button() {
                    @Override
                    public void onPush() {
                        cStudRating.getStudentRating().studentRatingState(ratingStateFactory.getList().get(idx).value);
                        ratingUpdateState();
                    }
                });
            }
        });
    }//GEN-LAST:event_RatingStudentStateMouseClicked

    private void TakingAddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TakingAddAllActionPerformed
        ExamBean examBean = new ExamBean();
        examBean.setDisciplineId(cDiscipline.getDiscipline().getId());
        examBean.setOneGroup(false);
        examBean.setName("Новый приемп экзамена");
        new APICall<ExamBean>(main) {
            @Override
            public Call<ExamBean> apiFun() {
                return main.client.getExamApi().createExam(examBean);
                }
            @Override
            public void onSucess(ExamBean oo) {
                savePos();
                refreshSelectedDiscipline();
                }
            };
    }//GEN-LAST:event_TakingAddAllActionPerformed

    private void AnswerMessagesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_AnswerMessagesItemStateChanged
        refreshSelectedMessage();
    }//GEN-LAST:event_AnswerMessagesItemStateChanged

    private void AnswerArtifactViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerArtifactViewActionPerformed

    }//GEN-LAST:event_AnswerArtifactViewActionPerformed

    private void AnswerArtifactUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerArtifactUploadActionPerformed
        main.uploadFileAsync(new I_Value<ArtefactBean>() {
            @Override
            public void onEnter(final ArtefactBean value) {
                MessageBean message = new MessageBean();
                message.setText(AnswerMessageText.getText());
                message.setAccountId(main.loginUser.getOid());
                new APICall<MessageBean>(main) {
                    @Override
                    public Call<MessageBean> apiFun() {
                        return main.client.getAnswerApi().newMessage(message,cAnswer.getAnswer().getId());
                    }
                    @Override
                    public void onSucess(MessageBean oo) {
                        popup("Ответ с фото");
                        savePos();
                        refreshStudRatingFull(true);
                    }
                };
            }
        });
    }//GEN-LAST:event_AnswerArtifactUploadActionPerformed

     private void AnswerArtifactDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerArtifactDownLoadActionPerformed

    }//GEN-LAST:event_AnswerArtifactDownLoadActionPerformed

    private void AnswerMessageAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerMessageAddActionPerformed
        if (cAnswer==null)
        return;
        new OK(200, 200, "Добавить сообщение к ответу", new I_Button() {
            @Override
            public void onPush() {
                final MessageBean message = new MessageBean();
                message.setText(AnswerMessageText.getText());
                message.setAccountId(main.loginUser.getOid());
                new APICall<MessageBean>(main) {
                    @Override
                    public Call<MessageBean> apiFun() {
                        return main.client.getAnswerApi().newMessage(message,cAnswer.getAnswer().getId());
                    }
                    @Override
                    public void onSucess(MessageBean oo) {
                        popup("Ответ изменен");
                        savePos();
                        refreshStudRatingFull(true);
                    }
                };
            }
        });
    }//GEN-LAST:event_AnswerMessageAddActionPerformed

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
    private javax.swing.JButton AnswerArtifactDownLoad;
    private javax.swing.JButton AnswerArtifactUpload;
    private javax.swing.JButton AnswerArtifactView;
    private javax.swing.JTextField AnswerBall;
    private java.awt.Choice AnswerBallSelector;
    private javax.swing.JButton AnswerMessageAdd;
    private java.awt.TextArea AnswerMessageText;
    private java.awt.Choice AnswerMessages;
    private javax.swing.JTextField AnswerState;
    private java.awt.TextArea AnswerThemeTask;
    private java.awt.Choice Answers;
    private javax.swing.JButton DisciplineAdd;
    private javax.swing.JButton DisciplineEdit;
    private javax.swing.JButton DisciplineImport;
    private javax.swing.JButton DisciplineRemove;
    private javax.swing.JButton DisciplineSaveImport;
    private java.awt.Choice Disciplines;
    private javax.swing.JCheckBox FullTrace;
    private javax.swing.JButton GroupAdd;
    private javax.swing.JButton GroupEdit;
    private javax.swing.JButton GroupRatingAdd;
    private javax.swing.JTextField GroupRatingName;
    private javax.swing.JButton GroupRatingRemove;
    private java.awt.Choice GroupRatings;
    private javax.swing.JButton GroupRemove;
    private java.awt.Choice Groups;
    private javax.swing.JButton GroupsImport;
    private javax.swing.JTextField RatingExcerciseSum1;
    private javax.swing.JCheckBox RatingOrTakingMode;
    private javax.swing.JTextField RatingQuestionSum;
    private javax.swing.JTextField RatingSemesterSum;
    private java.awt.Choice RatingStudentList;
    private javax.swing.JTextField RatingStudentState;
    private javax.swing.JTextField RatingSum;
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
    private javax.swing.JButton TakingAddAll;
    private javax.swing.JTextField TakingData;
    private javax.swing.JTextField TakingDuration;
    private javax.swing.JTextField TakingEndTime;
    private javax.swing.JCheckBox TakingForGroup;
    private javax.swing.JTextField TakingGroup;
    private javax.swing.JTextField TakingName;
    private javax.swing.JButton TakingRemove;
    private javax.swing.JTextField TakingStartTime;
    private javax.swing.JTextField TakingState;
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel Состояние;
    private javax.swing.JLabel Состояние1;
    // End of variables declaration//GEN-END:variables
}
