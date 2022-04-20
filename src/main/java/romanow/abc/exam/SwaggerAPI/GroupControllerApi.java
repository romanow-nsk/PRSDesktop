package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;



import io.reactivex.Observable;


import retrofit2.Call;

import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.GroupBean;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public interface GroupControllerApi {
  
  /**
   * Get all groups
   * 

   * @return Call&lt;List&lt;GroupBean&gt;&gt;

   */
  
  
  
    
  @GET("group")
  Call<List<GroupBean>> getAll2();
    

  
  /**
   * Get one group
   * 

   * @param groupId  (required)

   * @return Call&lt;GroupBean&gt;

   */
  
  
  
    
  @GET("group/{groupId}")
  Call<GroupBean> getOne2(
    @retrofit2.http.Path("groupId") Long groupId
  );

  
}

