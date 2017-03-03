package se.redis.util.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * 序列化抽象类
 * @author 刘宪领
 * @date 2016-9-12
 * @Time 上午9:18:17
 */
public abstract class SerializeTranscoder {

  protected static Logger logger = LoggerFactory.getLogger(SerializeTranscoder.class);
  
  public abstract byte[] serialize(Object value);
  
  public abstract Object deserialize(byte[] in);
  
  public void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception e) {
         logger.info("Unable to close " + closeable, e); 
      }
    }
  }
}