package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.FullTaskBean;
import romanow.abc.exam.model.TaskBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TasksApi {
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
   * Delete a task (also deletes artefact)
   * 
   * @param taskId  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("task/{taskId}")
  Call<Void> deleteTask(
            @retrofit2.http.Path("taskId") Long taskId            
  );

  /**
   * Get all tasks
   * 
   * @return Call&lt;List&lt;TaskBean&gt;&gt;
   */
  @GET("task")
  Call<List<TaskBean>> findAll();
    

  /**
   * Get one task
   * 
   * @param taskId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullTaskBean&gt;
   */
  @GET("task/{taskId}")
  Call<FullTaskBean> findOne(
            @retrofit2.http.Path("taskId") Long taskId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Update a task
   * 
   * @param body  (required)
   * @param taskId  (required)
   * @return Call&lt;TaskBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("task/{taskId}")
  Call<TaskBean> updateTask(
                    @retrofit2.http.Body TaskBean body    ,         @retrofit2.http.Path("taskId") Long taskId            
  );

}
