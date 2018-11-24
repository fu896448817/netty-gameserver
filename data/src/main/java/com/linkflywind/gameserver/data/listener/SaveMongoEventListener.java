package com.linkflywind.gameserver.data.listener;


import java.lang.reflect.Field;

import com.linkflywind.gameserver.data.annotation.AutoIncrement;
import com.linkflywind.gameserver.data.monoModel.SequenceId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;



@Component
public class SaveMongoEventListener extends AbstractMongoEventListener<Object> {
    // private final static Logger LOGGER =
    // LoggerFactory.getLogger(SaveMongoEventListener.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 在新增记录的时候，回调接口方法，监听文档插入记录的操作，使用反射方法对ID进行自增
     *
     * @param event
     * @since 1.8
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        final Object source = event.getSource(); // 获取事件最初发生的对象
        if (source != null) {
            // 使用反射工具类，实现回调接口的方法，对成员进行操作
            ReflectionUtils.doWithFields(source.getClass(), new FieldCallback() {

                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field); // 使操作的成员可访问

                    // 如果是带有@AutoIncrement注解的，成员调用getter方法返回的类型是Number类型的，返回的类型都是0的(没有赋值，默认为0)
                    if (field.isAnnotationPresent(AutoIncrement.class) && field.get(source) instanceof Number
                            && field.getLong(source) == 0) {
                        String collName = source.getClass().getSimpleName().substring(0, 1).toLowerCase()
                                + source.getClass().getSimpleName().substring(1);

                        // setter方法的调用，使ID成员属性，进行自增
                        field.set(source, getNextId(collName));
                    }
                }
            });
        }
    }

    /**
     * 返回下一个自增的ID
     *
     * @param collName
     *            集合名称（一般规则是，类名的第一个字母小写，然后按照驼峰书写法）
     * @return Long 序列值
     */
    private Long getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SequenceId seq = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        return seq.getSeqId();
    }
}