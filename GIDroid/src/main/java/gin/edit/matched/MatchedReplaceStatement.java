package gin.edit.matched;

import gin.SourceFile;
import gin.SourceFileTree;
import gin.edit.Edit;
import gin.edit.statement.ReplaceStatement;

import java.util.Random;

/*
 * Replace statement but restricted so we only ever replace matching types
 */
public class MatchedReplaceStatement extends ReplaceStatement {

    /**
     * This is our attempt at a Java-specific grammar aware operator.
     * Replace one statement over another one of the same type
     *
     * @param sourceFile to create an edit for
     * @param rng        random number generator, used to choose the target statements
     */
    // @param cu is provided to allow a matching node to be chosen
    public MatchedReplaceStatement(SourceFile sourceFile, Random rng) {
        super(sourceFile.getFilename(), 0, sourceFile.getFilename(), 0);

        SourceFileTree sf = (SourceFileTree) sourceFile;

        // target is in target method only
        destinationStatement = sf.getRandomStatementID(true, rng);

        // source can be anywhere in the class, but must be of a matching type
        sourceStatement = sf.getRandomNodeID(false, sf.getStatement(destinationStatement).getClass(), rng);
    }

    private MatchedReplaceStatement(String sourceFilename, int sourceStatement, String destinationFilename, int destinationStatement) {
        super(sourceFilename, sourceStatement, destinationFilename, destinationStatement);
    }

    @Override
    public String toString() {
        return super.toString(); //.replace("ReplaceStatement", "MatchedReplaceStatement");
    }

    public static Edit fromString(String description) {
        String[] tokens = description.split("\\s+");
        String[] srcTokens = tokens[1].split(":");
        String srcFilename = srcTokens[0];
        int source = Integer.parseInt(srcTokens[1]);
        String[] destTokens = tokens[3].split(":");
        String destFilename = destTokens[0];
        int destination = Integer.parseInt(destTokens[1]);
        return new MatchedReplaceStatement(srcFilename, source, destFilename, destination);
    }

    @Override
    public EditType getEditType() {
        return EditType.MATCHED_STATEMENT;
    }
}
