package romanow.abc.excel;

import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ExcelBase {
    public abstract String [] openTable(String fileName, String columns[]) throws IOException;
    public abstract void procSheet(String sheetName,I_ExcelBack back);
    public abstract void close();
    }
