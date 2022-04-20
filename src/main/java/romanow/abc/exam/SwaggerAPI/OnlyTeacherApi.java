package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.CreateExamBean;
import romanow.abc.exam.model.CreateExamRuleBean;
import romanow.abc.exam.model.CreateRatingSystemBean;
import romanow.abc.exam.model.DisciplineBean;
import romanow.abc.exam.model.ExamBean;
import romanow.abc.exam.model.ExamPeriodBean;
import romanow.abc.exam.model.ExamRuleBean;
import romanow.abc.exam.model.GroupBean;
import romanow.abc.exam.model.RatingSystemBean;
import romanow.abc.exam.model.StudentBean;
import romanow.abc.exam.model.TaskBean;
import romanow.abc.exam.model.TeacherBean;
import romanow.abc.exam.model.ThemeBean;
import romanow.abc.exam.model.TicketBean;
import romanow.abc.exam.model.UpdateAnswerBean;
import romanow.abc.exam.model.UpdateExamPeriodBean;
import romanow.abc.exam.model.UpdateTicketBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface OnlyTeacherApi {
  
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
   * Create rating mapping
   * 

   * @param body  (required)

   * @return Call&lt;RatingSystemBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("rating-systems")
  Call<RatingSystemBean> create1(
    @retrofit2.http.Body CreateRatingSystemBean body
  );

  
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
    @retrofit2.http.Body CreateExamRuleBean body
  );

  
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
   * Create a task
   * 

   * @param body  (required)

   * @return Call&lt;TaskBean&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("task")
  Call<TaskBean> createTask(
    @retrofit2.http.Body TaskBean body
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
   * Get all tasks
   * 

   * @return Call&lt;List&lt;TaskBean&gt;&gt;

   */
  
  
  
    
  @GET("task")
  Call<List<TaskBean>> findAll();
    

  
  /**
   * Get exam rules by discipline
   * 

   * @param disciplineId  (required)

   * @return Call&lt;List&lt;ExamRuleBean&gt;&gt;

   */
  
  
  
    
  @GET("discipline/{disciplineId}/exam-rule")
  Call<List<ExamRuleBean>> findExamRules(
    @retrofit2.http.Path("disciplineId") Long disciplineId
  );

  
  /**
   * Get groups by discipline
   * 

   * @param disciplineId  (required)

   * @return Call&lt;List&lt;GroupBean&gt;&gt;

   */
  
  
  
    
  @GET("discipline/{disciplineId}/group")
  Call<List<GroupBean>> findGroups(
    @retrofit2.http.Path("disciplineId") Long disciplineId
  );

  
  /**
   * Get all themes
   * 

   * @return Call&lt;List&lt;ThemeBean&gt;&gt;

   */
  
  
  
    
  @GET("theme")
  Call<List<ThemeBean>> getAll();
    

  
  /**
   * Get all rating mappings
   * 

   * @return Call&lt;Void&gt;

   */
  
  
  
    
  @GET("rating-systems")
  Call<Void> getAll1();
    

  
  /**
   * Get all teacher&#x27;s exams
   * 

   * @return Call&lt;List&lt;ExamBean&gt;&gt;

   */
  
  
  
    
  @GET("exams")
  Call<List<ExamBean>> getAll3();
    

  
  /**
   * Get all exam rules
   * 

   * @return Call&lt;List&lt;ExamRuleBean&gt;&gt;

   */
  
  
  
    
  @GET("exam-rule")
  Call<List<ExamRuleBean>> getAll4();
    

  
  /**
   * Get teacher&#x27;s disciplines
   * 

   * @return Call&lt;List&lt;DisciplineBean&gt;&gt;

   */
  
  
  
    
  @GET("teacher/discipline")
  Call<List<DisciplineBean>> getDisciplines();
    

  
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
   * Get info about sender teacher
   * 

   * @return Call&lt;TeacherBean&gt;

   */
  
  
  
    
  @GET("teacher/me")
  Call<TeacherBean> getSelf();
    

  
  /**
   * Get students in a group
   * 

   * @param groupId  (required)

   * @return Call&lt;List&lt;StudentBean&gt;&gt;

   */
  
  
  
    
  @GET("group/{groupId}/student")
  Call<List<StudentBean>> getStudents(
    @retrofit2.http.Path("groupId") Long groupId
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
   * Put a mark to an answer
   * 

   * @param body  (required)

   * @param answerId  (required)

   * @return Call&lt;Void&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @PUT("answer/{answerId}")
  Call<Void> rate(
    @retrofit2.http.Body UpdateAnswerBean body, @retrofit2.http.Path("answerId") Long answerId
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

  
  /**
   * Update rating
   * 

   * @param body  (required)

   * @return Call&lt;Void&gt;

   */
  
  
  
  
  @Headers({
    "Content-Type:application/json"
  })
  
  
    
  @POST("ticket/rating")
  Call<Void> updateTicketsRating(
    @retrofit2.http.Body List<UpdateTicketBean> body
  );

  
}

