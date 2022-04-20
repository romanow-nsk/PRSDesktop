package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.TaskBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface TaskControllerApi {
  
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

  
}

