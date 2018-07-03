package jaram.groupware.groupware.model.value;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by NamHyunsil on 2018. 7. 3..
 */
public class AmountFactory implements FactoryBean {
    public AmountFactory() {
    }
    public Object getObject() throws Exception {
        return new Amount();
    }

    public Class getObjectType() {
        return Amount.class;
    }

    public boolean isSingleton() {
        return false;
    }

}

