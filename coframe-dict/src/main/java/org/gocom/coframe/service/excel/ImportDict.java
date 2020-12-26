package org.gocom.coframe.service.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImportDict {

	private final List<ExcelDictLine> lines = new ArrayList<>();
	private final AtomicInteger counter = new AtomicInteger(0);

	public void addLine(ExcelDictLine line) {
		this.lines.add(line);
		counter.incrementAndGet();
	}

	public int getCount() {
		return counter.get();
	}

	public List<ExcelDictLine> getLines() {
		return lines;
	}

	public AtomicInteger getCounter() {
		return counter;
	}
}
