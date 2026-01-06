package top.hcode.hoj.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;

import java.util.Date;

/**
 * @Author: Himit_ZH
 * @Date: 2020/12/4 14:14
 * @Description: 处理mybatis-plus自动填充时间
 */
@Component
public class MyMetaObjectConfig  implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof ExamHistory) {
            this.setFieldValByName("startTime",new Date(),metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("submitTime", new Date(), metaObject);

    }
}