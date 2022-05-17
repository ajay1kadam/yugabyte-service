package ak.db.yugabyte;

import ak.db.yugabyte.repo.CompanyDiversityInfoRepository;
import ak.db.yugabyte.repo.EmployeeRepository;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;
import com.yugabyte.data.jdbc.datasource.YugabyteTransactionManager;
import com.yugabyte.data.jdbc.repository.config.AbstractYugabyteJdbcConfiguration;
import com.yugabyte.data.jdbc.repository.config.EnableYsqlRepositories;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableYsqlRepositories(basePackageClasses = {CompanyDiversityInfoRepository.class, LeaderDiversityInfoRepository.class, EmployeeRepository.class})
public class YsqlConfig extends AbstractYugabyteJdbcConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(YsqlConfig.class);

    @Bean
    DataSource dataSource(
            @Value("${driver-class-name:com.yugabyte.ysql.YBClusterAwareDataSource}") String driverClassName,
            @Value("${yugabyte.datasource.host}") String host,
            @Value("${yugabyte.datasource.port}") String port,
            @Value("${yugabyte.datasource.db-name}") String dbName,
            @Value("${yugabyte.datasource.username}") String username,
            @Value("${yugabyte.datasource.password}") String password,

            @Value("${yugabyte.datasource.connect-timeout:10000}") int connectTimeOut,
            @Value("${yugabyte.datasource.socket-timeout:10000}") int socketTimeOut,
            @Value("${yugabyte.datasource.login-timeout:10000}") int loginTimeOut,

            @Value("${yugabyte.datasource.load-balance:false}") boolean loadBalance,
            @Value("${yugabyte.datasource.min-pool-size:1}") int minPoolSize,
            @Value("${yugabyte.datasource.max-pool-size:4}") int maxPoolSize,

            @Value("${yugabyte.datasource.ssl-mode:disable}") String sslMode,
            @Value("${yugabyte.datasource.ssl-root-cert:dummy.crt}") String sslRootCert
    ) {

        try {

            Properties poolProperties = new Properties();
            poolProperties.setProperty("dataSourceClassName", driverClassName);

            poolProperties.setProperty("maximumPoolSize", String.valueOf(maxPoolSize));
            poolProperties.setProperty("minimumIdle", String.valueOf(minPoolSize));

            poolProperties.setProperty("dataSource.serverName", host);
            poolProperties.setProperty("dataSource.portNumber", port);
            poolProperties.setProperty("dataSource.databaseName", dbName);

            poolProperties.setProperty("dataSource.user", username);
            poolProperties.setProperty("dataSource.password", password);

            poolProperties.setProperty("dataSource.connectTimeout", String.valueOf(connectTimeOut));
            poolProperties.setProperty("dataSource.socketTimeout", String.valueOf(socketTimeOut));
            poolProperties.setProperty("dataSource.loginTimeout", String.valueOf(loginTimeOut));

            poolProperties.setProperty("dataSource.loadBalanceHosts", String.valueOf(loadBalance));

            if (!sslMode.isEmpty() && !sslMode.equalsIgnoreCase("disable")) {
                poolProperties.setProperty("dataSource.ssl", String.valueOf(true));
                poolProperties.setProperty("dataSource.sslMode", sslMode);

                if (!sslRootCert.isEmpty()) {
                    poolProperties.setProperty("dataSource.sslRootCert", sslRootCert);
                }
            }

            HikariConfig config = new HikariConfig(poolProperties);
            config.validate();
            HikariDataSource hikariDataSource = new HikariDataSource(config);

            return hikariDataSource;


        } catch (Exception ex) {
            LOGGER.error("Exception creating datasource :" + ex.getMessage());
            throw ex;
        }
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean
    NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    TransactionManager transactionManager(DataSource dataSource) {
        return new YugabyteTransactionManager(dataSource);
    }

}