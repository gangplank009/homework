package interfaces.local;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DirectoryProvider {
    List<?> getDirectories();
    String getCurrDir();
}
