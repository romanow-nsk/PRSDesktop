package romanow.abc.bridge;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;


import java.util.List;

public interface RestAPI {
    //======================== Артефакты =================================================================================
    /** Получить описатель артефакта по oid  */
    //@GET("/artifact/get")
    //Call<ArtifactBean> getArtifactById(@Header("SessionToken") String token, @Query("id") long id);
    /** Получить список артефактов  */
    //@GET("/artifact/list")
    //Call<List<ArtifactBean>> getArtifactList(@Header("SessionToken") String token);
    /** Записать файл как артефакт через multipart-запрос http */
    //@Streaming
    //@Multipart
    //@POST("/file/upload")
    //Call<ArtifactBean> upload(@Header("SessionToken") String token,@Query("description") String description,@Query("origname") String origName, @Part MultipartBody.Part file);
    //@Streaming
    //@GET("/file/download")
    //Call<ResponseBody> downLoad(@Header("SessionToken") String token, @Query("id") long id);
    /** Получить список объектов-артефактов по условию */
    //@GET("/api/artifact/condition/list")
    //Call<List<ArtifactBean>> getArtifactConditionList(@Header("SessionToken") String token,
    //    @Query("type") int type, @Query("owner") long ownerId,
    //    @Query("namemask") String nameMask, @Query("filenamemask") String fileNameMask,
    //    @Query("size1") long size1, @Query("size2") long size2,
    //    @Query("dateInMS1") long dateMS1, @Query("dateInMS2") long dateMS2);
    /** Удалить артефакт вместе с файлом */
    //@POST("/artifact/remove")
    //Call<EmptyBean> removeArtifact(@Header("SessionToken") String token, @Query("id") long id);
    //=================== Авторизация ==================================================================================
    @POST("/login")
    Call<ResponseBody> login(@Header("X-Authentication") String loginBase64);
    //=================== Предметы/темы/вопросы/экзамены/регламенты ====================================================
    }
