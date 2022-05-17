package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMExamTakingStop extends EMClientEmpty {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        return "";
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline();
    }
    }
