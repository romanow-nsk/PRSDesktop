package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.ExamBean;
import romanow.abc.exam.model.ExamPeriodBean;
import romanow.abc.exam.model.MessageBean;
import romanow.abc.exam.model.NewMessageBean;
import romanow.abc.exam.model.PageMessageBean;
import romanow.abc.exam.model.Pageable;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface ExamControllerApi {
  
  /**
   * Get messages by an exam period
   * 

   * @param periodId  (required)

   * @param pageable  (required)

   * @return Call&lt;PageMessageBean&gt;

   */
  
  
  
    
  @GET("periods/{periodId}/messages")
  Call<PageMessageBean> getMessages(
    @retrofit2.http.Path("periodId") Long periodId, @retrofit2.http.Path("pageable") Pageable pageable
  );

  
  /**
   * Get exam by id
   * 

   * @param examId  (required)

   * @return Call&lt;ExamBean&gt;

   */
  
  
  
    
  @GET("exams/{examId}")
  Call<ExamBean> getOne3(
    @retrofit2.http.Path("examId") Long examId
  );

  
  /**
   * Get exam period
   * 

   * @param periodId  (required)

   * @return Call&lt;ExamPeriodBean&gt;

   */
  
  
  
    
  @GET("periods/{periodId}")
  Call<ExamPeriodBean> getPeriod(
    @retrofit2.http.Path("periodId") Long periodId
  );

  
  /**
   * Send a message to an exam period
   * 

   * @param body  (required)

   * @param periodId  (required)

   * @return Call&lt;MessageBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("periods/{periodId}/messages")
  Call<MessageBean> newMessage(
    @retrofit2.http.Body NewMessageBean body, @retrofit2.http.Path("periodId") Long periodId
  );

  
}

