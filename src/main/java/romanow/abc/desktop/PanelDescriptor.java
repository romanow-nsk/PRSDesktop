package romanow.abc.desktop;

import romanow.abc.bridge.constants.UserRole;

public class PanelDescriptor {
    public final String name;
    public final Class view;
    public final UserRole userTypes[];
    public PanelDescriptor(String nm, Class zz, UserRole tt[]) {
        name=nm;
        view=zz;
        userTypes=tt;
        }
}
