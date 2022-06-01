package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.DisciplineBean;
import romanow.abc.exam.model.ExamRuleBean;
import romanow.abc.exam.model.FullDisciplineBean;
import romanow.abc.exam.model.ThemeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DisciplinesApi {
  /**
   * Create a discipline
   * 
   * @param body  (required)
   * @return Call&lt;DisciplineBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("discipline")
  Call<DisciplineBean> create4(
                    @retrofit2.http.Body DisciplineBean body    
  );

  /**
   * Delete a discipline
   * 
   * @param id  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("discipline/{id}")
  Call<Void> delete3(
            @retrofit2.http.Path("id") Long id            
  );

  /**
   * Get exam rules by discipline
   * 
   * @param id  (required)
   * @return Call&lt;List&lt;ExamRuleBean&gt;&gt;
   */
  @GET("discipline/{id}/exam-rule")
  Call<List<ExamRuleBean>> findExamRules(
            @retrofit2.http.Path("id") Long id            
  );

  /**
   * Get all disciplines
   * 
   * @return Call&lt;List&lt;DisciplineBean&gt;&gt;
   */
  @GET("discipline")
  Call<List<DisciplineBean>> getAll4();
    

  /**
   * Get one discipline
   * 
   * @param disciplineId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullDisciplineBean&gt;
   */
  @GET("discipline/{disciplineId}/full")
  Call<FullDisciplineBean> getFull5(
            @retrofit2.http.Path("disciplineId") Long disciplineId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get one discipline
   * 
   * @param disciplineId  (required)
   * @return Call&lt;DisciplineBean&gt;
   */
  @GET("discipline/{disciplineId}")
  Call<DisciplineBean> getOne4(
            @retrofit2.http.Path("disciplineId") Long disciplineId            
  );

  /**
   * 
   * 
   * @param disciplineId  (required)
   * @return Call&lt;List&lt;ThemeBean&gt;&gt;
   */
  @GET("discipline/{disciplineId}/themes")
  Call<List<ThemeBean>> getThemes(
            @retrofit2.http.Path("disciplineId") Long disciplineId            
  );

  /**
   * Update a discipline
   * 
   * @param body  (required)
   * @return Call&lt;DisciplineBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("discipline")
  Call<DisciplineBean> update4(
                    @retrofit2.http.Body DisciplineBean body    
  );

}
