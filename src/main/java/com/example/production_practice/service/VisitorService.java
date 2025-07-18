package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными о посетителях, методы: save, remove, findAll

import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.VisitorMapper;
import com.example.production_practice.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    Long COUNT_VISITOR = 0L;

    public void save(VisitorRequestDTO visitorDTO) {
        Visitor visitor = visitorMapper.toEntity(visitorDTO);
        visitor.setId(++COUNT_VISITOR);
        visitorRepository.save(visitor);
    }

    public void remove(Long id) {
        Visitor visitor = visitorRepository.findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (visitor != null) visitorRepository.remove(visitor);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VisitorResponseDTO findById(Long id) {
        return visitorMapper.toResponseDTO(visitorRepository.findById(id));
    }

    public void update(Long id, VisitorRequestDTO visitorDTO) {
        Visitor oldVisitor = visitorRepository.findById(id);
        oldVisitor.setAge(visitorDTO.getAge());
        oldVisitor.setName(visitorDTO.getName());
        oldVisitor.setGender(visitorDTO.getGender());
    }
}
