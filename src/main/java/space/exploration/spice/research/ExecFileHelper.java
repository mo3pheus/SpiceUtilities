package space.exploration.spice.research;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.exploration.spice.utilities.ExecUtils;

import java.io.File;

public class ExecFileHelper {
    private File   positionsCalcFile = ExecUtils.getExecutionFile
            ("/POSITIONS/copyPositionExecFiles.sh");
    private Logger logger            = LoggerFactory.getLogger(ExecFileHelper.class);

    private String sourceFileName      = null;
    private String destinationFileName = null;

    public ExecFileHelper(String sourceFileName, String destinationFileName) {
        this.sourceFileName = sourceFileName;
        this.destinationFileName = destinationFileName;
    }

    public String[] makeFiles(){
        return ExecUtils.getExecutionOutput(positionsCalcFile,sourceFileName,destinationFileName);
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public File getDestinationFileName() {
        return ExecUtils.getExecutionFile(destinationFileName);
    }
}
