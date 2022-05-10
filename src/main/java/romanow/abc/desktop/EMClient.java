package romanow.abc.desktop;

import romanow.abc.bridge.constants.UserRole;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.API.RestAPIEM;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.User;


public class EMClient extends Client {
    long mainServerNodeId = 0;                         // oid текущего узла для ДЦ
    RestAPIEM service2;
    public EMClient(){
        this(true);
        }
    public EMClient(boolean setLog){
        super(setLog);
        Values.init();
        setLoginName("9139877277");
        setPassword("schwanensee1969");
        }
    public void initPanels(){
        panelDescList.add(new PanelDescriptor("Трассировка", LogPanel.class,new int[]
                {Values.UserAdminType}));
        //---------- <0 - readOnly Mode
        panelDescList.add(new PanelDescriptor("Пользователи", UserPanelBase.class,new int[]
                {Values.UserAdminType}));
        panelDescList.add(new PanelDescriptor("Отчеты/Уведомления", ReportsPanelBase.class,new int[]
                {Values.UserAdminType}));
        panelDescList.add(new PanelDescriptor("Сервер",ServerPanel.class,new int[]
                {Values.UserAdminType}));
        //panelDescList.add(new PanelDescriptor("Помощь",HelpPanel.class,new int[]
        //        {UserRole.ROLE_ADMIN, UserAdminType}));
        panelDescList.add(new PanelDescriptor("Артефакты",ArtifactPanel.class,new int[]
                {Values.UserAdminType}));
        panelDescList.add(new PanelDescriptor("Настройки сервера",WorkSettingsPanel.class,new int[]
                {Values.UserAdminType}));
        panelDescList.add(new PanelDescriptor("Тьютор",EMExamAdminPanel.class,new int[]
                {Values.UserAdminType,Values.UserEMTutor}));
        }
    //-------------------------------------------------------------------------------------------------------
    @Override
    public void onLoginSuccess(){
        try {
            service2 = (RestAPIEM) startSecondClient(getServerIP(),""+getServerPort(),RestAPIEM.class);
            getWorkSettings();
            } catch (UniException e) {
                popup("Ошибка соединения: " +e.toString());
                }
        }

    public void setExternalData(String token, User uu, WorkSettings ws0, RestAPIBase service0, RestAPIEM service20, boolean localUser0){
        debugToken = token;
        loginUser = uu;
        workSettings = ws0;
        service = service0;
        service2 = service20;
        localUser = localUser0;
        }
    //-------------------------------------------------------------------------------------------------------
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
            java.util.logging.Logger.getLogger(EMVKRClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EMVKRClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EMVKRClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EMVKRClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Values.init();
                new EMClient().setVisible(false);
            }
        });
    }
}
