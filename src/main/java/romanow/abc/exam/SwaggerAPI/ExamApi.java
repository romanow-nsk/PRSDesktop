package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.CreateExamBean;
import romanow.abc.exam.model.ExamBean;
import romanow.abc.exam.model.ExamPeriodBean;
import romanow.abc.exam.model.MessageBean;
import romanow.abc.exam.model.NewMessageBean;
import romanow.abc.exam.model.PageMessageBean;
import romanow.abc.exam.model.Pageable;
import romanow.abc.exam.model.TicketBean;
import romanow.abc.exam.model.UpdateExamPeriodBean;



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
    @retrofit2.http.Body CreateExamBean body
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
   * Get all teacher&#x27;s exams
   * 

   * @return Call&lt;List&lt;ExamBean&gt;&gt;

   */
  
  
  
    
  @GET("exams")
  Call<List<ExamBean>> getAll3();
    

  
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
   * Get exam periods by exam
   * 

   * @param examId  (required)

   * @return Call&lt;List&lt;ExamPeriodBean&gt;&gt;

   */
  
  
  
    
  @GET("exams/{examId}/periods")
  Call<List<ExamPeriodBean>> getPeriods(
    @retrofit2.http.Path("examId") Long examId
  );

  
  /**
   * Get tickets by a period
   * 

   * @param periodId  (required)

   * @return Call&lt;List&lt;TicketBean&gt;&gt;

   */
  
  
  
    
  @GET("periods/{periodId}/ticket")
  Call<List<TicketBean>> getTickets1(
    @retrofit2.http.Path("periodId") Long periodId
  );

  
  /**
   * Get tickets of people who didn&#x27;t pass an exam
   * 

   * @param examId  (required)

   * @return Call&lt;List&lt;TicketBean&gt;&gt;

   */
  
  
  
    
  @GET("exams/{examId}/un-passed")
  Call<List<TicketBean>> getUnPassed(
    @retrofit2.http.Path("examId") Long examId
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

  
  /**
   * Update an exam
   * 

   * @param body  (required)

   * @param examId  (required)

   * @return Call&lt;ExamBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @PUT("exams/{examId}")
  Call<ExamBean> updateExam(
    @retrofit2.http.Body CreateExamBean body, @retrofit2.http.Path("examId") Long examId
  );

  
  /**
   * Update exam period (start time or state NOT together)
   * 

   * @param body  (required)

   * @param periodId  (required)

   * @return Call&lt;ExamPeriodBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @PUT("periods/{periodId}")
  Call<ExamPeriodBean> updatePeriod(
    @retrofit2.http.Body UpdateExamPeriodBean body, @retrofit2.http.Path("periodId") Long periodId
  );

  
}

