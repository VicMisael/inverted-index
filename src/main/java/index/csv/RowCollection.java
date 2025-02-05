package index.csv;

import index.abstractions.IRowCollection;
import index.abstractions.IRowKey;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RowCollection implements IRowCollection<RowItem> {
    private final List<RowItem> rows = new ArrayList<>();

    public RowCollection(String csvFilePath) {

        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));

            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            CSVParser parser = new CSVParser(reader,format);

            int rowIndex = 0;
            for (CSVRecord record : parser) {
                rows.add(new RowItem(record, rowIndex++));
            }
            parser.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions more gracefully depending on your application's requirements
        }
    }

    @Override
    public Iterator<RowItem> iterator() {
        return rows.iterator();
    }

    public long size(){
        return rows.size();
    }

    @Override
    public RowItem findByKey(IRowKey key) {
        if (key instanceof RowKey) {
            int index = ((RowKey) key).getKeyAsInt();
            if (index >= 0 && index < rows.size()) {
                return rows.get(index);
            }
        }
        return null;
    }
}
