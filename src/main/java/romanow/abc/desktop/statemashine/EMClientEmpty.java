package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMClientEmpty implements I_ClientTransition{
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        return "";
    }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {}
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {}
    }
