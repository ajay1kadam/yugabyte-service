package ak.db.yugabyte.poi;

import ak.db.yugabyte.entity.CompanyDiversityInfo;
import ak.db.yugabyte.entity.LeaderDiversityInfo;
import ak.db.yugabyte.repo.CompanyDiversityInfoRepository;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DiversityInclusionReadExcel implements DiversityInclusion {

/*
    CompanyDiversityInfo objCDI = null;
    LeaderDiversityInfo objLDI1 = null;
    LeaderDiversityInfo objLDI2 = null;
    Set<LeaderDiversityInfo> setOfLeaders = null;
*/

    public void readDiversityOwnedData(CompanyDiversityInfoRepository companyDiversityInfoRepository,
                                       LeaderDiversityInfoRepository leaderDiversityInfoRepository) throws IOException {

        try {
            FileInputStream file = new FileInputStream(new File("C:\\devl\\wfb\\hackathon\\docs\\Hackathon_Data_MinorityWomenOwned_2022 v1.xlsx"));


            List<CompanyDiversityInfo> companyDiversityInfoList = new ArrayList<>();
            List<LeaderDiversityInfo> leaderDiversityInfoList = new ArrayList<>();

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int rowCounter = 0;
            String strValue = null;
            int iColumnIndex = 0;

            final int batchSize = 50;
            int batchCounter = 1;

            while (rowIterator.hasNext()) {


                rowCounter++;
                if (rowCounter <= 1000) continue;
                if (rowCounter > 10000) {
                    break;
                }


                CompanyDiversityInfo objCDI = new CompanyDiversityInfo();
                LeaderDiversityInfo objLDI1 = new LeaderDiversityInfo();
                LeaderDiversityInfo objLDI2 = new LeaderDiversityInfo();
                Set<LeaderDiversityInfo> setOfLeaders = new HashSet<>();

                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                if (row.getRowNum() == 0) {
                    continue;
                }
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    iColumnIndex = cell.getColumnIndex();

                    if (cell.getCellType() == CellType.NUMERIC) {
                        strValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                    } else {
                        strValue = cell.getStringCellValue();
                    }
                    if (iColumnIndex == 0) {
                        objCDI.setDunsNumber(strValue);
                    } else if (iColumnIndex == 1) {
                        objCDI.setDunsName(strValue);
                    } else if (iColumnIndex == 2) {
                        objCDI.setCounty(strValue);
                    } else if (iColumnIndex == 3) {
                        objCDI.setStreetAddress(strValue);
                    } else if (iColumnIndex == 4) {
                        objCDI.setCity(strValue);
                    } else if (iColumnIndex == 5) {
                        objCDI.setState(strValue);
                    } else if (iColumnIndex == 6) {
                        objCDI.setZipCode(strValue);
                    } else if (iColumnIndex == 7) {
                        objCDI.setPhone(strValue);
                    } else if (iColumnIndex == 8) {
                        objLDI1.setName(strValue);
                        //setOfLeaders.add(objLDI1);
                        if (objLDI1.getName() != null && objLDI1.getName().length() > 0) {
                            setOfLeaders.add(objLDI1);
                        }

                    } else if (iColumnIndex == 9) {
                        objLDI2.setName(strValue);
                        //setOfLeaders.add(objLDI2);
                        if (objLDI2.getName() != null && objLDI2.getName().length() > 0) {
                            setOfLeaders.add(objLDI2);
                        }

                    } else if (iColumnIndex == 10) {

                    } else if (iColumnIndex == 11) {
                        //objLDI1.setCompany(objCDI);
                        //objLDI2.setCompany(objCDI);
                        objCDI.setLeaders(setOfLeaders);

/*
                        if (objLDI1.getName() != null && objLDI1.getName().length() > 0) {
                            lsLDI.add(objLDI1);
                        }
                        if (objLDI2.getName() != null && objLDI2.getName().length() > 0) {
                            lsLDI.add(objLDI2);
                        }
*/
                    }
                }

                //companyDiversityInfoRepository.save(objCDI);
                companyDiversityInfoList.add(objCDI);
                if (objLDI1.getName() != null && objLDI1.getName().length() > 0) {
                    //leaderDiversityInfoRepository.save(objLDI1);
                    leaderDiversityInfoList.add(objLDI1);
                }
                if (objLDI2.getName() != null && objLDI2.getName().length() > 0) {
                    //leaderDiversityInfoRepository.save(objLDI2);
                    leaderDiversityInfoList.add(objLDI2);
                }

                batchCounter++;
                if (batchCounter == batchSize) {
                    long st_time = System.currentTimeMillis();

                    try {

                        companyDiversityInfoRepository.saveAll(companyDiversityInfoList);
                        leaderDiversityInfoRepository.saveAll(leaderDiversityInfoList);
                        System.out.println("#################### : " + rowCounter);
                    }
                    finally {
                        System.out.println("#################### : " + rowCounter + ", time (ms) :"
                                + (System.currentTimeMillis()-st_time));
                    }
                    batchCounter = 0;
                }


            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

   /* @Override
    public List<LeaderDiversityInfo> getLeaders() {
        return lsLDI;
    }

    @Override
    public List<CompanyDiversityInfo> getCompanies() {
        return lsCDI;
    }
*/
}
