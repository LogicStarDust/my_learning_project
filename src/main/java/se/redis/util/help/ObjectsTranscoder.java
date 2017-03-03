package se.redis.util.help;

import java.io.*;


/**
 * 对象序列化反序列化
 * @author 刘宪领
 * @date 2016-9-12
 * @Time 上午9:17:59
 */
public class ObjectsTranscoder<M extends Serializable> extends SerializeTranscoder {

	
  @SuppressWarnings("unchecked")
  @Override
  public byte[] serialize(Object value) {
    if (value == null) {  
      throw new NullPointerException("Can't serialize null");  
    }  
    byte[] result = null;  
    ByteArrayOutputStream bos = null;  
    ObjectOutputStream os = null;  
    try {  
      bos = new ByteArrayOutputStream();  
      os = new ObjectOutputStream(bos);
      M m = (M) value;
      os.writeObject(m);  
      os.close();  
      bos.close();  
      result = bos.toByteArray();  
    } catch (IOException e) {  
      throw new IllegalArgumentException("Non-serializable object", e);  
    } finally {  
      close(os);  
      close(bos);  
    }  
    return result;  
  }

  @SuppressWarnings("unchecked")
  @Override
  public M deserialize(byte[] in) {
    M result = null;  
    ByteArrayInputStream bis = null;  
    ObjectInputStream is = null;  
    try {  
      if (in != null) {  
        bis = new ByteArrayInputStream(in);  
        is = new ObjectInputStream(bis);  
        result = (M) is.readObject();  
        is.close();  
        bis.close();  
      }  
    } catch (IOException e) {  
    	 logger.error("Caught IOException decoding %d bytes of data",e);
    } catch (ClassNotFoundException e) {  
    	logger.error("aught CNFE decoding %d bytes of data",e);
    } finally {  
      close(is);  
      close(bis);  
    }  
    return result;  
  }
}