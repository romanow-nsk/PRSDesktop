/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import com.google.gson.Gson;
import com.mongodb.DB;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.users.User;
import retrofit2.Call;
import retrofit2.Response;

import java.awt.*;
import java.util.ArrayList;

import static romanow.abc.core.constants.ValuesBase.*;

/**
 *
 * @author romanow
 */
public class Client extends MainBaseFrame   {
    public final static int PanelOffsetY=60;
    public final static int RatioW=4;
    public final static int RatioH=3;
    public final static int PanelH=700;
    public final static int PanelW=PanelH*RatioW/RatioH;        // Для отношения сторон 800;
    public final static int MesW=600;
    public final static int ShortView=PanelW+30;
    public final static int ViewHight = PanelH+100;
    public final static int X0 = 50;
    public final static int Y0 = 50;
    private LogView logView = new LogView();
    private LogPanel logPanel;
    private Login loginForm=null;
    private I_OK disposeBack = null;
    private ArrayList<I_PanelEvent> panels = new ArrayList();
    private boolean secondForm=false;
    //----------------------------------------------------------------
    public final ArrayList<PanelDescriptor> panelDescList=new ArrayList<>();
    public void setLoginName(String name){
        loginForm.setLoginName(name);
        }
    public void setPassword(String name){
        loginForm.setPassword(name);
    }
    public void initPanels(){
        panelDescList.add(new PanelDescriptor("Трассировка", LogPanel.class,new int[]
                {UserSuperAdminType, UserAdminType}));
        //---------- <0 - readOnly Mode
        panelDescList.add(new PanelDescriptor("Пользователи", UserPanelBase.class,new int[]
                {UserSuperAdminType, UserAdminType}));
        //panelDescList.add(new PanelDescriptor("Отчеты/Уведомления", ReportsPanelBase.class,new int[]
        //        {UserSuperAdminType, UserAdminType}));
        panelDescList.add(new PanelDescriptor("Сервер",ServerPanel.class,new int[]
                {UserSuperAdminType}));
        //panelDescList.add(new PanelDescriptor("Помощь",HelpPanel.class,new int[]
        //        {UserSuperAdminType, UserAdminType}));
        panelDescList.add(new PanelDescriptor("Артефакты",ArtifactPanel.class,new int[]
                {UserSuperAdminType, UserAdminType}));
        panelDescList.add(new PanelDescriptor("Настройки сервера",WorkSettingsPanel.class,new int[]
                {UserSuperAdminType}));
        }

    private void login(){
        setVisible(false);
        if (loginForm!=null)
            loginForm.setVisible(true);
        else
        loginForm = new Login(this,new I_Button(){
            @Override
            public void onPush() {
                startUser();
                Client.this.setVisible(true);
                }
            });
        }

    public Client() {
        this(true);
        }
    public Client(boolean setLog) {
        super(setLog);
        secondForm=false;
        initComponents();
        setMES(loginForm);
        initPanels();
        login();
        }

    public Client(RestAPIBase service0, User user0, I_OK disposeBack0) throws UniException{
        ValuesBase.init();
        initComponents();
        disposeBack = disposeBack0;
        secondForm=true;
        service = service0;
        loginUser(user0);
        debugToken = user0.getSessionToken();
        loadConstants();
        startUser();
        }
    public void startUser(){
        try {
            setTitle(ValuesBase.env().applicationName(AppNameTitle)+": "+loginUser().getHeader());
            debugToken = loginUser().getSessionToken();
            setBounds(X0, Y0, ShortView, ViewHight);
            PanelList.setBounds(10,10,PanelW,PanelH);
            ShowLog.setSelected(false);
            PanelList.removeAll();
            panels.clear();
            DBRequest ws = new APICall2<DBRequest>(){
                @Override
                public Call<DBRequest> apiFun() {
                    return service.workSettings(debugToken);
                    }
                }.call(this);
            WorkSettingsBase base = (WorkSettingsBase)ws.get(new Gson());
            boolean mainMode = base.isMainServer();
            for(PanelDescriptor pp : panelDescList){
                boolean bb=false;
                boolean editMode = true;
                for(int vv : pp.userTypes)
                    if (Math.abs(vv)==loginUser().getTypeId()){
                        bb=true;
                        editMode = vv > 0;
                        break;
                    }
                if (bb){
                    BasePanel panel = (BasePanel) pp.view.newInstance();
                    if (panel instanceof LogPanel)
                        logPanel = (LogPanel)panel;
                    if (mainMode && panel.isMainMode() || !mainMode && panel.isESSMode()){
                        panel.editMode = editMode;
                        panel.initPanel(this);
                        panels.add(panel);
                        PanelList.add(pp.name, panel);
                        }
                    }
                }
            setMES(logPanel.mes(),logView,MESLOC);
            BasePanel pn;
            refresh();
            } catch(Exception ee){
                System.out.println(ee.toString());
                ee.printStackTrace();
                }
        }
    @Override
    public void refresh(){
        refreshMode=true;
        for(I_PanelEvent xx : panels)
            xx.refresh();
        refreshMode=false;
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelList = new javax.swing.JTabbedPane();
        ShowLog = new javax.swing.JCheckBox();
        MESLOC = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        PanelList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(PanelList);
        PanelList.setBounds(10, 10, 700, 700);

        ShowLog.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ShowLog.setText("log");
        ShowLog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ShowLogItemStateChanged(evt);
            }
        });
        getContentPane().add(ShowLog);
        ShowLog.setBounds(650, 720, 60, 25);
        getContentPane().add(MESLOC);
        MESLOC.setBounds(10, 720, 630, 25);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    @Override
    public void logOff(){
        ShowLog.setSelected(false);
        if (secondForm){
            dispose();
            return;
            }
        for(I_PanelEvent xx : panels)
            xx.shutDown();
        try {
            Response<JEmpty> res = service.logoff(debugToken).execute();
            if (!res.isSuccessful()){
                System.out.println("Ошибка сервера: "+ Utils.httpError(res));
                }
            loginForm.disConnect();
            setMES(loginForm);
            login();
            }catch (Exception ee){
                System.out.println("Ошибка сервера: "+ee.toString());
                loginForm.disConnect();
                login();
                }
        }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        logOff();
        if (disposeBack!=null)
            disposeBack.onOK(null);
    }//GEN-LAST:event_formWindowClosing

    private void ShowLogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ShowLogItemStateChanged
        logView.setVisible(ShowLog.isSelected());
    }//GEN-LAST:event_ShowLogItemStateChanged
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void sendEvent(int code, long par2){
        sendEventPanel(code,0,par2,"");
        }
    @Override
    public void sendEventPanel(int code, int par1, long par2, String par3,Object oo){
        if (refreshMode) return;
        for(I_PanelEvent xx : panels)
            xx.eventPanel(code, par1,par2,par3,oo);
        }
    @Override
    public void panelToFront(BasePanel pp){
        for (int i=0;i<panels.size();i++)
            if (panels.get(i) == pp) {
                PanelList.setSelectedIndex(i);
                break;
            }
        }
    @Override
    public void popup(String ss){
        System.out.println(ss);
        new Message(300,300,ss, ValuesBase.PopupMessageDelay);
        }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField MESLOC;
    private javax.swing.JTabbedPane PanelList;
    private javax.swing.JCheckBox ShowLog;

    // End of variables declaration//GEN-END:variables
}
