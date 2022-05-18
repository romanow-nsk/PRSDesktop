package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.EMExamTaking;
import romanow.abc.core.entity.subjectarea.EMStudRating;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMStudRatingTakingSet implements I_ClientTransition {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        EMStudRating rating = (EMStudRating)env;
        EMExamTaking taking = panel.getCTaking();
        if (taking==null)
            return "Не выбран прием экзамена";
        if (taking.isOneGroup() && rating.getStudent().getRef().getEMGroup().getOid()!=taking.getGroup().getOid()){
            return "Прием экзамена для другой группы";
            }
        return "";
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
    }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {
        EMStudRating rating = (EMStudRating)env;
        rating.getEMExamTaking().setOid(panel.getCTaking().getOid());
        }
}
