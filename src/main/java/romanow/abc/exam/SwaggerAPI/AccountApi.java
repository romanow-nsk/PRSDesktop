package romanow.abc.exam.SwaggerAPI;

import romanow.abc.exam.CollectionFormats.*;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import romanow.abc.exam.model.AccountBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountApi {
  /**
   * Create an account
   * Used by admins to create admins
   * @param body  (required)
   * @return Call&lt;AccountBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("account")
  Call<AccountBean> createAccount(
                    @retrofit2.http.Body AccountBean body    
  );

  /**
   * Get all accounts
   * 
   * @return Call&lt;List&lt;AccountBean&gt;&gt;
   */
  @GET("account")
  Call<List<AccountBean>> findAll3();
    

  /**
   * Get one account
   * 
   * @param accountId  (required)
   * @return Call&lt;AccountBean&gt;
   */
  @GET("account/{accountId}")
  Call<AccountBean> findOne2(
            @retrofit2.http.Path("accountId") Long accountId            
  );

  /**
   * Get info about self
   * 
   * @return Call&lt;AccountBean&gt;
   */
  @GET("account/me")
  Call<AccountBean> me();
    

  /**
   * Update other&#x27;s account
   * 
   * @param body  (required)
   * @return Call&lt;AccountBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("account")
  Call<AccountBean> updateAccount(
                    @retrofit2.http.Body AccountBean body    
  );

  /**
   * Update sender&#x27;s account
   * 
   * @param body  (required)
   * @return Call&lt;AccountBean&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("account/me")
  Call<AccountBean> updateMyAccount(
                    @retrofit2.http.Body AccountBean body    
  );

}
