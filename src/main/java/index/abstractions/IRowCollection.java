package index.abstractions;
import java.util.Iterator;

public interface IRowCollection<T extends IRowItem<T>> extends Iterable<T> {
   T findByKey(IRowKey key);
}
