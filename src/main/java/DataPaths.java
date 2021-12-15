/**
 * Class responsible for mananging the data paths
 */
public class DataPaths 
{
    private static final String DATASET = "dataset.csv";
    private static final String URL = "https://data.brasil.io/dataset/covid19/caso.csv.gz";
    public static final String NULL = "";
    public static final String GZIP = ".gz";
    
    /**
     * @return download URL 
     */
    public static String getURL() 
    {
        return URL;
    }  
    
    /**
     * @param prefix prefix to the "dataset.csv" path
     * @param suffix sufix to the "dataset.csv" path
     * @return a custom data path
     */
    public static String getCustomPath(String prefix, String suffix)
    {
        return prefix + DATASET + suffix;
    } 
}
