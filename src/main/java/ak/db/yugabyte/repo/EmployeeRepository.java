package ak.db.yugabyte.repo;

import ak.db.yugabyte.entity.Employee;
import com.yugabyte.data.jdbc.repository.YsqlRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface EmployeeRepository extends YsqlRepository<Employee, Integer> {

    public Employee findByName(String name);
}
