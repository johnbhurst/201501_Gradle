import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;

/**
 * An <code>InputStreamSource</code> based on an existing <code>InputStream</code>.
 * <p>The <code>InputStream</code>s returned by this <code>InputStreamSource</code>
 * are specially wrapped in a <code>NonClosingInputStream</code> decorator so that they never
 * close the underlying target <code>InputStream</code>.
 * This permits the <code>InputStreamSource</code> to be re-used more than once.
 * </p>
 */
public class InputStreamInputStreamSource implements InputStreamSource {
  private InputStream inputStream;

  public InputStreamInputStreamSource(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public InputStream getInputStream() throws IOException {
    return new NonClosingInputStream(inputStream);
  }
}
