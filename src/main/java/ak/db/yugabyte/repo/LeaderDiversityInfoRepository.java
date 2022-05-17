package ak.db.yugabyte.repo;

import ak.db.yugabyte.entity.LeaderDiversityInfo;
import com.yugabyte.data.jdbc.repository.YsqlRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderDiversityInfoRepository extends YsqlRepository<LeaderDiversityInfo, Integer> {

    public List<LeaderDiversityInfo> findByGender(String gender);
}
