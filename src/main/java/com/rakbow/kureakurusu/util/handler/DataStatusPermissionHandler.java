package com.rakbow.kureakurusu.util.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2024/4/3 18:19
 */
@Component
public class DataStatusPermissionHandler implements DataPermissionHandler {

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {

        if(StringUtils.equals(mappedStatementId, "com.rakbow.kureakurusu.dao.UserMapper.selectById")) return where;

        if (AuthorityInterceptor.isJunior()) {
            return where;
        } else {
            EqualsTo equalsTo = new EqualsTo(new Column("status"), new LongValue("1"));
            if (where == null) {
                // 如果原来没有where条件, 就添加一个where条件
                return equalsTo;
            } else {
                return new AndExpression(where, equalsTo);
            }

        }
    }

}
