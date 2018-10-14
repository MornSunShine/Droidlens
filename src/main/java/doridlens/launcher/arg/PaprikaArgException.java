package doridlens.launcher.arg;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:17
 * Description: Paprika参数异常类
 *              当传递给Paprika的CLI中包含的参数K-V不匹配的时候，抛出此异常
 */
public class PaprikaArgException extends Exception {

    public PaprikaArgException(String message) {
        super("Invalid args were passed to Paprika:\n" + message);
    }

}
