package doridlens.query.neo4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:22
 * Description: CSV写入器,将数据写入到CSV文件
 */
public class CSVWriter {

    private String csvPrefix;

    public CSVWriter(String csvPrefix) {
        this.csvPrefix = csvPrefix;
    }

    /**
     * 导出结果到CSV文件
     * @param rows          数据行列表
     * @param columns       数据列名列表
     * @param csvSuffix     CSV文件后缀
     * @throws IOException 读写异常
     */
    public void resultToCSV(List<Map<String, Object>> rows, List<String> columns, String csvSuffix)
            throws IOException {
        String name = csvPrefix + csvSuffix;
        BufferedWriter writer = new BufferedWriter(new FileWriter(name));
        writeColumnLabels(columns, writer);
        for (Map<String, Object> row : rows) {
            writeRowValues(row, columns, writer);
        }
        writer.close();
    }

    /**
     * 写入列名到CSV文件
     * @param columns   列名列表
     * @param writer    文件写入器实例
     * @throws IOException 读写异常
     */
    private void writeColumnLabels(List<String> columns, BufferedWriter writer) throws IOException {
        for (int i = 0; i < columns.size() - 1; i++) {
            writer.write(columns.get(i));
            writer.write(',');
        }
        writer.write(columns.get(columns.size() - 1));
        writer.newLine();
    }

    /**
     * 写入数据到CSV文件
     * @param row       列名-数据,键值对
     * @param columns   列名列表
     * @param writer    文件写入器实例
     * @throws IOException 读写异常
     */
    private void writeRowValues(Map<String, Object> row, List<String> columns, BufferedWriter writer)
            throws IOException {
        Object val;
        for (int i = 0; i < columns.size() - 1; i++) {
            val = row.get(columns.get(i));
            if (val != null) {
                writer.write(val.toString());
                writer.write(',');
            }
        }
        val = row.get(columns.get(columns.size() - 1));
        if (val != null) {
            writer.write(val.toString());
        }
        writer.newLine();
    }

    /**
     * 写入统计数据到CSV文件
     * @param stats 统计数据
     * @param csvSuffix CSV文件后缀,包括文件名和.csv后缀
     * @throws IOException 读写异常
     */
    public void statsToCSV(Map<String, Double> stats, String csvSuffix) throws IOException {
        String name = csvPrefix + csvSuffix;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(name))) {
            Set<String> keys = stats.keySet();
            for (String key : keys) {
                writer.write(key);
                writer.write(',');
            }
            writer.newLine();
            for (String key : keys) {
                writer.write(String.valueOf(stats.get(key)));
                writer.write(',');
            }
        }
    }

}
