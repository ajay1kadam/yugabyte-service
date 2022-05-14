package ak.db.yugabyte.poi;


import ak.db.yugabyte.entity.CompanyDiversityInfo;
import ak.db.yugabyte.entity.LeaderDiversityInfo;
import ak.db.yugabyte.repo.CompanyDiversityInfoRepository;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;

import java.io.IOException;
import java.util.List;

public interface DiversityInclusion {
    public void readDiversityOwnedData(CompanyDiversityInfoRepository companyDiversityInfoRepository,
                                       LeaderDiversityInfoRepository leaderDiversityInfoRepository) throws IOException;
    /*public List<LeaderDiversityInfo> getLeaders();
    public List<CompanyDiversityInfo> getCompanies();
*/}
