package doridlens.analyse;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 19:58
 * Description: JSON属性文件操作异常
 */
public class PropertiesException extends Exception {

    public PropertiesException(Throwable cause) {
        super("Invalid JSON apk properties syntax", cause);
    }

    public PropertiesException(String message) {
        super(message);
    }

}