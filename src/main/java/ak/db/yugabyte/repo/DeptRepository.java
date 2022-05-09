package ak.db.yugabyte.repo;

import com.yugabyte.data.jdbc.repository.YsqlRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DeptRepository extends YsqlRepository<Dept, Integer> {
/*

    @Override
    Optional<Dept> findById(Integer integer);
*/

    @Transactional(readOnly = true)
    @Query("SELECT * FROM dept WHERE dname LIKE concat(:deptName,'%')")
    Collection<Dept> findByDeptName(@Param("dname") String deptName);
}
