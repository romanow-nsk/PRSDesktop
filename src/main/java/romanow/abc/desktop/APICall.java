package romanow.abc.desktop;

import romanow.abc.core.Utils;
import retrofit2.Call;
import retrofit2.Response;
import romanow.abc.core.constants.ValuesBase;

public abstract class APICall<T> {
    public abstract Call<T> apiFun();
    public abstract void onSucess(T oo);
    public APICall(MainBaseFrame base){
            try {
                long tt = System.currentTimeMillis();
                Response<T> res = apiFun().execute();
                if (!res.isSuccessful()){
                    if (res.code()== ValuesBase.HTTPAuthorization){
                        System.out.println("Сеанс закрыт " + Utils.httpError(res));
                        if (base!=null)
                            base.logOff();
                        return;
                        }
                    System.out.println("Ошибка " + res.message()+" ("+res.code()+") "+res.errorBody().string());
                    return;
                    }
                else{
                    //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
                    onSucess(res.body());
                    }
                } catch (Exception ex) {
                    Utils.printFatalMessage(ex);
                    }
            }

}
