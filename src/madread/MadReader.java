package madread;

import static madread.Print.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.*;

/**
 * As the reader processes large files, the strings involved will not be passed around as variables but stored within
 * the corresponding MadReader objects.
 */
public class MadReader
{
    private final int[] reagents;

    private final int[] products;

    /**
     * Total number of reagents and products.
     */
    public final int multiplicity;

    private final String eventsText;

    private String eventBlock;

    private LinkedList<String> eventEntries;

    // Extracted physical quantities of interest below.

    /**
     * 4-momenta of incoming and outgoing particles, the first component being energy (not the 4th, as recorded in MadGraph file).
     */
    private String[][][] momenta;

    /**
     * Masses.
     */
    private String[] masses;

    /**
     * Construct a reader for a MadGraph generated unweighted_events.ihe file that parses it to extract relevant data
     * for export.
     * @param reagents Number code for incoming particles.
     * @param products Number code for outgoing particles.
     * @param iheFilePath Path (in string) to unweighted_events.ihe as generated by MadGraph.
     * @throws IOException
     */
    public MadReader(int[] reagents, int[] products, String iheFilePath) throws IOException
    {
        this.reagents = Arrays.copyOf(reagents, reagents.length);
        this.products = Arrays.copyOf(products, products.length);
        multiplicity = reagents.length + products.length;

        // Read unweighted events file in as a single string. The following command SHOULD do.
        eventsText = new String(Files.readAllBytes(Paths.get(iheFilePath)), StandardCharsets.UTF_8);
    }

    /**
     * Main method to execute parsing of the unweighted_events.ihe file to extract data. Construction does not
     * automatically initiate parsing.
     * @param path If provided, a .csv file recording data will be stored at the given directory.
     */
    public void parse(String... path) throws FileNotFoundException {
        // Parse into entries of events.
        extractEventBlock();
        extractEvents();
        // Extract selected kinematics from events.
        extractMomenta(path);
    }

    public void extractEventBlock()
    {
        // Create regex objects.
        String rex = "</init>\n(.*)\n</LesHouchesEvents>";
        Pattern form = Pattern.compile(rex, Pattern.DOTALL);
        Matcher mat = form.matcher(eventsText);

        // Extract event block.
        mat.find();
        this.eventBlock = mat.group(1);
    }

    public void extractEvents()
    {
        if (eventBlock == null)
            throw new RuntimeException("Event block has not been extracted! Call extractEventBlock() first.");
        println("Parsing event block into events.");
        String rex = "<event>([^<>]+).*?</event>";
        Pattern form = Pattern.compile(rex, Pattern.DOTALL);
        Matcher mat = form.matcher(eventBlock);

        LinkedList<String> events = new LinkedList<>();
        while(mat.find())
        {
            events.add(mat.group(1).trim());
        }
        this.eventEntries = events;
    }

