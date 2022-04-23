package romanow.abc.excel;

import java.util.HashMap;

public interface I_ExcelBack {
    public void onRow(String values[]);
    public void onFinishSheet(String errorMes);
}
