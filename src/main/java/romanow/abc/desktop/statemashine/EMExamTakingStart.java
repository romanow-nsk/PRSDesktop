package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.EMExamTaking;
import romanow.abc.core.utils.OwnDateTime;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMExamTakingStart implements I_ClientTransition {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        EMExamTaking taking = (EMExamTaking) env;
        OwnDateTime date = new OwnDateTime();
        date.onlyDate();
        OwnDateTime date2 = taking.getStartTime().clone();
        date2.onlyDate();
        if (date.timeInMS()!=date2.timeInMS())
            return "Дата экзамена другая";
        return "";
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline(true);
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {
        }
}
