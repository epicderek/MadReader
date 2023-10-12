package madread.cases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

import static madread.Print.*;
import static madread.utils.Utils.*;

/**
 *
 */
public abstract class LeptonPairScript
{
    private static final double mtau = 100;

    /**
     *
     * @param ebeam1 Energies of the first incoming beam in GeV.
     * @param ebeam2 Energies of the second incoming beam in GeV.
     * @param nevents Number of simulated events.
     * @param processDir Name of madgraph directory in which the process should be hereby initiated; no '/' at the end.
     * @param writeDir Directory for file to be written in.
     * @param fileName Name of the file to be written.
     */
    public static void iterateEbeams(double[] ebeam1, double[] ebeam2, int nevents, String processDir,
                                     String writeDir, String fileName) throws FileNotFoundException
    {
        if (ebeam1.length != ebeam2.length)
            throw new RuntimeException("Lengths of two beam energy arrays not equal!");
        // create folder in which momenta of different particles will be stored in separate files; automatically
        // skipped if directory exists.
        new File(writeDir).mkdirs();
        PrintWriter out = new PrintWriter(new File(String.format("%s%s", writeDir, fileName)).getAbsoluteFile());
        // Initiate process.
        out.println("generate e- e+ > a > ta- ta+");
        out.println(String.format("output %s", processDir));
        for (int i=0; i<ebeam1.length; i++)
        {
            out.println("launch");
            out.println(String.format("set ebeam1 %s", ebeam1[i]));
            out.println(String.format("set ebeam2 %s", ebeam2[i]));
            // set m_τ = 100 GeV.
            out.println(String.format("set mta %s", mtau));
            out.println(String.format("set nevents %s", nevents));
        }
        out.close();
    }

    /**
     *
     * @param path
     * @param E
     * @param fileName Should terminate as ".csv".
     * @throws FileNotFoundException
     */
    public static void writeEnergy(String path, double[] E, String fileName) throws FileNotFoundException
    {
        // create folder in which initial CM beam energy will be stored in separate files;
        // automatically skipped if directory exists.
        new File(path).mkdirs();
        PrintWriter out = new PrintWriter(new File(String.format("%s%s", path, fileName)).getAbsoluteFile());
        out.println("CM energy per beam (GeV)");
        for (double e: E)
            out.println(e);
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        // max speed
        double vmax = Math.pow(10, -3);
        double vmin = Math.pow(10, -6);
        int ndata = 20;
//        double inc = (vmax-vmin) / ndata;
        double pinc = Math.pow(vmax/vmin, 1.0/(ndata-1));
        // infer input beam energy
        double[] E = new double[ndata];
        for (int i=0; i<ndata; i++)
        {
//            E[i] = lfactor(vmin + i*inc)*mtau;
            E[i] = lfactor(vmin*Math.pow(pinc, i))*mtau;
        }
        iterateEbeams(E, E, 50000, "Repository/e-e+_a_tau-tau+_4", "./src/madread/e-e+_a_tau-tau+/trials/", "ebeamscan4.txt");
        writeEnergy("./src/madread/e-e+_a_tau-tau+/trials/", E, "beam_energy4.csv");
    }
}
