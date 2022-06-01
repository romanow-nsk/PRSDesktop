package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.FullThemeBean;
import romanow.abc.exam.model.TaskBean;
import romanow.abc.exam.model.ThemeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ThemesApi {
  /**
   * Create a theme
   * 
   * @param body  (required)
   * @return Call&lt;ThemeBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("theme")
  Call<ThemeBean> create(
                    @retrofit2.http.Body ThemeBean body    
  );

  /**
   * Delete a theme
   * 
   * @param themeId  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("theme/{themeId}")
  Call<Void> delete(
            @retrofit2.http.Path("themeId") Long themeId            
  );

  /**
   * 
   * 
   * @param themeId  (required)
   * @return Call&lt;List&lt;TaskBean&gt;&gt;
   */
  @GET("theme/{themeId}/tasks")
  Call<List<TaskBean>> findTasks(
            @retrofit2.http.Path("themeId") Long themeId            
  );

  /**
   * Get all themes
   * 
   * @return Call&lt;List&lt;ThemeBean&gt;&gt;
   */
  @GET("theme")
  Call<List<ThemeBean>> getAll();
    

  /**
   * Get one theme
   * 
   * @param themeId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullThemeBean&gt;
   */
  @GET("theme/{themeId}/full")
  Call<FullThemeBean> getFull(
            @retrofit2.http.Path("themeId") Long themeId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get one theme
   * 
   * @param themeId  (required)
   * @return Call&lt;ThemeBean&gt;
   */
  @GET("theme/{themeId}")
  Call<ThemeBean> getOne(
            @retrofit2.http.Path("themeId") Long themeId            
  );

  /**
   * Update a theme
   * 
   * @param body  (required)
   * @return Call&lt;ThemeBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("theme")
  Call<ThemeBean> update(
                    @retrofit2.http.Body ThemeBean body    
  );

}
