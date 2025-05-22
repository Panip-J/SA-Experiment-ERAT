package com.erat.factory;

import com.erat.service.FileParser;
import com.erat.service.impl.CsvParser;
import com.erat.service.impl.ExcelParser;

public class FileParserFactory {
    public static FileParser getParser(String fileType) {
        if ("excel".equalsIgnoreCase(fileType)) {
            return new ExcelParser();
        } else if ("csv".equalsIgnoreCase(fileType)) {
            return new CsvParser();
        }
        throw new IllegalArgumentException("不支持的文件类型: " + fileType);
    }
}