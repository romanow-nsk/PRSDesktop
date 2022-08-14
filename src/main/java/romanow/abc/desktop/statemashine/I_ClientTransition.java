package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamAdminPanel;
import romanow.abc.desktop.PRSExamAdminPanel;

public interface I_ClientTransition{
public String testTransition(PRSExamAdminPanel panel, StateEntity env);
public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env);
public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env);
}
