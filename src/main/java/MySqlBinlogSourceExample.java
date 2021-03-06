import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import com.alibaba.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;

public class MySqlBinlogSourceExample {
    public static void main(String[] args) throws Exception {
        SourceFunction<String> sourceFunction = MySQLSource.<String>builder()
                .hostname("bt-01")
                .port(3306)
                .databaseList("test") // monitor all tables under inventory database
                .username("root")
                .password("123456")
                .deserializer(new StringDebeziumDeserializationSchema()) // converts SourceRecord to String
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        env.addSource(sourceFunction)
                .print()
                .setParallelism(1); // use parallelism 1 for sink to keep message ordering
        env.execute();
    }
}