package romanow.abc.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import romanow.abc.core.entity.Entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class ExcelX2 extends ExcelBase{
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;
    private HashMap<String,Integer> colIndexes = new HashMap<>();
    private String columns[];
    @Override
    public String[] openTable(String fileName, String[] columns0) throws IOException {
        columns = columns0;
        FileInputStream out = new FileInputStream(new File(fileName));
        workbook = new XSSFWorkbook(out);
        int ns = workbook.getNumberOfSheets();
        String res[] = new String[ns];
        for (int i = 0; i < ns; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            res[i] = sheetName;
            }
        return res;
        }

    public void procSheet(String sheetName,I_ExcelBack back){
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet==null){
            back.onFinishSheet("Не найден лист: "+sheetName);
            return;
            }
            int sz = sheet.getLastRowNum();
            if (sz<=1){
                back.onFinishSheet("");
                return;
                }
            Row hd = sheet.getRow(0);
            boolean done=false;
            colIndexes.clear();
            for(int k=0;!done;k++){
                String s1;
                try {
                    s1 = hd.getCell(k).getStringCellValue();
                    if (s1.length()==0)
                        done=true;
                    else
                        colIndexes.put(s1,k);
                    } catch (Exception ee){ done=true; }
                }
            boolean bb=true;
            String gg = "";
            for(int k=0;k<columns.length;k++)
                if (colIndexes.get(columns[k])==null){
                    gg +="Не найден столбец "+sheetName+"."+columns[k]+"\n";
                    bb=false;
                    }
            if (!bb){
                back.onFinishSheet(gg);
                return;
                }
            for (int k = 1; k <= sz; k++) {     // Пропустить пустую
                Row row = sheet.getRow(k);
                String vv[] = new String[columns.length];
                for(int j=0;j<columns.length;j++){
                    try {
                        vv[j] = row.getCell(colIndexes.get(columns[j])).getStringCellValue();
                        } catch (Exception ee){
                            gg+="Ошибка таблицы ["+(k+1)+","+(j+1)+"]\n";
                            }
                    }
                back.onRow(vv);
                }
            back.onFinishSheet(gg);
            }

    @Override
    public void close() {
        try {
            workbook.close();
            } catch (IOException e) {}
    }
}
