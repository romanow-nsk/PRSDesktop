package romanow.abc.desktop.vkr;

import romanow.abc.bridge.constants.UserRole;

public class EMVKRPanelDescriptor {
    public final String name;
    public final Class view;
    public final UserRole userTypes[];
    public EMVKRPanelDescriptor(String nm, Class zz, UserRole tt[]) {
        name=nm;
        view=zz;
        userTypes=tt;
        }
}
