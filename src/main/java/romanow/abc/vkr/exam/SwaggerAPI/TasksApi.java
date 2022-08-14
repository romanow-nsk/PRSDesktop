package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import romanow.abc.vkr.exam.model.FullTaskBean;
import romanow.abc.vkr.exam.model.TaskBean;

import java.util.List;

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
   * Get full task
   * 
   * @param taskId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullTaskBean&gt;
   */
  @GET("task/{taskId}/full")
  Call<FullTaskBean> findFull(
            @retrofit2.http.Path("taskId") Long taskId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get one task
   * 
   * @param taskId  (required)
   * @return Call&lt;TaskBean&gt;
   */
  @GET("task/{taskId}")
  Call<TaskBean> findOne(
            @retrofit2.http.Path("taskId") Long taskId            
  );

  /**
   * Update a task
   * 
   * @param body  (required)
   * @return Call&lt;TaskBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("task")
  Call<TaskBean> updateTask(
                    @retrofit2.http.Body TaskBean body    
  );

}
