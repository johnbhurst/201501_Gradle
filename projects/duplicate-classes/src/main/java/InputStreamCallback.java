import java.io.IOException;
import java.io.InputStream;

/**
 * Callback interface for processing input from an <code>InputStream</code>.
 *
 * @see InputStreamTemplate
 */
public interface InputStreamCallback {
  Object doInInputStream(InputStream inputStream) throws IOException;
}
