package com.innowise.candidatemodule.controller.mapper;

import com.innowise.candidatemodule.controller.dto.AttachmentDTO;
import com.innowise.candidatemodule.domain.Attachment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CandidateMapper.class})
public interface AttachmentMapper {

    @Mappings({})
    AttachmentDTO toDTO(Attachment attachment);

    @Mappings({})
    List<AttachmentDTO> toListDTO(List<Attachment> attachments);

    @InheritInverseConfiguration
    Attachment fromDTO(AttachmentDTO attachmentDTO);

    @InheritInverseConfiguration
    List<Attachment> fromListDTO(List<AttachmentDTO> attachmentDTOs);

}
