package com.nv.hclutility.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfReportBuilder {

	private static final Logger LOGGER = Logger.getLogger(PdfReportBuilder.class);
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);

	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	static ArrayList<String> fileNameList = new ArrayList<>();

	public static void preparePdfReport(List<LinkedHashMap<String, String>> tableList, String fileName) {
		// List<LinkedHashMap<String, String>> tableList = null;
		int totalCalls = 0;
		int callsCount_customerInformation = 0;

		try {
			String FILEPATH = PropertyUtil.getInstance().getValueForKey("FilePath");
			totalCalls = tableList.size() - 1;
			callsCount_customerInformation = PdfReportBuilder.getCustomerInfoCallsCount(tableList);

			Document doc = new Document();
			String file = FILEPATH + fileName;
			File outFile = new File(file);
			fileNameList.add(file.substring(file.lastIndexOf("/") + 1));
			LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "pdf file is generated with the name :" + file);
			FileOutputStream fos = new FileOutputStream(outFile);
			PdfWriter.getInstance(doc, fos);
			doc.setPageSize(new Rectangle(1200, 1200));
			doc.open();
			/* add Title page to pdf report */
			// addTitlePage(doc);

			Chunk chunk = new Chunk("RTO Report :", catFont);
			chunk.setUnderline(0.2f, -2f);
			Paragraph paragraph = new Paragraph(chunk);
			addEmptyLine(paragraph, 2);
			doc.add(paragraph);
			addEmptyLine(paragraph, 2);

			Paragraph paragraph2 = new Paragraph(" Total Request Time Out : " + callsCount_customerInformation);
			doc.add(paragraph2);
			Paragraph paragraph3 = new Paragraph(" Total Calls in Report : " + totalCalls);
			addEmptyLine(paragraph3, 2);
			doc.add(paragraph3);
			// addEmptyLine(paragraph3, 2);

			PdfPTable table = createTable(tableList);
			doc.add(table);

			doc.close();
			LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "pdf file is created successfully");
		} catch (Exception ex) {
			LOGGER.error(
					PropertyFileConstants.EMAIL_THREAD + "Exception caught while preparing call history pdf report",
					ex);
		}

	}

	private static void addTitlePage(Document document) throws DocumentException {
		try {
			Paragraph preface = new Paragraph();
			addEmptyLine(preface, 1);
			preface.add(new Paragraph("Calls History Report", catFont));
			addEmptyLine(preface, 1);
			preface.add(new Paragraph(
					"Report generated by: " + System.getProperty("user.name") + ", " + dateFormat.format(new Date()),
					smallBold));
			addEmptyLine(preface, 3);
			preface.add(new Paragraph("This document provide call details for Gray Bar", smallBold));
			addEmptyLine(preface, 8);
			preface.add(new Paragraph("This document is a preliminary version and not subject"
					+ " to your license agreement or any other agreement.", redFont));
			document.add(preface);
			document.newPage();
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "exception caught in addtitlePage of pdfReportBuilder",
					e);
		}

	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
		LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "empty paragraph is inserted in pdf file");
	}

	private static PdfPTable createTable(List<LinkedHashMap<String, String>> callHistory) throws BadElementException {
		PdfPTable table = null;
		LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "table is created in pdf report");
		try {
			float[] relativeWidth = { 70f, 70f, 70f, 70f, 90f };
			table = new PdfPTable(relativeWidth);
			table.setWidthPercentage(100.0F);

			for (Entry<String, String> entry : callHistory.get(0).entrySet()) {
				Font font = new Font();
				font.setColor(BaseColor.WHITE);
				Phrase phrase = new Phrase();
				phrase.setFont(font);
				LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "entry value is :" + entry.getValue());
				phrase.add(entry.getValue());
				PdfPCell cell = new PdfPCell(phrase);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.BLACK);
				cell.setFixedHeight(40F);
				table.addCell(cell);
			}
			table.setHeaderRows(1);

			for (int i = 1; i < callHistory.size(); i++) {
				LinkedHashMap<String, String> map = callHistory.get(i);
				if (map != null) {
					Set<String> rowData = map.keySet();
					for (String colName : rowData) {
						Phrase phrase = new Phrase();
						phrase.add(callHistory.get(i).get(colName));
						PdfPCell cell = new PdfPCell(phrase);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setFixedHeight(500F);
						table.addCell(cell);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD + "exception caught in createTable of pdfReportBuilder", e);
		}
		LOGGER.info(PropertyFileConstants.EMAIL_THREAD + "table creation is completed in pdf report");
		return table;
	}

	private static int getCustomerInfoCallsCount(List<LinkedHashMap<String, String>> callHistory) {
		LOGGER.info(
				PropertyFileConstants.EMAIL_THREAD + " getCustomerInfoCallsCount map length is:" + callHistory.size());
		int callsCount_customerInformation = 0;
		for (LinkedHashMap<String, String> map : callHistory) {
			if (map.containsKey("var7")) {
				if (map.get("var7") != null) {
					if (map.get("var7").equalsIgnoreCase("true") || map.get("var7").equalsIgnoreCase("Yes")) {
						++callsCount_customerInformation;
					}
				}
			}

		}
		return callsCount_customerInformation;
	}
}
