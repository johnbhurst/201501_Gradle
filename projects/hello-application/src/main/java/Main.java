import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Main {

  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    if (args.length == 0) {
      logger.info("Hello, World.");
    }
    else {
      logger.info("Hello, [" + args[0] + "].");
    }
  }
}
