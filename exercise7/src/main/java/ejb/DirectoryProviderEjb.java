package ejb;

import util.entity.ListItem;
import interfaces.local.DirectoryProvider;

import javax.ejb.Stateless;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class DirectoryProviderEjb implements DirectoryProvider {

    private String currDir;

    public DirectoryProviderEjb() {
        currDir = System.getProperty("user.dir") + "\\";
    }

    @Override
    public List<ListItem> getDirectories() {
        List<ListItem> fileList = new ArrayList<>();
        File curDir = new File(currDir);
        File[] curDirFiles = curDir.listFiles();
        int nestingLvl = 1;
        processDirectory(curDirFiles, fileList, nestingLvl);
        return fileList;
    }

    @Override
    public String getCurrDir() {
        return currDir;
    }

    private void processDirectory(File[] dir, List<ListItem> fileList, int nestingLvl) {
        if (dir != null)
            Arrays.stream(dir).forEach((file -> {
                if (file.isDirectory()) {
                    int nextNestingLvl = nestingLvl + 1;
                    fileList.add(new ListItem(file.getName() + "\\", nestingLvl));
                    processDirectory(file.listFiles(), fileList, nextNestingLvl);
                }
                else
                    fileList.add(new ListItem(file.getName(), nestingLvl));
            }));
    }
}
