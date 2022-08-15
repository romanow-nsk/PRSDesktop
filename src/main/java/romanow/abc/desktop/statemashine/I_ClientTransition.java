package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.BasePanel;
import romanow.abc.desktop.PRSBasePanel;


public interface I_ClientTransition{
public String testTransition(PRSBasePanel panel, StateEntity env);
public void onTransitionAfter(PRSBasePanel panel, StateEntity env);
public void onTransitionBefore(PRSBasePanel panel, StateEntity env);
}
