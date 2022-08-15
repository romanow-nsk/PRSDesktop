package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.BasePanel;
import romanow.abc.desktop.PRSBasePanel;

import java.awt.*;

public class EMAnswerCheck extends EMClientEmpty {
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        return onlyInTaking(panel,env);
        }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {}
    }
