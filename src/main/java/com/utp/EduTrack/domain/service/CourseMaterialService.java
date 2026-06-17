package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.CourseMaterialDTO;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.persistance.entity.CourseMaterial;
import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.persistance.repository.CourseMaterialRepository;
import com.utp.EduTrack.persistance.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseMaterialService {

    private final CourseMaterialRepository materialRepository;
    private final SectionRepository sectionRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public CourseMaterialDTO uploadMaterial(Long sectionId, Integer weekNumber, String title, MultipartFile file) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        String filePath = fileStorageService.storeFile(file);

        CourseMaterial material = CourseMaterial.builder()
                .title(title)
                .filePath(filePath)
                .weekNumber(weekNumber)
                .uploadDate(LocalDateTime.now())
                .section(section)
                .visible(false) // Nuevo material inicia oculto por defecto
                .build();

        CourseMaterial saved = materialRepository.save(material);

        return CourseMaterialDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .filePath(normalizeFilePath(saved.getFilePath()))
                .weekNumber(saved.getWeekNumber())
                .uploadDate(saved.getUploadDate().toString())
                .sectionId(saved.getSection().getId())
                .visible(saved.getVisible())
                .build();
    }

    public List<CourseMaterialDTO> getMaterialsBySection(Long sectionId) {
        return materialRepository.findBySectionId(sectionId).stream()
                .map(m -> CourseMaterialDTO.builder()
                        .id(m.getId())
                        .title(m.getTitle())
                        .filePath(normalizeFilePath(m.getFilePath()))
                        .weekNumber(m.getWeekNumber())
                        .uploadDate(m.getUploadDate().toString())
                        .sectionId(m.getSection().getId())
                        .visible(m.getVisible())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseMaterialDTO toggleVisibility(Long materialId) {
        CourseMaterial material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado"));
        material.setVisible(!material.getVisible());
        CourseMaterial saved = materialRepository.save(material);
        return CourseMaterialDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .filePath(normalizeFilePath(saved.getFilePath()))
                .weekNumber(saved.getWeekNumber())
                .uploadDate(saved.getUploadDate().toString())
                .sectionId(saved.getSection().getId())
                .visible(saved.getVisible())
                .build();
    }

    @Transactional
    public void deleteMaterial(Long materialId) {
        CourseMaterial material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado"));
        materialRepository.delete(material);
    }

    private String normalizeFilePath(String path) {
        if (path == null) return null;
        String normalized = path.replace("\\", "/");
        int uploadsIdx = normalized.lastIndexOf("uploads/");
        if (uploadsIdx != -1) {
            return normalized.substring(uploadsIdx);
        }
        try {
            java.nio.file.Path p = java.nio.file.Paths.get(path);
            return "uploads/" + p.getFileName().toString();
        } catch (Exception e) {
            return path;
        }
    }
}
