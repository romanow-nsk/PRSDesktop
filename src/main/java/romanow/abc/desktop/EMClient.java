package romanow.abc.desktop;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanow.abc.bridge.constants.UserRole;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.API.RestAPIEM;
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.User;

import java.util.concurrent.TimeUnit;

import static romanow.abc.core.constants.Values.*;
import static romanow.abc.core.constants.ValuesBase.UserSuperAdminType;


public class EMClient extends Client {
    long mainServerNodeId = 0;                         // oid текущего узла для ДЦ
    RestAPIEM service2;
    public EMClient(){
        this(true);
        }
    public EMClient(boolean setLog){
        super(setLog);
        Values.init();
        setLoginName("9130000000");
        setPassword("pi31415926");
        }
    public void initPanels(){
        panelDescList.add(new PanelDescriptor("Трассировка", LogPanel.class,new UserRole[]
                {UserRole.ROLE_ADMIN}));
        //---------- <0 - readOnly Mode
        panelDescList.add(new PanelDescriptor("Пользователи", UserPanelBase.class,new UserRole[]
                {UserRole.ROLE_ADMIN}));
        //panelDescList.add(new PanelDescriptor("Отчеты/Уведомления", ReportsPanelBase.class,new int[]
        //        {UserRole.ROLE_ADMIN, UserAdminType}));
        //panelDescList.add(new PanelDescriptor("Сервер",ServerPanel.class,new UserRole[]
        //        {UserRole.ROLE_ADMIN}));
        //panelDescList.add(new PanelDescriptor("Помощь",HelpPanel.class,new int[]
        //        {UserRole.ROLE_ADMIN, UserAdminType}));
        //panelDescList.add(new PanelDescriptor("Артефакты",ArtifactPanel.class,new UserRole[]
        //        {UserRole.ROLE_ADMIN}));
        //panelDescList.add(new PanelDescriptor("Настройки сервера",WorkSettingsPanel.class,new UserRole[]
        //        {UserRole.ROLE_ADMIN}));
        panelDescList.add(new PanelDescriptor("Тьютор",ExamAdminPanel.class,new UserRole[]
                {UserRole.ROLE_ADMIN,UserRole.ROLE_TEACHER}));
        }
    //-------------------------------------------------------------------------------------------------------
    @Override
    public void onLoginSuccess(){
        try {
            service2 = startSecondClient(getServerIP(),""+getServerPort());
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
    public RestAPIEM startSecondClient(String ip, String port) throws UniException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":"+port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        RestAPIEM service = (RestAPIEM)retrofit.create(RestAPIEM.class);
        return service;
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
            java.util.logging.Logger.getLogger(EMStudentClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EMStudentClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EMStudentClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EMStudentClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
