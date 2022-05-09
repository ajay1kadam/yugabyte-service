package ak.db.yugabyte;

import ak.db.yugabyte.repo.EmployeeRepository;
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
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableYsqlRepositories(basePackageClasses = EmployeeRepository.class)
public class YsqlConfig extends AbstractYugabyteJdbcConfiguration {

	private static Logger LOGGER = LoggerFactory.getLogger(YsqlConfig.class);

/*

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
			String jdbcUrl = "jdbc:yugabytedb://" + host + ":" + port + "/" + dbName;
			jdbcUrl += "?ssl=true&sslmode=verify-full&sslrootcert=C:/devl/wfb/hackathon/petclinic-spring-data-yugabytedb/.postgresql/root.crt";

			hikariConfig.setJdbcUrl(jdbcUrl);
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

*/

	@Bean
	DataSource dataSource(
			@Value("${driver-class-name:com.yugabyte.ysql.YBClusterAwareDataSource}") String driverClassName,
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
			@Value("${yugabyte.datasource.max-pool-size:4}") int maxPoolSize) throws SQLException {

		try {

			Properties poolProperties = new Properties();
			poolProperties.setProperty("dataSourceClassName", "com.yugabyte.ysql.YBClusterAwareDataSource");

			poolProperties.setProperty("maximumPoolSize", String.valueOf(maxPoolSize));

			poolProperties.setProperty("dataSource.serverName", host);
			poolProperties.setProperty("dataSource.portNumber", port);
			poolProperties.setProperty("dataSource.databaseName", dbName);

			poolProperties.setProperty("dataSource.user", username);
			poolProperties.setProperty("dataSource.password", password);


			HikariConfig config = new HikariConfig(poolProperties);
			config.validate();
			HikariDataSource hikariDataSource = new HikariDataSource(config);

			return hikariDataSource;

	/*		YBClusterAwareDataSource ds = new YBClusterAwareDataSource();

			String jdbcUrl = "jdbc:yugabytedb://" + host + ":" + port  + "/" + dbName;
			ds.setUrl(jdbcUrl);
			ds.setUser(username);
			ds.setPassword(password);

			Connection conn = ds.getConnection();
			System.out.println(">>>> Successfully connected to " + dbName);
			return ds;
*/

		} catch (Exception ex) {
			//System.out.println("Error creating datasource :" + ex.getMessage());
			LOGGER.error("Exception creating datasource :" + ex.getMessage());
			throw ex;
		}
	}


/*

	@Bean
	DataSource getDataSource (
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
			@Value("${yugabyte.datasource.max-pool-size:4}") int maxPoolSize) throws SQLException {

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