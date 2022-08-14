package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import romanow.abc.vkr.exam.model.FullStudentRatingBean;
import romanow.abc.vkr.exam.model.Pageable;
import romanow.abc.vkr.exam.model.StudentAnswerBean;
import romanow.abc.vkr.exam.model.StudentRatingBean;

import java.util.List;

public interface StudentRatingApi {
  /**
   * Get all student ratings
   * 
   * @return Call&lt;List&lt;StudentRatingBean&gt;&gt;
   */
  @GET("student-rating")
  Call<List<StudentRatingBean>> findAll1();
    

  /**
   * Get answers by student rating
   * Sorted by default by task type (questions first)
   * @param id  (required)
   * @param pageable  (required)
   * @return Call&lt;List&lt;StudentAnswerBean&gt;&gt;
   */
  @GET("student-rating/{id}/answer")
  Call<List<StudentAnswerBean>> getAnswers(
            @retrofit2.http.Path("id") Long id            ,     @retrofit2.http.Query("pageable") Pageable pageable
  );

  /**
   * Get full student rating
   * 
   * @param id  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullStudentRatingBean&gt;
   */
  @GET("student-rating/{id}/full")
  Call<FullStudentRatingBean> getFull1(
            @retrofit2.http.Path("id") Long id            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Update Student rating
   * Updates semester rating and moves to Allowed or Not Allowed states
   * @param body  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("student-rating")
  Call<Void> update1(
                    @retrofit2.http.Body StudentRatingBean body    
  );

  /**
   * Update student rating state
   * 
   * @param body  (required)
   * @return Call&lt;StudentRatingBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("student-rating/state")
  Call<StudentRatingBean> updateState(
                    @retrofit2.http.Body StudentRatingBean body    
  );

}
