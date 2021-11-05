import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class XClassLoader extends ClassLoader{
    public static void main(String[] args) throws Exception {
        Class clz = new XClassLoader().loadClass("Hello");
        Object instance = clz.newInstance();
        Method method = clz.getDeclaredMethod("hello", null);
        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass(String name) throws  ClassNotFoundException{
        InputStream input = this.getClass().getResourceAsStream("Hello.xlass");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        try{
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] bytes = decode(output.toByteArray());
            return defineClass(name,bytes,0,bytes.length);
        }catch (IOException e){
            e.fillInStackTrace();
        }finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] decode(byte[] bytes) {
        byte[] nbytes = new byte[bytes.length];
        for(int i=0;i<bytes.length;i++){
            nbytes[i] = (byte)(255-bytes[i]);
        }
        return nbytes;
    }
}
