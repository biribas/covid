import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Class responsible for initiating the data collect
 */
public class DataCollector 
{
    /**
     * Collects the data
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void dataCollector() throws IOException, URISyntaxException 
    {
        downloadData();
        
        Path source = Paths.get(DataPaths.getCustomPath(DataPaths.NULL, DataPaths.GZIP));
        Path target = Paths.get(DataPaths.getCustomPath(DataPaths.NULL, DataPaths.NULL));

        ungz(source, target);
        Files.delete(source);

        DataProcessor.formatter(target);
    }

    /**
     * Downloads the data from https://data.brasil.io/dataset/covid19/caso.csv.gz 
     * @throws IOException
     */
    public static void downloadData() throws IOException
    {
        URL url = new URL(DataPaths.getURL());
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(DataPaths.getCustomPath(DataPaths.NULL, DataPaths.GZIP));
        
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    /**
     * Unzips the downloaded data 
     * @param source zipped file path
     * @param target unzipped file path
     * @throws IOException
     */
    public static void ungz(Path source, Path target) throws IOException 
    { 
        GZIPInputStream gis = new GZIPInputStream(new FileInputStream(source.toFile()));
        if (Files.exists(target))
        {
            Files.delete(target);
        }
        Files.copy(gis, target);
        gis.close();
    }
}
