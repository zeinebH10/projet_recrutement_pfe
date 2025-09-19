package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {
    private long totalUsers;
    private long totalApplications;
    private long activeRecruiters;
    private long totalJobOffers;
}
