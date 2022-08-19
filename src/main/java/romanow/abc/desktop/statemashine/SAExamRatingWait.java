package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamPanel;

public class SAExamRatingWait implements I_ClientTransition<PRSExamPanel>{
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        return "Переводится автоматически с началом экзамена";
        }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {
        }
}
