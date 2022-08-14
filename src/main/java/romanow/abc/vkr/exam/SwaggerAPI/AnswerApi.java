package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import romanow.abc.vkr.exam.model.AnswerBean;
import romanow.abc.vkr.exam.model.FullAnswerBean;
import romanow.abc.vkr.exam.model.MessageBean;
import romanow.abc.vkr.exam.model.PageMessageBean;
import romanow.abc.vkr.exam.model.Pageable;

public interface AnswerApi {
  /**
   * 
   * 
   * @param answerId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullAnswerBean&gt;
   */
  @GET("answers/{answerId}/full")
  Call<FullAnswerBean> getFull6(
            @retrofit2.http.Path("answerId") Long answerId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get messages by an answer
   * 
   * @param answerId  (required)
   * @param pageable  (required)
   * @return Call&lt;PageMessageBean&gt;
   */
  @GET("answers/{answerId}/message")
  Call<PageMessageBean> getMessages(
            @retrofit2.http.Path("answerId") Long answerId            ,     @retrofit2.http.Query("pageable") Pageable pageable
  );

  /**
   * Send message to an answer
   * 
   * @param body  (required)
   * @param answerId  (required)
   * @return Call&lt;MessageBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("answers/{answerId}/message")
  Call<MessageBean> newMessage(
                    @retrofit2.http.Body MessageBean body    ,         @retrofit2.http.Path("answerId") Long answerId            
  );

  /**
   * 
   * 
   * @param body  (required)
   * @return Call&lt;AnswerBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("answers/state")
  Call<AnswerBean> updateState2(
                    @retrofit2.http.Body AnswerBean body    
  );

}
