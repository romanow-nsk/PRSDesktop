/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package romanow.abc.desktop;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import romanow.abc.bridge.EMVKRConsoleClient;
import romanow.abc.core.*;
import romanow.abc.core.API.RestAPIBase;
import romanow.abc.core.constants.ConstValue;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.artifacts.ArtifactTypes;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.baseentityes.JString;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.utils.FileNameExt;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.core.utils.Pair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanow.abc.core.utils.StringFIFO;
import romanow.abc.exam.model.ArtefactBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static romanow.abc.core.Utils.httpError;

/**
 *
 * @author romanow
 */
public class EMVKRMainBaseFrame extends MainBaseFrame implements I_Important {
    @Getter @Setter protected EMVKRConsoleClient client=null;
    //------------------------------------------------------------------------------------------------------------------
    public EMVKRMainBaseFrame() {
        this(true);
        }
    public EMVKRMainBaseFrame(boolean setLog) {
        initComponents();
        gblEncoding = System.getProperty("file.encoding");
        utf8 = gblEncoding.equals("UTF-8");
        if (setLog)
            restoreContext();
        }
    //------------------------------------------------------------------------------------------------------------------
    public void uploadFileAsync(final I_Value<ArtefactBean> ok){
        FileNameExt fname = getInputFileName("Выгрузить файл","*",null);
        if (fname==null){
            System.out.println("Файл не выбран");
            return;
        }
        try {
            //------------------ Вариант 2  для -----------------------
            //@retrofit2.http.Multipart
            //@POST("artefact/upload")
            //Call<ArtefactBean> uploadFile(@Part MultipartBody.Part part);
            //----------------------------------------------------------
            MultipartBody.Part body2 = createMultipsrtBody(fname);
            //RequestBody body2 = createRequestBody(fname);
            Call<ArtefactBean> call3 = client.getArtefactApi().uploadFile(body2);
            call3.enqueue(new Callback<ArtefactBean>() {
                @Override
                public void onResponse(Call<ArtefactBean> call, Response<ArtefactBean> response) {
                    if (!response.isSuccessful()){
                        System.out.println("Ошибка выгрузки файла  "+ Utils.httpError(response));
                    }
                    else{
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                ok.onEnter(response.body());
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<ArtefactBean> call, Throwable ee) {
                    System.out.println("Ошибка сервера: "+ ee.toString());
                    }
                });
            } catch (Exception e) { System.out.println("Ошибка сервера: "+e.toString()); }
        }

    public static MultipartBody.Part createMultipsrtBody(FileNameExt fname){
        if (fname == null) return null;
        File file = new File(fname.fullName());
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = ArtifactTypes.getMimeType(fname.getExt());
        //System.out.println(type);
        MediaType mType = MediaType.parse(type);
        //System.out.println(mType);
        RequestBody requestFile = RequestBody.create(mType, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fname.fileName(), requestFile);
        return body;
        }
    public static RequestBody createRequestBody(FileNameExt fname){
        if (fname == null) return null;
        File file = new File(fname.fullName());
        RequestBody formBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", fname.getName(),
                    RequestBody.create(MediaType.parse("text/plain"), file))
            .build();
        return formBody;
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(EMVKRMainBaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EMVKRMainBaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EMVKRMainBaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EMVKRMainBaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EMVKRMainBaseFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
