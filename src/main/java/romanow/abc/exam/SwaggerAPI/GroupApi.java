package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.CreateGroupBean;
import romanow.abc.exam.model.GroupBean;
import romanow.abc.exam.model.StudentBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GroupApi {
  /**
   * Create a group
   * 
   * @param body  (required)
   * @return Call&lt;GroupBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("group")
  Call<GroupBean> create2(
                    @retrofit2.http.Body CreateGroupBean body    
  );

  /**
   * Edit group
   * 
   * @param body  (required)
   * @param groupId  (required)
   * @return Call&lt;GroupBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("group/{groupId}")
  Call<GroupBean> edit(
                    @retrofit2.http.Body CreateGroupBean body    ,         @retrofit2.http.Path("groupId") Long groupId            
  );

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

  /**
   * Get students in a group
   * 
   * @param groupId  (required)
   * @return Call&lt;List&lt;StudentBean&gt;&gt;
   */
  @GET("group/{groupId}/students")
  Call<List<StudentBean>> getStudents(
            @retrofit2.http.Path("groupId") Long groupId            
  );

}
