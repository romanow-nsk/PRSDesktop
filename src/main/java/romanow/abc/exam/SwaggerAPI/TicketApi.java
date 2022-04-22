package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.Pageable;
import romanow.abc.exam.model.StudentAnswerBean;
import romanow.abc.exam.model.UpdateTicketBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TicketApi {
  /**
   * Get answers by ticket
   * Sorted by default by task type (questions first)
   * @param ticketId  (required)
   * @param pageable  (required)
   * @return Call&lt;List&lt;StudentAnswerBean&gt;&gt;
   */
  @GET("ticket/{ticketId}/answer")
  Call<List<StudentAnswerBean>> getAnswers(
            @retrofit2.http.Path("ticketId") Long ticketId            ,     @retrofit2.http.Query("pageable") Pageable pageable                
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
  @PUT("ticket/rating")
  Call<Void> updateTicketsRating(
                    @retrofit2.http.Body List<UpdateTicketBean> body    
  );

}
