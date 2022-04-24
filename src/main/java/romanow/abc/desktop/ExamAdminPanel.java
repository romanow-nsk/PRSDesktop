/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import romanow.abc.bridge.APICallSync;
import romanow.abc.bridge.constants.TaskType;
import romanow.abc.bridge.constants.UserRole;
import romanow.abc.convert.onewayticket.OWTDiscipline;
import romanow.abc.convert.onewayticket.OWTReader;
import romanow.abc.convert.onewayticket.OWTTheme;
import romanow.abc.core.Utils;
import romanow.abc.core.entity.artifacts.ArtifactTypes;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.exam.model.*;
import romanow.abc.excel.ExcelX2;
import romanow.abc.excel.I_ExcelBack;

import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author romanow
 */
public class ExamAdminPanel extends BasePanel{
    private List<DisciplineBean> disciplines = new ArrayList<>();
    private FullDisciplineBean cDiscipline = null;
    private FullThemeBean cTheme = null;
    private FullTaskBean cTask=null;
    private int cTaskNum=0;
    private OWTDiscipline owtImportData = null;
    private List<GroupBean> groups = new ArrayList<>();
    private FullGroupBean cGroup = null;
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
                }
            };
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
        long oid = disciplines.get(Discipline.getSelectedIndex()).getId();
        new APICall<FullDisciplineBean>(main) {
            @Override
            public Call<FullDisciplineBean> apiFun() {
                return main.client.getDisciplineApi().getOne5(oid,2);
                }
            @Override
            public void onSucess(FullDisciplineBean oo) {
                cDiscipline = oo;
                Theme.removeAll();
                for(FullThemeBean theme : cDiscipline.getThemes())
                    Theme.add(theme.getTheme().getName());
                refreshThemeFull();
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
        Discipline.setBounds(20, 40, 240, 20);

        Theme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ThemeItemStateChanged(evt);
            }
        });
        add(Theme);
        Theme.setBounds(20, 80, 240, 20);

        RefreshDisciplines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshDisciplines.setBorderPainted(false);
        RefreshDisciplines.setContentAreaFilled(false);
        RefreshDisciplines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshDisciplinesActionPerformed(evt);
            }
        });
        add(RefreshDisciplines);
        RefreshDisciplines.setBounds(230, 5, 30, 30);

        DisciplineImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload.png"))); // NOI18N
        DisciplineImport.setBorderPainted(false);
        DisciplineImport.setContentAreaFilled(false);
        DisciplineImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineImportActionPerformed(evt);
            }
        });
        add(DisciplineImport);
        DisciplineImport.setBounds(400, 40, 40, 30);

        RemoveTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveTask.setBorderPainted(false);
        RemoveTask.setContentAreaFilled(false);
        RemoveTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveTaskActionPerformed(evt);
            }
        });
        add(RemoveTask);
        RemoveTask.setBounds(310, 110, 30, 30);

        AddTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddTask.setBorderPainted(false);
        AddTask.setContentAreaFilled(false);
        AddTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddTaskActionPerformed(evt);
            }
        });
        add(AddTask);
        AddTask.setBounds(270, 110, 30, 30);

        AddDiscipline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddDiscipline.setBorderPainted(false);
        AddDiscipline.setContentAreaFilled(false);
        AddDiscipline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddDisciplineActionPerformed(evt);
            }
        });
        add(AddDiscipline);
        AddDiscipline.setBounds(270, 35, 30, 30);

        RemoveDiscipline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveDiscipline.setBorderPainted(false);
        RemoveDiscipline.setContentAreaFilled(false);
        RemoveDiscipline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveDisciplineActionPerformed(evt);
            }
        });
        add(RemoveDiscipline);
        RemoveDiscipline.setBounds(310, 35, 30, 30);

        AddTheme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddTheme.setBorderPainted(false);
        AddTheme.setContentAreaFilled(false);
        AddTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddThemeActionPerformed(evt);
            }
        });
        add(AddTheme);
        AddTheme.setBounds(270, 75, 30, 30);

        RemoveTheme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveTheme.setBorderPainted(false);
        RemoveTheme.setContentAreaFilled(false);
        RemoveTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveThemeActionPerformed(evt);
            }
        });
        add(RemoveTheme);
        RemoveTheme.setBounds(310, 75, 30, 30);

        Task.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TaskItemStateChanged(evt);
            }
        });
        add(Task);
        Task.setBounds(20, 120, 240, 20);

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
        EditTask.setBounds(350, 110, 30, 30);

        EditDiscipline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditDiscipline.setBorderPainted(false);
        EditDiscipline.setContentAreaFilled(false);
        EditDiscipline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditDisciplineActionPerformed(evt);
            }
        });
        add(EditDiscipline);
        EditDiscipline.setBounds(350, 35, 30, 30);

        EditTheme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditTheme.setBorderPainted(false);
        EditTheme.setContentAreaFilled(false);
        EditTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditThemeActionPerformed(evt);
            }
        });
        add(EditTheme);
        EditTheme.setBounds(350, 75, 30, 30);

        FullTrace.setText("тесты");
        add(FullTrace);
        FullTrace.setBounds(380, 10, 60, 20);

        DisciplineSaveImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/archive.png"))); // NOI18N
        DisciplineSaveImport.setBorderPainted(false);
        DisciplineSaveImport.setContentAreaFilled(false);
        DisciplineSaveImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisciplineSaveImportActionPerformed(evt);
            }
        });
        add(DisciplineSaveImport);
        DisciplineSaveImport.setBounds(400, 75, 40, 30);

        jLabel4.setText("Группа");
        add(jLabel4);
        jLabel4.setBounds(450, 25, 70, 16);

        jLabel5.setText("Студент");
        add(jLabel5);
        jLabel5.setBounds(450, 65, 70, 16);

        Group.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GroupItemStateChanged(evt);
            }
        });
        add(Group);
        Group.setBounds(450, 40, 240, 20);

        Student.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StudentItemStateChanged(evt);
            }
        });
        add(Student);
        Student.setBounds(450, 80, 240, 20);

        RefreshGroups.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshGroups.setBorderPainted(false);
        RefreshGroups.setContentAreaFilled(false);
        RefreshGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshGroupsActionPerformed(evt);
            }
        });
        add(RefreshGroups);
        RefreshGroups.setBounds(660, 5, 30, 30);

        AddGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddGroup.setBorderPainted(false);
        AddGroup.setContentAreaFilled(false);
        AddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddGroupActionPerformed(evt);
            }
        });
        add(AddGroup);
        AddGroup.setBounds(700, 35, 30, 30);

        RemoveGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveGroup.setBorderPainted(false);
        RemoveGroup.setContentAreaFilled(false);
        RemoveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveGroupActionPerformed(evt);
            }
        });
        add(RemoveGroup);
        RemoveGroup.setBounds(740, 35, 30, 30);

        AddStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/add.png"))); // NOI18N
        AddStudent.setBorderPainted(false);
        AddStudent.setContentAreaFilled(false);
        AddStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddStudentActionPerformed(evt);
            }
        });
        add(AddStudent);
        AddStudent.setBounds(700, 70, 30, 30);

        RemoveStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/remove.png"))); // NOI18N
        RemoveStudent.setBorderPainted(false);
        RemoveStudent.setContentAreaFilled(false);
        RemoveStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveStudentActionPerformed(evt);
            }
        });
        add(RemoveStudent);
        RemoveStudent.setBounds(740, 70, 30, 30);

        EditGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditGroup.setBorderPainted(false);
        EditGroup.setContentAreaFilled(false);
        EditGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditGroupActionPerformed(evt);
            }
        });
        add(EditGroup);
        EditGroup.setBounds(780, 35, 30, 30);

        EditStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/edit.png"))); // NOI18N
        EditStudent.setBorderPainted(false);
        EditStudent.setContentAreaFilled(false);
        EditStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditStudentActionPerformed(evt);
            }
        });
        add(EditStudent);
        EditStudent.setBounds(780, 70, 30, 30);

        GroupsImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/archive.png"))); // NOI18N
        GroupsImport.setBorderPainted(false);
        GroupsImport.setContentAreaFilled(false);
        GroupsImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupsImportActionPerformed(evt);
            }
        });
        add(GroupsImport);
        GroupsImport.setBounds(820, 35, 40, 30);
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
                System.out.println("Ошибка импорта тестов:\n"+ee.toString());
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
                refreshTaskFull();
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
                            ArrayList roles = new ArrayList<UserRole>();
                            roles.add(UserRole.ROLE_TEACHER);
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
    private java.awt.Choice Student;
    private java.awt.Choice Task;
    private java.awt.TextArea TaskText;
    private java.awt.Choice Theme;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}
