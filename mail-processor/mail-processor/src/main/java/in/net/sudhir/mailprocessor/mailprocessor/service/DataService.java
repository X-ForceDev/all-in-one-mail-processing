package in.net.sudhir.mailprocessor.mailprocessor.service;

import in.net.sudhir.mailprocessor.mailprocessor.model.BlockedEmailIds;
import in.net.sudhir.mailprocessor.mailprocessor.model.Statistics;
import in.net.sudhir.mailprocessor.mailprocessor.repository.BlockedEmailRepository;
import in.net.sudhir.mailprocessor.mailprocessor.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 Package Name: in.net.sudhir.mailprocessor.mailprocessor.service
 User Name: SUDHIR
 Created Date: 20-08-2022 at 06:35
 Description:
 */
@Service("dataService")
public class DataService {

    @Autowired
    BlockedEmailRepository blockedEmailRepository;

    @Autowired
    StatisticsRepository statisticsRepository;


    public List<String> getBlockedEmailIds(){
        List<String> blockedEmailIdsList = new ArrayList<>();
        List<BlockedEmailIds> blockedEmailIds = blockedEmailRepository.findAll();
        blockedEmailIds.forEach(blockedEmailId -> blockedEmailIdsList.add(blockedEmailId.getBlockedEmailId()));
        return blockedEmailIdsList;
    }

    public void populateStatistics(List<Statistics> statistics){
        statisticsRepository.saveAll(statistics);
    }

    public void deleteAllInBatch() {
        statisticsRepository.deleteAll();
    }

}
