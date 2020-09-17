package com.project.assessment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.project.assessment.model.ToDoDto;

public class Utility {

	public static Map<String, String> getMapData() throws IOException {

		Map<String, String> data = new HashMap<String, String>();
		try {
			FileInputStream fileInputStream = new FileInputStream("./data.xlsx");

			Workbook workbook = new XSSFWorkbook(fileInputStream);

			Sheet sheet = workbook.getSheetAt(0);
			int lastRowNumber = sheet.getLastRowNum();

			for (int i = 0; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				Cell keyCell = row.getCell(0);
				String key = keyCell.getStringCellValue().trim();

				Cell valueCell = row.getCell(1);
				String value = valueCell.getRichStringCellValue().getString();
				data.put(key, value);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}
	
	public static void generateExcelData(List<ToDoDto> dto) throws IOException {
		String[] COLUMNs = { "Key", "Value"};	
		
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			List<ToDoDto> temp = new ArrayList<>();
			int listSize = dto.size();
			int index = 0;

			do {
				temp.add(dto.get(index));
				index++;
				if (temp.size() >= 1000000 || temp.size() >= listSize) {

					Sheet sheet = workbook.createSheet("data");

					for (int i = 0; i < 10; i++) {
						sheet.setColumnWidth(i, 5000);
					}

					
					// Row for Header
					Row headerRow = sheet.createRow(0);

					// Header
					for (int col = 0; col < COLUMNs.length; col++) {
						Cell cell = headerRow.createCell(col);
						cell.setCellValue(COLUMNs[col]);
					}

					int rowIdx = 1;
					for (ToDoDto toDoDto : temp) {
						Row row = sheet.createRow(rowIdx++);
						
						row.createCell(0).setCellValue(toDoDto.getId());
						row.createCell(1).setCellValue(toDoDto.getName());
					

					}
					temp.clear();
				}
			} while (listSize > index);

			workbook.write(out);
		}
	}

}
