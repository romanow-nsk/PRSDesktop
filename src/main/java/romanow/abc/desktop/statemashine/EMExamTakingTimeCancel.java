package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMExamTakingTimeCancel extends EMClientEmpty {
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
        return null;
    }

    @Override
    public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env) {

    }

    @Override
    public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env) {

    }
}
