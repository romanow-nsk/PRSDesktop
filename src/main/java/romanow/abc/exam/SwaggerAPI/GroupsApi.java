package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.FullGroupBean;
import romanow.abc.exam.model.GroupBean;
import romanow.abc.exam.model.StudentBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GroupsApi {
  /**
   * Add disciplines to a group
   * 
   * @param body  (required)
   * @param groupId  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("groups/{groupId}/disciplines")
  Call<Void> addDiscipline(
                    @retrofit2.http.Body List<Long> body    ,         @retrofit2.http.Path("groupId") Long groupId            
  );

  /**
   * Create a group
   * 
   * @param body  (required)
   * @return Call&lt;GroupBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("groups")
  Call<GroupBean> create2(
                    @retrofit2.http.Body GroupBean body    
  );

  /**
   * Delete group
   * 
   * @param groupId  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("groups/{groupId}")
  Call<Void> delete1(
            @retrofit2.http.Path("groupId") Long groupId            
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
  @PUT("groups/{groupId}")
  Call<GroupBean> edit(
                    @retrofit2.http.Body GroupBean body    ,         @retrofit2.http.Path("groupId") Long groupId            
  );

  /**
   * Get all groups
   * 
   * @return Call&lt;List&lt;GroupBean&gt;&gt;
   */
  @GET("groups")
  Call<List<GroupBean>> getAll2();
    

  /**
   * Get one group
   * 
   * @param groupId  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullGroupBean&gt;
   */
  @GET("groups/{groupId}")
  Call<FullGroupBean> getOne2(
            @retrofit2.http.Path("groupId") Long groupId            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get students in a group
   * 
   * @param groupId  (required)
   * @return Call&lt;List&lt;StudentBean&gt;&gt;
   */
  @GET("groups/{groupId}/students")
  Call<List<StudentBean>> getStudents(
            @retrofit2.http.Path("groupId") Long groupId            
  );

  /**
   * Remove disciplines from a group
   * 
   * @param body  (required)
   * @param groupId  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @DELETE("groups/{groupId}/disciplines")
  Call<Void> removeDiscipline(
                    @retrofit2.http.Body List<Long> body    ,         @retrofit2.http.Path("groupId") Long groupId            
  );

}
