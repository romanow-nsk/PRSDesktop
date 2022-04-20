package romanow.abc.desktop;

import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import retrofit2.Call;
import retrofit2.Response;
import romanow.abc.core.constants.ValuesBase;

import java.io.IOException;

public abstract class APICall2<T> {
    public abstract Call<T> apiFun();
    public APICall2(){}
    public T call(MainBaseFrame base)throws UniException {
        String mes="";
        String mes1="";
        Response<T> res;
        long tt;
        try {
            tt = System.currentTimeMillis();
            res = apiFun().execute();
            } catch (Exception ex) {
                throw UniException.bug(ex);
                }
            if (!res.isSuccessful()){
                if (res.code()== ValuesBase.HTTPAuthorization){
                    mes =  "Сеанс закрыт " + Utils.httpError(res);
                    System.out.println(mes.replace("$","\n"));
                    //new Message(300,300,mes,ValuesBase.PopupMessageDelay);
                    base.logOff();
                    throw UniException.io(mes);
                    }
                try {
                    //mes1 = "Ошибка " + res.message() + " (" + res.code() + ")";
                    mes = res.errorBody().string();
                    } catch (IOException ex){ mes += "$Ошибка: "+ex.toString(); }
                System.out.println(mes.replace("$","\n"));
                //new Message(300,300,mes,ValuesBase.PopupMessageDelay);
                throw UniException.io(mes);
                }
            //System.out.println("time="+(System.currentTimeMillis()-tt)+" мс");
            return res.body();
            }
    }

