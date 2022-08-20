package romanow.abc.desktop.statemashine;

import romanow.abc.core.UniException;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSSemesterPanel;

public class SAPointIssue extends SAPointEmpty {
    @Override
    public String testTransition(PRSSemesterPanel panel, StateEntity env) {
        try {
            if (panel.isNewPoint())
                panel.pointAdd();
            return "";
            } catch (UniException ee){
                return ee.toString();
                }
    }
}
