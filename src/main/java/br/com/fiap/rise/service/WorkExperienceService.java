package br.com.fiap.rise.service;

import br.com.fiap.rise.repository.WorkExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;

    public WorkExperienceService(WorkExperienceRepository workExperienceRepository) {
        this.workExperienceRepository = workExperienceRepository;
    }


}
