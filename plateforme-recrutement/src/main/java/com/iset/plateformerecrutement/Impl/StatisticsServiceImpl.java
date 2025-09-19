package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.repository.*;
import com.iset.plateformerecrutement.requests.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DemandeStageRepository demandestageRepository;
    @Autowired
    private RecruteurRepository recruteurRepository;
    @Autowired
    private OffreRepository offreRepository;


    public StatisticsDTO getStatistics() {
        long totalUsers = userRepository.count();
        long totalApplications = demandestageRepository.count();
        long activeRecruiters = recruteurRepository.countByIsEnabledTrue();
        long totalJobOffers = offreRepository.count();

        StatisticsDTO stats = new StatisticsDTO();
        stats.setTotalUsers(totalUsers);
        stats.setTotalApplications(totalApplications);
        stats.setActiveRecruiters(activeRecruiters);
        stats.setTotalJobOffers(totalJobOffers);

        return stats;
    }
}
