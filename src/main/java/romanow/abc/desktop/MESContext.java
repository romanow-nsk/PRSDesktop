package romanow.abc.desktop;

import javax.swing.*;
import java.awt.*;

public class MESContext {
    public final TextArea MES;
    public final I_LogArea  logFrame;
    public final JTextField MESShort;
    public MESContext(TextArea mes0, I_LogArea frame, JTextField ff) {
        MES = mes0;
        logFrame = frame;
        MESShort = ff;
        }
}
