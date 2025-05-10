package be.kdg.hackathonsetup.controllerini.dto;


public record GenerateSideQuestDto(
        Long userId,
        String preference,
        int count
) {
}
