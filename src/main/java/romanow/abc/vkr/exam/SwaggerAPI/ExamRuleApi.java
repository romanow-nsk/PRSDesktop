package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import romanow.abc.vkr.exam.model.ExamRuleBean;
import romanow.abc.vkr.exam.model.FullExamRuleBean;

import java.util.List;

public interface ExamRuleApi {
  /**
   * Create an exam rule
   * 
   * @param body  (required)
   * @return Call&lt;ExamRuleBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("exam-rule")
  Call<ExamRuleBean> create3(
                    @retrofit2.http.Body ExamRuleBean body    
  );

  /**
   * Get all exam rules
   * 
   * @return Call&lt;List&lt;ExamRuleBean&gt;&gt;
   */
  @GET("exam-rule")
  Call<List<ExamRuleBean>> getAll3();
    

  /**
   * Get full exam rule
   * 
   * @param id  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullExamRuleBean&gt;
   */
  @GET("exam-rule/{id}/full")
  Call<FullExamRuleBean> getFull4(
            @retrofit2.http.Path("id") Long id            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get one exam rule
   * 
   * @param id  (required)
   * @return Call&lt;ExamRuleBean&gt;
   */
  @GET("exam-rule/{id}")
  Call<ExamRuleBean> getOne3(
            @retrofit2.http.Path("id") Long id            
  );

  /**
   * Update an exam rule
   * 
   * @param body  (required)
   * @return Call&lt;ExamRuleBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("exam-rule")
  Call<ExamRuleBean> update3(
                    @retrofit2.http.Body ExamRuleBean body    
  );

}
