package romanow.abc.convert.onewayticket;


import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.math.BigInteger;

public class OWTDocWriter implements OWT_Writer {
    private XWPFDocument docxModel = new XWPFDocument();
    //CTSectPr ctSectPr = docxModel.getDocument().getBody().addNewSectPr();
    //XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docxModel, ctSectPr);
    //CTP ctpHeaderModel = createHeaderModel("ОГК - приказы");
    //XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeaderModel, docxModel);
    //headerFooterPolicy.createHeader( XWPFHeaderFooterPolicy.DEFAULT, new XWPFParagraph[]{headerParagraph} );
    //CTP ctpFooterModel = createFooterModel("ОГК - приказы");
    //XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooterModel, docxModel);
    //headerFooterPolicy.createFooter( XWPFHeaderFooterPolicy.DEFAULT,  new XWPFParagraph[]{footerParagraph} );
    private String fname;
    public void open(String fspec) throws Exception {
        fname = fspec;
        }
    public void close() throws Exception{
        FileOutputStream outputStream = new FileOutputStream(fname);
        docxModel.write(outputStream);
        outputStream.close();
        }

    @Override
    public void space() throws Exception {
        emptyParaghaph();
        }

    public class ParagraphParams{
        public final ParagraphAlignment align;
        public final boolean bold;
        public final boolean italic;
        public final int fontSize;
        public final String font;
        public final String color;
        public final int first;
        public final int after;
        public ParagraphParams(ParagraphAlignment align, boolean bold, boolean italic, int fontSize, String font, String color, int first0, int after0) {
            this.align = align;
            this.bold = bold;
            this.italic = italic;
            this.fontSize = fontSize;
            this.font = font;
            this.color = color;
            after = after0;
            first = first0;

            }
        }
    private ParagraphParams defaultParam = new ParagraphParams(ParagraphAlignment.LEFT,false,false,12,"Times New Roman","00000000",0,20);
    public void write(String txt){
        createParaghaph(defaultParam,txt);
        }
    private void emptyParaghaph(){
        XWPFParagraph bodyParagraph = docxModel.createParagraph();
        bodyParagraph.setSpacingAfter(0);
        bodyParagraph.setSpacingAfterLines(0);
        bodyParagraph.setIndentationFirstLine(0);
        XWPFRun paragraphConfig = bodyParagraph.createRun();
        paragraphConfig.setFontSize(8);
        paragraphConfig.setFontFamily("Times New Roman");
        paragraphConfig.setColor("000000");
        }
    private void breakParaghaph(){
        XWPFParagraph bodyParagraph = docxModel.createParagraph();
        bodyParagraph.setSpacingAfter(0);
        bodyParagraph.setSpacingAfterLines(0);
        bodyParagraph.setIndentationFirstLine(0);
        XWPFRun paragraphConfig = bodyParagraph.createRun();
        paragraphConfig.setFontSize(8);
        paragraphConfig.setFontFamily("Times New Roman");
        paragraphConfig.setColor("000000");
        paragraphConfig.addBreak(BreakType.PAGE);
        }
    private void setSpacing(XWPFParagraph bodyParagraph){
        CTPPr ppr = bodyParagraph.getCTP().getPPr();
        if (ppr == null) ppr = bodyParagraph.getCTP().addNewPPr();
        CTSpacing spacing = ppr.isSetSpacing()? ppr.getSpacing() : ppr.addNewSpacing();
        spacing.setAfter(BigInteger.valueOf(0));
        spacing.setBefore(BigInteger.valueOf(0));
        spacing.setLineRule(STLineSpacingRule.AUTO);
        spacing.setLine(BigInteger.valueOf(240));
        }
    private void setParagraphContent(XWPFParagraph bodyParagraph, ParagraphParams params, String text ){
        bodyParagraph.setAlignment(params.align);
        bodyParagraph.setSpacingAfter(0);
        bodyParagraph.setSpacingAfterLines(0);
        bodyParagraph.setIndentationFirstLine(params.first);
        setSpacing(bodyParagraph);
        setRunContent(bodyParagraph.createRun(),params,text);
        }
    private void setRunContent(XWPFRun paragraphConfig , ParagraphParams params, String text ){
        paragraphConfig.setItalic(params.italic);
        paragraphConfig.setBold(params.bold);
        paragraphConfig.setFontSize(params.fontSize);
        paragraphConfig.setFontFamily(params.font);
        paragraphConfig.setColor(params.color);
        paragraphConfig.setText(text);
        }

    private void createParaghaph(ParagraphParams params, String text ){
        XWPFParagraph bodyParagraph = docxModel.createParagraph();
        setParagraphContent(bodyParagraph,params,text);
        }
    public void setCell(XWPFTableCell cell, ParagraphParams params, String text, int size){
        cell.removeParagraph(0);
        setParagraphContent(cell.addParagraph(),params,text);
        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(size));
        }
    private CTP createFooterModel(String footerContent) {
        // создаем футер или нижний колонтитул
        CTP ctpFooterModel = CTP.Factory.newInstance();
        CTR ctrFooterModel = ctpFooterModel.addNewR();
        CTText cttFooter = ctrFooterModel.addNewT();

        cttFooter.setStringValue(footerContent);
        return ctpFooterModel;
        }
    private CTP createHeaderModel(String headerContent) {
        // создаем хедер или верхний колонтитул
        CTP ctpHeaderModel = CTP.Factory.newInstance();
        CTR ctrHeaderModel = ctpHeaderModel.addNewR();
        CTText cttHeader = ctrHeaderModel.addNewT();
        cttHeader.setStringValue(headerContent);
        return ctpHeaderModel;
        }
    //----------------------------------------------------------------------------------------------------
    public static void main(String[] args){
    }
}
