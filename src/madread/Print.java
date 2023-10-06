package madread;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Print
{
    /**
     * The number of dashes printed when <code>dermatj.utils.disp.Print.printDash()<code/> is invoked.
     */
    public static int dashNum = 50;

    /**
     * Print the variable argument list on the current line--with nested containers, if present, printed in their
     * extrapolated formats: arrays as <code>[]...</code>, collections as <code>&lt;...&gt;</code>, maps as <code>
     * {key=val}</code>--with the items separated by the provided delimiter.
     * <br/><br/>
     * Note that a primitive array is treated as a single object and not as an object array, while all object arrays
     * can be recognized as an object array.<br/>
     * Note also that when an object array is to be printed by itself, the <code>dermatj.utils.disp.Print.print</code>
     * methods will omit the brackets for that array as the argument list cannot distinguish an object array, so for
     * printing an object array, use <code>dermatj.utils.disp.Print.printArray</code>.
     *
     * @param pstrm The <code>PrintStream</code> where the objects are to be printed to.
     * @param delim The delimiter by which the items of the list of objects are separated in their printing.
     * @param args The list of objects to be printed.
     */
    @SafeVarargs
    public static void print(PrintStream pstrm, String delim, Object... args)
    {
        for(int i = 0; i < args.length; i++)
        {
            Object item = args[i];
            //If a null pointer is detected.
            if(item == null)
                pstrm.print("null");
            //If an array is present.
            else if(item.getClass().getComponentType() != null)
            {
                //If a 1D primitive array is encountered.
                if(printPrimitiveArray(pstrm, delim, item))
                    ;
                else
                    printArray(pstrm, delim, (Object[])item);
                pstrm.print(delim);
            }
            //If a collection is present.
            else if(item instanceof Collection)
                printCollection(pstrm, delim, (Collection<?>)item);
            //If a maps is present.
            else if(item instanceof Map)
                printMap(pstrm, delim, (Map<?,?>)item);
            else
                pstrm.print(item);
            if(i != args.length - 1)
                pstrm.print(delim);
        }
    }

    /**
     * Print the list of objects provided separated by a single space-followed comma to <code>System.out</code> in the
     * format specified in <code>dermatj.utils.disp.Print.print(PrintStream, String, Object...)</code>.
     * @param args The list of objects to be printed.
     */
    public static void print(Object... args)
    {
        print(System.out,", ", args);
    }


    /**
     * Print the variable argument list on the current line--with nested containers, if present, printed in their
     * extrapolated formats: arrays as <code>[...]</code>, collections as <code>&lt;...&gt;</code>, maps as <code>
     * {key=val}</code>--with the items separated by the provided delimiter and initiate a new line hereafter.
     * <br/><br/>
     * Note that a primitive array is treated as a single object and not as an object array, while all object arrays
     * can be recognized as an object array.
     *
     * @param pstrm The <code>PrintStream</code> where the objects are to be printed to.
     * @param delim The delimiter by which the items of the list of objects are separated in their printing.
     * @param args The list of objects to be printed.
     */
    public static void println(PrintStream pstrm, String delim, Object... args)
    {
        print(pstrm, delim, args);
        pstrm.println();
    }

    /**
     * Print the list of objects provided separated by a single space-followed comma to <code>System.out</code> in the
     * format specified in <code>dermatj.utils.disp.Print.print(PrintStream, String, Object...)</code> and initiate a
     * new line hereafter.
     * @param args The list of objects to be printed.
     */
    public static void println(Object... args)
    {
        println(System.out,", ", args);
    }

    /**
     * Print the list of items in a line connected by single spaces to the given stream.
     * @param pstream The print stream to which the items are printed to.
     * @param args Items to be printed in a line and inter-connected by single spaces.
     */
    public static void printSpacedln(PrintStream pstream, Object... args)
    {
        println(pstream, " ", args);
    }

    /**
     * Print the list of items in a line connected by single spaces to <code>System.out</code>.
     * @param args Items to be printed in a line and inter-connected by single spaces.
     */
    public static void printSpacedln(Object... args)
    {
        printSpacedln(System.out, args);
    }

    /**
     * Print this single array in its proper format. This method is invoked when it is certain that the subject to be
     * printed is an object array, as when <code>dermatj.utils.disp.Print.print</code> is invoked, the argument list
     * accepted by convenience will, instead of recognizing this single array as one entity, treat it as a list of
     * collected objects to be printed individually--whose net effect is the omission of the square brackets. Primitive
     * arrays are recognized as singular objects and so do not need to be printed specially with this method.
     * @param args The elements of the object array to be printed.
     * @param pstrm The optional <code>PrintStream</code> (contained in the first element) of the list that the printing
     *              goes to.
     */
    public static void printArray(PrintStream pstrm, String delim, Object[] args)
    {
        pstrm.print("[");
        if(args.length == 0)
            pstrm.print("]");
        for(int i = 0; i < args.length; i++)
        {
            Object arg = args[i];
            //If a null pointer is detected.
            if(arg == null)
                pstrm.print("null");
            //An array of any specified class is an heir to the class Object[].
            else if(arg instanceof Object[])
                printArray(pstrm, delim, (Object[])arg);
            else if(printPrimitiveArray(pstrm, delim, arg))
                ;
            else if(arg instanceof Collection)
                printCollection(pstrm, delim, (Collection<?>)arg);
            else if(arg instanceof Map)
                printMap(pstrm, delim, (Map<?,?>)arg);
            else
                pstrm.print(arg);
            if(i == args.length - 1)
                pstrm.print("]");
            else
                pstrm.print(delim);
        }
    }

    /**
     * Print this single array in its proper format to <code>System.out<code/>, its elements separated by ", ". This
     * method is invoked when it is certain that the subject to be printed is an object array, as when <code>dermatj.
     * utils.disp.Print.print</code> is invoked, the argument list accepted by convenience will, instead of recognizing
     * this single array as one entity, treat it as a list of collected objects to be printed individually--whose net
     * effect is the omission of the square brackets. Primitive arrays are recognized as singular objects and so do not
     * need to be printed specially with this method.
     * @param args The elements of the object array to be printed.
     */
    public static void printArray(Object[] args)
    {
        printArray(System.out, ", ", args);
    }

    /**
     * Print the suspect primitive array if so, that is to first check whether the object is a
     * one dimensional primitive array, then print such array if so. The result of printing, whether
     * printed, is shown by the returned boolean value--true for printed, false for not.
     *
     * @param obj The suspected primitive array.
     * @return true if the object is indeed a primitive array and is printed, false otherwise.
     */
    private static boolean printPrimitiveArray(PrintStream pstrm, String delim, Object obj)
    {
        if(obj instanceof boolean[])
        {
            pstrm.print("[");
            boolean[] casted = (boolean[])obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof byte[])
        {
            pstrm.print("[");
            byte[] casted = (byte[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof char[])
        {
            pstrm.print("[");
            char[] casted = (char[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof short[])
        {
            pstrm.print("[");
            short[] casted = (short[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof int[])
        {
            pstrm.print("[");
            int[] casted = (int[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof long[])
        {
            pstrm.print("[");
            long[] casted = (long[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof float[])
        {
            pstrm.print("[");
            float[] casted = (float[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        if(obj instanceof double[])
        {
            pstrm.print("[");
            double[] casted = (double[]) obj;
            for(int i = 0; i < casted.length; i++)
                if(i != casted.length - 1)
                    pstrm.print(casted[i] + delim);
                else
                    pstrm.print(casted[i]);
            pstrm.print("]");
            return true;
        }
        return false;
    }

    /**
     * Print potentially nested Collection with Collections, Maps, and Arrays in the standard format
     * as for arrays.
     * @param col The collection to be printed.
     */
    private static <T> void printCollection(PrintStream pstrm, String delim, Collection<T> col)
    {
        Iterator<T> ite = col.iterator();
        pstrm.print("<");
        for(int i = 0; i < col.size(); i++)
        {
            //Get the next item.
            T item = ite.next();
            //If a null pointer.
            if(item == null)
                pstrm.print("null");
            //If an array is present.
            else if(item.getClass().getComponentType() != null)
                //If a 1D primitive array.
                if(printPrimitiveArray(pstrm, delim, item))
                    ;
                else
                    printArray(pstrm, delim, (Object[])item);
                //If a collection is nested.
            else if(item instanceof Collection)
                printCollection(pstrm, delim, (Collection<?>)item);
                //If a maps is present.
            else if(item instanceof Map)
                printMap(pstrm, delim, (Map<?,?>)item);
            else
                pstrm.print(item);
            if(i == col.size() - 1)
                pstrm.print(">");
            else
                pstrm.print(delim);
        }
    }

    /**
     * Print the Map potentially nested with Maps, Collections, and Arrays in its entries in the standard
     * format <code>{key=val...}</code>as for a Map.
     * @param map The maps to be printed.
     */
    private static <K,V> void printMap(PrintStream pstrm, String delim, Map<K,V> map)
    {
        pstrm.print("{");
        Iterator<Map.Entry<K,V>> ite = map.entrySet().iterator();
        for(int i = 0; i < map.size(); i++)
        {
            //Get the next entry.
            Map.Entry<K,V> ent = ite.next();
            //If a null pointer is detected for the key.
            if(ent.getKey() == null)
                pstrm.print("null");
            //If a maps is nested as the key of the entry.
            else if(ent.getKey() instanceof Map)
                printMap(pstrm, delim, (Map<?,?>)ent.getKey());
            //If an array is present.
            else if(ent.getKey().getClass().getComponentType() != null)
                //If a 1D primitive array.
                if(printPrimitiveArray(pstrm, delim, ent.getKey()))
                    ;
                else
                    printArray(pstrm, delim, (Object[])ent.getKey());
                //If a collection is present.
            else if(ent.getKey() instanceof Collection)
                printCollection(pstrm, delim, (Collection<?>)ent.getKey());
            else
                pstrm.print(ent.getKey());
            pstrm.print("=");
            //If a null pointer is detected for the value of the entry.
            if(ent.getValue() == null)
                pstrm.print("null");
            //If a maps is nested as the value of the entry.
            else if(ent.getValue() instanceof Map)
                printMap(pstrm, delim, (Map<?,?>)ent.getValue());
                //If an array is present.
            else if(ent.getValue().getClass().getComponentType() != null)
                printArray(pstrm, delim, (Object[])ent.getValue());
                //If a collection is present.
            else if(ent.getValue() instanceof Collection)
                printCollection(pstrm, delim, (Collection<?>)ent.getValue());
            else
                pstrm.print(ent.getValue());
            if(i == map.size() - 1)
                pstrm.print("}");
            else
                pstrm.print(delim);
        }
    }

    /**
     * Print the object a specified number of times to the <code>PrintStream</code> given, which defaults to <code>
     * System.out </code> if not provided.
     * @param dup The object to be duplicated.
     * @param num The number of times the object is to be duplicatedly printed.
     * @param delim The delimiter to separate each printing of the object.
     * @param pstrm The optional <code>PrintStream</code> (contained in the first element) of the list that the printing
     *              goes to.
     */
    public static void printDup(Object dup, int num, String delim, PrintStream... pstrm)
    {
        PrintStream ps = pstrm.length == 0? System.out: pstrm[0];
        for(int i = 0; i < num; i++)
        {
            print(dup);
            ps.print(i == num - 1? "": delim);
        }
    }

    /**
     * Print the object a specified number of times to the <code>PrintStream</code> given, which defaults to <code>
     * System.out </code> if not provided and initiate a new line.
     * @param dup The object to be duplicated.
     * @param num The number of times the object is to be duplicatedly printed.
     * @param delim The delimiter to separate each printing of the object.
     * @param pstrm The optional <code>PrintStream</code> (contained in the first element) of the list that the printing
     *              goes to.
     */
    public static void printDupln(Object dup, int num, String delim, PrintStream... pstrm)
    {
        PrintStream ps = pstrm.length == 0? System.out: pstrm[0];
        printDup(dup, num, delim, pstrm);
        ps.println();
    }

    /**
     * Print a dashed line of the number of segments given by the customary <code>dermatj.utils.disp.Print.dashNum</code>
     * field to the given <code>PrintStream</code>, defaulted to <code>System.out</code> if not provided, and initiate
     * a new line.
     *
     * @param pstrm The optional <code>PrintStream</code> (contained in the first element) of the list that the printing
     *              goes to.
     */
    public static void printDashln(PrintStream... pstrm)
    {
        if(pstrm.length != 0)
            printDashln(pstrm[0], dashNum);
        else
            printDashln(System.out, dashNum);
    }

    /**
     * Print a dashed line of the number of segments given by the number in the argument.
     * @param num The number of dashed segments to be printed in this line.
     */
    private static void printDash(PrintStream pstrm, int num)
    {
        for(int i = 0; i < num; i++)
            pstrm.print("-");
    }

    /**
     * Print a dashed line of the number of segments given by the number in the argument and initiate a new line.
     * @param num The number of dashed segments to be printed in this line.
     */
    public static void printDashln(PrintStream pstrm, int num)
    {
        printDash(pstrm, num);
        pstrm.println();
    }

    /**
     * Print a dashed line embedding a title string as provided to the <code>PrintStream</code> if provided, defaulted
     * to <code>System.out</code> if not provided: ----------Title----------.
     * @param title The title string to be embedded in a dashed line.
     * @param pstrm The optional <code>PrintStream</code> (contained in the first element) of the list that the printing
     *              goes to.
     */
    public static void printTitleln(Object title, PrintStream... pstrm)
    {
        PrintStream ps = pstrm.length == 0? System.out: pstrm[0];
        printDash(ps,dashNum/2);
        print(ps, "", title);
        printDash(ps, dashNum/2);
        ps.println();
    }

    public static void main(String[] args)
    {

    }
}
