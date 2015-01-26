import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Callback interface for processing an entry from a ZIP stream.
 *
 * @see ZipInputStreamTemplate
 */
public interface ZipInputStreamCallback {
  Object doWithZipEntry(InputStream is, ZipEntry entry) throws Exception;
}
