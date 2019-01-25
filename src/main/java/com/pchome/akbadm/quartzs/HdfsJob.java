package com.pchome.akbadm.quartzs;

import java.util.Calendar;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.factory.store.StoreFactory;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.factory.EnumStore;

@Deprecated
public class HdfsJob {
    private StoreFactory storeFactory;

    public void process() throws Exception {
        this.process(Calendar.getInstance().getTime());
    }

    public void process(Date date) throws Exception {
        for (EnumStore enumStore: EnumStore.values()) {
            if (enumStore.isPut()) {
                this.storeFactory.getStoreObject(enumStore).put(date);
            }
        }
    }

    public void setStoreFactory(StoreFactory storeFactory) {
        this.storeFactory = storeFactory;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

        HdfsJob job = context.getBean(HdfsJob.class);
        job.process();
    }
}