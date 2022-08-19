package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSSemesterPanel;

public class SAPointIssue extends SAPointEmpty {
    @Override
    public String testTransition(PRSSemesterPanel panel, StateEntity env) {
        return panel.isNewPoint() ? "Заполните данные (вариант)" : "";
    }
}
