package com.connectravel.domain.dto.crew;


import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Participation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewManageResponse {

    private Long id;
    private String title;
    private String crewCaptain;

    private String strict;

    private Integer crewLimit;
    private Integer currentParticipants; // user에서 crew아이디가 그것인 사람 수

    private String status;

    public static CrewManageResponse fromEntity(Crew crew){
        String status = null;

        Participation participation = crew.getParticipations().get(0);
        if(participation.getStatus() == 1){
            status = "승인대기";
        } else if (participation.getStatus() == 2) {
            status = "모집중";

        } else if (participation.getStatus() == 3){
            status = "모집종료";
        } else{
            status = "기타";
        }


        return CrewManageResponse.builder()
                .id(crew.getId())
                .title(crew.getTitle())
                .crewCaptain(crew.getParticipations().get(0).getUser().getUsername())
                .strict(crew.getStrict())
                .crewLimit(crew.getCrewLimit())
                .currentParticipants(crew.getParticipations().size())
                .status(status)
                .build();

    }

}
