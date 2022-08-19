package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.BasePanel;


public interface I_ClientTransition<T extends BasePanel>{
public String testTransition(T panel, StateEntity env);
public void onTransitionAfter(T panel, StateEntity env);
public void onTransitionBefore(T panel, StateEntity env);
}
