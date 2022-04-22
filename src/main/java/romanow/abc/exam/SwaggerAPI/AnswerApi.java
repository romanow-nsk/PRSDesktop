package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.MessageBean;
import romanow.abc.exam.model.NewMessageBean;
import romanow.abc.exam.model.PageMessageBean;
import romanow.abc.exam.model.Pageable;
import romanow.abc.exam.model.UpdateAnswerBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AnswerApi {
  /**
   * Get messages by an answer
   * 
   * @param answerId  (required)
   * @param pageable  (required)
   * @return Call&lt;PageMessageBean&gt;
   */
  @GET("answer/{answerId}/message")
  Call<PageMessageBean> getMessages1(
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
  @POST("answer/{answerId}/message")
  Call<MessageBean> newMessage1(
                    @retrofit2.http.Body NewMessageBean body    ,         @retrofit2.http.Path("answerId") Long answerId            
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
                    @retrofit2.http.Body UpdateAnswerBean body    ,         @retrofit2.http.Path("answerId") Long answerId            
  );

}
