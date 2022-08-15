package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.desktop.PRSBasePanel;

public class EMExamTakingStart implements I_ClientTransition {
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        SAExamTaking taking = (SAExamTaking) env;
        OwnDateTime date = new OwnDateTime();
        date.onlyDate();
        OwnDateTime date2 = taking.getStartTime().clone();
        date2.onlyDate();
        if (date.timeInMS()!=date2.timeInMS())
            return "Дата экзамена другая";
        return "";
        }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline(true);
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {
        }
}
