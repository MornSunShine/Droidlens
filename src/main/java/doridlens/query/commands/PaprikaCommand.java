package doridlens.query.commands;

import java.io.IOException;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:21
 * Description: Paprika的Query模式下使用的Command接口
 */
public interface PaprikaCommand {

    /**
     * 运行指令
     *
     * @param details 是否添加详情到结果
     * @throws IOException 访问数据库失败,则抛出
     */
    void run(boolean details) throws IOException;

}
