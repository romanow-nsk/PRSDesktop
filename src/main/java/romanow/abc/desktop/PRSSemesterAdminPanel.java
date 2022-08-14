/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

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
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
 *
 * @author romanow
 */
public class PRSSemesterAdminPanel extends BasePanel{


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
        Disciplines = new java.awt.Choice();
        Themes = new java.awt.Choice();
        RefreshAll = new javax.swing.JButton();
        TutorPermissionAdd = new javax.swing.JButton();
        TutorPermissionRemove = new javax.swing.JButton();
        Tasks = new java.awt.Choice();

        setVerifyInputWhenFocusTarget(false);
        setLayout(null);

        TaskTypeLabel.setText("Не назначенные");
        add(TaskTypeLabel);
        TaskTypeLabel.setBounds(20, 90, 130, 16);

        jLabel2.setText("Преподаватель");
        add(jLabel2);
        jLabel2.setBounds(70, 10, 120, 16);

        jLabel3.setText("Назначенные");
        add(jLabel3);
        jLabel3.setBounds(20, 50, 160, 16);

        Disciplines.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DisciplinesItemStateChanged(evt);
            }
        });
        add(Disciplines);
        Disciplines.setBounds(70, 30, 270, 20);

        Themes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ThemesItemStateChanged(evt);
            }
        });
        add(Themes);
        Themes.setBounds(20, 70, 320, 20);

        RefreshAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/refresh.png"))); // NOI18N
        RefreshAll.setBorderPainted(false);
        RefreshAll.setContentAreaFilled(false);
        RefreshAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshAllActionPerformed(evt);
            }
        });
        add(RefreshAll);
        RefreshAll.setBounds(20, 20, 30, 30);

        TutorPermissionAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/up.PNG"))); // NOI18N
        TutorPermissionAdd.setBorderPainted(false);
        TutorPermissionAdd.setContentAreaFilled(false);
        TutorPermissionAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TutorPermissionAddActionPerformed(evt);
            }
        });
        add(TutorPermissionAdd);
        TutorPermissionAdd.setBounds(350, 110, 30, 30);

        TutorPermissionRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/down.png"))); // NOI18N
        TutorPermissionRemove.setBorderPainted(false);
        TutorPermissionRemove.setContentAreaFilled(false);
        TutorPermissionRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TutorPermissionRemoveActionPerformed(evt);
            }
        });
        add(TutorPermissionRemove);
        TutorPermissionRemove.setBounds(350, 70, 30, 30);

        Tasks.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TasksItemStateChanged(evt);
            }
        });
        add(Tasks);
        Tasks.setBounds(20, 110, 320, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void RefreshAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshAllActionPerformed

    }//GEN-LAST:event_RefreshAllActionPerformed

    private void TutorPermissionAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TutorPermissionAddActionPerformed

    }//GEN-LAST:event_TutorPermissionAddActionPerformed

    private void TutorPermissionRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TutorPermissionRemoveActionPerformed

    }//GEN-LAST:event_TutorPermissionRemoveActionPerformed

    private void DisciplinesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DisciplinesItemStateChanged

    }//GEN-LAST:event_DisciplinesItemStateChanged

    private void ThemesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ThemesItemStateChanged

    }//GEN-LAST:event_ThemesItemStateChanged

    private void TasksItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TasksItemStateChanged

    }//GEN-LAST:event_TasksItemStateChanged



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
    private java.awt.Choice Disciplines;
    private javax.swing.JButton RefreshAll;
    private javax.swing.JLabel TaskTypeLabel;
    private java.awt.Choice Tasks;
    private java.awt.Choice Themes;
    private javax.swing.JButton TutorPermissionAdd;
    private javax.swing.JButton TutorPermissionRemove;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
