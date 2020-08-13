package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.domain.splitter.Record;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.camel.Handler;

import java.util.List;

public class PartitionBean {
    @Handler
    @SneakyThrows
    public List<List<Record>> records(List<Record> records) {
        return Lists.partition(records, 3);
    }
}
