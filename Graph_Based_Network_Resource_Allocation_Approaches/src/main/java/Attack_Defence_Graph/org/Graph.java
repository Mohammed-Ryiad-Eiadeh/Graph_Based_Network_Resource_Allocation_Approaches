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
        return switch (this) {
            case SCADA -> Path.of(System.getProperty("user.dir"), "\\SCADA.txt");
            case DER -> Path.of(System.getProperty("user.dir"), "\\DER.txt");
            case e_commerce -> Path.of(System.getProperty("user.dir"), "\\e_commerce.txt");
            case VOIP -> Path.of(System.getProperty("user.dir"), "\\VOiP.txt");
            case ABSNP -> Path.of(System.getProperty("user.dir"), "\\ABSNP.txt");
            case ASFS3 -> Path.of(System.getProperty("user.dir"), "\\ASFS3.txt");
            case ASS2009 -> Path.of(System.getProperty("user.dir"), "\\ASS2009.txt");
            case HG1 -> Path.of(System.getProperty("user.dir"), "\\HG1.txt");
            case HG2 -> Path.of(System.getProperty("user.dir"), "\\HG2.txt");
            case AWS03 -> Path.of(System.getProperty("user.dir"), "\\AWS03.txt");
            case ALSFSA2 -> Path.of(System.getProperty("user.dir"), "\\ALSFSA2.txt");
            case ASS -> Path.of(System.getProperty("user.dir"), "\\ASS.txt");
            case ATF -> Path.of(System.getProperty("user.dir"), "\\ATF.txt");
            case SCADA_rand -> Path.of(System.getProperty("user.dir"), "\\SCADA_rand.txt");
            case DER_rand -> Path.of(System.getProperty("user.dir"), "\\DER_rand.txt");
            case e_commerce_rand -> Path.of(System.getProperty("user.dir"), "\\e_commerce_rand.txt");
            case VOIP_rand -> Path.of(System.getProperty("user.dir"), "\\VOiP_rand.txt");
            case ABSNP_rand -> Path.of(System.getProperty("user.dir"), "\\ABSNP_rand.txt");
            case ASFS3_rand -> Path.of(System.getProperty("user.dir"), "\\ASFS3_rand.txt");
            case ASS2009_rand -> Path.of(System.getProperty("user.dir"), "\\ASS2009_rand.txt");
            case HG1_rand -> Path.of(System.getProperty("user.dir"), "\\HG1_rand.txt");
            case HG2_rand -> Path.of(System.getProperty("user.dir"), "\\HG2_rand.txt");
            case AWS03_rand -> Path.of(System.getProperty("user.dir"), "\\AWS03_rand.txt");
            case Figure3 -> Path.of(System.getProperty("user.dir"), "\\Figure3.txt");
        };
    }
}
