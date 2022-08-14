package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;

import romanow.abc.vkr.exam.model.ArtefactBean;

public interface ArtefactApi {
  /**
   * Download a file
   * 
   * @param artefactId  (required)
   * @return Call&lt;Void&gt;
   */
  @GET("artefact/{artefactId}/download")
  Call<Void> downloadFile(
            @retrofit2.http.Path("artefactId") Long artefactId            
  );

  /**
   * Get info about file
   * 
   * @param artefactId  (required)
   * @return Call&lt;ArtefactBean&gt;
   */
  @GET("artefact/{artefactId}/info")
  Call<ArtefactBean> getInfo(
            @retrofit2.http.Path("artefactId") Long artefactId            
  );

  /**
   * Upload a file
   * 
   * @param file  (optional)
   * @return Call&lt;ArtefactBean&gt;
   */
  @retrofit2.http.Multipart
  @POST("artefact/upload")
  Call<ArtefactBean> uploadFile(@retrofit2.http.Part("file\"; filename=\"file") RequestBody file);

  // Сгенерированный
  //@retrofit2.http.Multipart
  //@POST("artefact/upload")
  //Call<ArtefactBean> uploadFile(
  //                      @retrofit2.http.Part("file\"; filename=\"file") RequestBody file);

}
