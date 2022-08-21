package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSSemesterPanel;

public class SAPointEmpty implements I_ClientTransition<PRSSemesterPanel> {
    @Override
    public String testTransition(PRSSemesterPanel panel, StateEntity env) {
        return "";
        }
    @Override
    public void onTransitionAfter(PRSSemesterPanel panel, StateEntity env) {
        panel.studentRatingUpdate();
        panel.refreshStudentPoints();
        }
    @Override
    public void onTransitionBefore(PRSSemesterPanel panel, StateEntity env) {
        }
}
