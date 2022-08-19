package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.desktop.PRSExamPanel;

public class EMExamTakingStart implements I_ClientTransition<PRSExamPanel> {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
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
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline(true);
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {
        }
}
