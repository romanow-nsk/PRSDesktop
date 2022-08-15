package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSBasePanel;

public class EMStudRatingWait implements I_ClientTransition{
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        return "Переводится автоматически с началом экзамена";
        }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {
        }
}
