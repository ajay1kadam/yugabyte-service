package ak.db.yugabyte;

import ak.db.yugabyte.repo.DeptRepository;
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

@Configuration
@EnableYsqlRepositories(basePackages = "ak.db.yugabyte.repo")
public class YsqlConfig extends AbstractYugabyteJdbcConfiguration {

	private static Logger LOGGER = LoggerFactory.getLogger(YsqlConfig.class);


	@Bean
	DataSource dataSource(
			@Value("${driver-class-name:com.yugabyte.Driver}") String driverClassName,
			@Value("${yugabyte.datasource.host}") String host,
			@Value("${yugabyte.datasource.port}") String port,
			@Value("${yugabyte.datasource.db-name}") String dbName,
			@Value("${yugabyte.datasource.username}") String username,
			@Value("${yugabyte.datasource.password}") String password,
			@Value("${yugabyte.datasource.ssl-mode}") String sslMode,
			@Value("${yugabyte.datasource.ssl-root-cert}") String sslRootCert,
			@Value("${yugabyte.datasource.connect-timeout:10000}") int connectTimeOut,
			@Value("${yugabyte.datasource.socket-timeout:20000}") int socketTimeOut,
			@Value("${yugabyte.datasource.login-timeout:40000}") int loginTimeOut,
			@Value("${yugabyte.datasource.load-balance:false}") boolean loadBalance,
			@Value("${yugabyte.datasource.min-pool-size:1}") int minPoolSize,
			@Value("${yugabyte.datasource.max-pool-size:4}") int maxPoolSize) {

		try {
			HikariConfig hikariConfig = new HikariConfig();
			if (driverClassName != null) {
				hikariConfig.setDriverClassName(driverClassName);
			}
			hikariConfig.setJdbcUrl("jdbc:yugabytedb://" + host + ":" + port + "/" + dbName);
			hikariConfig.setUsername(username);
			hikariConfig.setPassword(password);
			hikariConfig.setMinimumIdle(minPoolSize);
			hikariConfig.setMaximumPoolSize(maxPoolSize);

			//hikariConfig.setValidationTimeout(30 * 1000);
			hikariConfig.setConnectionTimeout(connectTimeOut);
			hikariConfig.setInitializationFailTimeout(30 * 1000);
			hikariConfig.addDataSourceProperty("load-balance", loadBalance);

			return new HikariDataSource(hikariConfig);
		} catch (Exception ex) {
			//System.out.println("Error creating datasource :" + ex.getMessage());
			LOGGER.error("Exception creating datasource :" + ex.getMessage());
			throw ex;
		}
	}



/*
	@Bean
	DataSource getDataSource(
			@Value("${yugabyte.datasource.host}") String host,
			@Value("${yugabyte.datasource.port}") String port,
			@Value("${yugabyte.datasource.db-name}") String dbName,
			@Value("${yugabyte.datasource.username}") String username,
			@Value("${yugabyte.datasource.password}") String password,
			@Value("${yugabyte.datasource.ssl-mode}") String sslMode,
			@Value("${yugabyte.datasource.ssl-root-cert}") String sslRootCert,
			@Value("${yugabyte.datasource.connect-timeout:10000}") int connectTimeOut,
			@Value("${yugabyte.datasource.socket-timeout:20000}") int socketTimeOut,
			@Value("${yugabyte.datasource.login-timeout:40000}") int loginTimeOut,
			@Value("${yugabyte.datasource.load-balance:false}") boolean loadBalance) {

		try {
			YBClusterAwareDataSource ds = new YBClusterAwareDataSource();

			ds.setUrl("jdbc:yugabytedb://" + host + ":" + port + "/" + dbName);
			ds.setUser(username);
			ds.setPassword(password);
			ds.setConnectTimeout(connectTimeOut);
			ds.setSocketTimeout(socketTimeOut);
			ds.setLoginTimeout(loginTimeOut);
			ds.setTcpKeepAlive(true);


			ds.setLoadBalanceHosts(loadBalance);

			if (!sslMode.isEmpty() && !sslMode.equalsIgnoreCase("disable")) {
				ds.setSsl(true);
				ds.setSslMode(sslMode);

				if (!sslRootCert.isEmpty())
					ds.setSslRootCert(sslRootCert);
			}
			try {
				Connection conn = ds.getConnection();
				System.out.println(">>>> Successfully connected to YugabyteDB!");
				conn.close();
			} catch (Exception ex) {
				LOGGER.error("Exception testing datasource :" + ex.getMessage());
			}
			return ds;
		} catch (Exception ex) {
			LOGGER.error("Exception creating datasource :" + ex.getMessage());
			throw ex;
		}
	}
*/

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