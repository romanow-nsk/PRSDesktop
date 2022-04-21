package romanow.abc.bridge;

import lombok.Getter;
import romanow.abc.bridge.constants.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class TokenBody {
    @Getter private String token;
    @Getter private String roles;
    public HashMap<UserRole,UserRole> getRoles(){
        String ss = roles;
        HashMap<UserRole,UserRole> out = new HashMap<>();
        while (ss.length()!=0){
            int idx=ss.indexOf(",");
            if (idx==-1) {
                UserRole vv = UserRole.valueOf(ss.trim());
                out.put(vv,vv);
                return out;
                }
            String rr = ss.substring(0,idx);
            UserRole dd = UserRole.valueOf(rr.trim());
            out.put(dd,dd);
            ss = ss.substring(idx+1);
            }
        return out;
    }
}
