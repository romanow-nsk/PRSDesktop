package romanow.abc.vkr.exam.SwaggerAPI;

import retrofit2.Call;
import retrofit2.http.*;

import romanow.abc.vkr.exam.model.FullGroupRatingBean;
import romanow.abc.vkr.exam.model.GroupRatingBean;

import java.util.List;

public interface GroupRatingApi {
  /**
   * Create group rating
   * 
   * @param body  (required)
   * @return Call&lt;GroupRatingBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("group-ratings")
  Call<GroupRatingBean> create2(
                    @retrofit2.http.Body GroupRatingBean body    
  );

  /**
   * Delete group rating
   * 
   * @param id  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("group-ratings/{id}")
  Call<Void> delete2(
            @retrofit2.http.Path("id") Long id            
  );

  /**
   * Get all group ratings
   * 
   * @return Call&lt;List&lt;GroupRatingBean&gt;&gt;
   */
  @GET("group-ratings")
  Call<List<GroupRatingBean>> findAll2();
    

  /**
   * Get full group rating
   * 
   * @param id  (required)
   * @param level  (optional, default to 0)
   * @return Call&lt;FullGroupRatingBean&gt;
   */
  @GET("group-ratings/{id}/full")
  Call<FullGroupRatingBean> findFull1(
            @retrofit2.http.Path("id") Long id            ,     @retrofit2.http.Query("level") Integer level                
  );

  /**
   * Get one group rating
   * 
   * @param id  (required)
   * @return Call&lt;GroupRatingBean&gt;
   */
  @GET("group-ratings/{id}")
  Call<GroupRatingBean> findOne1(
            @retrofit2.http.Path("id") Long id            
  );

  /**
   * Update group rating
   * 
   * @param body  (required)
   * @return Call&lt;GroupRatingBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("group-ratings")
  Call<GroupRatingBean> update2(
                    @retrofit2.http.Body GroupRatingBean body    
  );

}
