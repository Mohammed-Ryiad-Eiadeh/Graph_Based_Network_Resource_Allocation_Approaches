package Attack_Defence_Graph.org;

import java.nio.file.Path;

/**
 * Thi enumeration includes the value id for each graph problem we have
 */
public enum Graph {
    SCADA, DER, e_commerce, VOIP, ABSNP, ASFS3, ASS2009, HG1, HG2, AWS03, ALSFSA2, ASS, ATF, Figure3,
    SCADA_rand, DER_rand, e_commerce_rand, VOIP_rand, ABSNP_rand, ASFS3_rand, ASS2009_rand, HG1_rand, HG2_rand, AWS03_rand;

    /**
     * This method is used to retrieve the path of the graph problem
     * @return The path of the graph problem
     */
    public Path getPath() {
       String directory = System.getProperty("user.dir");
        return switch (this) {
            case SCADA -> Path.of(directory, "\\SCADA.txt");
            case DER -> Path.of(directory, "\\DER.txt");
            case e_commerce -> Path.of(directory, "\\e_commerce.txt");
            case VOIP -> Path.of(directory, "\\VOiP.txt");
            case ABSNP -> Path.of(directory, "\\ABSNP.txt");
            case ASFS3 -> Path.of(directory, "\\ASFS3.txt");
            case ASS2009 -> Path.of(directory, "\\ASS2009.txt");
            case HG1 -> Path.of(directory, "\\HG1.txt");
            case HG2 -> Path.of(directory, "\\HG2.txt");
            case AWS03 -> Path.of(directory, "\\AWS03.txt");
            case ALSFSA2 -> Path.of(directory, "\\ALSFSA2.txt");
            case ASS -> Path.of(directory, "\\ASS.txt");
            case ATF -> Path.of(directory, "\\ATF.txt");
            case SCADA_rand -> Path.of(directory, "\\SCADA_rand.txt");
            case DER_rand -> Path.of(directory, "\\DER_rand.txt");
            case e_commerce_rand -> Path.of(directory, "\\e_commerce_rand.txt");
            case VOIP_rand -> Path.of(directory, "\\VOiP_rand.txt");
            case ABSNP_rand -> Path.of(directory, "\\ABSNP_rand.txt");
            case ASFS3_rand -> Path.of(directory, "\\ASFS3_rand.txt");
            case ASS2009_rand -> Path.of(directory, "\\ASS2009_rand.txt");
            case HG1_rand -> Path.of(directory, "\\HG1_rand.txt");
            case HG2_rand -> Path.of(directory, "\\HG2_rand.txt");
            case AWS03_rand -> Path.of(directory, "\\AWS03_rand.txt");
            case Figure3 -> Path.of(directory, "\\Figure3.txt");
        };
    }
}
