package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.Pageable;
import romanow.abc.exam.model.StudentAnswerBean;
import romanow.abc.exam.model.StudentBean;
import romanow.abc.exam.model.StudentTicketBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface OnlyStudentApi {
  
  /**
   * Get answers by ticket
   * Sorted by default by task type (questions first)

   * @param ticketId  (required)

   * @param pageable  (required)

   * @return Call&lt;List&lt;StudentAnswerBean&gt;&gt;

   */
  
  
  
    
  @GET("ticket/{ticketId}/answer")
  Call<List<StudentAnswerBean>> getAnswers(
    @retrofit2.http.Path("ticketId") Long ticketId, @retrofit2.http.Path("pageable") Pageable pageable
  );

  
  /**
   * Get info about sender student
   * 

   * @return Call&lt;StudentBean&gt;

   */
  
  
  
    
  @GET("student/me")
  Call<StudentBean> getSelf1();
    

  
  /**
   * Get student&#x27;s tickets
   * 

   * @return Call&lt;List&lt;StudentTicketBean&gt;&gt;

   */
  
  
  
    
  @GET("student/tickets")
  Call<List<StudentTicketBean>> getTickets();
    

  
}

