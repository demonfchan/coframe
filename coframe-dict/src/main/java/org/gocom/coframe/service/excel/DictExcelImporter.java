package org.gocom.coframe.service.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class DictExcelImporter {

	public static ImportDict dictImport(ExcelTemplate template, InputStream excelFile) throws IOException {
		return template.importDictData(excelFile, 500);
	}

	public static void dictExport(List<ExcelDictLine> dictData, String title, ExcelTemplate template, OutputStream out)
			throws IOException {

		if (template.getStartRow() == -1) {
			return;
		}
		if (title != null)// 先填充标题
		{
			template.generateTitleData(title);
		}
		template.generateDictData(dictData);// 生成数据内容
		template.writeToStream(out);
	}
}
