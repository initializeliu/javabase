package test02;

/**
 * 1. String是不可变的，如果尝试去修改，会新⽣成⼀个字符串对象，StringBuffer和StringBuilder是可变的
 * 2. StringBuffer是线程安全的，StringBuilder是线程不安全的，所以在单线程环境下StringBuilder效率会更⾼
 * 3. StringBuffer和StringBuilder共同继承AbstractStringBuilder
 *     //StringBuffer
 *     public synchronized StringBuffer append(int i) {
 *         toStringCache = null;
 *         super.append(i);
 *         return this;
 *     }
 *     //StringBuilder
 *     public StringBuilder append(int i) {
 *         super.append(i);
 *         return this;
 *     }
 * 4. 调用StringBuffer和StringBuilder本质都是调用AbstractStringBuilder的方法。
 *
 */
public class StringBufferAndStringBuilder {
    public static void main(String[] args) {

        /**
         *  public final class StringBuffer extends AbstractStringBuilder implements java.io.Serializable, CharSequence
         *
         */
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(99);


        /**
         * public final class StringBuilder extends AbstractStringBuilder implements java.io.Serializable, CharSequence
         *
         */
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(99);

    }
}
