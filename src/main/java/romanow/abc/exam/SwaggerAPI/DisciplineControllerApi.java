package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.DisciplineBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface DisciplineControllerApi {
  
  /**
   * Get all disciplines
   * 

   * @return Call&lt;List&lt;DisciplineBean&gt;&gt;

   */
  
  
  
    
  @GET("discipline")
  Call<List<DisciplineBean>> getAll5();
    

  
  /**
   * Get one discipline
   * 

   * @param disciplineId  (required)

   * @return Call&lt;DisciplineBean&gt;

   */
  
  
  
    
  @GET("discipline/{disciplineId}")
  Call<DisciplineBean> getOne5(
    @retrofit2.http.Path("disciplineId") Long disciplineId
  );

  
}

