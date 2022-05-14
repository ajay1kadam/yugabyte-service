package ak.db.yugabyte.service;


import ak.db.yugabyte.entity.CompanyDiversityInfo;
import ak.db.yugabyte.entity.LeaderDiversityInfo;

import java.util.List;

public interface DiversityInclusionService {
    public void saveCompanyDiversityInfo(List<CompanyDiversityInfo> lsCompanyDI);
    List <CompanyDiversityInfo> findAllCompanies();
    public void saveLeaderDiversityInfo(List<LeaderDiversityInfo> lsLeaderDI);
}
