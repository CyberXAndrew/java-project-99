package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAllLabels() {
        List<Label> labels = labelRepository.findAll();
        return labels.stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDTO findLabelById(Long id) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO createLabel(LabelCreateDTO createDTO) {
        Label label = labelMapper.map(createDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO updateLabel(LabelUpdateDTO updateDTO, Long id) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(updateDTO, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void deleteLabelById(Long id) {
        labelRepository.deleteById(id);
    }
}
