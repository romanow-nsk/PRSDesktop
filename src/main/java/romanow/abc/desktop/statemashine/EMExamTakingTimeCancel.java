package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMExamTakingTimeCancel extends EMClientEmpty {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        return null;
    }

    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {

    }

    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {

    }
}
