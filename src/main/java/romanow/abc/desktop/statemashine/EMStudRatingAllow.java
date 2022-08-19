package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.PRSExamPanel;

public class EMStudRatingAllow implements I_ClientTransition<PRSExamPanel>{
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        long ruleId = panel.getCRating().getExamRule().getOid();
        int minBall = panel.getCDiscipline().getRules().getById(ruleId).getMinimalRating();
        return ((SAStudRating)env).getSemesterRating()>=minBall ? "" : "Недостаточно баллов для допуска";
        }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {
        }
}
