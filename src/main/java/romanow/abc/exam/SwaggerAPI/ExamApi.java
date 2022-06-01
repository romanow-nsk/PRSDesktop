package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.AnswerBean;
import romanow.abc.exam.model.ExamBean;
import romanow.abc.exam.model.FullExamBean;
import romanow.abc.exam.model.StudentRatingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ExamApi {
  /**
   * Create an exam
   * 
   * @param body  (required)
   * @return Call&lt;ExamBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("exams")
  Call<ExamBean> createExam(
                    @retrofit2.http.Body ExamBean body    
  );

  /**
   * Delete an exam
   * 
   * @param examId  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("exams/{examId}")
  Call<Void> deleteExam(
            @retrofit2.http.Path("examId") Long examId            
  );

  /**
   * Get all exams
   * 
   * @return Call&lt;List&lt;ExamBean&gt;&gt;
   */
  @GET("exams")
  Call<List<ExamBean>> getAll2();
    

  /**
   * Get answers by a exam
   * 
   * @param examId  (required)
   * @return Call&lt;List&lt;AnswerBean&gt;&gt;
   */
  @GET("exams/{examId}/answers")
  Call<List<AnswerBean>> getAnswers1(
            @retrofit2.http.Path("examId") Long examId            
  );

  /**
   * Get full exam by id
   * 
   * @param examId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullExamBean&gt;
   */
  @GET("exams/{examId}/full")
  Call<FullExamBean> getFull3(
            @retrofit2.http.Path("examId") Long examId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get exam by id
   * 
   * @param examId  (required)
   * @return Call&lt;ExamBean&gt;
   */
  @GET("exams/{examId}")
  Call<ExamBean> getOne2(
            @retrofit2.http.Path("examId") Long examId            
  );

  /**
   * Get student ratings by a exam
   * 
   * @param examId  (required)
   * @return Call&lt;List&lt;StudentRatingBean&gt;&gt;
   */
  @GET("exams/{examId}/student-ratings")
  Call<List<StudentRatingBean>> getStudentRatings(
            @retrofit2.http.Path("examId") Long examId            
  );

  /**
   * Update an exam
   * 
   * @param body  (required)
   * @return Call&lt;ExamBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("exams")
  Call<ExamBean> updateExam(
                    @retrofit2.http.Body ExamBean body    
  );

  /**
   * Update exam state
   * 
   * @param body  (required)
   * @return Call&lt;ExamBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("exams/state")
  Call<ExamBean> updateState1(
                    @retrofit2.http.Body ExamBean body    
  );

}