    /**
     * Extract the momenta data from the event entries. Optionally write the extracted momenta data into a .csv named
     * sim_momenta.csv.
     * @param path If provided, a .csv file will be generated recording the extracted momenta at the given directory.
     *             e.g. ./src/madread/pp_a_tau-tau+/run_01/sim_momenta/
     */
    public void extractMomenta(String... path) throws FileNotFoundException {
        if (eventEntries == null)
            throw new RuntimeException("Event entries have not been isolated! Run extractEvents()!");
        println("Extracting momenta data from event entries.");

        momenta = new String[multiplicity][eventEntries.size()][4];
        String rex = "\\s*(?<pdg>-?\\d+)\\s+(?<inout>-?\\d+)\\s+(?<parent1>\\d+)\\s+(?<parent2>\\d+)\\s+(?<color1>\\d+)\\s+(?<color2>\\d+)" +
                "\\s+(?<px>[-+]\\S+)\\s+(?<py>[-+]\\S+)\\s+(?<pz>[-+]\\S+)\\s+(?<E>\\S+)\\s+(?<m>\\S+)\\s+(?<dist>\\S+)\\s+(?<helicity>\\S+)\\s*";
        Pattern form = Pattern.compile(rex);

        // counter for counting events.
        int e = 0;
        // extract the particle kinematics, as given after the first line.
        for (String eve: eventEntries)
        {
            String[] lines = eve.split("\n");
            for (int i=1; i<=multiplicity; i++)
            {
                Matcher mat = form.matcher(lines[i]);
                if(!mat.find())
                    throw new RuntimeException("Invalid kinematical entry of particle " + i);
                else
                {
                    momenta[i-1][e][0] = mat.group("E");
                    momenta[i-1][e][1] = mat.group("px");
                    momenta[i-1][e][2] = mat.group("py");
                    momenta[i-1][e][3] = mat.group("pz");
                }
            }
            // increment event counter.
            e++;
        }
        // if requested for output
        if (path.length != 0)
        {
            // create folder in which momenta of different particles will be stored in separate files; automatically
            // skipped if directory exists.
            new File(path[0]).mkdirs();
            for (int i=0; i<multiplicity; i++)
            {
                PrintWriter out = new PrintWriter(new File(String.format("%ssim_momenta_%d.csv", path[0], i+1)).getAbsoluteFile());
                out.println("E (GeV), px (GeV), py (GeV), pz (GeV)");
                for (int j=0; j<eventEntries.size(); j++)
                    out.println(String.join(",", momenta[i][j]));
                out.close();
            }
        }
    }

    public int[] getReagents()
    {
        return Arrays.copyOf(reagents, reagents.length);
    }

    public int[] getProducts()
    {
        return Arrays.copyOf(products, products.length);
    }

    /**
     *
     * @param csdir
     * @param storedir
     * @param fileName Should terminate in ".csv".
     * @throws IOException
     */
    public static void extractCrossSections(String csdir, String storedir, String fileName) throws IOException {
        Pattern form = Pattern.compile("results.html\">\\s*(?<cs>[-.e\\d]+)\\s*<font.*?</font>\\s*(?<dcs>[-.e\\d]+)\\s*</a>", Pattern.DOTALL);
        // read unweighted events file in as a single string. The following command SHOULD do.
        Matcher mat = form.matcher(new String(Files.readAllBytes(Paths.get(csdir)), StandardCharsets.UTF_8));
        // extract from .html file.
        LinkedList<String> cs = new LinkedList<>();
        LinkedList<String> dcs = new LinkedList<>();
        while (mat.find()) {
            cs.add(mat.group("cs"));
            dcs.add(mat.group("dcs"));
        }
        // store at specified location
        // create folder in which cross sections will be stored in separate files; automatically skipped if directory exists.
        new File(storedir).mkdirs();
        PrintWriter out = new PrintWriter(new File(String.format("%s%s", storedir, fileName)).getAbsoluteFile());
        out.println("cross section (pb), uncertainty (pb)");
        // combine entries; need an iterator.
        Iterator<String> ite = dcs.iterator();
        for (String c : cs)
        {
            out.println(String.format("%s,%s", c, ite.next()));
        }
        out.close();
    }

    public static void main(String[] args) throws IOException
    {
//        String iheFilepath = "C:/Users/derek/physics/hep-sim/MG5_aMC_v3_5_1/Repository/pp_a_tau-tau+/Events/run_06/unweighted_events.lhe";
//        MadReader reader = new MadReader(new int[]{11, -11}, new int[]{13, -13}, iheFilepath);
//        // storage path
//        String storePath = "./src/madread/pp_a_tau-tau+/run_06/sim_momenta/";
//        reader.parse(storePath);
        // println(reader.eventEntries.size());

        String csPath = "C:/Users/derek/physics/hep-sim/MG5_aMC_v3_5_1/Repository/e-e+_a_tau-tau+_4/crossx.html";
        extractCrossSections(csPath, "./src/madread/e-e+_a_tau-tau+/trials/", "cross_section4.csv");
    }
}
