import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Library {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public boolean someLibraryMethod() {
    logger.info("someLibraryMethod() called.");
    return true;
  }
}
